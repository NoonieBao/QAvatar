package com.cppzeal.rdavatar.data;

public class Storage {
    private static Storage storage;
    private volatile Object appInterface;

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
