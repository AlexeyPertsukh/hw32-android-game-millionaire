package com.example.gamemillionair;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;


public class ConnectFragment extends Fragment {

    private Button btnConnect;
    private EditText etHost;
    private EditText etPort;

    private TcpClient tcpClient;


    public ConnectFragment() {

    }

    public static ConnectFragment newInstance(String param1, String param2) {
        ConnectFragment fragment = new ConnectFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

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
        View view = inflater.inflate(R.layout.fragment_connect, container, false);

        tcpClient = new TcpClient();
//        tcpClient.setOnReadListener(this::onReadStringQuestions);

        initViews(view);
        initListeners(view);

        return view;
    }

    private void initViews(View view) {
        btnConnect = view.findViewById(R.id.btnConnect);
        etHost =  view.findViewById(R.id.etHost);
        etPort =  view.findViewById(R.id.etPort);
    }

    private void initListeners(View view) {
        btnConnect.setOnClickListener(this::clickConnect);
    }

    private void clickConnect(View view) {
        String host = etHost.getText().toString();
        int port = Integer.parseInt(etPort.getText().toString());

        tcpClient.connect(host, port);

    }

//    public void onReadStringQuestions(ArrayList<String> list, Exception ex) {
//        MainActivity ma = (MainActivity) getActivity();
//        if(ma != null) {
//            ma.showGameFragment(list);
//        }
//    }


}