package com.google.corrigan.owen.wordformed;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

class WordAdapterView extends LinearLayout {
    public WordAdapterView(Context context, Word hiScore) {
        TextView nameControl, scoreControl;
        LinearLayout.LayoutParams nameParams, scoreParams;

        super(context);
        this.setOrientation(HORIZONTAL);

        nameParams = new LinearLayout.LayoutParams(300, 40);
        nameParams.setMargins(20, 20, 60, 1);

        scoreParams = new LinearLayout.LayoutParams(160, 40);
        scoreParams.setMargins(1, 1, 1, 1);

        nameControl = new TextView(context);
        nameControl.setText(hiScore.getWord());
        nameControl.setTextSize(14f);
        nameControl.setTextColor(Color.WHITE);
        addView(nameControl, nameParams);

        scoreControl = new TextView(context);
        scoreControl.setText("" + hiScore.getScore());
        scoreControl.setTextSize(14f);
        scoreControl.setTextColor(Color.WHITE);
        addView(scoreControl, scoreParams);
    }
}

public class WordAdapter extends BaseAdapter {
    private Context context;
    private List<Word> hiScoreList;

    public WordAdapter(Context context, List<Word> hiScoreList) {
        this.context = context;
        this.hiScoreList = hiScoreList;
    }

    public int getCount() {
        return hiScoreList.size();
    }

    public Object getItem(int position) {
        return hiScoreList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Word hiScore = hiScoreList.get(position);
        return new WordAdapterView(this.context, hiScore);
    }
}
