package com.example.gamemillionair;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

public class TcpClient implements IConst {
    private final static String FORMAT_MESSAGE_FAILED_CONNECTION = "не удалось получить данные с сервера %s:%d ";
    private final static String QUERY = "select *";
    private final static String KEY_LOG = "TcpClient";

    private OnEndReadStringsListener onEndReadStringsListener;

    public TcpClient() {

    }

    public void setOnEndReadStringsListener(OnEndReadStringsListener onEndReadStringsListener) {
        this.onEndReadStringsListener = onEndReadStringsListener;
    }

    public void connect(String host, int port) {

        InetSocketAddress inetSocketAddress = new InetSocketAddress(host, port);

        ConnectTask connectTask = new ConnectTask();
        InetSocketAddress socketAddress = new InetSocketAddress(host, port);
        connectTask.execute(socketAddress);
    }

    //
    class ConnectTask extends AsyncTask<InetSocketAddress, Void, DataStrings> {

        private Socket socket;
        private PrintWriter printWriter;

        private ArrayList<String> list;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected DataStrings doInBackground(InetSocketAddress... socketAddresses) {
            Exception exception = null;
            list = new ArrayList<>();
            InetSocketAddress socketAddress = socketAddresses[0];
            try {
                socket = new Socket();
                socket.connect(socketAddress, 5000);
                if(socket.isConnected()) {
                    sendQuery();
                    readServer();
                    throw new IOException("!!!!");
                }
            } catch (IOException e) {
                Log.d("LOG","IOException on doInBackground");
                @SuppressLint("DefaultLocale") String message = String.format(FORMAT_MESSAGE_FAILED_CONNECTION,
                        socketAddress.getHostString(), socketAddress.getPort());
                exception = new ConnectException(message);
            }
            return new DataStrings(list, exception);
        }

        @Override
        protected void onPostExecute(DataStrings dataStrings) {
            super.onPostExecute(dataStrings);

            if(onEndReadStringsListener != null) {
                onEndReadStringsListener.action(dataStrings);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        private void sendQuery() {
            try {
                printWriter = new PrintWriter(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(KEY_LOG,"IOException on sendQuery()");
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

            } catch (IOException e) {
                e.printStackTrace();
                Log.d(KEY_LOG,"IOException on readServer()");
            }
        }
    }


}
