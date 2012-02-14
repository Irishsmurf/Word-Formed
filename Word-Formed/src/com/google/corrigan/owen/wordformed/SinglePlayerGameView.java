package com.google.corrigan.owen.wordformed;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class SinglePlayerGameView extends View
{
	private int rectX = 50;
	private int rectY = 60;
	private int rectSize = 80;
	private Rect rect = new Rect(rectX-40, rectY-40, rectX+rectSize-40, rectY+rectSize-40);
	private boolean dragging = false;
	
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
		
		//Draw Rectangle
		Paint dragRectangle = new Paint();
		dragRectangle.setColor(Color.BLUE);
		canvas.drawRect(rect, dragRectangle);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		float mouseX = event.getX();
		float mouseY = event.getY();
		switch(event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				if(mouseX < rectX + 40 && mouseX > rectX - 40 && mouseY < rectY + 40 && mouseY > rectY - 40)
					dragging = true;
				else dragging = false;
				break;
			case MotionEvent.ACTION_UP:
				dragging = false;
				break;
			default:
				if(dragging)
				{
					invalidate(rect);
					rectX = (int) mouseX;
					rectY = (int) mouseY;
					rect = new Rect(rectX-40, rectY-40, rectX+rectSize-40, rectY+rectSize-40);
					invalidate(rect);
				}
		}
		return true;
	}
}
