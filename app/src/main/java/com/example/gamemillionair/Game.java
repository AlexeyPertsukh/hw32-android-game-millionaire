package com.example.gamemillionair;

import android.os.Handler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game implements Serializable {
    private final static String MESSAGE_NO_QUESTIONS = "нет вопросов для игры";
    private static final int DELAY_INTRIGUE = 3_000;
    private static final int DELAY_UNTIL_NEW_QUESTION = 3_000;

    private final ArrayList<Question> actualQuestions;
    private final ArrayList<Question> oldQuestions;
    private Question currentQuestion;
    private final Round round;
    private List<String> currentAnswers;

    private OnReportQuestionResultListener onReportResultListener;
    private OnSelectNewQuestionListener onSelectNewQuestionListener;
    private OnSelectAnswerListener onSelectAnswerListener;

    private boolean isAnswerReceived;

    public Game(ArrayList<Question> actualQuestions) {
        this.actualQuestions = actualQuestions;
        oldQuestions = new ArrayList<>();
        round = new Round();
    }

    public void clearQuestions() {
        actualQuestions.clear();
        oldQuestions.clear();
    }

    public int size() {
        return actualQuestions.size();
    }

    public void nextQuestion(){
        isAnswerReceived = false;
        if(actualQuestions.size() == 0) {
            throw new GameException(MESSAGE_NO_QUESTIONS);
        }
        int num = random(actualQuestions.size());
        currentQuestion = actualQuestions.remove(num);
        oldQuestions.add(currentQuestion);

        currentAnswers = getShuffledAllAnswers(currentQuestion);
        round.inc();
        if(onSelectNewQuestionListener != null) {
//            String[] answers = (String[]) currentAnswers.toArray();
//            onShowNewQuestionListener.onShowNewQuestion(currentQuestion.getStrQuestion(), answers);
            onSelectNewQuestionListener.onSelectNewQuestion(currentQuestion.getStrQuestion(),
                    currentAnswers.get(0),
                    currentAnswers.get(1),
                    currentAnswers.get(2),
                    currentAnswers.get(3)
                    );
        }
    }

    public static List<String> getShuffledAllAnswers(Question question) {
        List<String> answers = new ArrayList<>(question.getWrongAnswers());
        answers.add(question.getCorrectAnswer());
        Collections.shuffle(answers);
        return answers;
    }

    public boolean checkCorrectAnswer(String answer ){
        return currentQuestion.checkCorrectAnswer(answer);
    }

    public void sendAnswer(String selectedAnswer) {
        if(isAnswerReceived) {
            return;
        }
        isAnswerReceived = true;
        onSelectAnswerListener.onSelectAnswer(selectedAnswer);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if(onReportResultListener != null) {
                onReportResultListener.onReportQuestionResult(selectedAnswer, currentQuestion.getCorrectAnswer());
            }
            pauseUntilNewQuestion();
        }, DELAY_INTRIGUE);
    }

    private void pauseUntilNewQuestion() {
        Handler handler = new Handler();
        handler.postDelayed(this::nextQuestion, DELAY_UNTIL_NEW_QUESTION);
    }

    void setOnSelectAnswerListener(OnSelectAnswerListener onSelectAnswerListener) {
        this.onSelectAnswerListener = onSelectAnswerListener;
    }

    void setOnReportQuestionResultListener(OnReportQuestionResultListener onReportResultListener) {
        this.onReportResultListener = onReportResultListener;
    }

    void setOnSelectNewQuestionListener(OnSelectNewQuestionListener onSelectNewQuestionListener) {
        this.onSelectNewQuestionListener = onSelectNewQuestionListener;
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

    //Слушатели

    interface OnReportQuestionResultListener {
        void onReportQuestionResult(String selectedAnswer, String correctAnswer);
    }

    interface OnSelectNewQuestionListener {
        void onSelectNewQuestion(String question, String... answers);
    }

    interface OnSelectAnswerListener {
        void onSelectAnswer(String selectedAnswer);
    }


}
