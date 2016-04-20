package com.tbg.simplestvallet.model.dto;

import java.util.Date;

/**
 * Created by wws2003 on 10/29/15.
 */
public class SVLocalEntry extends SVEntry {

    private int mId;

    public SVLocalEntry(int id, Date createdAt, SVMoneyQuantity amount, String type, String note) {
        super(createdAt, amount, type, note);
        mId = id;
    }

    public SVLocalEntry(int id, SVEntry entry) {
        super(entry.getCreatedAt(), entry.getMoneyQuantity(), entry.getType(), entry.getNote());
        mId = id;
    }

    public int getId() {
        return mId;
    }
}
