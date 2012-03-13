package com.google.corrigan.owen.wordformed;

import java.util.LinkedList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

public class Dropbox
{
	//X, Y, width and height
	private int border = 5;
	//Outer and inner rectangles used to draw it
	private RectF dragBorder, dragFill;
	//Max number of boxes stored
	//Array of references to Draggable boxes stored at certain position
	private LinkedList<DraggableBox> tiles = new LinkedList<DraggableBox>();
	private final String TAG = "DRAGGABLEBOX";
	private char[] word = new char[7];
	//Constructor. Takes x, y, height and width and parameters
	public Dropbox(int x, int y, int width, int height)
	{
		dragBorder = new RectF(x, y, x + width, y + height);
		dragFill = new RectF(x + border, y + border, x + width - border, y + height - border);
	}
	
	public void add(DraggableBox d)
	{
		if(tiles.size() < 7)
			tiles.add(d);
		updatePositions();
		//Adding the current word to an char array for easy printing.
		for(int i = 0; i < tiles.size(); i++)
		{
			word[i] = tiles.get(i).letter();
		}
		Log.d("Word: ", new String(word));
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
	}
	
	public boolean full()
	{
		return tiles.size() == 7;
	}
	
	public DraggableBox getFirst()
	{
		return tiles.get(0);
	}
	
	//Draws method. Takes canvas as parameter
	public void draw(Canvas canvas)
	{
		Paint dragRectangle = new Paint();
		dragRectangle.setColor(Color.BLACK);
		canvas.drawRect(dragBorder, dragRectangle);
		dragRectangle.setColor(Color.rgb(68, 89, 108));
		canvas.drawRect(dragFill, dragRectangle);
	}
	
	//Contains method. Returns true is point is within bounds of drop box
	public boolean contains(float x, float y)
	{
		return dragBorder.contains(x, y);
	}
}
