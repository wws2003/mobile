package com.tbg.simplestvallet.app;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.tbg.simplestvallet.app.container.AuthenticationManagerContainer;
import com.tbg.simplestvallet.app.container.EntryCollectionContainer;
import com.tbg.simplestvallet.app.container.LocatorContainer;
import com.tbg.simplestvallet.app.container.SVEntryQueryBuilderContainer;
import com.tbg.simplestvallet.app.container.SheetServiceManagerContainer;
import com.tbg.simplestvallet.app.container.TaskExecutorContainer;
import com.tbg.simplestvallet.app.container.TaskIdPool;
import com.tbg.simplestvallet.app.manager.authentication.impl.SVAuthenticationManagerImpl;
import com.tbg.simplestvallet.ioc.taskmanager.locator.MapBasedLocator;
import com.tbg.simplestvallet.model.active.impl.SVEntryWrapperBuilderImpl;
import com.tbg.simplestvallet.model.active.impl.SVSamplePendingEntryStore;
import com.tbg.simplestvallet.model.active.impl.SVSampleSheet;
import com.tbg.simplestvallet.persist.impl.SVJSONBasedPersistorImpl;
import com.tbg.taskmanager.abstr.task.ITask;
import com.tbg.taskmanager.common.Result;
import com.tbg.taskmanager.impl.executor.AsyncTaskBasedTaskExecutorImpl;

/**
 * Created by wws2003 on 10/24/15.
 */
public class SimplestValetApp extends MultiDexApplication {

    private static Context mContext;

    private static AuthenticationManagerContainer gAuthenticationManagerContainer;
    private static SheetServiceManagerContainer gSheetServiceManagerContainer;

    private static TaskExecutorContainer gTaskExecutorContainer;
    private static TaskIdPool gTaskIdPool;
    private static LocatorContainer gLocatorContainer;

    private static EntryCollectionContainer gEntryCollectionContainer;
    private static SVEntryQueryBuilderContainer gEntryQueryBuilderContainer;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        initContainers();
        Log.d("SimplestValetApp", "Application created");
    }

    public static Context getContext() {
        return mContext;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static AuthenticationManagerContainer getAuthenticationManagerContainer() {
        return gAuthenticationManagerContainer;
    }

    public static TaskExecutorContainer getTaskExecutorContainer() {
        return gTaskExecutorContainer;
    }

    public static EntryCollectionContainer getEntryCollectionContainer() {
        return gEntryCollectionContainer;
    }

    public static SVEntryQueryBuilderContainer getEntryQueryWrapperBuilderContainer() {
        return gEntryQueryBuilderContainer;
    }

    public static LocatorContainer getLocatorContainer() {
        return gLocatorContainer;
    }

    public static TaskIdPool getTaskIdPool() {
        return gTaskIdPool;
    }

    public static SheetServiceManagerContainer getSheetServiceManagerContainer() {
        return gSheetServiceManagerContainer;
    }

    private void initContainers() {
        initAuthenticationManagerContainer();
        initSheetServiceManagerContainer();

        initEntrySheetContainer();
        initEntryQueryWrapperBuilderContainer();

        initTaskExecutorContainer();
        initLocatorContainer();
        initTaskPool();
    }

    private void initAuthenticationManagerContainer() {
        gAuthenticationManagerContainer = new AuthenticationManagerContainer();
        gAuthenticationManagerContainer.setAuthenticationManager(new SVAuthenticationManagerImpl(new SVJSONBasedPersistorImpl()));
    }

    private void initSheetServiceManagerContainer() {
        gSheetServiceManagerContainer = new SheetServiceManagerContainer(new SVJSONBasedPersistorImpl());
    }

    private void initEntrySheetContainer() {
        gEntryCollectionContainer = new EntryCollectionContainer();
        gEntryCollectionContainer.setEntrySheet(new SVSampleSheet());
        gEntryCollectionContainer.setPendingEntryStore(new SVSamplePendingEntryStore());
    }

    private void initEntryQueryWrapperBuilderContainer() {
        gEntryQueryBuilderContainer = new SVEntryQueryBuilderContainer(new SVEntryWrapperBuilderImpl());
    }

    private void initTaskExecutorContainer() {
        gTaskExecutorContainer = new TaskExecutorContainer();
        gTaskExecutorContainer.setTaskExecutor(new AsyncTaskBasedTaskExecutorImpl());
    }

    private void initLocatorContainer() {
        gLocatorContainer = new LocatorContainer();
        gLocatorContainer.setTaskLocator(new MapBasedLocator<ITask>());
        gLocatorContainer.setTaskResultLocator(new MapBasedLocator<Result>());
    }

    private void initTaskPool() {
        gTaskIdPool = TaskIdPool.getInstance();
    }
}
