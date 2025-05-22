package com.example.speedtypinggame;

import java.util.Random;

public class Utils {

    /**
     * Calculates words per minute based on characters typed and elapsed time
     * @param charactersTyped Number of characters typed correctly
     * @param elapsedTimeSeconds Time elapsed in seconds
     * @return WPM calculation
     */
    public static int calculateWPM(int charactersTyped, int elapsedTimeSeconds) {
        // Average word length is considered to be 5 characters
        double words = charactersTyped / 5.0;
        // Convert seconds to minutes and calculate WPM
        double minutes = elapsedTimeSeconds / 60.0;
        
        // Avoid division by zero
        if (minutes > 0) {
            return (int) Math.round(words / minutes);
        } else {
            return 0;
        }
    }

    /**
     * Get a random item from a string array
     * @param array The string array
     * @return A random item from the array
     */
    public static String getRandomItem(String[] array) {
        Random random = new Random();
        int index = random.nextInt(array.length);
        return array[index];
    }
}
