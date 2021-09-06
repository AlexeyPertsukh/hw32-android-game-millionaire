package com.example.gamemillionair;

import java.util.ArrayList;

public class DataQuestions {
    private final ArrayList<String> listStrQuestions;
    private final Exception exception;

    public DataQuestions(ArrayList<String> listStrQuestions, Exception exception) {
        this.listStrQuestions = listStrQuestions;
        this.exception = exception;
    }

    public boolean isError() {
        return exception != null;
    }

    public ArrayList<String> getListStrQuestions() {
        return listStrQuestions;
    }

    public String getExceptionMessage() {
        return exception.getMessage();
    }
}