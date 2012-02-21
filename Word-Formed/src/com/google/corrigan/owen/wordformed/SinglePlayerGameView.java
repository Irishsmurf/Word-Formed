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
	private float rectX = 50;
	private float rectY = 60;
	private int rectSize = 80;
	private float offsetX;
	private float offsetY;
	private RectF rect = new RectF(rectX, rectY, rectX+rectSize, rectY+rectSize);
	private boolean dragging = false;
	private int snapX = 300;
	private int snapY = 300;
	private RectF snapBox = new RectF(snapX, snapY, snapX+rectSize, snapY+rectSize);
	
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
		Paint snap = new Paint();
		snap.setColor(Color.RED);
		canvas.drawRect(snapBox, snap);
				
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
		Log.d("WORDFORMED", "MouseX = "+ mouseX + ", Mouse Y = " + mouseY);
		switch(event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				if(mouseX + 10 > rectX - offsetX && 
						mouseX - 10 < rectX - offsetX + rectSize && 
						mouseY + 10 > rectY - offsetY && 
						mouseY - 10 < rectY + rectSize - offsetY)
				{
					//offsetX = mouseX - rectX;
					//offsetY = mouseY - rectY;
					dragging = true;
					Log.d("WORDFORMED", "Inside Rectangle");
				}
				else dragging = false;
				invalidate();
				break;
			case MotionEvent.ACTION_UP:
				Log.d("MOUSEUP", "Mouse X = " + mouseX + " Mouse Y = " + mouseY );
				if(mouseX > snapX && 
						mouseX < snapX + rectSize && 
						mouseY > snapY && 
						mouseY <  snapY + rectSize)
				{
					Log.d("MOUSEUP", "Inside the Snapper!" );
					rectX = snapX;
					rectY = snapY;
					Log.d("WORDFORMED", "Mouse up and inside snapX");
					rect = new RectF(rectX, rectY, rectX+rectSize, rectY+rectSize);
				}
				//offsetX = 0;
				//offsetY = 0;
				dragging = false;
				invalidate();
				break;
			default:
				if(dragging)
				{
					rectX = mouseX;
					rectY = mouseY;
					rect = new RectF(rectX-offsetX, rectY-offsetY, rectX+rectSize-offsetX, rectY+rectSize-offsetY);
					invalidate();
					
				}
		}
		return true;
	}
}
