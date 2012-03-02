package com.google.corrigan.owen.wordformed;

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
	
	RectF boxBorder = new RectF(10, 50, 470, 120);
	RectF boxFill = new RectF(15, 55, 465, 115);
	
	RectF answerBorder = new RectF(10, 50 + 400, 470, 120 + 400);
	RectF answerFill = new RectF(15, 55 + 400, 465, 115 + 400);
	
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
		
		//Draw background for dragboxes
		drop.draw(canvas);
		
		//Draw Outer Rectangle
		Paint dragRectangle = new Paint();
		dragRectangle.setColor(Color.BLACK);
		canvas.drawRect(boxBorder, dragRectangle);
		canvas.drawRect(answerBorder, dragRectangle);
		
		//Draw inner Rectangle
		dragRectangle.setColor(Color.rgb(68, 89, 108));
		canvas.drawRect(boxFill, dragRectangle);
		canvas.drawRect(answerFill, dragRectangle);
		
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
			d.onTouchEvent(event, drop);
		}
		invalidate();
		return true;
	}
}
