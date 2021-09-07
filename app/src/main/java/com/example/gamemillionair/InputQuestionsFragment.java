package com.example.gamemillionair;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

public class InputQuestionsFragment extends Fragment implements IToast, IConst {

    private Button btnServerQuestions;
    private Button btnLocalQuestions;

    private TcpClient tcpClient;
    private FileReader fileReader;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
        btnServerQuestions.setOnClickListener(this::loadQuestionsFromServer);
    }

    private void loadQuestionsFromCsvFile(View view) {
        if(tcpClient.isExecute()) {
            return;
        }

        fileReader.read(FILE_NAME_CSV_QUESTIONS);

    }

    private void loadQuestionsFromServer(View view) {
        if(fileReader.isExecute()) {
            return;
        }
//        String host = etHost.getText().toString();
//        int port = Integer.parseInt(etPort.getText().toString());

//        tcpClient.connect(host, port);
//        showToast(getContext(), "!!!");
        try {
            tcpClient.connect("192.168.0.102", 6789);
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
        ArrayList<Question> listQuestion = QuestionFabric.createFromJson(dataStrings.getList());
        goToGame(listQuestion);

    }

    private void onEndReadStringsFromCsv(DataStrings dataStrings) {
        if(dataStrings.isError()) {
            showToast(getContext(), dataStrings.getExceptionMessage());
            return;
        }
        ArrayList<Question> listQuestion = QuestionFabric.createFromCsv(dataStrings.getList());
        goToGame(listQuestion);
    }

    private void goToGame(ArrayList<Question> listQuestion) {
        MainActivity ma = (MainActivity) getActivity();
        if(ma != null) {
            ma.showGameFragment(listQuestion);
        }
    }

}