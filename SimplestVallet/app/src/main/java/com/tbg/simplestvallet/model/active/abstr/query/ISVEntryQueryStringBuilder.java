package com.tbg.simplestvallet.model.active.abstr.query;

import com.tbg.simplestvallet.model.active.abstr.query.ISVQuery;
import com.tbg.simplestvallet.model.dto.SVMoneyQuantity;

import java.util.Date;

/**
 * Created by wws2003 on 4/21/16.
 */
public interface ISVEntryQueryStringBuilder {
    void visitOperand(int operand);

    void visitDateQuery(ISVQuery<Date> dateQuery);
    void visitStringQuery(ISVQuery<String> stringQuery);
    void visitAmountQuery(ISVQuery<SVMoneyQuantity> amountQuery);

    String build();

    void reset();

    //Possibly add more methods
}
