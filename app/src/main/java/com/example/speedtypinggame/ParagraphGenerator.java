package com.example.speedtypinggame;

import android.content.Context;
import java.util.Random;

/**
 * Utility class for paragraph-related functionality
 */
public class ParagraphGenerator {

    private final Context context;
    private final Random random;
    private String[] paragraphs;

    /**
     * Constructor for the ParagraphGenerator
     * @param context Context used to access resources
     */
    public ParagraphGenerator(Context context) {
        this.context = context;
        this.random = new Random();
        loadParagraphs();
    }

    /**
     * Loads the paragraphs from resources
     */
    private void loadParagraphs() {
        paragraphs = context.getResources().getStringArray(R.array.paragraphs);
    }

    /**
     * Gets a random paragraph from the loaded paragraphs
     * @return A random paragraph
     */
    public String getRandomParagraph() {
        if (paragraphs == null || paragraphs.length == 0) {
            return "No paragraphs available.";
        }
        int index = random.nextInt(paragraphs.length);
        return paragraphs[index];
    }

    /**
     * Gets a specific paragraph by index
     * @param index The index of the paragraph to retrieve
     * @return The paragraph at the specified index or an error message if index is invalid
     */
    public String getParagraphByIndex(int index) {
        if (paragraphs == null || index < 0 || index >= paragraphs.length) {
            return "Invalid paragraph index.";
        }
        return paragraphs[index];
    }

    /**
     * Gets the total number of available paragraphs
     * @return The number of paragraphs
     */
    public int getParagraphCount() {
        return paragraphs != null ? paragraphs.length : 0;
    }

    /**
     * Gets a paragraph of approximately the specified length by trimming or combining existing paragraphs
     * @param targetLength The approximate length desired
     * @return A paragraph of the approximate desired length
     */
    public String getParagraphOfLength(int targetLength) {
        if (paragraphs == null || paragraphs.length == 0) {
            return "No paragraphs available.";
        }

        String paragraph = getRandomParagraph();

        // If paragraph is too long, trim it
        if (paragraph.length() > targetLength) {
            // Find the last space before the target length
            int lastSpace = paragraph.lastIndexOf(' ', targetLength);
            if (lastSpace > 0) {
                return paragraph.substring(0, lastSpace) + ".";
            } else {
                return paragraph.substring(0, targetLength);
            }
        }

        return paragraph;
    }
}
