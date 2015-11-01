package com.tbg.taskmanager.abstr.executor;

import com.tbg.taskmanager.abstr.delegate.ITaskDelegate;
import com.tbg.taskmanager.abstr.task.ITask;

/**
 * Created by wws2003 on 10/18/15.
 */
public interface ITaskExecutor {
    //Must be sure that taskDelegate.onTaskToBeExecuted and taskDelegate.onTaskExecuted are executed on the same thread
    <T> void executeTask(ITask<T> task, ITaskDelegate<T> taskDelegate);

    //Try to cancel the given task
    <T> void tryToCancelTask(long taskId, ITaskDelegate<T> taskDelegate);
}
