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

class HiScoreAdapterView extends LinearLayout {        
        public HiScoreAdapterView(Context context, 
								HiScore hiScore ) {
            super( context );

            this.setOrientation(HORIZONTAL);        
            LinearLayout.LayoutParams nameParams = 
                new LinearLayout.LayoutParams(300, 80);
            nameParams.setMargins(20, 20, 60, 1);
            
            TextView nameControl = new TextView( context );
			nameControl.setText( hiScore.getName() );
            nameControl.setTextSize(14f);
            nameControl.setTextColor(Color.WHITE);
            addView(nameControl, nameParams);       

            LinearLayout.LayoutParams scoreParams = 
                new LinearLayout.LayoutParams(160, LayoutParams.WRAP_CONTENT);
            scoreParams.setMargins(1, 1, 1, 1);

            TextView scoreControl = new TextView(context);
            scoreControl.setText( ""+hiScore.getScore());
            scoreControl.setTextSize( 14f );
           
            scoreControl.setTextColor(Color.WHITE);
            addView( scoreControl, scoreParams);
        }
}

public class HiScoreAdapter extends BaseAdapter {

    private Context context;
    private List<HiScore> hiScoreList;

    public HiScoreAdapter(Context context, List<HiScore> hiScoreList ) { 
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
        HiScore hiScore = hiScoreList.get(position);
        return new HiScoreAdapterView(this.context, hiScore );
    }

}
