package com.tbg.simplestvallet.model.dto;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by wws2003 on 5/2/16.
 */
public class SVEntryLineDataSet {

    private String mDescription;

    private SparseArray<SVMoneyQuantity> mCurrentMonthEntries = new SparseArray<>();
    private SparseArray<SVMoneyQuantity> mPrevMonthEntries = new SparseArray<>();

    private String[] mTwoMonthsNames = new String[2];

    private Calendar mCalendar;
    private Locale mLocale;

    public SVEntryLineDataSet(String description) {
        mDescription = description;
        mCalendar = Calendar.getInstance();
        mLocale = Locale.getDefault();

        mCalendar = Calendar.getInstance();
        mLocale = Locale.getDefault();
    }

    public String getDescription() {
        return mDescription;
    }

    public SparseArray<SVMoneyQuantity>[] getEntriesMap() {
        SparseArray<SVMoneyQuantity>[] entriesLists = new SparseArray[2];
        entriesLists[0] = mPrevMonthEntries;
        entriesLists[1] = mCurrentMonthEntries;
        return entriesLists;
    }

    public List<String> getDates() {
        List<String> dates = new ArrayList<>();
        for(int i = 1; i <= 31; i++) {
            dates.add(String.valueOf(i));
        }
        return dates;
    }

    //Currently called in background
    public synchronized void updateData(Date currentMonth, List<SVEntry> currentMonthEntries, List<SVEntry> prevMonthEntries) {
        setTwoMonthNames(currentMonth);

        mCurrentMonthEntries.clear();
        mPrevMonthEntries.clear();

        fillDateEntries(mCurrentMonthEntries, currentMonthEntries);
        fillDateEntries(mPrevMonthEntries, prevMonthEntries);
    }

    public String[] getMonths() {
        return mTwoMonthsNames;
    }

    //----------------MARK: Private methods----------------

    private void setTwoMonthNames(Date currentMonth) {
        mCalendar.setTime(currentMonth);
        mTwoMonthsNames[1] = String.format("%s %d", mCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, mLocale), mCalendar.get(Calendar.YEAR));

        mCalendar.add(Calendar.MONTH, -1);
        mTwoMonthsNames[0] = String.format("%s %d", mCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, mLocale), mCalendar.get(Calendar.YEAR));
    }

    private void fillDateEntries(SparseArray<SVMoneyQuantity> dst, List<SVEntry> src) {
        dst.clear();

        //Collect data for dates
        for(SVEntry entry : src) {
            int dayInMonth = getEntryCreatedDateInMonth(entry);
            SVMoneyQuantity entryAmount = entry.getMoneyQuantity();

            SVMoneyQuantity amountInDay = dst.get(dayInMonth);
            if(amountInDay != null) {
                amountInDay = amountInDay.add(entryAmount);
            }
            else {
                amountInDay = new SVMoneyQuantity(entryAmount.getAmount(), entryAmount.getCurrency());
            }
            dst.put(dayInMonth, amountInDay);
        }

        //Accumulate amount for dates
        int lastDayLastAmount = 31;
        for (int i = lastDayLastAmount; i >= 1; i--) {
            if(dst.get(i) != null) {
                lastDayLastAmount = i;
                break;
            }
        }

        SVMoneyQuantity accumulatedAmount = new SVMoneyQuantity((float)0.0);
        for(int i = 1; i <= lastDayLastAmount; i++) {
            SVMoneyQuantity amountOfDay = dst.get(i);
            if(amountOfDay != null) {
                accumulatedAmount = amountOfDay.add(accumulatedAmount);
            }
            amountOfDay = new SVMoneyQuantity(accumulatedAmount);
            dst.put(i, amountOfDay);
        }
    }

    private int getEntryCreatedDateInMonth(SVEntry entry) {
        mCalendar.setTime(entry.getCreatedAt());
        return mCalendar.get(Calendar.DAY_OF_MONTH);
    }
}
