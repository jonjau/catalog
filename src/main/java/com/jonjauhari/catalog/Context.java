package com.jonjauhari.catalog;

public class Context {
    private final static Context instance = new Context();
    public static Context getInstance() {
        return instance;
    }

    private Database database;

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }
}
