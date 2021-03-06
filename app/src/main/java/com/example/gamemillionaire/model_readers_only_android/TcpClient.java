package com.example.gamemillionaire.model_readers_only_android;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import com.example.gamemillionaire.constants.IConst;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class TcpClient implements IConst {
    private final static String FORMAT_MESSAGE_FAILED_CONNECTION = "Не удалось получить данные с сервера %s : %d ";

    private boolean isExecute;

    private OnEndReadStringsListener onEndReadStringsListener;

    public TcpClient() {

    }

    public void setOnEndReadStringsListener(OnEndReadStringsListener onEndReadStringsListener) {
        this.onEndReadStringsListener = onEndReadStringsListener;
    }

    public void read(InetSocketAddress socketAddress) {
        ConnectTask connectTask = new ConnectTask();
        connectTask.execute(socketAddress);
    }

    public boolean isExecute() {
        return isExecute;
    }

    //
    @SuppressLint("StaticFieldLeak")
    class ConnectTask extends AsyncTask<InetSocketAddress, Void, DataStrings> {

        private Socket socket;

        private ArrayList<String> strings;

        @Override
        protected void onPreExecute() {
            isExecute = true;
        }

        @Override
        protected DataStrings doInBackground(InetSocketAddress... socketAddresses) {
            Exception exception = null;
            strings = new ArrayList<>();
            InetSocketAddress socketAddress = socketAddresses[0];
            try {
                socket = new Socket();
                socket.connect(socketAddress, 3000);
                if (socket.isConnected()) {
                    sendQuery();
                    readServer();
                }
                socket.close();
            } catch (IOException | ClassNotFoundException e) {
                Log.d("LOG", "IOException on doInBackground");
                @SuppressLint("DefaultLocale") String message = String.format(FORMAT_MESSAGE_FAILED_CONNECTION,
                        socketAddress.getHostString(), socketAddress.getPort());
                exception = new ConnectException(message);
            }
            return new DataStrings(strings, exception);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(DataStrings dataStrings) {
            super.onPostExecute(dataStrings);
            if(onEndReadStringsListener != null) {
                onEndReadStringsListener.action(dataStrings);
            }
        }

        private void sendQuery() throws IOException {
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
            printWriter.println(QUERY);
            printWriter.flush();
        }

        private void readServer() throws IOException, ClassNotFoundException {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            strings = (ArrayList<String>)in.readObject();
            in.close();
        }

    }


}
