package com.tbg.simplestvallet.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.tbg.simplestvallet.R;
import com.tbg.simplestvallet.app.SimplestValetApp;
import com.tbg.simplestvallet.ioc.taskmanager.service.TaskService;
import com.tbg.simplestvallet.model.active.EntryActionResult;
import com.tbg.simplestvallet.model.active.abstr.IEntrySheet;
import com.tbg.simplestvallet.model.active.abstr.IPendingEntryStore;
import com.tbg.simplestvallet.model.dto.Entry;
import com.tbg.simplestvallet.model.dto.MoneyQuantity;
import com.tbg.simplestvallet.ioc.taskmanager.task.AddEntryTask;
import com.tbg.simplestvallet.ioc.taskmanager.task.AddEntryTaskResultProcessor;
import com.tbg.taskmanager.abstr.delegate.ITaskDelegate;
import com.tbg.taskmanager.abstr.executor.ITaskExecutor;
import com.tbg.taskmanager.abstr.task.ITaskResultProcessor;
import com.tbg.taskmanager.common.Result;
import com.tbg.taskmanager.impl.executor.ServiceBasedTaskExecutor;
import com.tbg.taskmanager.impl.task.ChainedTask;

import java.util.Calendar;

public class InputFragment extends Fragment implements View.OnClickListener {

    private EditText mEtDate = null;
    private AutoCompleteTextView mEtType = null;
    private EditText mEtAmount = null;
    private EditText mEtNote = null;
    private Button mBtnAddEntry = null;

    private ViewWrapper mViewWrapper = new ViewWrapper();
    private ITaskExecutor mTaskExecutor = null;

    public InputFragment() {
        //mTaskExecutor = SimplestValetApp.getTaskExecutorContainer().getTaskExecutor();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Just to test service-based task executor
        mTaskExecutor = new ServiceBasedTaskExecutor(getActivity().getApplicationContext(),
                SimplestValetApp.getLocatorContainer().getTaskLocator(),
                SimplestValetApp.getLocatorContainer().getTaskResultLocator(),
                TaskService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_input, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
    }

    @Override
    public void onResume() {
        super.onResume();
        renderView();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_add_entry) {
            onBtnAddEntryClicked();
        }
    }

    private void setupView() {
        mEtDate = (EditText)getActivity().findViewById(R.id.et_date);
        mEtAmount = (EditText)getActivity().findViewById(R.id.et_amount);
        mEtType = (AutoCompleteTextView)getActivity().findViewById(R.id.et_type);
        mEtNote = (EditText)getActivity().findViewById(R.id.et_note);
        mBtnAddEntry = (Button)getActivity().findViewById(R.id.btn_add_entry);

        mViewWrapper.setup(mEtDate, mEtAmount, mEtType, mBtnAddEntry);

        mBtnAddEntry.setOnClickListener(this);
    }

    private void renderView() {
        //TODO Retrieve theses values from somewhere rather than hardcoded
        String[] types = {"Coop Card","Softbank","Other"};
        mViewWrapper.render(types);
    }

    //Button click handler
    private void onBtnAddEntryClicked() {
        Entry inputEntry = getInputEntry();

        IEntrySheet sheet = SimplestValetApp.getEntryCollectionContainer().getEntrySheet();
        IPendingEntryStore pendingEntryStore = SimplestValetApp.getEntryCollectionContainer().getPendingEntryStore();
        AddEntryTask addEntryTask = new AddEntryTask(sheet, inputEntry);
        ITaskResultProcessor<Entry> addEntryTaskResultProcessor = new AddEntryTaskResultProcessor(pendingEntryStore);
        ChainedTask<Entry> addEntryChainedTask = new ChainedTask<>(addEntryTask, addEntryTaskResultProcessor, false);
        addEntryChainedTask.setId(SimplestValetApp.getTaskIdPool().getAvailableTaskId());

        AddEntryTaskDelegate taskDelegate = new AddEntryTaskDelegate();

        mTaskExecutor.executeTask(addEntryChainedTask, taskDelegate);
    }

    private Entry getInputEntry() {
        MoneyQuantity quantity = new MoneyQuantity(Double.valueOf(mEtAmount.getText().toString()));
        return new Entry(Calendar.getInstance().getTime(),
                quantity,
                mEtType.getText().toString(),
                mEtNote.getText().toString());
    }

    private class AddEntryTaskDelegate implements ITaskDelegate<Entry> {

        @Override
        public void onTaskToBeExecuted() {
           mViewWrapper.freeze();
        }

        @Override
        public void onTaskExecuted(Result<Entry> taskResult) {
            mViewWrapper.unFreezing();
            switch (taskResult.getResultCode()) {
                case EntryActionResult.ADD_RESULT_OK:
                    ((MainActivity)getActivity()).switchToChartTab();
                    break;
                case EntryActionResult.ADD_RESULT_PENDED:
                    ((MainActivity)getActivity()).switchToPendingTab();
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onTaskCancelled() {
            //TODO Implement
        }
    }

    private static class ViewWrapper {
        private EditText mEtDate = null;
        private EditText mEtAmount = null;
        private AutoCompleteTextView mEtType = null;
        private Button mBtnAddEntry = null;
        private ProgressDialog mProgressDialog = null;
        private Context mContext;

        public void setup(EditText etDate, EditText etAmount, AutoCompleteTextView etType, Button btnAddEntry) {
            mEtDate = etDate;
            mEtAmount = etAmount;
            mEtType = etType;
            mBtnAddEntry = btnAddEntry;
            mContext = mBtnAddEntry.getContext();
            initProgressDialog();
        }

        public void render(String[] types) {
            //Set date
            Calendar rightNow = Calendar.getInstance();
            CharSequence dateSq = DateFormat.format("yyyy/MM/dd", rightNow);
            mEtDate.setText(dateSq);

            //Set autocomplete
            ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext,
                    android.R.layout.simple_dropdown_item_1line,
                    types);
            mEtType.setAdapter(adapter);
            mEtType.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        mEtType.showDropDown();
                    }
                    else {
                        mEtType.dismissDropDown();
                    }
                }
            });

            //Focus on amount field
            mEtAmount.requestFocus();
        }

        public void freeze() {
            //Disable add button
            mBtnAddEntry.setEnabled(false);

            //Show progress bar
            mProgressDialog.show();
        }

        public void unFreezing() {
            //Hide progress bar
            mProgressDialog.dismiss();

            //Re-enable add button
            mBtnAddEntry.setEnabled(true);
        }

        private void initProgressDialog() {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage(mContext.getString(R.string.dlg_add_entry_message));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setIndeterminate(true);
        }
    }

}
