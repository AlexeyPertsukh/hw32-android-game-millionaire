package com.example.gamemillionair;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IToast, IConst, IClientAction {

    private ConnectFragment connectFragment;
    private GameFragment gameFragment;
    private TcpClient tcpClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tcpClient = new TcpClient(this);

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

    @Override
    public void connect(String host, int port) {
        tcpClient.connect(host, port);
    }

    @Override
    public void tcpClientAction(ArrayList<String> list) {
        showGameFragment(list);
    }

}