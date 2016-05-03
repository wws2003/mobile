package com.tbg.simplestvallet.model.dto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by wws2003 on 5/2/16.
 */
public class SVEntryPieDataSet {

    private String mLabel;
    private String mDescription;

    //Current use only fixed types.
    private final String[] mFixedTypes = {"Coop Card", "Cafe", "Misc", "Gyomu", "Other"};

    private List<SVEntry>[] mEntriesLists = new List[mFixedTypes.length];

    private Calendar mCalendar;
    private Locale mLocale;

    private String mCurrentMonth = "";

    public SVEntryPieDataSet(String label, String description) {
        mLabel = label;
        mDescription = description;

        for (int i = 0; i < mFixedTypes.length; i++) {
            mEntriesLists[i] = new ArrayList<>();
        }

        mCalendar = Calendar.getInstance();
        mLocale = Locale.getDefault();
    }

    public String getLabel() {
        return mLabel;
    }

    public String getDescription() {
        return mDescription + mCurrentMonth;
    }

    //Always return 5 types
    public String[] getEntryTypes() {
        return mFixedTypes;
    }

    public List<SVEntry>[] getEntriesListForTypes() {
        return mEntriesLists;
    }

    //Currently called in background
    public synchronized void updateData(Date currentMonth, List<SVEntry> entries) {
        for (int i = 0; i < mFixedTypes.length; i++) {
            mEntriesLists[i].clear();
        }

        for(SVEntry entry : entries) {
            int entryTypeIndex = getEntryTypeIndex(entry);
            if (entryTypeIndex >= 0) {
                mEntriesLists[entryTypeIndex].add(entry);
            }
        }

        mCalendar.setTime(currentMonth);
        mCurrentMonth = String.format(" %s %d", mCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, mLocale), mCalendar.get(Calendar.YEAR));
    }

    private int getEntryTypeIndex(SVEntry entry) {
        int nbSpecifiedEntryTypes = mFixedTypes.length - 1;
        String entryType = entry.getType();
        String entryNote = entry.getNote();

        for (int i = 0; i < nbSpecifiedEntryTypes; i++) {
            if(mFixedTypes[i].equalsIgnoreCase(entryType) || (mFixedTypes[nbSpecifiedEntryTypes].equalsIgnoreCase(entryType) && mFixedTypes[i].equalsIgnoreCase(entryNote))) {
                return i;
            }
        }
        return nbSpecifiedEntryTypes;
    }
}
