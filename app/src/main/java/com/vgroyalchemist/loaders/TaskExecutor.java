package com.vgroyalchemist.loaders;

import android.content.Context;
import android.os.Bundle;

public abstract class TaskExecutor {

    Bundle bundle;
    Context context;
    Object result;

    protected TaskExecutor(Context context, Bundle bundle) {
        this.context = context;
        this.bundle = bundle;
    }

    protected abstract Object executeTask();

    public Object getResult() {
        return result;
    }
}
