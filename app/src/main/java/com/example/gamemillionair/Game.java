package com.example.gamemillionair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game implements Serializable {
    private final static String MESSAGE_NO_QUESTIONS = "нет вопросов для игры";

    private final ArrayList<Question> actualQuestions;
    private final ArrayList<Question> oldQuestions;
    private Question currentQuestion;
    private final Round round;
    private List<String> currentAnswers;




    public Game() {
        actualQuestions = new ArrayList<>();
        oldQuestions = new ArrayList<>();
        round = new Round();
    }


    public static Game ofJsonQuestions(List<String> listJson) {
        Game game = new Game();
        for (String json : listJson) {
            game.addJsonQuestion(json);
        }
        return game;
    }

    public void addJsonQuestion(String jsonString) throws GameException{
        Question question;
        try {
            question = Question.of(jsonString);
        } catch (QuestionException ex) {
            throw new GameException(ex.getMessage());
        }
        actualQuestions.add(question);
    }

    public void clearQuestions() {
        actualQuestions.clear();
        oldQuestions.clear();
    }

    public int size() {
        return actualQuestions.size();
    }

    public void nextQuestion(){
        if(actualQuestions.size() == 0) {
            throw new GameException(MESSAGE_NO_QUESTIONS);
        }
        int num = random(actualQuestions.size());
        currentQuestion = actualQuestions.remove(num);
        oldQuestions.add(currentQuestion);

        currentAnswers = getAllAnswers(currentQuestion);
        round.inc();
    }

    public ArrayList<Question> getActualQuestions() {
        return actualQuestions;
    }

    public String getStrQuestion() {
        return currentQuestion.getStrQuestion();
    }

    public String getCorrectAnswer() {
        return currentQuestion.getCorrectAnswer();
    }

    public int getBet(){
        return round.getBet();
    }

    public String getAnswer(int num) {
        return currentAnswers.get(num);
    }

    public static List<String> getAllAnswers(Question question) {
        List<String> answers = new ArrayList<>(question.getWrongAnswers());
        answers.add(question.getCorrectAnswer());
        Collections.shuffle(answers);
        return answers;
    }

    public boolean checkCorrectAnswer(String answer ){
        return currentQuestion.checkCorrectAnswer(answer);
    }

    int random(int max) {
        return (int) (Math.random() * max);
    }

    //РАУНД
    private static class Round {
        private final int[] BETS = {0, 100, 200, 300, 500, 1000,
                2_000, 4_000, 8_000, 16_000, 32_000,
                64_000, 125_000, 255_000, 500_000, 1000_000};
        private int step;

        public Round() {
        }

        public void reset(){
            step = 0;
        }

        public void inc() {
            step++;
        }

        public boolean isEnd(){
            return step == (BETS.length);
        }

        public int getBet() {
            return BETS[step];
        }

        public int getStep() {
            return step;
        }

        public int  getWin() {
            return 0;
        }

    }
}
