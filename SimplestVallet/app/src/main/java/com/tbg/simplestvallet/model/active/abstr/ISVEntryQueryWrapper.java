package com.tbg.simplestvallet.model.active.abstr;

import com.tbg.simplestvallet.model.dto.SVMoneyQuantity;

import java.util.Date;

/**
 * Created by wws2003 on 4/20/16.
 */
public interface ISVEntryQueryWrapper {

    int FIELD_INDEX_CREATED_DATE = 0;
    int FIELD_INDEX_TYPE = 1;
    int FIELD_INDEX_NOTE = 2;
    int FIELD_INDEX_AMOUNT = 3;

    ISVQuery<Date> getCreateDateQuery();
    ISVQuery<String> getNoteQuery();
    ISVQuery<String> getTypeQuery();
    ISVQuery<SVMoneyQuantity> getAmountQuery();
}
