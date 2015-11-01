package com.tbg.simplestvallet.app.container;

/**
 * Created by wws2003 on 10/30/15.
 */
public class TaskIdPool {
    private long mInitialId = 0;
    private static TaskIdPool gInstance = null;

    private TaskIdPool() {

    }

    public synchronized static TaskIdPool getInstance() {
        if(gInstance == null) {
            gInstance = new TaskIdPool();
        }
        return gInstance;
    }

    public synchronized long getAvailableTaskId() {
        //TODO More sophisticated solution
        mInitialId++;
        return mInitialId;
    }
}
