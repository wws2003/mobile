package com.tbg.simplestvallet.model.dto;

import java.util.Date;

/**
 * Created by wws2003 on 10/12/15.
 */
public class SVEntry {

    private Date mCreatedAt;
    private SVMoneyQuantity mMoneyQuantity;
    private String mType;
    private String mNote;

    public SVEntry(Date createdAt, SVMoneyQuantity amount, String type, String note) {
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

    public SVMoneyQuantity getMoneyQuantity() {
        return mMoneyQuantity;
    }
}
