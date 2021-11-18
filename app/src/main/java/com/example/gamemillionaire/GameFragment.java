package com.example.gamemillionaire;

import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.gamemillionair.R;
import com.example.gamemillionaire.constants.IConst;
import com.example.gamemillionaire.model_game.*;
import com.example.gamemillionaire.model_game_with_listeners.GameWithListeners;
import com.example.gamemillionaire.model_question.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.gamemillionair.R.drawable.*;


public class GameFragment extends Fragment implements IConst, IToast {

    private static final int CODE_SOUND_SELECT_ANSWER = 0;
    private static final int CODE_SOUND_WRONG_ANSWER = 1;
    private static final int CODE_SOUND_CORRECT_ANSWER = 2;

    private GameWithListeners game;

    private TextView tvQuestion;
    private TextView[] tvAnswers;
    private TextView tvQuestionBet;

    private TextView tvNewGame;

    private Map<String, TextView> map;

    private SoundPool soundPool;
    private int idSoundSelectAnswer;
    private int idSoundWrongAnswer;
    private int idSoundCorrectAnswer;

    public GameFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        initViews(view);
        initListeners();
        initSound();

        if(getArguments() != null) {
            game = (GameWithListeners) getArguments().getSerializable(KEY_GAME);
            game.setOnSelectAnswerListener(this::showSelectAnswer);
            game.setOnSelectNewQuestionListener(this::showNewQuestion);
            game.setOnRoundResultListener(this::showResult);
            game.setOnEndGameListener(this::onEndGame);
            game.start();
        }

        return view;
    }


    private void initListeners() {
        for (TextView tvAnswer : tvAnswers) {
            tvAnswer.setOnClickListener(this::clickAnswer);
        }
        tvNewGame.setOnClickListener(this::clickNewGame);
    }

    private void clickNewGame(View view) {
        game.start();
        tvNewGame.setVisibility(View.GONE);
    }

    private void clickAnswer(View view) {
        click((TextView) view);
    }

    private void initViews(View view) {

        tvAnswers = new TextView[4];
        tvQuestion = view.findViewById(R.id.tvQuestion);
        tvQuestionBet = view.findViewById(R.id.tvQuestionBet);
        tvAnswers[0] = view.findViewById(R.id.tvAnswer1);
        tvAnswers[1] = view.findViewById(R.id.tvAnswer2);
        tvAnswers[2] = view.findViewById(R.id.tvAnswer3);
        tvAnswers[3] = view.findViewById(R.id.tvAnswer4);

        tvNewGame = view.findViewById(R.id.tvNewGame);


        setAllTvAnswersWhite();
    }

    private void initSound() {
        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        idSoundWrongAnswer = soundPool.load(getActivity().getApplicationContext(), R.raw.answer_wrong,1);
        idSoundCorrectAnswer = soundPool.load(getActivity().getApplicationContext(), R.raw.answer_ok,1);
        idSoundSelectAnswer = soundPool.load(getActivity().getApplicationContext(), R.raw.click01,1);
    }

    private void setAllTvAnswersWhite() {
        for (TextView tv : tvAnswers) {
            setTextViewWhite(tv);
        }
    }

    public void click(TextView tv) {
        String answer = (String)tv.getTag();
        game.sendAnswer(answer);
    }

    private void showSelectAnswer(String selectedAnswer) {
        TextView tv = map.get(selectedAnswer);
        setTextViewOrange(tv);
        sound(CODE_SOUND_SELECT_ANSWER);
    }

    void showResult(String selectedAnswer, String correctAnswer) {
        TextView tvCorrectAnswer = map.get(correctAnswer);
        TextView tvSelectedAnswer = map.get(selectedAnswer);

        setTextViewGreen(tvCorrectAnswer);
        if (tvSelectedAnswer != tvCorrectAnswer) {
            setTextViewRed(tvSelectedAnswer);
            sound(CODE_SOUND_WRONG_ANSWER);
        } else {
            sound(CODE_SOUND_CORRECT_ANSWER);
        }
    }

    void showNewQuestion(Question question, Game.Bet bet) {
        tvNewGame.setVisibility(View.GONE);

        setAllTvAnswersWhite();
        List<String> allAnswers = question.getShuffledAllAnswers();

        tvQuestion.setText(question.getStrQuestion());
        @SuppressLint("DefaultLocale") String stringBet = String.format("Вопрос: %d %s", bet.getAmount(), MONEY_SIGN);
        tvQuestionBet.setText(stringBet);

        map = new HashMap<>();
        char letter = 'A';
        for (int i = 0; i < allAnswers.size(); i++) {
            TextView tvAnswer = tvAnswers[i];
            String answer = allAnswers.get(i);

            String letterAndAnswer = String.format("%c. %s", letter, answer);
            letter++;
            tvAnswer.setText(letterAndAnswer);
            tvAnswer.setTag(answer);
            map.put(answer, tvAnswer);
        }
    }

    private void showDialogGameResult() {
        DialogFragmentGameResult dialog = new DialogFragmentGameResult();
        Bundle args = new Bundle();
        args.putSerializable(KEY_GAME, game);
        dialog.setArguments(args);
        dialog.setOnClickNewGameListener(()->game.start());
        dialog.setOnClickQuitListener(()->getActivity().finish());
        dialog.show(getActivity().getSupportFragmentManager(), "custom");
        tvNewGame.setVisibility(View.VISIBLE);

    }

    private void onEndGame() {
//        longToast(getContext(),"LOSE");
        showDialogGameResult();
    }

    private void sound(int soundCode) {
        if(soundCode == CODE_SOUND_SELECT_ANSWER) {
            soundPool.play(idSoundSelectAnswer, 1, 1, 0, 0, 1);

        } else if(soundCode == CODE_SOUND_WRONG_ANSWER) {
            soundPool.play(idSoundWrongAnswer, 1, 1, 0, 0, 1);

        } else if(soundCode == CODE_SOUND_CORRECT_ANSWER) {
            soundPool.play(idSoundCorrectAnswer, 1, 1, 0, 0, 1);
        }

    }

    private void setTextViewWhite(TextView tv) {
        tv.setBackgroundResource(draw_tv_white);
    }

    private void setTextViewGreen(TextView tv) {
        tv.setBackgroundResource(draw_tv_green);
    }

    private void setTextViewRed(TextView tv) {
        tv.setBackgroundResource(draw_tv_red);
    }

    private void setTextViewOrange(TextView tv) {
        tv.setBackgroundResource(draw_tv_orange);
    }

}