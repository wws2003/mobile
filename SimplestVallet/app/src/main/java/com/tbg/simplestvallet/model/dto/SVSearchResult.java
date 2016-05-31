package com.tbg.simplestvallet.model.dto;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by wws2003 on 6/1/16.
 */
public class SVSearchResult {

    private Calendar mCalendar = Calendar.getInstance();
    private ArrayList<Integer> mSortedMonths = new ArrayList<>();
    private SparseArray<List<SVEntry>> mMonthEntriesMap = new SparseArray<>();

    public void consumeData(List<SVEntry> entries) {
        if(entries != null) {
            for(SVEntry entry : entries) {
                int monthKey = getMonthKey(entry);

                //Add to month-entries map
                List<SVEntry> entriesInMonth = mMonthEntriesMap.get(monthKey);
                if(entriesInMonth != null) {
                    entriesInMonth.add(entry);
                }
                else {
                    entriesInMonth = new ArrayList<>();
                    entriesInMonth.add(entry);
                    mMonthEntriesMap.put(monthKey, entriesInMonth);


                    //Add to sorted month map
                    mSortedMonths.add(monthKey);
                }
            }
        }

        Collections.sort(mSortedMonths, Collections.reverseOrder());
    }

    public void clean() {
        mMonthEntriesMap.clear();
        mSortedMonths.clear();
    }

    public int getMonthCount() {
        return mMonthEntriesMap.size();
    }

    public boolean isDataAvailableAtPosition(int position) {
        return position < mSortedMonths.size() && position >= 0;
    }

    public int[] getYearMonthInPosition(int position) {
        int monthKey = mSortedMonths.get(position);
        int year = monthKey / 100;
        int month = monthKey % 100;
        int[] yearMonth = new int[2];
        yearMonth[0] = year;
        yearMonth[1] = month + 1;
        return yearMonth;
    }

    public List<SVEntry> getEntriesInPosition(int position) {
        int monthKey = mSortedMonths.get(position);
        return mMonthEntriesMap.get(monthKey);
    }

    public int getEntriesCountInPosition(int position) {
        List<SVEntry> entries = getEntriesInPosition(position);
        return entries == null ? 0 : entries.size();
    }

    public SVMoneyQuantity getAmountInPosition(int position) {
        SVMoneyQuantity amount = new SVMoneyQuantity(0.0f);
        int monthKey = mSortedMonths.get(position);

        List<SVEntry> entriesInMonth = mMonthEntriesMap.get(monthKey);
        if(entriesInMonth != null) {
            for(SVEntry entry : entriesInMonth) {
                amount = amount.add(entry.getMoneyQuantity());
            }
        }
        return amount;
    }

    //MARK: Private methods
    private int getMonthKey(SVEntry entry) {
        Date createdAt = entry.getCreatedAt();
        mCalendar.setTime(createdAt);
        return mCalendar.get(Calendar.YEAR) * 100 + mCalendar.get(Calendar.MONTH);
    }
}
