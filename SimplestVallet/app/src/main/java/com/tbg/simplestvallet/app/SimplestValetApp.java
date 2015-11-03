package com.tbg.simplestvallet.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.tbg.simplestvallet.app.authen.Credential;
import com.tbg.simplestvallet.app.authen.impl.SampleAuthenticationManager;
import com.tbg.simplestvallet.app.container.AuthenticationManagerContainer;
import com.tbg.simplestvallet.app.container.EntryCollectionContainer;
import com.tbg.simplestvallet.app.container.LocatorContainer;
import com.tbg.simplestvallet.app.container.TaskExecutorContainer;
import com.tbg.simplestvallet.app.container.TaskIdPool;
import com.tbg.simplestvallet.ioc.taskmanager.locator.MapBasedLocator;
import com.tbg.simplestvallet.model.active.abstr.IEntrySheet;
import com.tbg.simplestvallet.model.active.impl.GoogleSpreadSheetBasedSheet;
import com.tbg.simplestvallet.model.active.impl.SamplePendingEntryStore;
import com.tbg.simplestvallet.model.active.impl.SampleSheet;
import com.tbg.taskmanager.abstr.task.ITask;
import com.tbg.taskmanager.common.Result;
import com.tbg.taskmanager.impl.executor.AsyncTaskBasedTaskExecutorImpl;

/**
 * Created by wws2003 on 10/24/15.
 */
public class SimplestValetApp extends Application {

    private static Context mContext;

    private static AuthenticationManagerContainer gAuthenticationManagerContainer;
    private static TaskExecutorContainer gTaskExecutorContainer;
    private static EntryCollectionContainer gEntryCollectionContainer;
    private static LocatorContainer gLocatorContainer;
    private static TaskIdPool gTaskIdPool;

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

    public static AuthenticationManagerContainer getAuthenticationManagerContainer() {
        return gAuthenticationManagerContainer;
    }

    public static TaskExecutorContainer getTaskExecutorContainer() {
        return gTaskExecutorContainer;
    }

    public static EntryCollectionContainer getEntryCollectionContainer() {
        return gEntryCollectionContainer;
    }

    public static void reloadEntrySheetForCredential(Credential credential, String spreadSheetId) {
        String accessToken = credential.getServiceAccessToken(Credential.SERVICE_NAME_GOOGLE_DRIVE);
        IEntrySheet entrySheet = new GoogleSpreadSheetBasedSheet(spreadSheetId, accessToken);
        gEntryCollectionContainer.setEntrySheet(entrySheet);
    }

    public static LocatorContainer getLocatorContainer() {
        return gLocatorContainer;
    }

    public static TaskIdPool getTaskIdPool() {
        return gTaskIdPool;
    }

    private void initContainers() {
        initAuthenticationManagerContainer();
        initTaskExecutorContainer();
        initEntrySheetContainer();
        initLocatorContainer();
        initTaskPool();
    }

    private void initAuthenticationManagerContainer() {
        gAuthenticationManagerContainer = new AuthenticationManagerContainer();
        gAuthenticationManagerContainer.setAuthenticationManager(SampleAuthenticationManager.newAuthenticationManager());
    }

    private void initTaskExecutorContainer() {
        gTaskExecutorContainer = new TaskExecutorContainer();
        gTaskExecutorContainer.setTaskExecutor(new AsyncTaskBasedTaskExecutorImpl());
    }

    private void initEntrySheetContainer() {
        gEntryCollectionContainer = new EntryCollectionContainer();
        gEntryCollectionContainer.setEntrySheet(new SampleSheet());
        gEntryCollectionContainer.setPendingEntryStore(new SamplePendingEntryStore());
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