package com.example.gamemillionair;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CsvReader implements IConst {
    private static final String FORMAT_MESSAGE_FAIL_READ_FILE = "Не удалось прочитать файл %s";
    private final static String KEY_LOG = "CsvReader";

    private ArrayList<String> list;
    private OnEndReadStringsListener onEndReadStringsListener;
    private AssetManager assetManager;
    private boolean isExecute;

    public CsvReader(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public void setOnEndReadStringListener(OnEndReadStringsListener onEndReadStringsListener) {
        this.onEndReadStringsListener = onEndReadStringsListener;
    }

    public void read(String fileName) {
        ReaderTask readerTask = new ReaderTask();
        readerTask.execute("");
    }

    public boolean isExecute() {
        return isExecute;
    }

    //
    private class ReaderTask extends AsyncTask<String, Void, DataStrings> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isExecute = true;
        }

        @Override
        protected DataStrings doInBackground(String... params) {
            String fileName = params[0];
            list = new ArrayList<>();
            Exception exception = null;
            try{
                InputStream file = assetManager.open(fileName);
                BufferedReader reader = new BufferedReader(new InputStreamReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    list.add(line);
                }
                reader.close();
            } catch(IOException e){
                String message = String.format(FORMAT_MESSAGE_FAIL_READ_FILE, fileName);
                Log.e(KEY_LOG, message);
                exception = new CsvReaderException(message);
            }
            return new DataStrings(list, exception);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(DataStrings dataStrings) {
            super.onPostExecute(dataStrings);
            isExecute = false;
            if(onEndReadStringsListener != null) {
                onEndReadStringsListener.action(dataStrings);
            }
        }
    }


}
