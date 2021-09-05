package com.example.gamemillionair;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IToast, IConst, IConnect {

    private ConnectFragment connectFragment;
    private GameFragment gameFragment;
    private ConnectTask connectTask;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        ArrayList<Question> questions = new ArrayList<>();

        for (String jsonString : list) {
            questions.add(Question.of(jsonString));
        };

        args.putSerializable(KEY_QUESTIONS, questions);

        gameFragment.setArguments(args);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, gameFragment)
                .commit();
    }

    @Override
    public void connect(String host, int port) {

        InetSocketAddress inetSocketAddress = new InetSocketAddress(host, port);

        connectTask = new ConnectTask();
        SocketAddress socketAddress = new InetSocketAddress(host, port);
        connectTask.execute(socketAddress);
    }


    class ConnectTask extends AsyncTask<SocketAddress, Integer, ArrayList<String>> {
        private final String QUERY = "select *";
        private Socket socket;
        private PrintWriter printWriter;

        private ArrayList<String> list;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected ArrayList<String> doInBackground(SocketAddress... socketAddresses) {
            list = new ArrayList<>();
            try {
                socket = new Socket();
                socket.connect(socketAddresses[0], 5000);
                if(socket.isConnected()) {
                    sendQuery();
                    readServer();
                }
            } catch (IOException e) {
                e.printStackTrace();
                showToast(getApplicationContext(),"IOException on doInBackground");
                Log.d("LOG","IOException on doInBackground");
                return null;
            }
            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<String> list) {
            super.onPostExecute(list);

            showGameFragment(list);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
//            pbProgress.setProgress(values[0]);
//            tvResult.setText(String.valueOf(values[0]));
        }

        private void sendQuery() {
            try {
                printWriter = new PrintWriter(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("LOG","IOException on sendQuery()");
            }
            printWriter.println(QUERY);
            printWriter.flush();
        }

        private void readServer() {
            String text;
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                int i = 0;
                while ((text = in.readLine()) != null) {
                    list.add(text);
                    if(i++ >= 32) {
                        break;
                    }
                }

            } catch (IOException ex) {
                Log.d("LOG","IOException on readServer()");
            }
        }

        public List<String> getList() {
            return list;
        }

    }

}