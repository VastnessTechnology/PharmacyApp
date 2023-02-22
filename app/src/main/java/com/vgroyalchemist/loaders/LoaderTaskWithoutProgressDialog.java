package com.vgroyalchemist.loaders;

import android.content.Context;

import androidx.loader.content.AsyncTaskLoader;

/**
 * Created by VT on 23-11-2015.
 */
public class LoaderTaskWithoutProgressDialog<T extends TaskExecutor> extends AsyncTaskLoader<TaskExecutor> {

    private TaskExecutor executor;
    //TransparentProgressDialog progressDialog;
    private Context context;

    public LoaderTaskWithoutProgressDialog(Context context, TaskExecutor executor) {
        super(context);
        this.context = context;
        this.executor = executor;
    }

    @Override
    public TaskExecutor loadInBackground() {
        executor.executeTask();
        return executor;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public void deliverResult(TaskExecutor executor) {
        if (isReset()) {
            // An async query came in while the loader is stopped
            return;
        }

        this.executor = executor;

        super.deliverResult(executor);
        cancelLoad();
    }


    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        executor = null;
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
        //super.onStopLoading();
    }

    @Override
    public void onCanceled(TaskExecutor data) {
        super.onCanceled(data);
    }
}
