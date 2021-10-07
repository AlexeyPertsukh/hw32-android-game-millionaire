package com.example.gamemillionaire.model_game;

import android.os.Handler;

import com.example.gamemillionaire.model_question.Question;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game implements Serializable {
    public final static boolean ENABLE_PAUSE = true;
    public final static boolean DISABLE_PAUSE = false;

    private final static String MESSAGE_NO_QUESTIONS = "Нет вопросов для игры";
    private static final int DELAY_INTRIGUE = 3_000;
    private static final int DELAY_AFTER_QUESTION_RESULT = 3_000;



    private final ArrayList<Question> allQuestions;
    private ArrayList<Question> actualQuestions;
    private Question currentQuestion;
    private final Round round;

    private OnReportQuestionResultListener onReportResultListener;
    private OnSelectNewQuestionListener onSelectNewQuestionListener;
    private OnSelectAnswerListener onSelectAnswerListener;
    private OnEndGameListener onEndGameListener;

    private String currentAnswer;

    private boolean isAnswerExecute;
    private boolean enablePause;

    public Game(ArrayList<Question> allQuestions, boolean enablePause) {
        this.allQuestions = allQuestions;
        this.enablePause = enablePause;
        round = new Round();
    }

    public void reset() {
        createActualQuestions();
        round.reset();
    }

    private void createActualQuestions() {
        actualQuestions = new ArrayList<>(allQuestions);
    }

    public int size() {
        return actualQuestions.size();
    }

    public int getNumAnswer() {
        return round.getStep();
    }

    public int getWin() {
        return round.getWin();
    }

    public void start() {
        reset();
        nextQuestion();
    }


    private void wrongAnswer() {
        if (onEndGameListener != null) {
            onEndGameListener.onEndGame();
        }
    }

    private void nextQuestion() {
        isAnswerExecute = false;
        if (actualQuestions.size() == 0) {
            throw new GameException(MESSAGE_NO_QUESTIONS);
        }

        round.inc();
        int bet;
        if (isEnd()) {
            if (onEndGameListener != null) {
                onEndGameListener.onEndGame();
            }

        } else {
            int num = random(actualQuestions.size());
            currentQuestion = actualQuestions.remove(num);
            if (onSelectNewQuestionListener != null) {
                onSelectNewQuestionListener.onSelectNewQuestion(currentQuestion, round.getBet());
            }

        }
    }

    public static List<String> getShuffledAllAnswers(Question question) {
        List<String> answers = new ArrayList<>(question.getWrongAnswers());
        answers.add(question.getCorrectAnswer());
        Collections.shuffle(answers);
        return answers;
    }

    public boolean checkCorrectAnswer(String answer) {
        return currentQuestion.checkCorrectAnswer(answer);
    }

    public void sendAnswer(String selectedAnswer) {
        if (isAnswerExecute || isEnd()) {
            return;
        }
        isAnswerExecute = true;
        currentAnswer = selectedAnswer;
        if(enablePause) {
            actionByEnablePause(selectedAnswer);
        } else {
            actionByDisablePause(selectedAnswer);
        }
    }

    private void actionByEnablePause(String selectedAnswer) {
        onSelectAnswerListener.onSelectAnswer(selectedAnswer);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (onReportResultListener != null) {
                onReportResultListener.onReportQuestionResult(selectedAnswer, currentQuestion.getCorrectAnswer());
            }
            pauseAfterQuestionResult();
        }, DELAY_INTRIGUE);
    }

    private void actionByDisablePause(String selectedAnswer) {
        if (checkCorrectAnswer(currentAnswer)) {
            nextQuestion();
        } else {
            wrongAnswer();
        }
    }

    private void pauseAfterQuestionResult() {
        Handler handler = new Handler();
        if (checkCorrectAnswer(currentAnswer)) {
            handler.postDelayed(this::nextQuestion, DELAY_AFTER_QUESTION_RESULT);
        } else {
            handler.postDelayed(this::wrongAnswer, DELAY_AFTER_QUESTION_RESULT);
        }

    }

    public boolean isEnd() {
        return round.isEnd();
    }

    public void setOnSelectAnswerListener(OnSelectAnswerListener onSelectAnswerListener) {
        this.onSelectAnswerListener = onSelectAnswerListener;
    }

    public void setOnReportQuestionResultListener(OnReportQuestionResultListener onReportResultListener) {
        this.onReportResultListener = onReportResultListener;
    }

    public void setOnSelectNewQuestionListener(OnSelectNewQuestionListener onSelectNewQuestionListener) {
        this.onSelectNewQuestionListener = onSelectNewQuestionListener;
    }

    public void setOnEndGameListener(OnEndGameListener onEndGameListener) {
        this.onEndGameListener = onEndGameListener;
    }

    int random(int max) {
        return (int) (Math.random() * max);
    }

    //РАУНД
    private static class Round {
        private final int[] BETS = {100, 200, 300, 500, 1000,
                2_000, 4_000, 8_000, 16_000, 32_000,
                64_000, 125_000, 255_000, 500_000, 1000_000};

        private final int[] IRREPARABLE_AMOUNTS = {1000, 32_000, 1000_000};
        private int step;

        public Round() {
        }

        public void reset() {
            step = -1;
        }

        public void inc() {
            if (!isEnd()) {
                step++;
            }
        }

        public boolean isEnd() {
            return step >= (BETS.length);
        }

        public int getBet() {
            return BETS[step];
        }

        public int getStep() {
            return step;
        }

        public int getWin() {
            int win = 0;

            int numQuestion = step;

            if (step >= BETS.length) {
                numQuestion = BETS.length - 1;
            }

            for (int i = 0; i < IRREPARABLE_AMOUNTS.length; i++) {
                int bet = BETS[numQuestion];
                if (bet >= IRREPARABLE_AMOUNTS[i]) {
                    win = IRREPARABLE_AMOUNTS[i];
                }
            }

            return win;
        }
    }

    //Слушатели
    @FunctionalInterface
    public interface OnReportQuestionResultListener {
        void onReportQuestionResult(String selectedAnswer, String correctAnswer);
    }

    @FunctionalInterface
    public interface OnSelectAnswerListener {
        void onSelectAnswer(String selectedAnswer);
    }

    @FunctionalInterface
    public interface OnSelectNewQuestionListener {
        void onSelectNewQuestion(Question question, int bet);
    }

    @FunctionalInterface
    public interface OnEndGameListener {
        void onEndGame();
    }

}
