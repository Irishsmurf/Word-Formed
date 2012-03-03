package com.google.corrigan.owen.wordformed;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class SinglePlayerGameView extends View
{
	Dropbox drop = new Dropbox(10, 250, 460, 70);
	Dropbox answer = new Dropbox(10, 450, 460, 70);
	ArrayList<DraggableBox> db = new ArrayList<DraggableBox>();
	CreateBox create;
	public SinglePlayerGameView(Context context)
	{
		super(context);
		create = new CreateBox(10, 50, 460, 70, context);
		for(int i = 0; i < 7; i++)
		{
			DraggableBox d = new DraggableBox(context, i * 65 + 20, 60);
			db.add(d);
			create.add(d);
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		//Draw the background...
		Paint background = new Paint();
		background.setColor(getResources().getColor(
				R.color.background));
		canvas.drawRect(0, 0, getWidth(), getHeight(), background);
		
		//Draw background for dragboxes
		drop.draw(canvas);
		answer.draw(canvas);
		create.draw(canvas);
		
		for(DraggableBox d: db)
		{
			d.draw(canvas);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		int selected = 0;
		for(DraggableBox d: db)
		{
			if(d.isSelected()) selected = db.indexOf(d);
		}
		Collections.swap(db, db.size() - 1, selected);
		
		for(DraggableBox d: db)
		{
			d.onTouchEvent(event, drop, answer, create);
		}
		invalidate();
		return true;
	}
}
