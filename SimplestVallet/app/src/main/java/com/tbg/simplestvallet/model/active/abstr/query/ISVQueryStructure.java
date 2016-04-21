package com.tbg.simplestvallet.model.active.abstr.query;

import java.util.List;

/**
 * Created by wws2003 on 4/21/16.
 */
public interface ISVQueryStructure extends ISVAcceptor {
    List<ISVQueryStructure> getChildren();
}
