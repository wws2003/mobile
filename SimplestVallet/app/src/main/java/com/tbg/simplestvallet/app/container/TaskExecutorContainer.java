package com.tbg.simplestvallet.app.container;

import com.tbg.taskmanager.abstr.executor.ITaskExecutor;

/**
 * Created by wws2003 on 10/24/15.
 */
public class TaskExecutorContainer {
    private ITaskExecutor mTaskExecutor;

    public void setTaskExecutor(ITaskExecutor taskExecutor) {
        this.mTaskExecutor = taskExecutor;
    }

    public ITaskExecutor getTaskExecutor() {
        return mTaskExecutor;
    }
}
