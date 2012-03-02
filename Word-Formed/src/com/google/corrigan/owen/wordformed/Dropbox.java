package com.google.corrigan.owen.wordformed;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class Dropbox
{
	//X, Y, width and height
	private int x = 10;
	private int y = 250;
	private int width = 460;
	private int height = 70;
	//Border size
	private int border = 5;
	//Outer and inner rectangles used to draw it
	private RectF dragBorder, dragFill;
	//Max number of boxes stored
	private int limBoxes = 7;
	//Array of references to Draggable boxes stored at certain position
	private DraggableBox [] fill = new DraggableBox[limBoxes];
	private final String TAG = "WORDFORMED"; 
	
	//Constructor. Takes x, y, height and width and parameters
	public Dropbox(int x, int y, int width, int height)
	{
		dragBorder = new RectF(x, y, x + width, y + height);
		dragFill = new RectF(x + border, y + border, x + width - border, y + height - border);
	}
	
	//Checks if a certain place is occupied by a DraggableBox
	//Parameter x must be less than limBoxes
	public boolean isFull(int x)
	{
		return fill[x] != null;
	}
	
	//Puts a reference to a draggable box into the array at place p
	//Parameter x must be less than limBoxes
	public void add(int x, DraggableBox d)
	{
		fill[x] = d;
	}
	
	//Frees space from the dropbox at position x
	//Parameter x must be less than limBoxes
	public void remove(int x)
	{
		fill[x] = null;
	}
	
	//Returns the reference to the draggable box at position x
	//Parameter x must be less than limBoxes
	public DraggableBox get(int x)
	{
		return fill[x];
	}
	
	//Draws method. Takes canvas as parameter
	public void draw(Canvas canvas)
	{
		Paint dragRectangle = new Paint();
		dragRectangle.setColor(Color.BLACK);
		canvas.drawRect(dragBorder, dragRectangle);
		dragRectangle.setColor(Color.GRAY);
		canvas.drawRect(dragFill, dragRectangle);
	}
	
	//Contains method. Returns true is point is within bounds of drop box
	public boolean contains(float x, float y)
	{
		return dragBorder.contains(x, y);
	}
}
