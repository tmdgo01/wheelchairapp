package com.example.wheelchairapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 버튼 클릭 시 소켓 통신 실행
        Button sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 소켓 통신 실행
                executeSocketCommunication("connected");
            }
        });

        // 목적지 설정 버튼 클릭 시 목적지 설정 모드 확인 대화상자 표시
        Button setDestinationButton = findViewById(R.id.setDestinationButton);
        setDestinationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDestinationModeConfirmationDialog();
            }
        });

        // 추적 모드 버튼 클릭 시 확인 대화상자 표시
        Button changeModeButton = findViewById(R.id.changeModeButton);
        changeModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmationDialog();
            }
        });
    }

    private void executeSocketCommunication(String dataToSend) {
        // 서버로 전송할 데이터
        String host = "192.168.205.135";
        int port = 33433;

        // SocketClient 객체 생성 및 실행
        SocketClient socketClient = new SocketClient(host, port, dataToSend);
        socketClient.execute();
    }

    private void showConfirmationDialog() {
        // Create an AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("추적 모드 변경 확인");
        builder.setMessage("추적 모드로 변경하시겠습니까?");

        // Add the buttons
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Handle 'Yes' button click
                sendCommandToServer("logomode");
                Intent intent = new Intent(MainActivity.this, TrackingActivity.class);
                startActivity(intent);
                dialog.dismiss(); // Dismiss the dialog
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Handle 'No' button click
                dialog.dismiss(); // Dismiss the dialog
            }
        });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDestinationModeConfirmationDialog() {
        // Create an AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("목적지 설정 모드 변경 확인");
        builder.setMessage("목적지 설정 모드로 변경하시겠습니까?");

        // Add the buttons
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Handle 'Yes' button click
                sendCommandToServer("navmode"); // Modified line to send 'navmode'
                Intent intent = new Intent(MainActivity.this, SetDestinationActivity.class);
                startActivity(intent);
                dialog.dismiss(); // Dismiss the dialog
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Handle 'No' button click
                dialog.dismiss(); // Dismiss the dialog
            }
        });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void sendCommandToServer(String command) {
        // 서버로 전송할 데이터
        String host = "192.168.205.135";
        int port = 33433;

        // Initialize and modify socket message if needed
        String initializedCommand = initializeSocketMessage(command);

        // SocketClient 객체 생성 및 실행
        SocketClient socketClient = new SocketClient(host, port, initializedCommand);
        socketClient.execute();
    }

    private String initializeSocketMessage(String originalCommand) {
        // Perform any necessary initialization here
        // For example, add a prefix or modify the original command

        // Return the initialized message
        return originalCommand;
    }

    private static class SocketClient extends AsyncTask<Void, Void, Void> {

        private Socket socket;
        private String host;
        private int port;
        private String message;

        public SocketClient(String host, int port, String message) {
            this.host = host;
            this.port = port;
            this.message = message;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                socket = new Socket(host, port);

                // 데이터 전송
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                writer.write(message);
                writer.flush();

                Log.d(TAG, "Socket communication successful: " + message);

                // 여기에서 필요한 추가 작업 수행

            } catch (IOException e) {
                Log.e(TAG, "Socket communication error: " + e.getMessage());
                e.printStackTrace();
            } finally {
                try {
                    if (socket != null && !socket.isClosed()) {
                        socket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
    }
}
