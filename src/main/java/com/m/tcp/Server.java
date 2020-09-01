package com.m.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.Channel;

/**
 * @author zhijie.he created on 2020/7/23
 * @version 1.0
 */
public class Server {

    private ServerSocket serverSocket;

    public Server() {
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(8999));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void receive() {
        try {
            Socket socket = serverSocket.accept();
            Channel channel = socket.getChannel();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
