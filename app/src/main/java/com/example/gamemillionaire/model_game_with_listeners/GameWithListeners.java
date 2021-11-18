package com.example.gamemillionaire.model_game_with_listeners;

import android.os.Handler;

import com.example.gamemillionaire.model_game.Game;
import com.example.gamemillionaire.model_question.Question;

import java.util.ArrayList;

public class GameWithListeners extends Game {

    private static final int DELAY_INTRIGUE = 3_000;
    private static final int DELAY_AFTER_QUESTION_RESULT = 3_000;

    private boolean blockAnswer;

    private OnRoundResultListener onRoundResultListener;
    private OnSelectNewQuestionListener onSelectNewQuestionListener;
    private OnSelectAnswerListener onSelectAnswerListener;
    private OnEndGameListener onEndGameListener;


    public GameWithListeners(ArrayList<Question> allQuestions) {
        super(allQuestions);
    }


    public void setOnSelectAnswerListener(OnSelectAnswerListener onSelectAnswerListener) {
        this.onSelectAnswerListener = onSelectAnswerListener;
    }

    public void setOnRoundResultListener(OnRoundResultListener onRoundResultListener) {
        this.onRoundResultListener = onRoundResultListener;
    }

    public void setOnSelectNewQuestionListener(OnSelectNewQuestionListener onSelectNewQuestionListener) {
        this.onSelectNewQuestionListener = onSelectNewQuestionListener;
    }

    public void setOnEndGameListener(OnEndGameListener onEndGameListener) {
        this.onEndGameListener = onEndGameListener;
    }


    @Override
    public void nextQuestion() {
        super.nextQuestion();
        blockAnswer = false;
        if(onSelectNewQuestionListener != null) {
            onSelectNewQuestionListener.onSelectNewQuestion(currentQuestion, getBet());
        }
    }

    @Override
    public void sendAnswer(String selectedAnswer) {

        if(blockAnswer) {
            return;
        }

        super.sendAnswer(selectedAnswer);

        if(onSelectAnswerListener != null) {
            onSelectAnswerListener.onSelectAnswer(selectedAnswer);
        }

            blockAnswer = true;

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (onRoundResultListener != null) {
                onRoundResultListener.onReportQuestionResult(selectedAnswer, currentQuestion.getCorrectAnswer());
            }
            pauseAfterQuestionResult();
        }, DELAY_INTRIGUE);
    }


    public void pauseAfterQuestionResult() {
        Handler handler = new Handler();
        if (isWin()) {
            handler.postDelayed(this::nextQuestion, DELAY_AFTER_QUESTION_RESULT);
        } else {
            handler.postDelayed(this::wrongAnswer, DELAY_AFTER_QUESTION_RESULT);
        }
    }

    private void wrongAnswer() {
        if (onEndGameListener != null) {
            onEndGameListener.onEndGame();
        }
    }


    //Слушатели
    @FunctionalInterface
    public interface OnRoundResultListener {
        void onReportQuestionResult(String selectedAnswer, String correctAnswer);
    }

    @FunctionalInterface
    public interface OnSelectAnswerListener {
        void onSelectAnswer(String selectedAnswer);
    }

    @FunctionalInterface
    public interface OnSelectNewQuestionListener {
        void onSelectNewQuestion(Question question, Bet bet);
    }

    @FunctionalInterface
    public interface OnEndGameListener {
        void onEndGame();
    }


}
