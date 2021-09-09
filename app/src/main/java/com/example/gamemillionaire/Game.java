package com.example.gamemillionaire;

import android.os.Handler;

import com.example.gamemillionaire.question.Question;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game implements Serializable {
    private final static String MESSAGE_NO_QUESTIONS = "Нет вопросов для игры";
    private static final int DELAY_INTRIGUE = 3_000;
    private static final int DELAY_UNTIL_NEW_QUESTION = 3_000;

    private final ArrayList<Question> actualQuestions;
    private final ArrayList<Question> oldQuestions;
    private Question currentQuestion;
    private final Round round;

    private OnReportQuestionResultListener onReportResultListener;
    private OnSelectNewQuestionListener onSelectNewQuestionListener;
    private OnSelectAnswerListener onSelectAnswerListener;

    private boolean isAnswerExecute;

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

    public int getStep() {
        return round.getStep();
    }

    public int getWin() {
        return round.getWin();
    }

    public void nextQuestion(){
        isAnswerExecute = false;
        if(actualQuestions.size() == 0) {
            throw new GameException(MESSAGE_NO_QUESTIONS);
        }

        int bet;
        if(isEnd()) {
            currentQuestion = new Question("","","","","");
            bet = 0;

        } else {
            int num = random(actualQuestions.size());
            currentQuestion = actualQuestions.remove(num);
            oldQuestions.add(currentQuestion);
            bet = round.getBet();
            round.inc();
        }

        if(onSelectNewQuestionListener != null) {
            onSelectNewQuestionListener.onSelectNewQuestion(currentQuestion, bet);
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
        if(isAnswerExecute) {
            return;
        }
        isAnswerExecute = true;
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

    public boolean isEnd() {
        return round.isEnd();
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
//        private final int[] BETS = {0, 100, 200, 300, 500, 1000,
//                2_000, 4_000, 8_000, 16_000, 32_000,
//                64_000, 125_000, 255_000, 500_000, 1000_000};

        private final int[] BETS = {100, 200, 300, 0};  //последний ноль - костыль
        private final int[] IRREPARABLE_AMOUNTS = {1000, 32_000, 1000_000};
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
            int win = 0;
            if(step > 1) {
                for (int i = 0; i < IRREPARABLE_AMOUNTS.length; i++) {
                    int bet = BETS[step];
                    if(bet >= IRREPARABLE_AMOUNTS[i]) {
                        win = IRREPARABLE_AMOUNTS[i];
                    }
                }
            }
            return win;
        }
    }

    //Слушатели

    interface OnReportQuestionResultListener {
        void onReportQuestionResult(String selectedAnswer, String correctAnswer);
    }

    interface OnSelectNewQuestionListener {
        void onSelectNewQuestion(Question question, int bet);
    }

    interface OnSelectAnswerListener {
        void onSelectAnswer(String selectedAnswer);
    }


}
