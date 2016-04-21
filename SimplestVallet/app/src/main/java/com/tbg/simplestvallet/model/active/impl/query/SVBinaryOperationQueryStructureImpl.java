package com.tbg.simplestvallet.model.active.impl.query;

import com.tbg.simplestvallet.model.active.abstr.query.ISVEntryQueryStringBuilder;
import com.tbg.simplestvallet.model.active.abstr.query.ISVQueryStructure;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wws2003 on 4/21/16.
 */

public class SVBinaryOperationQueryStructureImpl implements ISVQueryStructure {
    public static final int QUERY_OPERAND_AND = 1;
    public static final int QUERY_OPERAND_OR = 2;

    private Integer mOperand = null;
    private ISVQueryStructure mLhs = null;
    private ISVQueryStructure mRhs = null;

    public SVBinaryOperationQueryStructureImpl(Integer operand, ISVQueryStructure lhs, ISVQueryStructure rhs) {
        this.mOperand = operand;
        this.mLhs = lhs;
        this.mRhs = rhs;
    }

    @Override
    public void accept(ISVEntryQueryStringBuilder queryBuilder) {
        if(mLhs != null && mRhs != null) {
            mLhs.accept(queryBuilder);
            queryBuilder.visitOperand(mOperand);
            mRhs.accept(queryBuilder);
        }
    }

    @Override
    public List<ISVQueryStructure> getChildren() {
        List<ISVQueryStructure> result = new ArrayList<>();
        result.add(mLhs);
        result.add(mRhs);
        return result;
    }
}
