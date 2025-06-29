package com.example.wheelchairapp;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class TrackingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        // Stop Tracking Button
        Button stopTrackingButton = findViewById(R.id.stopTrackingButton);
        stopTrackingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStopTrackingConfirmationDialog();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // 뒤로가기 버튼이 눌렸을 때 종료 메시지 띄우기
        showStopTrackingConfirmationDialog();
    }

    private void showStopTrackingConfirmationDialog() {
        // Create an AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("추적 모드 종료 확인");
        builder.setMessage("추적 모드를 멈추시겠습니까?");

        // Add the buttons
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Handle 'Yes' button click
                // Add code to stop tracking (implement as needed)
                // ...

                // Send 'cancel' message through socket
                sendCancelMessageToServer();

                // Finish the TrackingActivity
                finish();
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

    private void sendCancelMessageToServer() {
        final String host = "192.168.205.135";
        final int port = 33433;

        // 'cancel' message
        final String cancelMessage = "cancel";

        // Send 'cancel' message through socket
        new Thread(new Runnable() {
            @Override
            public void run() {
                Socket socket = null;
                try {
                    socket = new Socket(host, port);
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    writer.write(cancelMessage);
                    writer.flush();
                } catch (IOException e) {
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
            }
        }).start();
    }
}
