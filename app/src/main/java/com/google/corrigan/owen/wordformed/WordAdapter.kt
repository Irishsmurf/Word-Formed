package com.google.corrigan.owen.wordformed

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView

class WordAdapterView(context: Context, word: Word) : LinearLayout(context) {
    init {
        orientation = HORIZONTAL
        
        val nameParams = LayoutParams(300, 40).apply {
            setMargins(20, 20, 60, 1)
        }

        val nameControl = TextView(context).apply {
            text = word.word
            textSize = 14f
            setTextColor(Color.WHITE)
        }
        addView(nameControl, nameParams)

        val scoreParams = LayoutParams(160, 40).apply {
            setMargins(1, 1, 1, 1)
        }

        val scoreControl = TextView(context).apply {
            text = word.score.toString()
            textSize = 14f
            setTextColor(Color.WHITE)
        }
        addView(scoreControl, scoreParams)
    }
}

class WordAdapter(private val context: Context, private val wordList: List<Word>) : BaseAdapter() {
    override fun getCount(): Int = wordList.size
    override fun getItem(position: Int): Any = wordList[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return WordAdapterView(context, wordList[position])
    }
}
