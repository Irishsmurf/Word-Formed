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
	DraggableBox db = new DraggableBox();
	
	public SinglePlayerGameView(Context context)
	{
		super(context);
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		//Draw the background...
		Paint background = new Paint();
		background.setColor(getResources().getColor(
				R.color.background));
		canvas.drawRect(0, 0, getWidth(), getHeight(), background);
				
		db.draw(canvas);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		db.onTouchEvent(event);
		invalidate();
		return true;
	}
}
