package com.google.corrigan.owen.wordformed;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;

public class DraggableBox
{
	private float rectX;
	private float rectY;
	private int rectSize = 45;
	private int borderSize = 5;
	private RectF rect, rect2;
	private boolean dragging = false;
	private char letter;
	
	public DraggableBox(float topX, float topY)
	{
		rectX = topX;
		rectY = topY;
		rect = new RectF(rectX, rectY, rectX+rectSize, rectY+rectSize);
		rect2 = new RectF(rectX + borderSize, rectY + borderSize, 
				rectX + rectSize - borderSize, rectY + rectSize - borderSize);
		Random r = new Random();
		letter = (char)(r.nextInt(26) + 'a');
	}
	
	public void draw(Canvas canvas)
	{
		//Draw Outer Rectangle
		Paint dragRectangle = new Paint();
		dragRectangle.setColor(Color.BLUE);
		canvas.drawRect(rect, dragRectangle);
		
		//Draw inner Rectangle
		dragRectangle.setColor(Color.WHITE);
		canvas.drawRect(rect2, dragRectangle);
		
		//Draw letter
		Paint font = new Paint();
		font.setColor(Color.BLACK);
		font.setTextSize(30);
		font.setTypeface(Typeface.MONOSPACE);
		//Very hacky - fix it!!
		if(!dragging)
			canvas.drawText(letter+"", rectX + 17, rectY + 30, font);
		else
			canvas.drawText(letter+"", rectX + 17 - 40, rectY + 30 - 40, font);
	}
	
	public boolean onTouchEvent(MotionEvent event, RectF dragBox)
	{
		float mouseX = event.getX();
		float mouseY = event.getY();
		Log.d("WORDFORMED", "MouseX = "+ mouseX + ", Mouse Y = " + mouseY);
		switch(event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				if(rect.contains(mouseX, mouseY))
				{
					dragging = true;
					Log.d("WORDFORMED", "Inside Rectangle");
				}
				else dragging = false;
				break;
			case MotionEvent.ACTION_UP:
				if(dragging)
				{
					if(dragBox.contains(rectX, rectY))
					{
						//Reset x and y to compensate bounding box for centering
						rectX -= 40;
						rectX = Math.round(rectX / 65);
						rectX *= 65;
						rectX += 20;
						rectY = 260;
						rect = new RectF(rectX, rectY, rectX+rectSize, rectY+rectSize);
						rect2 = new RectF(rectX + borderSize, rectY + borderSize, 
								rectX + rectSize - borderSize, rectY + rectSize - borderSize);
					}
					else
					{
						//remove box
						rect = new RectF();
						rect2 = new RectF();
						letter = ' ';
					}
				}
				dragging = false;
				break;
			default:
				if(dragging)
				{
					rectX = mouseX;
					rectY = mouseY;
					rect = new RectF(rectX - 40, rectY - 40, rectX+rectSize - 40, rectY+rectSize - 40);
					rect2 = new RectF(rectX + borderSize - 40, rectY + borderSize - 40, 
							rectX + rectSize - borderSize - 40, rectY + rectSize - borderSize - 40);
				}
		}
		return true;
	}
}
