package com.tbg.taskmanager.impl.executor;

import android.os.Handler;

import com.tbg.taskmanager.abstr.task.ITask;
import com.tbg.taskmanager.abstr.delegate.ITaskDelegate;
import com.tbg.taskmanager.abstr.executor.ITaskExecutor;
import com.tbg.taskmanager.common.Result;

/**
 * Created by wws2003 on 10/20/15.
 */
public class HandlerBasedTaskExecutorImpl implements ITaskExecutor {

    private Handler mTaskExecutingHandler;
    private Handler mTaskDelegateHandler;

    public HandlerBasedTaskExecutorImpl(Handler taskExecutingHandler, Handler taskDelegateHandler) {
        this.mTaskExecutingHandler = taskExecutingHandler;
        this.mTaskDelegateHandler = taskDelegateHandler;
    }

    @Override
    public <T> void executeTask(ITask<T> task, ITaskDelegate<T> taskDelegate) {

        preExecute(taskDelegate);

        waitToExecuteTask();

        startExecuteTask(task, taskDelegate);
    }

    @Override
    public <T> void tryToCancelTask(long taskId, ITaskDelegate<T> taskDelegate) {
        //TODO Implement or at least throw some kind of run time exception to notice
    }

    private <T> void preExecute(final ITaskDelegate<T> taskDelegate) {
        mTaskDelegateHandler.post(new Runnable() {
            @Override
            public void run() {
                taskDelegate.onTaskToBeExecuted();
            }
        });
    }

    private void waitToExecuteTask() {
        try {
            mTaskDelegateHandler.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private <T> void startExecuteTask(final ITask<T> task, final ITaskDelegate<T> taskDelegate) {
        mTaskExecutingHandler.post(new Runnable() {
            @Override
            public void run() {
                final Result<T> taskResult = task.execute();
                mTaskDelegateHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        taskDelegate.onTaskExecuted(taskResult);
                    }
                });
            }
        });
    }

}
