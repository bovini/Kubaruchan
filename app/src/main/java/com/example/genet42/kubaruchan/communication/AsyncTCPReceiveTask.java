package com.example.genet42.kubaruchan.communication;

import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * TCPパケットを受信する非同期タスク
 */
public class AsyncTCPReceiveTask extends AsyncReceiveTask {
    private static final int SIZE_BUFFER = 65536;
    private static final int SIZE_DATA_RECEIVE = 16;
    private final ByteArrayRingBuffer buffer = new ByteArrayRingBuffer(SIZE_BUFFER);
    private int port;

    public AsyncTCPReceiveTask(int port) {
        this.port = port;
    }

    @Override
    protected Void doInBackground(Void... params) {
        byte[] dataReceived = new byte[SIZE_DATA_RECEIVE];
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (isActive()) {
                Log.i("AsyncTCPReceiveTask", "waiting for connection...");
                Socket socket = serverSocket.accept();
                Log.i("AsyncTCPReceiveTask", "connection accepted: " + socket.toString());
                DataInputStream in = new DataInputStream(socket.getInputStream());
                while (!socket.isClosed()) {
                    try {
                        int lengthReceived = in.read(dataReceived);
                        // Log.i("AsyncTCPReceiveTask", "received length: " + lengthReceived);
                        buffer.offer(dataReceived, lengthReceived);
                    } catch (SocketException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public byte[] getData() {
        return buffer.pollAll();
    }
}
