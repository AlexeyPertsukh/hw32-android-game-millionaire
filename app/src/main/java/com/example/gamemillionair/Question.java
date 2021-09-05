
package com.example.gamemillionair;

import com.google.gson.Gson;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Question {
    private String question;
    private String correctAnswer;
    private List<String> wrongAnswers;

    public Question(String question, String correctAnswer, String... wrongAnswers) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.wrongAnswers = new ArrayList<>(Arrays.asList(wrongAnswers));
    }

    public static Question of(String jsonString) {
        Gson gson=new Gson();
        return gson.fromJson(jsonString,Question.class);
    }


    public String getQuestion() {
        return question;
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
                "question='" + question + '\'' +
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
