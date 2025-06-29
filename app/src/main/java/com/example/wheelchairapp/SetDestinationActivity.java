package com.example.wheelchairapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class SetDestinationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_destination);

        setButtonClickEvent(R.id.surgeryRoom1Button, "외과 진료실1", 1);
        setButtonClickEvent(R.id.surgeryRoom2Button, "외과 진료실2", 2);
        setButtonClickEvent(R.id.internalMedicineRoom1Button, "내과 진료실1", 3);
        setButtonClickEvent(R.id.ctRoomButton, "CT 촬영실", 4);
        setButtonClickEvent(R.id.cancelDestinationButton, "", 5);
    }

    @Override
    public void onBackPressed() {
        showExitConfirmationDialog();
    }

    private void setButtonClickEvent(int buttonId, final String roomName, final double x) {
        Button button = findViewById(buttonId);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmationDialog(roomName, x);
            }
        });
    }

    private void showConfirmationDialog(final String roomName, final double x) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String confirmationMessage = roomName.isEmpty() ? "목적지 이동을 취소하시겠습니까?" : roomName + "으로 이동하시겠습니까?";
        builder.setTitle("이동 확인")
                .setMessage(confirmationMessage)
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (roomName.isEmpty()) {
                            // Handle cancel destination button click
                            cancelDestination();
                            navigateToMainActivity();
                        } else {
                            // Handle other room buttons click
                            sendCoordinatesToServer(roomName, x);
                        }
                    }
                })
                .setNegativeButton("아니오", null);
        builder.create().show();
    }

    private void cancelDestination() {
        // Implement logic to handle the cancel destination button click
        sendCoordinatesToServer("Cancel", 5.0);
        Toast.makeText(this, "목적지 이동이 취소되었습니다.", Toast.LENGTH_SHORT).show();
    }

    private void sendCoordinatesToServer(String roomName, double x) {
        String host = "192.168.205.135";
        int port = 33433;

        String coordinatesData = String.valueOf(x);
        new SocketClient(host, port, coordinatesData).execute();

        Log.d("SetDestinationActivity", roomName + "으로 이동합니다.");
        Toast.makeText(this, roomName + "으로 이동합니다.", Toast.LENGTH_SHORT).show();
    }

    private void showExitConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("종료 확인")
                .setMessage("목적지 설정 모드를 종료하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        navigateToMainActivity();
                    }
                })
                .setNegativeButton("아니오", null);
        builder.create().show();
    }

    private void navigateToMainActivity() {
        finish();
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

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                writer.write(message);
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

            return null;
        }
    }
}
