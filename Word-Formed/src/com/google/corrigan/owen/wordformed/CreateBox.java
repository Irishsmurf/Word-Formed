package com.google.corrigan.owen.wordformed;

import java.util.LinkedList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

public class CreateBox
{
	//X, Y, width and height
	private int border = 5;
	//Outer and inner rectangles used to draw it
	private RectF dragBorder, dragFill;
	//Max number of boxes stored
	//Array of references to Draggable boxes stored at certain position
	private LinkedList<DraggableBox> tiles = new LinkedList<DraggableBox>();
	private final String TAG = "DRAGGABLEBOX";
	
	//Constructor. Takes x, y, height and width and parameters
	public CreateBox(int x, int y, int width, int height)
	{
		dragBorder = new RectF(x, y, x + width, y + height);
		dragFill = new RectF(x + border, y + border, x + width - border, y + height - border);
	}
	
	public void add(DraggableBox d)
	{
		if(tiles.size() < 7)
			tiles.add(d);
		updatePositions();
	}
	
	public void updatePositions()
	{
		for(int i = 0; i < tiles.size(); i++)
			tiles.get(i).move(i*65+20, dragBorder.bottom - 60);
	}
	
	public void remove(DraggableBox d)
	{
		tiles.remove(d);
		updatePositions();
		tiles.add(new DraggableBox(65*8+20, 60));
	}
	
	//Draws method. Takes canvas as parameter
	public void draw(Canvas canvas)
	{
		Paint dragRectangle = new Paint();
		dragRectangle.setColor(Color.BLACK);
		canvas.drawRect(dragBorder, dragRectangle);
		dragRectangle.setColor(Color.rgb(68, 89, 108));
		canvas.drawRect(dragFill, dragRectangle);
		for(DraggableBox d: tiles)
			d.draw(canvas);
	}
	
	//Contains method. Returns true is point is within bounds of drop box
	public boolean contains(float x, float y)
	{
		return dragBorder.contains(x, y);
	}
}