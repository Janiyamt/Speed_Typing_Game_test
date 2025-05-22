package com.example.speedtypinggame;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ParagraphModeActivity extends AppCompatActivity {

    private TextView timerTextView;
    private TextView paragraphTextView;
    private EditText typingEditText;
    private Button startButton;

    private String currentParagraph;
    private CountDownTimer countDownTimer;
    private boolean gameActive = false;
    private long timeLeftMillis = 60000; // 60 seconds
    private int correctCharacters = 0;
    private int totalCharacters = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paragraph_mode);

        // Initialize views
        timerTextView = findViewById(R.id.timerTextView);
        paragraphTextView = findViewById(R.id.paragraphTextView);
        typingEditText = findViewById(R.id.typingEditText);
        startButton = findViewById(R.id.startButton);

        // Get random paragraph
        String[] paragraphs = getResources().getStringArray(R.array.paragraphs);
        currentParagraph = Utils.getRandomItem(paragraphs);
        paragraphTextView.setText(currentParagraph);

        // Disable typing initially
        typingEditText.setEnabled(false);

        // Set up text change listener
        typingEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (gameActive) {
                    checkTypingAccuracy();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Set up start button
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!gameActive) {
                    startGame();
                }
            }
        });

        updateTimerText();
    }

    private void startGame() {
        gameActive = true;
        typingEditText.setEnabled(true);
        typingEditText.setText("");
        typingEditText.requestFocus();
        startButton.setVisibility(View.INVISIBLE);
        correctCharacters = 0;
        totalCharacters = 0;

        // Start the countdown timer
        countDownTimer = new CountDownTimer(timeLeftMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftMillis = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                endGame();
            }
        }.start();
    }

    private void endGame() {
        gameActive = false;
        typingEditText.setEnabled(false);
        startButton.setVisibility(View.VISIBLE);
        startButton.setText(R.string.try_again);

        // Calculate WPM
        int elapsedSeconds = 60 - (int)(timeLeftMillis / 1000);
        int wpm = Utils.calculateWPM(correctCharacters, elapsedSeconds);

        // Launch results activity
        Intent intent = new Intent(ParagraphModeActivity.this, ResultActivity.class);
        intent.putExtra("WPM", wpm);
        intent.putExtra("MODE", "paragraph");
        startActivity(intent);
    }

    private void checkTypingAccuracy() {
        String userText = typingEditText.getText().toString();
        totalCharacters = userText.length();
        correctCharacters = 0;

        // Count correct characters
        for (int i = 0; i < Math.min(userText.length(), currentParagraph.length()); i++) {
            if (userText.charAt(i) == currentParagraph.charAt(i)) {
                correctCharacters++;
            }
        }

        // If user completed the paragraph, end the game
        if (userText.length() == currentParagraph.length()) {
            countDownTimer.cancel();
            endGame();
        }
    }

    private void updateTimerText() {
        int secondsRemaining = (int) (timeLeftMillis / 1000);
        timerTextView.setText(getString(R.string.time_remaining, secondsRemaining));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
