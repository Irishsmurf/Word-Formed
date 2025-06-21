package com.google.corrigan.owen.wordformed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.util.Log;

public class Dictionary {
    private static Trie trie;
    private static boolean initialized = false;
    private static final String TAG = "Dictionary";

    //Ensure this constructor is called once, e.g. in Application class or main activity
    public Dictionary(Context context) {
        if (!initialized) {
            // Check if context is null, which can happen if constructor is called at wrong time.
            if (context == null) {
                Log.e(TAG, "Context is null, cannot initialize Dictionary.");
                return;
            }
            loadWords(context);
        }
    }

    // Make loadWords synchronized to prevent concurrent modification issues if called from multiple threads,
    // though ideally initialization happens once in a controlled manner.
    private synchronized void loadWords(Context context) {
        if (initialized) {
            return; // Already loaded
        }
        trie = new Trie();
        InputStream inputStream = null;
        BufferedReader reader = null;
        Log.d(TAG, "Starting to load words into Trie from R.raw.word_list...");
        try {
            // Use R.raw.word_list (the plain text file)
            inputStream = context.getResources().openRawResource(R.raw.word_list);
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            int count = 0;
            long startTime = System.currentTimeMillis();
            while ((line = reader.readLine()) != null) {
                String word = line.trim().toUpperCase(); // Standardize to uppercase
                if (!word.isEmpty() && word.matches("^[A-Z]+$")) { // Ensure only letters
                    trie.add(word);
                    count++;
                }
            }
            long endTime = System.currentTimeMillis();
            Log.d(TAG, "Loaded " + count + " words into the Trie in " + (endTime - startTime) + " ms.");
            initialized = true;
        } catch (IOException e) {
            Log.e(TAG, "Error loading words from R.raw.word_list", e);
            // Consider how to handle this error - perhaps the game cannot proceed.
        } catch (NullPointerException e) {
            Log.e(TAG, "NullPointerException during word loading. Resources or context might be an issue.", e);
        }
        finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "Error closing streams after loading words", e);
            }
        }
    }

    public static boolean isWord(String word) {
        if (trie == null || !initialized) {
            Log.e(TAG, "Dictionary not initialized. Trie is null. Cannot check word.");
            // This case should ideally not happen if Dictionary is initialized properly at app start.
            // Returning false to prevent crashes, but this indicates an initialization problem.
            return false;
        }
        if (word == null || word.isEmpty()) {
            return false;
        }
        // Words in Trie are stored as uppercase.
        // Dropbox.tilesToString() already returns uppercase.
        return trie.isEntry(word.toUpperCase());
    }

    public static int getWordCount() {
        if (trie == null || !initialized) {
            Log.w(TAG, "Dictionary not initialized. Cannot get word count.");
            return 0;
        }
        return trie.size();
    }

    // Call this method if you need to re-initialize or force a reload (e.g., for testing or language change)
    // Make sure context is valid.
    public static synchronized void resetAndLoad(Context context) {
        if (context == null) {
            Log.e(TAG, "Context is null, cannot reset and load Dictionary.");
            return;
        }
        initialized = false;
        trie = null; // Allow GC of old trie
        Log.d(TAG, "Dictionary reset. Reloading words...");
        new Dictionary(context); // This will trigger loadWords if not initialized
    }
}
