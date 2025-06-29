package com.example.wheelchairapp;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketClient extends AsyncTask<Void, Void, Void> {

    private String host;
    private int port;
    private String dataToSend;
    private boolean isRunning = true;

    public SocketClient(String host, int port, String dataToSend) {
        this.host = host;
        this.port = port;
        this.dataToSend = dataToSend;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Socket clientSocket = null;

        try {
            // 소켓 생성
            Log.d("SocketClient", "Creating socket...");
            clientSocket = new Socket(host, port);
            Log.d("SocketClient", "Socket created.");

            // 데이터 전송
            sendData(clientSocket);
        } catch (IOException e) {
            Log.e("SocketClient", "Error during socket operation: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 소켓 닫기
            if (clientSocket != null && !clientSocket.isClosed()) {
                try {
                    clientSocket.close();
                    Log.d("SocketClient", "Socket closed.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    private void sendData(Socket clientSocket) throws IOException {
        if (clientSocket != null && clientSocket.isConnected()) {
            OutputStream outputStream = clientSocket.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream, true);
            Log.d("SocketClient", "Sending data: " + dataToSend);
            printWriter.println(dataToSend);
            Log.d("SocketClient", "Data sent.");
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        // 작업이 취소되면 여기에서 추가적인 정리 작업을 수행할 수 있습니다.
        Log.d("SocketClient", "Task cancelled. Cleaning up...");
        isRunning = false;
    }
}
