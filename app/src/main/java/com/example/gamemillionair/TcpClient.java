package com.example.gamemillionair;

import android.os.AsyncTask;
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

public class TcpClient implements IConst {
    private final static String QUERY = "select *";
    private ConnectTask connectTask;

    private String host;
    private int port;
    private OnReadStringQuestionsListener onReadStringQuestionsListener;

    public TcpClient() {

    }


    public void setOnReadListener(OnReadStringQuestionsListener onReadStringQuestionsListener) {
        this.onReadStringQuestionsListener = onReadStringQuestionsListener;
    }

    public void connect(String host, int port) {

        this.host = host;
        this.port = port;

        InetSocketAddress inetSocketAddress = new InetSocketAddress(host, port);

        connectTask = new ConnectTask();
        SocketAddress socketAddress = new InetSocketAddress(host, port);
        connectTask.execute(socketAddress);
    }


    //
    class ConnectTask extends AsyncTask<SocketAddress, Integer, ArrayList<String>> {

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
//                showToast(getCo,"IOException on doInBackground");
                Log.d("LOG","IOException on doInBackground");
                return null;
            }
            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<String> list) {
            super.onPostExecute(list);
            if(onReadStringQuestionsListener != null) {
                onReadStringQuestionsListener.onReadStringQuestions(list);
            }

//            showGameFragment(list);
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

    //
    public interface OnReadStringQuestionsListener {
        void onReadStringQuestions(ArrayList<String> list);
    }
}
