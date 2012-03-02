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

public class SinglePlayerGameView extends View
{
	Dropbox drop = new Dropbox(10, 250, 460, 70);
	Dropbox answer = new Dropbox(10, 450, 460, 70);
	
	RectF boxBorder = new RectF(10, 50, 470, 120);
	RectF boxFill = new RectF(15, 55, 465, 115);
	
	ArrayList<DraggableBox> db = new ArrayList<DraggableBox>();
	
	public SinglePlayerGameView(Context context)
	{
		super(context);
		for(int i = 0; i < 7; i++)
		{
			db.add(new DraggableBox(i * 65 + 20, 60));
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
		
		//Draw Outer Rectangle
		Paint dragRectangle = new Paint();
		dragRectangle.setColor(Color.BLACK);
		canvas.drawRect(boxBorder, dragRectangle);
		
		//Draw inner Rectangle
		dragRectangle.setColor(Color.rgb(68, 89, 108));
		canvas.drawRect(boxFill, dragRectangle);
		
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
			d.onTouchEvent(event, drop, answer);
		}
		invalidate();
		return true;
	}
}
