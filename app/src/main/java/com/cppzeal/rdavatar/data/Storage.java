package com.cppzeal.rdavatar.data;

import android.content.Context;

public class Storage {
    private static Storage storage;
    private volatile Object appInterface;

    private Context context;

    private ClassLoader classLoader;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public static Storage getInstance() {
        if (storage == null) {
            storage = new Storage();
        }
        return storage;
    }

    public void setAppInterface(Object context) {
        this.appInterface = context;
    }

    public Object getAppInterface() {
        return appInterface;
    }

}
