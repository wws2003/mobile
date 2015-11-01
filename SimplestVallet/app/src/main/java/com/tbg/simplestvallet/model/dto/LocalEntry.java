package com.tbg.simplestvallet.model.dto;

import java.util.Date;

/**
 * Created by wws2003 on 10/29/15.
 */
public class LocalEntry extends Entry {

    private int mId;

    public LocalEntry(int id, Date createdAt, MoneyQuantity amount, String type, String note) {
        super(createdAt, amount, type, note);
        mId = id;
    }

    public LocalEntry(int id, Entry entry) {
        super(entry.getCreatedAt(), entry.getMoneyQuantity(), entry.getType(), entry.getNote());
        mId = id;
    }

    public int getId() {
        return mId;
    }
}
