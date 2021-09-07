package com.example.gamemillionair;

import java.util.ArrayList;

public class DataStrings {
    private final ArrayList<String> list;
    private final Exception exception;

    public DataStrings(ArrayList<String> list, Exception exception) {
        this.list = list;
        this.exception = exception;
    }

    public boolean isError() {
        return exception != null;
    }

    public ArrayList<String> getList() {
        return list;
    }

    public String getExceptionMessage() {
        return exception.getMessage();
    }
}