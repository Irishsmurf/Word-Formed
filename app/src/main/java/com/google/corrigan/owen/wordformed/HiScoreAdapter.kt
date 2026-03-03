package com.google.corrigan.owen.wordformed

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView

class HiScoreAdapterView(context: Context, hiScore: HiScore, position: Int) : LinearLayout(context) {
    init {
        orientation = HORIZONTAL
        
        val nameParams = LayoutParams(300, 80).apply {
            setMargins(20, 20, 60, 1)
        }

        val nameControl = TextView(context).apply {
            text = "${position + 1}. ${hiScore.name}"
            textSize = 14f
            setTextColor(Color.WHITE)
        }
        addView(nameControl, nameParams)

        val scoreParams = LayoutParams(160, LayoutParams.WRAP_CONTENT).apply {
            setMargins(1, 1, 1, 1)
        }

        val scoreControl = TextView(context).apply {
            text = hiScore.score.toString()
            textSize = 14f
            setTextColor(Color.WHITE)
        }
        addView(scoreControl, scoreParams)
    }
}

class HiScoreAdapter(private val context: Context, private val hiScoreList: List<HiScore>) : BaseAdapter() {
    override fun getCount(): Int = hiScoreList.size
    override fun getItem(position: Int): Any = hiScoreList[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return HiScoreAdapterView(context, hiScoreList[position], position)
    }
}
