package com.example.gamemillionaire.question;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Question {
    public static final int AMOUNT_ANSWERS = 4;

    private static final String MESSAGE_EXCEPTION_JSON_CONVERT = "Invalid json format for created";
    private static final String FORMAT_MESSAGE_EXCEPTION_FAILED_CREATE  = "Invalid numbers answers (valid values is %d): %s";

    private final String strQuestion;
    private final String correctAnswer;
    private final List<String> wrongAnswers;

    public Question(String strQuestion, String correctAnswer, String... wrongAnswers) throws QuestionException {
        validateWrongAnswers(wrongAnswers);
        this.strQuestion = strQuestion;
        this.correctAnswer = correctAnswer;
        this.wrongAnswers = new ArrayList<>(Arrays.asList(wrongAnswers));
    }

    public static Question of(String jsonString) throws QuestionException {
        Gson gson = new Gson();
        Question question;
        try{
            question = gson.fromJson(jsonString, Question.class);
        } catch (JsonSyntaxException ex) {
            throw new QuestionException(MESSAGE_EXCEPTION_JSON_CONVERT);
       }

        validateWrongAnswers(question.getWrongAnswers());
        return question;

    }

    private static void validateWrongAnswers(int amount) {
        if(amount != AMOUNT_ANSWERS - 1) {
            String message = String.format(FORMAT_MESSAGE_EXCEPTION_FAILED_CREATE, AMOUNT_ANSWERS - 1, amount);
            throw new QuestionException(message);
        }
    }

    private static void validateWrongAnswers(String[] wrongAnswers) {
        validateWrongAnswers(wrongAnswers.length);
    }

    private static void validateWrongAnswers(List<String> listWrongAnswers) {
        validateWrongAnswers(listWrongAnswers.size());
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
