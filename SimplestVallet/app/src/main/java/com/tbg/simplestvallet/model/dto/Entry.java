package com.tbg.simplestvallet.model.dto;

import java.util.Date;

/**
 * Created by wws2003 on 10/12/15.
 */
public class Entry {

    private Date mCreatedAt;
    private MoneyQuantity mMoneyQuantity;
    private String mType;
    private String mNote;

    public Entry(Date createdAt, MoneyQuantity amount, String type, String note) {
        this.mCreatedAt = createdAt;
        this.mMoneyQuantity = amount;
        this.mType = type;
        this.mNote = note;
    }

    public Date getCreatedAt() {
        return mCreatedAt;
    }

    public String getType() {
        return mType;
    }

    public String getNote() {
        return mNote;
    }

    public MoneyQuantity getMoneyQuantity() {
        return mMoneyQuantity;
    }
}
