package com.example.gamemillionair;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class InputQuestionsFragment extends Fragment implements IToast {

    private Button btnServerQuestions;
    private Button btnLocalQuestions;

    private TcpClient tcpClient;


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

        return view;
    }

    private void initViews(View view) {
        btnLocalQuestions = view.findViewById(R.id.btnLocalQuestions);
        btnServerQuestions = view.findViewById(R.id.btnServerQuestions);
    }

    private void initListeners() {
        btnLocalQuestions.setOnClickListener(this::loadQuestionsFromFile);
        btnServerQuestions.setOnClickListener(this::loadQuestionsFromServer);
    }

    private void loadQuestionsFromFile(View view) {

    }

    private void loadQuestionsFromServer(View view) {
//        String host = etHost.getText().toString();
//        int port = Integer.parseInt(etPort.getText().toString());

//        tcpClient.connect(host, port);
//        showToast(getContext(), "!!!");
        try {
            tcpClient.connect("192.168.0.102", 6789);
        } catch (Exception ex) {
            showToast(getContext(), "Не удалось прочитать вопросы с сервера");
        }
    }

    private void initTcpClient() {
        tcpClient = new TcpClient();
        tcpClient.setOnReadListener(this::onReadStringQuestions);
    }

    public void onReadStringQuestions(DataQuestions dataQuestions) {
        if(dataQuestions.isError()) {
            showToast(getContext(), dataQuestions.getExceptionMessage());
            return;
        }
        MainActivity ma = (MainActivity) getActivity();
        if(ma != null) {
            ma.showGameFragment(dataQuestions.getListStrQuestions());
        }
    }

}