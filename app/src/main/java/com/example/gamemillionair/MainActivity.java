package com.example.gamemillionair;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.io.IOException;
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
        Game game = new Game(listQuestion);

        args.putSerializable(KEY_GAME, game);

        gameFragment.setArguments(args);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, gameFragment)
                .commit();
    }





}