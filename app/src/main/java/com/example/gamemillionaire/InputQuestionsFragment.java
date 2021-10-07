package com.example.gamemillionaire;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.gamemillionair.R;
import com.example.gamemillionaire.constants.IConst;
import com.example.gamemillionaire.model_question.Question;
import com.example.gamemillionaire.model_question.QuestionFabric;
import com.example.gamemillionaire.model_question.QuestionFabricException;
import com.example.gamemillionaire.model_readers.DataStrings;
import com.example.gamemillionaire.model_readers.FileReader;
import com.example.gamemillionaire.model_readers.TcpClient;

import java.net.InetSocketAddress;
import java.util.ArrayList;

public class InputQuestionsFragment extends Fragment implements IToast, IConst {

    private Button btnServerQuestions;
    private Button btnLocalQuestions;
    private ProgressBar pbConnect;

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
        pbConnect = view.findViewById(R.id.pbConnect);
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
        pbConnect.setVisibility(View.VISIBLE);
        try {
            tcpClient.read(socketAddress);
        } catch (Exception ex) {
            longToast(getContext(), "Не удалось прочитать вопросы с сервера");
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
        pbConnect.setVisibility(View.INVISIBLE);
        if(dataStrings.isError()) {
            shortToast(getContext(), dataStrings.getExceptionMessage());
            return;
        }

        try {
            ArrayList<Question> listQuestion = QuestionFabric.createFromJson(dataStrings.getList());
            goToGame(listQuestion);
        } catch (QuestionFabricException ex) {
            shortToast(getContext(), ex.getMessage());
        }
    }

    private void onEndReadStringsFromCsv(DataStrings dataStrings) {
        if(dataStrings.isError()) {
            shortToast(getContext(), dataStrings.getExceptionMessage());
            return;
        }

        ArrayList<Question> listQuestion;
        try {
            listQuestion = QuestionFabric.createFromCsv(dataStrings.getList());
            goToGame(listQuestion);
        } catch (QuestionFabricException ex) {
            shortToast(getContext(), ex.getMessage());
        }
    }

    private void goToGame(ArrayList<Question> listQuestion) {
        MainActivity ma = (MainActivity) getActivity();
        if(ma != null) {
            ma.showGameFragment(listQuestion);
        }
    }

}