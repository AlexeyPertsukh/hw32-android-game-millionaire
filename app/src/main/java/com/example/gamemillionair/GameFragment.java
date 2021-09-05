package com.example.gamemillionair;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.gamemillionair.R.drawable.*;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameFragment extends Fragment implements IConst, IToast, IClickItem {

    private RecyclerView rvAnswers;
    RecyclerView.Adapter adapter;
    TextView tvQuestion;
    TextView tvAnswer1;
    TextView tvAnswer2;
    TextView tvAnswer3;
    TextView tvAnswer4;

    Question currentQuestion;

    private ArrayList<String> listAnswers;
    ArrayList<Question> questions;
    ArrayList<Question> workQuestions;

    boolean isChecked;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConnectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GameFragment newInstance(String param1, String param2) {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        initViews(view);
        initListeners();
        listAnswers = new ArrayList<>();

        if(getArguments() != null) {
            Game game = (Game) getArguments().getSerializable(KEY_GAME);
            questions = game.getActualQuestions();
            workQuestions = new ArrayList<>(questions);
            Collections.shuffle(workQuestions);
            setAndShowQuestion();
        }

        return view;
    }

    private void initListeners() {
        tvAnswer1.setOnClickListener(this::clickAnswer);
        tvAnswer2.setOnClickListener(this::clickAnswer);
        tvAnswer3.setOnClickListener(this::clickAnswer);
        tvAnswer4.setOnClickListener(this::clickAnswer);
    }

    private void clickAnswer(View view) {
        if(isChecked) {
            return;
        }
        isChecked = true;
        click((int)view.getTag());
    }

    private void initViews(View view) {

        tvQuestion = view.findViewById(R.id.tvQuestion);
        tvAnswer1 = view.findViewById(R.id.tvAnswer1);
        tvAnswer2 = view.findViewById(R.id.tvAnswer2);
        tvAnswer3 = view.findViewById(R.id.tvAnswer3);
        tvAnswer4 = view.findViewById(R.id.tvAnswer4);

        reinitTextViews();

    }


    private void reinitTextViews() {
        isChecked = false;
        TextView[] tvs = {tvAnswer1, tvAnswer2, tvAnswer3, tvAnswer4};
        for (int i = 0; i < tvs.length; i++) {
            TextView tv = tvs[i];
            tv.setTag(i);
            tv.setBackgroundResource(draw_tv_white);
        }
    }

    private void setAndShowQuestion() {
        reinitTextViews();
        if(workQuestions.size() <= 0) {
            workQuestions = new ArrayList<>(questions);
        }
        currentQuestion = workQuestions.remove(random(workQuestions.size()));
        listAnswers = new ArrayList<>(currentQuestion.getWrongAnswers());
        listAnswers.add(currentQuestion.getCorrectAnswer());
        Collections.shuffle(listAnswers);

        tvQuestion.setText(currentQuestion.getStrQuestion());
        tvAnswer1.setText("  A. " + listAnswers.get(0));
        tvAnswer2.setText("  B. " + listAnswers.get(1));
        tvAnswer3.setText("  C. " + listAnswers.get(2));
        tvAnswer4.setText("  D. " + listAnswers.get(3));


    }


    void pause() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void click(int num) {
        TextView tv = tvByNum(num);
        tv.setBackgroundResource(draw_tv_orange);

        Handler handlerPause1 = new Handler();
        handlerPause1.postDelayed(() -> {
            showCorrectAnswer(tv, num);
        }, 3_000);

    }

    private void showCorrectAnswer(TextView tv, int num) {
        if(currentQuestion.checkCorrectAnswer(listAnswers.get(num))) {
            tv.setBackgroundResource(draw_tv_green);
        } else {
            tv.setBackgroundResource(draw_tv_red);
            TextView tvCorrectAnswer = getTvCorrectAnswer();
            tvCorrectAnswer.setBackgroundResource(draw_tv_green);
        }

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            setAndShowQuestion();
        }, 3_000);
    }

    int random(int max) {
        return (int) (Math.random() * max);
    }


    private TextView tvByNum(int num) {
        if(num == 0) {
            return tvAnswer1;
        } else if(num == 1) {
            return tvAnswer2;
        } else if(num == 2) {
            return tvAnswer3;
        } else if(num == 3) {
            return tvAnswer4;
        }
        return null;
    }


    private TextView getTvCorrectAnswer() {
        TextView[] tvs = getTvAnswers();

        for (int i = 0; i < listAnswers.size(); i++) {
            if(currentQuestion.checkCorrectAnswer(listAnswers.get(i))) {
                return tvs[i];
            }
        }
        return null;
    }

    private TextView[] getTvAnswers() {
        return new TextView[]{tvAnswer1, tvAnswer2, tvAnswer3, tvAnswer4};
    }

}