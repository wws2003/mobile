package com.tbg.simplestvallet.ioc.taskmanager.service;

import com.tbg.simplestvallet.app.SimplestValetApp;
import com.tbg.taskmanager.abstr.locator.ILocator;
import com.tbg.taskmanager.abstr.service.AbstractTaskService;
import com.tbg.taskmanager.abstr.task.ITask;
import com.tbg.taskmanager.common.Result;

/**
 * Created by wws2003 on 10/30/15.
 */
public class TaskService extends AbstractTaskService {

    public TaskService() {
        super("com.tbg.simplestvallet.concrete_for_dependencies.service.TaskService");
    }

    public TaskService(String name) {
        super(name);
    }

    @Override
    protected ILocator<ITask> getTaskLocator() {
        return SimplestValetApp.getLocatorContainer().getTaskLocator();
    }

    @Override
    protected ILocator<Result> getTaskResultLocator() {
        return SimplestValetApp.getLocatorContainer().getTaskResultLocator();
    }
}
