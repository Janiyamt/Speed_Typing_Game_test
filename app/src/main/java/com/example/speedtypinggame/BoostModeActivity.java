package com.example.speedtypinggame;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BoostModeActivity extends AppCompatActivity {

    private TextView timerTextView;
    private TextView wordTextView;
    private EditText typingEditText;
    private Button startButton;

    private String[] wordsList;
    private String currentWord;
    private CountDownTimer countDownTimer;
    private boolean gameActive = false;
    private long timeLeftMillis = 60000; // 60 seconds
    private int correctWords = 0;
    private int totalAttempts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boost_mode);

        // Initialize views
        timerTextView = findViewById(R.id.timerTextView);
        wordTextView = findViewById(R.id.wordTextView);
        typingEditText = findViewById(R.id.typingEditText);
        startButton = findViewById(R.id.startButton);

        // Get words list
        wordsList = getResources().getStringArray(R.array.words);

        // Disable typing initially
        typingEditText.setEnabled(false);

        // Set up enter key listener
        typingEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || 
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    checkWord();
                    return true;
                }
                return false;
            }
        });

        // Add text change listener as an alternative way to submit
        typingEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // If the user has typed the complete word, check it
                if (gameActive && s.toString().trim().equals(currentWord)) {
                    checkWord();
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
        correctWords = 0;
        totalAttempts = 0;

        // Display the first word
        showNextWord();

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

    private void showNextWord() {
        currentWord = Utils.getRandomItem(wordsList);
        wordTextView.setText(currentWord);
        typingEditText.setText("");
    }

    private void checkWord() {
        if (!gameActive) return;

        String userInput = typingEditText.getText().toString().trim();
        totalAttempts++;

        if (userInput.equals(currentWord)) {
            correctWords++;
        }

        showNextWord();
    }

    private void endGame() {
        gameActive = false;
        typingEditText.setEnabled(false);
        startButton.setVisibility(View.VISIBLE);
        startButton.setText(R.string.try_again);
        wordTextView.setText("");

        // Calculate WPM (for boost mode, we'll use words typed correctly)
        int elapsedSeconds = 60 - (int)(timeLeftMillis / 1000);
        int wpm = Utils.calculateWPM(correctWords * 5, elapsedSeconds);

        // Launch results activity
        Intent intent = new Intent(BoostModeActivity.this, ResultActivity.class);
        intent.putExtra("WPM", wpm);
        intent.putExtra("MODE", "boost");
        intent.putExtra("CORRECT_WORDS", correctWords);
        intent.putExtra("TOTAL_ATTEMPTS", totalAttempts);
        startActivity(intent);
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
