package com.vgroyalchemist.loaders;


import android.content.Context;

import androidx.loader.content.AsyncTaskLoader;

import com.vgroyalchemist.utils.TransparentProgressDialog;


public class LoaderTask<T extends TaskExecutor> extends AsyncTaskLoader<TaskExecutor> {

    private TaskExecutor executor;
    TransparentProgressDialog progressDialog;
    private Context context;

    public LoaderTask(Context context, TaskExecutor executor) {
        super(context);
        this.context = context;
        this.executor = executor;
    }

    @Override
    public TaskExecutor loadInBackground() {
        executor.executeTask();
        if (progressDialog != null)
            progressDialog.dismiss();

        return executor;
    }

    @Override
    protected void onStartLoading() {
        if (progressDialog == null) {
            progressDialog = new TransparentProgressDialog(context, 0, true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
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
        if (progressDialog != null)
            progressDialog.dismiss();
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
