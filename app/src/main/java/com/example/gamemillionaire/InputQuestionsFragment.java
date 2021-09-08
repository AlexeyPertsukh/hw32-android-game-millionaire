package com.example.gamemillionaire;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.gamemillionair.R;
import com.example.gamemillionaire.question.Question;
import com.example.gamemillionaire.question.QuestionFabric;
import com.example.gamemillionaire.question.QuestionFabricException;
import com.example.gamemillionaire.readers.DataStrings;
import com.example.gamemillionaire.readers.FileReader;
import com.example.gamemillionaire.readers.TcpClient;

import java.net.InetSocketAddress;
import java.util.ArrayList;

public class InputQuestionsFragment extends Fragment implements IToast, IConst {

//    private static final String MESSAGE_CSV_CONVERT_FILED = "ошибка преобразования вопроса из формата csv";
//    private static final String MESSAGE_JSON_CONVERT_FILED = "ошибка преобразования вопроса из формата json";

    private Button btnServerQuestions;
    private Button btnLocalQuestions;

    private TcpClient tcpClient;
    private FileReader fileReader;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input_questions, container, false);
        initViews(view);
        initListeners();

        initTcpClient();
        initCsvReader();

        return view;
    }

    private void initViews(View view) {
        btnLocalQuestions = view.findViewById(R.id.btnLocalQuestions);
        btnServerQuestions = view.findViewById(R.id.btnServerQuestions);
    }

    private void initListeners() {
        btnLocalQuestions.setOnClickListener(this::loadQuestionsFromCsvFile);
        btnServerQuestions.setOnClickListener(this::showDialogConnect);
    }

    private void loadQuestionsFromCsvFile(View view) {
        if(fileReader.isExecute() || tcpClient.isExecute()) {
            return;
        }

        fileReader.read(FILE_NAME_CSV_QUESTIONS);
    }

    private void showDialogConnect(View view) {
        if(fileReader.isExecute() || tcpClient.isExecute()) {
            return;
        }

        DialogConnect dialogConnect = new DialogConnect(getActivity());
        dialogConnect.setOnClickConnectListener(this::loadQuestionsFromServer);
        dialogConnect.show();
    }

    private void loadQuestionsFromServer(InetSocketAddress socketAddress) {
        if(fileReader.isExecute() || tcpClient.isExecute()) {
            return;
        }

        try {
            tcpClient.connect(socketAddress);
        } catch (Exception ex) {
            showToast(getContext(), "Не удалось прочитать вопросы с сервера!!!");
        }
    }



    private void initTcpClient() {
        tcpClient = new TcpClient();
        tcpClient.setOnEndReadStringsListener(this::onEndReadStringsFromServer);
    }

    private void initCsvReader() {
        if(getActivity() != null) {
            fileReader = new FileReader(getActivity().getAssets());
            fileReader.setOnEndReadStringListener(this::onEndReadStringsFromCsv);

        }
    }

    public void onEndReadStringsFromServer(DataStrings dataStrings) {
        if(dataStrings.isError()) {
            showToast(getContext(), dataStrings.getExceptionMessage());
            return;
        }

        try {
            ArrayList<Question> listQuestion = QuestionFabric.createFromJson(dataStrings.getList());
            goToGame(listQuestion);
        } catch (QuestionFabricException ex) {
            showToast(getContext(), ex.getMessage());
        }
    }

    private void onEndReadStringsFromCsv(DataStrings dataStrings) {
        if(dataStrings.isError()) {
            showToast(getContext(), dataStrings.getExceptionMessage());
            return;
        }

        ArrayList<Question> listQuestion;
        try {
            listQuestion = QuestionFabric.createFromCsv(dataStrings.getList());
            goToGame(listQuestion);
        } catch (QuestionFabricException ex) {
            showToast(getContext(), ex.getMessage());
        }
    }

    private void goToGame(ArrayList<Question> listQuestion) {
        MainActivity ma = (MainActivity) getActivity();
        if(ma != null) {
            ma.showGameFragment(listQuestion);
        }
    }

}