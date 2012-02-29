package com.google.corrigan.owen.wordformed;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class Dropbox
{
	int x = 10;
	int y = 250;
	int width = 460;
	int height = 70;
	int border = 5;
	RectF dragBorder;
	RectF dragFill;
	
	public Dropbox(int x, int y, int width, int height)
	{
		dragBorder = new RectF(x, y, x + width, y + height);
		dragFill = new RectF(x + border, y + border, x + width - border, y + height - border);
	}
	
	
	public void draw(Canvas canvas)
	{
		Paint dragRectangle = new Paint();
		dragRectangle.setColor(Color.BLACK);
		canvas.drawRect(dragBorder, dragRectangle);
		canvas.drawRect(dragBorder, dragRectangle);
	}
	
	public boolean contains(float x, float y)
	{
		return dragBorder.contains(x, y);
	}
}