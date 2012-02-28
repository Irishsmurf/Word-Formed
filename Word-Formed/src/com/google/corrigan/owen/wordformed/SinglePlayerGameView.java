package com.google.corrigan.owen.wordformed;

import java.util.Random;

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
	RectF boxBorder = new RectF(10, 50, 470, 120);
	RectF boxFill = new RectF(15, 55, 465, 115);
	DraggableBox[] db = new DraggableBox[7];
	
	public SinglePlayerGameView(Context context)
	{
		super(context);
		for(int i = 0; i < db.length; i++)
		{
			db[i] = new DraggableBox(i * 65 + 20, 60);
			Log.d("WORDFORMED", i + " was initilized");
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
		
		//Draw background for dragboxs
		//Draw Outer Rectangle
		Paint dragRectangle = new Paint();
		dragRectangle.setColor(Color.BLACK);
		canvas.drawRect(boxBorder, dragRectangle);
		
		//Draw inner Rectangle
		dragRectangle.setColor(Color.GRAY);
		canvas.drawRect(boxFill, dragRectangle);
		
		for(int i = 0; i < db.length; i++)
		{
			if(db[i]==null) Log.d("WORDFORMED", i + " draggableBox is null");
			db[i].draw(canvas);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		for(DraggableBox d: db)
		{
			if(d==null) Log.d("WORDFORMED", "draggableBox is null");
			d.onTouchEvent(event);
		}
		invalidate();
		return true;
	}
}
