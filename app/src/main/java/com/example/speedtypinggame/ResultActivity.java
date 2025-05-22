package com.example.speedtypinggame;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    private TextView resultTitleTextView;
    private TextView wpmTextView;
    private TextView accuracyTextView;
    private Button doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Initialize views
        resultTitleTextView = findViewById(R.id.resultTitleTextView);
        wpmTextView = findViewById(R.id.wpmTextView);
        accuracyTextView = findViewById(R.id.accuracyTextView);
        doneButton = findViewById(R.id.doneButton);

        // Get data from intent
        int wpm = getIntent().getIntExtra("WPM", 0);
        String mode = getIntent().getStringExtra("MODE");

        // Display the results
        wpmTextView.setText(getString(R.string.wpm_result, wpm));

        // Handle specific mode data
        if ("boost".equals(mode)) {
            int correctWords = getIntent().getIntExtra("CORRECT_WORDS", 0);
            int totalAttempts = getIntent().getIntExtra("TOTAL_ATTEMPTS", 1);
            double accuracy = (totalAttempts > 0) ? (double) correctWords / totalAttempts * 100 : 0;

            accuracyTextView.setText(getString(R.string.accuracy_result, accuracy));
            resultTitleTextView.setText(R.string.boost_mode_result);
        } else {
            // Paragraph mode or other modes
            accuracyTextView.setVisibility(View.GONE);
            resultTitleTextView.setText(R.string.paragraph_mode_result);
        }

        // Set up the done button
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
