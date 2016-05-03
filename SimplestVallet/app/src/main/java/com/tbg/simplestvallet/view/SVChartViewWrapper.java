package com.tbg.simplestvallet.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewSwitcher;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.tbg.simplestvallet.R;
import com.tbg.simplestvallet.model.dto.SVEntry;
import com.tbg.simplestvallet.model.dto.SVEntryLineDataSet;
import com.tbg.simplestvallet.model.dto.SVEntryPieDataSet;
import com.tbg.simplestvallet.model.dto.SVMoneyQuantity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wws2003 on 5/2/16.
 */
public class SVChartViewWrapper {

    public static final int CHART_TYPE_LINE = 0;
    public static final int CHART_TYPE_PIE = 1;

    public static final int FLIP_DIRECTION_LEFT_TO_RIGHT = 0;
    public static final int FLIP_DIRECTION_RIGHT_TO_LEFT = 1;

    private static final int NB_PIE_PARTS = 5;

    private SVEntryLineDataSet mEntriesLineDataSet;
    private SVEntryPieDataSet mEntriesPieDataSet;

    private Context mContext;
    private ViewSwitcher mViewSwitcher;

    private LineChart[] mLineCharts = new LineChart[2];
    private PieChart[] mPieCharts = new PieChart[2];
    private int[] mPieColors;

    private View[] mCurrentViews = mLineCharts;

    public SVChartViewWrapper(Context context, ViewSwitcher viewSwitcher,
                              SVEntryLineDataSet entriesLineDataSet,
                              SVEntryPieDataSet entriesPieDataSet) {

        mContext = context;
        mViewSwitcher = viewSwitcher;
        mEntriesLineDataSet = entriesLineDataSet;
        mEntriesPieDataSet = entriesPieDataSet;

        int[] colorResIds = {R.color.clr_pie1, R.color.clr_pie2, R.color.clr_pie3, R.color.clr_pie4, R.color.clr_pie5};
        mPieColors = new int[colorResIds.length];
        for (int i = 0; i < colorResIds.length; i++) {
            mPieColors[i] = getResColor(colorResIds[i]);
        }
    }

    public void setup(View.OnTouchListener onTouchListener) {
        createCharts(onTouchListener);
        fillInViewSwitcher();
    }

    public void switchChart(int chartType) {
        switch (chartType) {
            case CHART_TYPE_LINE:
                mCurrentViews = mLineCharts;
                break;
            case CHART_TYPE_PIE:
                mCurrentViews = mPieCharts;
                break;
            default:
                return;
        }
        fillInViewSwitcher();
    }

    public void flipChart(int flipDirection) {
        switch (flipDirection) {
            case FLIP_DIRECTION_RIGHT_TO_LEFT:
                // Next screen comes in from right.
                mViewSwitcher.setInAnimation(mContext, R.anim.slide_in_from_right);
                // Current screen goes out from left.
                mViewSwitcher.setOutAnimation(mContext, R.anim.slide_out_to_left);

                // Display previous screen.
                mViewSwitcher.showPrevious();
                break;
            case FLIP_DIRECTION_LEFT_TO_RIGHT:
                // Next screen comes in from left.
                mViewSwitcher.setInAnimation(mContext, R.anim.slide_in_from_left);
                // Current screen goes out from right.
                mViewSwitcher.setOutAnimation(mContext, R.anim.slide_out_to_right);

                // Display next screen.
                mViewSwitcher.showNext();
                break;
        }
    }

    public void dataChangedNotified() {
        updateLineChartData(mLineCharts[0]);
        updateLineChartData(mLineCharts[1]);

        updatePieChartData(mPieCharts[0]);
        updatePieChartData(mPieCharts[1]);
    }

    //------------------MARK: Private methods------------------
    private void createCharts(View.OnTouchListener onTouchListener) {
        mLineCharts[0] = createLineChart(onTouchListener);
        mLineCharts[1] = createLineChart(onTouchListener);

        mPieCharts[0] = createPieChart(onTouchListener);
        mPieCharts[1] = createPieChart(onTouchListener);
    }

    private void fillInViewSwitcher() {
        mViewSwitcher.removeAllViews();
        for(View view : mCurrentViews) {
            mViewSwitcher.addView(view);
        }
    }

    //------------------MARK: Private methods for line chart------------------
    private LineChart createLineChart(View.OnTouchListener onTouchListener) {
        LineChart lineChart = new LineChart(mContext);
        setLineChartData(lineChart);
        setLineChartLayout(lineChart);
        lineChart.setOnTouchListener(onTouchListener);
        return lineChart;
    }

    private void setLineChartData(LineChart lineChart) {
        // use the interface ILineDataSet
        List<ILineDataSet> dataSets = new ArrayList<>();
        getChartLineDataSets(dataSets);

        List<String> xVals = mEntriesLineDataSet.getDates();
        LineData lineData = new LineData(xVals, dataSets);

        lineChart.setData(lineData);
        lineChart.setDescription(mEntriesLineDataSet.getDescription());

        lineData.setDrawValues(false);
        lineChart.invalidate();
    }

