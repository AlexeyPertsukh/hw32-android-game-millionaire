package com.example.gamemillionaire;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.gamemillionair.R;

import java.util.HashMap;
import java.util.Map;

import static com.example.gamemillionair.R.drawable.*;


public class GameFragment extends Fragment implements IConst, IToast {

    private static final int DELAY_INTRIGUE = 3_000;
    private static final int DELAY_RESULT = 3_000;

    private Game game;

    private TextView tvQuestion;
    private TextView[] tvAnswers;

    private Map<String, TextView> map;

    public GameFragment() {
    }

    public static GameFragment newInstance(String param1, String param2) {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        initViews(view);
        initListeners();

        if(getArguments() != null) {
            game = (Game) getArguments().getSerializable(KEY_GAME);
            game.setOnSelectAnswerListener(this::showSelectAnswer);
            game.setOnSelectNewQuestionListener(this::showNewQuestion);
            game.setOnReportQuestionResultListener(this::showResult);
            game.nextQuestion();
        }
        return view;
    }

    private void initListeners() {
        for (TextView tvAnswer : tvAnswers) {
            tvAnswer.setOnClickListener(this::clickAnswer);
        }
    }

    private void clickAnswer(View view) {
        click((TextView) view);
    }

    private void initViews(View view) {

        tvAnswers = new TextView[4];
        tvQuestion = view.findViewById(R.id.tvQuestion);
        tvAnswers[0] = view.findViewById(R.id.tvAnswer1);
        tvAnswers[1] = view.findViewById(R.id.tvAnswer2);
        tvAnswers[2] = view.findViewById(R.id.tvAnswer3);
        tvAnswers[3] = view.findViewById(R.id.tvAnswer4);

        reinitTextViews();
    }

    private void reinitTextViews() {
        for (TextView tv : tvAnswers) {
            tv.setBackgroundResource(draw_tv_white);
        }
    }

    public void click(TextView tv) {
        game.sendAnswer((String)tv.getTag());
    }

    private void showSelectAnswer(String selectedAnswer) {
        TextView tv = map.get(selectedAnswer);
        tv.setBackgroundResource(draw_tv_orange);
    }

    void showResult(String selectedAnswer, String correctAnswer) {
        TextView tvCorrectAnswer = map.get(correctAnswer);
        TextView tvSelectedAnswer = map.get(selectedAnswer);

        tvCorrectAnswer.setBackgroundResource(draw_tv_green);
        if (tvSelectedAnswer != tvCorrectAnswer) {
            tvSelectedAnswer.setBackgroundResource(draw_tv_red);
        }
    }

    void showNewQuestion(String question, String... answers) {
        reinitTextViews();

        tvQuestion.setText(question);

        String[] letters = {"A", "B", "C", "D"};
        map = new HashMap<>();
        for (int i = 0; i < tvAnswers.length; i++) {
            String text = String.format("%s. %s", letters[i], answers[i]);
            tvAnswers[i].setText(text);
            tvAnswers[i].setTag(answers[i]);
            map.put(answers[i], tvAnswers[i]);
        }
    }

}