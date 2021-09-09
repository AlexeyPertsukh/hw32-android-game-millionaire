package com.example.gamemillionaire;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.gamemillionair.R;
import com.example.gamemillionaire.question.Question;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.gamemillionair.R.drawable.*;


public class GameFragment extends Fragment implements IConst, IToast {

    private static final String[] LETTERS = {"A", "B", "C", "D"};

    private Game game;

    private TextView tvQuestion;
    private TextView[] tvAnswers;
    private TextView tvQuestionBet;

    private Map<String, TextView> map;

    public GameFragment() {
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

        tvQuestionBet.setOnClickListener(this::test);

        return view;
    }

    private void test(View view) {
        showDialogResult();
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
        tvQuestionBet = view.findViewById(R.id.tvQuestionBet);
        tvAnswers[0] = view.findViewById(R.id.tvAnswer1);
        tvAnswers[1] = view.findViewById(R.id.tvAnswer2);
        tvAnswers[2] = view.findViewById(R.id.tvAnswer3);
        tvAnswers[3] = view.findViewById(R.id.tvAnswer4);

        setAllTvAnswersWhite();
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
    }

    void showResult(String selectedAnswer, String correctAnswer) {
        TextView tvCorrectAnswer = map.get(correctAnswer);
        TextView tvSelectedAnswer = map.get(selectedAnswer);

        setTextViewGreen(tvCorrectAnswer);
        if (tvSelectedAnswer != tvCorrectAnswer) {
            setTextViewRed(tvSelectedAnswer);
        }
    }

    void showNewQuestion(Question question, int bet) {

        if(game.isEnd()) {
            longToast(getContext(), "ВСЕ!");
            showDialogResult();
            return;
        }

        setAllTvAnswersWhite();
        List<String> allAnswers = question.getShuffledAllAnswers();

        tvQuestion.setText(question.getStrQuestion());
        @SuppressLint("DefaultLocale") String stringBet = String.format("Вопрос: %d ₽", bet);
        tvQuestionBet.setText(stringBet);

        map = new HashMap<>();
        for (int i = 0; i < allAnswers.size(); i++) {
            TextView tvAnswer = tvAnswers[i];
            String answer = allAnswers.get(i);
            String letter = LETTERS[i];

            String letterAndAnswer = String.format("%s. %s", letter, answer);

            tvAnswer.setText(letterAndAnswer);
            tvAnswer.setTag(answer);
            map.put(answer, tvAnswer);
        }
    }


    private void showDialogResult() {

        DialogFragmentGameResult dialog = new DialogFragmentGameResult();
        Bundle args = new Bundle();
        args.putSerializable(KEY_GAME, game);
        dialog.setArguments(args);
        dialog.show(getActivity().getSupportFragmentManager(), "custom");
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