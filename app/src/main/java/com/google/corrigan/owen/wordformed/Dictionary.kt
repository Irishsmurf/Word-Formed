package com.google.corrigan.owen.wordformed

import android.content.Context
import android.util.Log
import java.util.Collections
import java.util.Scanner

object Dictionary {
    private var data: MutableList<String> = mutableListOf()

    fun load(context: Context) {
        try {
            // Prefer reading from raw text file instead of serialized object
            val inputStream = context.resources.openRawResource(R.raw.word_list)
            val scanner = Scanner(inputStream)
            val newData = mutableListOf<String>()
            while (scanner.hasNextLine()) {
                newData.add(scanner.nextLine().trim().lowercase())
            }
            scanner.close()
            inputStream.close()
            
            // Sort for binary search
            newData.sort()
            data = newData
            Log.d("Dictionary", "Loaded ${data.size} words")
        } catch (e: Exception) {
            Log.e("Dictionary", "Error loading dictionary", e)
        }
    }

    fun isWord(word: String): Boolean {
        if (data.isEmpty()) return false
        val index = Collections.binarySearch(data, word.lowercase())
        return index >= 0
    }

    fun size(): Int = data.size
}
