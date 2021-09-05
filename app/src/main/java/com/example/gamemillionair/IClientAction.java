package com.example.gamemillionair;

import java.util.ArrayList;

public interface IClientAction {
    void connect(String host, int port);

    void tcpClientAction(ArrayList<String> list);
}