    private void updateLineChartData(LineChart lineChart) {
        // use the interface ILineDataSet
        List<ILineDataSet> dataSets = new ArrayList<>();
        getChartLineDataSets(dataSets);

        LineData lineData = lineChart.getLineData();
        lineData.clearValues();

        for (ILineDataSet dataSet : dataSets) {
            lineData.addDataSet(dataSet);
        }

        lineData.setDrawValues(false);
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
    }

    private void getChartLineDataSets(List<ILineDataSet> dataSets) {
        List<Entry>[] chartEntries = new List[2];

        SparseArray<SVMoneyQuantity>[] entriesLists = mEntriesLineDataSet.getEntriesMap();

        for (int i = 0; i < 2; i++) {
            chartEntries[i] = new ArrayList<>();

            //Always check days 1 -> 31
            for (int day = 1; day <= 31; day++) {
                SVMoneyQuantity amount = entriesLists[i].get(day);
                if(amount != null) {
                    Entry c1e1 = new Entry(amount.getAmount(), day);
                    chartEntries[i].add(c1e1);
                }
            }
        }

        String[] monthLabels = mEntriesLineDataSet.getMonths();

        LineDataSet setComp1 = new LineDataSet(chartEntries[0], monthLabels[0]);
        setComp1.setColor(getResColor(R.color.clr_accent));
        setComp1.setCircleRadius(2.0f);

        LineDataSet setComp2 = new LineDataSet(chartEntries[1], monthLabels[1]);
        setComp2.setColor(getResColor(R.color.clr_primary));
        setComp2.setCircleRadius(2.0f);

        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
        setComp2.setAxisDependency(YAxis.AxisDependency.LEFT);

        dataSets.add(setComp1);
        dataSets.add(setComp2);
    }

    private void setLineChartLayout(LineChart lineChart) {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        lineChart.setLayoutParams(layoutParams);

        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setLabelsToSkip(4);

        lineChart.getAxisRight().setDrawLabels(false);

        lineChart.getAxisLeft().setLabelCount(16, false);
        lineChart.getAxisLeft().setAxisMinValue(0.0f);

        lineChart.setBackgroundColor(Color.WHITE);
    }

    //------------------MARK: Private methods for pie chart------------------
    private PieChart createPieChart(View.OnTouchListener onTouchListener) {
        PieChart pieChart = new PieChart(mContext);
        setPieChartLayout(pieChart);
        setPieChartData(pieChart);
        pieChart.setOnTouchListener(onTouchListener);
        return pieChart;
    }

    private void setPieChartData(PieChart pieChart) {
        List<String> xVals = new ArrayList<>();
        for (String xVal : mEntriesPieDataSet.getEntryTypes()) {
            xVals.add(xVal);
        }

        PieDataSet dataSet = getPieDataSet();

        PieData data = new PieData(xVals, dataSet);

        pieChart.setData(data);
        pieChart.setDescription(mEntriesPieDataSet.getDescription());

        pieChart.invalidate();
    }

    private void updatePieChartData(PieChart pieChart) {
        PieDataSet dataSet = getPieDataSet();

        PieData data = pieChart.getData();
        data.clearValues();
        data.setDataSet(dataSet);

        pieChart.setDescription(mEntriesPieDataSet.getDescription());

        pieChart.notifyDataSetChanged();
        pieChart.invalidate();
    }

    private PieDataSet getPieDataSet() {
        List<Entry> yVals = new ArrayList<>();
        List<SVEntry>[] entriesForTypes = mEntriesPieDataSet.getEntriesListForTypes();

        for(int i = 0; i < NB_PIE_PARTS; i++) {
            SVMoneyQuantity moneyOfType = new SVMoneyQuantity((float) 0.0);
            for(SVEntry entryOfType : entriesForTypes[i]) {
                moneyOfType = moneyOfType.add(entryOfType.getMoneyQuantity());
            }
            Entry c2e1 = new Entry(moneyOfType.getAmount(), 0);
            yVals.add(c2e1);
        }

        String label = mEntriesPieDataSet.getLabel();

        PieDataSet dataSet = new PieDataSet(yVals, label);
        dataSet.setColors(mPieColors);

        return dataSet;
    }

    private void setPieChartLayout(PieChart pieChart) {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        pieChart.setLayoutParams(layoutParams);
        pieChart.setHoleRadius((float)0.0);
        pieChart.setCenterTextRadiusPercent((float)0.0);
        pieChart.setTransparentCircleRadius((float)0.0);
        pieChart.setDrawSliceText(true);
        pieChart.setRotationEnabled(false);

        pieChart.setNoDataText("No data");
        pieChart.setDescriptionTextSize(12);
    }

    private int getResColor(int resId) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return mContext.getResources().getColor(resId, mContext.getTheme());
        }
        else {
            return mContext.getResources().getColor(resId);
        }
    }
}
