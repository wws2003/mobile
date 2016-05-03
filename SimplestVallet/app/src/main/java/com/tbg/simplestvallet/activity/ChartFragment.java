package com.tbg.simplestvallet.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.tbg.simplestvallet.R;
import com.tbg.simplestvallet.activity.delegate.SVEntryDataSetFlipDelegate;


public class ChartFragment extends Fragment implements View.OnTouchListener, RadioGroup.OnCheckedChangeListener {

    private SVEntryDataSetFlipDelegate mChartDelegate = null;
    private RadioGroup mRdgCharts;

    private float mLastTouchedX;
    private static final float FLOAT_DELTA_X_THRESHOLD = 160;

    public ChartFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chart, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {

            case MotionEvent.ACTION_DOWN:
                mLastTouchedX = motionEvent.getX();
                break;
            case MotionEvent.ACTION_UP:
                float currentX = motionEvent.getX();

                // Handling left to right screen swap.
                float deltaX = currentX - mLastTouchedX;
                if (deltaX > FLOAT_DELTA_X_THRESHOLD) {
                    mChartDelegate.flipLeftToRight();
                }

                // Handling right to left screen swap.
                if (deltaX < -FLOAT_DELTA_X_THRESHOLD) {
                    mChartDelegate.flipRightToLeft();
                }
                break;
        }
        return false;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rdb_linechart:
                mChartDelegate.showLineChart();
                break;
            case R.id.rdb_piechart:
                mChartDelegate.showPieChart();
                break;
            default:
                break;
        }
    }

    private void setupView() {
        mRdgCharts = (RadioGroup)getActivity().findViewById(R.id.rdg_charts);
        mRdgCharts.setOnCheckedChangeListener(this);

        TextView tvSpentAmountPrevMonth = (TextView)getActivity().findViewById(R.id.tv_spent_amount_last_month);
        TextView tvSpentAmountThisMonth = (TextView)getActivity().findViewById(R.id.tv_spent_amount_this_month);
        ViewSwitcher viewSwitcher = (ViewSwitcher)getActivity().findViewById(R.id.vsw_charts);
        String lineChartDescription = getContext().getResources().getString(R.string.tv_line_chart_description);
        String pieChartLabel = getContext().getResources().getString(R.string.tv_pie_chart_label);
        String pieChartDescription = getContext().getResources().getString(R.string.tv_pie_chart_description);

        mChartDelegate = new SVEntryDataSetFlipDelegate(lineChartDescription,
                pieChartLabel,
                pieChartDescription,
                getContext(),
                tvSpentAmountThisMonth,
                tvSpentAmountPrevMonth,
                viewSwitcher,
                this);

        mChartDelegate.firstLoad();
    }
}
