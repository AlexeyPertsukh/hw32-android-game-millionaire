package com.example.gamemillionair;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements IToast, IConst {

    private ConnectFragment connectFragment;
    private GameFragment gameFragment;
    private TcpClient tcpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tcpClient = new TcpClient();
        tcpClient.setOnReadListener(this::onReadStringQuestions);

        initFragments();
        showConnectFragment();
    }

    private void initFragments() {
        connectFragment = new ConnectFragment();
        gameFragment = new GameFragment();
    }


    public void showConnectFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, connectFragment)
                .commit();
    }

    public void showGameFragment(ArrayList<String> list) {
        Bundle args = new Bundle();

        Game game = Game.ofJsonQuestions(list);

        args.putSerializable(KEY_GAME, game);

        gameFragment.setArguments(args);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, gameFragment)
                .commit();
    }


    public void connect(String host, int port) {
        tcpClient.connect(host, port);
    }

    public void onReadStringQuestions(ArrayList<String> list) {
        showGameFragment(list);
    }

}