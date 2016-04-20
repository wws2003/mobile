package com.tbg.simplestvallet.model.active.abstr;

import com.tbg.simplestvallet.model.dto.SVMoneyQuantity;

import java.util.Date;

/**
 * Created by wws2003 on 4/20/16.
 */
public interface ISVEntryWrapperBuilder {

    void setDateRanges(Date fromDate, Date toDate);
    void setType(String type);
    void setNote(String note);
    void setMoneyQuantityRanges(SVMoneyQuantity lower, SVMoneyQuantity upper);

    void reset();

    ISVEntryQueryWrapper build();
}
