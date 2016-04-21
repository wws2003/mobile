package com.tbg.simplestvallet.model.active.abstr.query;

import com.tbg.simplestvallet.model.dto.SVMoneyQuantity;

import java.util.Date;

/**
 * Created by wws2003 on 4/21/16.
 */
public interface ISVEntryQueryStructureBuilder {

    ISVEntryQueryStructureBuilder newInstance();

    ISVEntryQueryStructureBuilder and(ISVEntryQueryStructureBuilder lhs, ISVEntryQueryStructureBuilder rhs);

    ISVEntryQueryStructureBuilder or(ISVEntryQueryStructureBuilder lhs, ISVEntryQueryStructureBuilder rhs);

    ISVEntryQueryStructureBuilder setDateRanges(Date fromDate, Date toDate);
    ISVEntryQueryStructureBuilder setType(String type);
    ISVEntryQueryStructureBuilder setNote(String note);
    ISVEntryQueryStructureBuilder setMoneyQuantityRanges(SVMoneyQuantity lower, SVMoneyQuantity upper);

    void reset();

    ISVQueryStructure build();
}
