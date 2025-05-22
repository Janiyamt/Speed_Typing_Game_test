package com.example.speedtypinggame;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class MultiplayerActivity extends AppCompatActivity {

    private TextView statusTextView;
    private Button startButton;
    private EditText typingEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);

        // Initialize views
        statusTextView = findViewById(R.id.statusTextView);
        startButton = findViewById(R.id.startButton);
        typingEditText = findViewById(R.id.typingEditText);

        // Set initial status
        statusTextView.setText("Multiplayer mode coming soon!");

        // Disable typing and start button for now
        typingEditText.setEnabled(false);
        startButton.setEnabled(false);
    }
}
