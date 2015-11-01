package com.tbg.simplestvallet.app.container;

import com.tbg.taskmanager.abstr.executor.ITaskExecutor;
import com.tbg.taskmanager.abstr.locator.ILocator;
import com.tbg.taskmanager.abstr.task.ITask;
import com.tbg.taskmanager.common.Result;

/**
 * Created by wws2003 on 10/30/15.
 */
public class LocatorContainer {
    private ILocator mLocator;

    private ILocator<ITask> mTaskLocator;
    private ILocator<Result> mResultLocator;

    public void setTaskLocator(ILocator<ITask> taskLocator) {
        this.mTaskLocator = taskLocator;
    }

    public void setTaskResultLocator(ILocator<Result> resultLocator) {
        this.mResultLocator = resultLocator;
    }

    public ILocator<ITask> getTaskLocator() {
        return mTaskLocator;
    }

    public ILocator<Result> getTaskResultLocator() {
        return mResultLocator;
    }

}
