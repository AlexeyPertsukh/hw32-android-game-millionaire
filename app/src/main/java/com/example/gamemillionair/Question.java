
package com.example.gamemillionair;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Question {
    private static final String MESSAGE_EXCEPTION_JSON_CONVERT = "create question from json failed";

    private final String strQuestion;
    private final String correctAnswer;
    private final List<String> wrongAnswers;

    public Question(String strQuestion, String correctAnswer, String... wrongAnswers) {
        this.strQuestion = strQuestion;
        this.correctAnswer = correctAnswer;
        this.wrongAnswers = new ArrayList<>(Arrays.asList(wrongAnswers));
    }

    public static Question of(String jsonString) throws QuestionException {
        Gson gson = new Gson();
        try{
            return gson.fromJson(jsonString, Question.class);
        } catch (JsonSyntaxException ex) {
            throw new QuestionException(MESSAGE_EXCEPTION_JSON_CONVERT);
        }
    }


    public String getStrQuestion() {
        return strQuestion;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public List<String> getWrongAnswers() {
        return wrongAnswers;
    }

    @Override
    public String toString() {
        return "Question{" +
                "strQuestion='" + strQuestion + '\'' +
                ", correctAnswer='" + correctAnswer + '\'' +
                ", wrongAnswers=" + wrongAnswers.toString() +
                '}';
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public boolean checkCorrectAnswer(String answer) {
        return correctAnswer.equals(answer);
    }
}
