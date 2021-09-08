package com.example.gamemillionaire;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.example.gamemillionair.R;

import java.net.InetSocketAddress;


class DialogConnect extends Dialog implements IToast {
    private static final String MESSAGE_ADDRESS_FAILED = "Неправильный хост или порт сервера";

    private Activity activity;
    private EditText etHost;
    private EditText etPort;
    private Button btnConnectOk;
    private Button btnConnectCancel;
    private OnClickConnectListener onClickConnectListener;

    public DialogConnect(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_connect_layout);

        etHost = findViewById(R.id.etHost);
        etPort = findViewById(R.id.etPort);

        btnConnectCancel = findViewById(R.id.btnConnectCancel);
        btnConnectCancel.setOnClickListener(this::clickCancel);

        btnConnectOk = findViewById(R.id.btnConnectOk);
        btnConnectOk.setOnClickListener(this::clickConnect);
    }

    private void clickCancel(View view) {
        dismiss();
    }

    private void clickConnect(View view) {
        InetSocketAddress socketAddress;
        try {
            socketAddress = createSocketAddress();
        } catch (Exception ex) {
            shortToast(getContext(), MESSAGE_ADDRESS_FAILED);
            return;
        }

        if(onClickConnectListener != null) {
            onClickConnectListener.action(socketAddress);
        }
        dismiss();
    }

    private InetSocketAddress createSocketAddress(){
        String host = etHost.getText().toString();
        int port = Integer.parseInt(etPort.getText().toString());

        return new InetSocketAddress(host, port);
    }

    public void setOnClickConnectListener(OnClickConnectListener onClickConnectListener) {
        this.onClickConnectListener = onClickConnectListener;
    }

    interface OnClickConnectListener {
        void action(InetSocketAddress socketAddress);
    }
}
