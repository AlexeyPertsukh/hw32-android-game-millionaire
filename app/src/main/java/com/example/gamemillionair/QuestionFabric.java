package com.example.gamemillionair;

import java.util.ArrayList;
import java.util.List;

public class QuestionFabric {

    private final static String SPLIT_STR = ";";

    private QuestionFabric() {
    }

    public static ArrayList<Question> createFromJson(List<String> listJson) {
        ArrayList<Question> listQuestion = new ArrayList<>();
        for (String json : listJson) {
            Question question = Question.of(json);
            listQuestion.add(question);
        }
        return listQuestion;
    }

    public static ArrayList<Question> createFromCsv(List<String> listCsv) {
        int numWrongAnswers = Question.NUM_ANSWERS - 1;

        String strQuestion = "";
        String correctAnswer = "";
        String[] wrongAnswers = new String[numWrongAnswers];

        ArrayList<Question> listQuestion = new ArrayList<>();
        for (String csv : listCsv) {
            String[] strings = сsvToStrings(csv);

            strQuestion = strings[0];
            correctAnswer = strings[1];
            wrongAnswers[0] = strings[2];
            wrongAnswers[1] = strings[3];
            wrongAnswers[2] = strings[4];
//            System.arraycopy(wrongAnswers,0,strings,2,numWrongAnswers);

            Question question = new Question(strQuestion, correctAnswer, wrongAnswers);
            listQuestion.add(question);
        }
        return listQuestion;
    }

    @SuppressWarnings("NonAsciiCharacters")
    public static String[] сsvToStrings(String line) {
        return line.split(SPLIT_STR);
    }
}
