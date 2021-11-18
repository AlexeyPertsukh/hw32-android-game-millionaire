package com.example.gamemillionaire;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.gamemillionair.R;
import com.example.gamemillionaire.constants.IConst;
import com.example.gamemillionaire.model_game_with_listeners.GameWithListeners;
import com.example.gamemillionaire.model_question.Question;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements IToast, IConst {

    private GameFragment gameFragment;
    private InputQuestionsFragment inputQuestionsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragments();
        showInputQuestionsFragment();
    }

    private void initFragments() {
        gameFragment = new GameFragment();
        inputQuestionsFragment = new InputQuestionsFragment();
    }

    public void showInputQuestionsFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, inputQuestionsFragment)
                .commit();
    }

    public void showGameFragment(ArrayList<Question> listQuestion) {
        Bundle args = new Bundle();
        GameWithListeners game = new GameWithListeners(listQuestion);

        args.putSerializable(KEY_GAME, game);

        gameFragment.setArguments(args);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, gameFragment)
                .commit();
    }



}