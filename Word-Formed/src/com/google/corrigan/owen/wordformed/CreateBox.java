package com.google.corrigan.owen.wordformed;

import java.util.LinkedList;

import android.content.Context;
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
	private Context context;
	private LinkedList<DraggableBox> ref;
	//Constructor. Takes x, y, height and width and parameters
	public CreateBox(int x, int y, int width, int height, Context context0, LinkedList<DraggableBox> ref0)
	{
		dragBorder = new RectF(x, y, x + width, y + height);
		dragFill = new RectF(x + border, y + border, x + width - border, y + height - border);
		context = context0;
		ref = ref0;
	}
	
	public void setContext(Context context0)
	{
		context = context0;
	}
	
	public void setRef(LinkedList<DraggableBox> ref0)
	{
		ref = ref0;
	}
	
	public CreateBox(int x, int y, int width, int height)
	{
		dragBorder = new RectF(x, y, x+width, y+height);
		dragFill = new RectF(x+border, y + border, x+width - border, y+ height - border);
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
			tiles.get(i).moveLeft(i*65+20, dragBorder.bottom - 60);
	}
	
	public void remove(DraggableBox d)
	{
		tiles.remove(d);
		DraggableBox tmp = new DraggableBox(context, 65*8+20, 60);
		tiles.add(tmp);
		ref.add(tmp);
		updatePositions();
	}
	
	public boolean contains(DraggableBox d)
	{
		return tiles.contains(d);
	}
	
	//Draws method. Takes canvas as parameter
	public void draw(Canvas canvas)
	{
		Paint dragRectangle = new Paint();
		dragRectangle.setColor(Color.BLACK);
		canvas.drawRect(dragBorder, dragRectangle);
		dragRectangle.setColor(Color.rgb(68, 89, 108));
		canvas.drawRect(dragFill, dragRectangle);
		for(DraggableBox d: (LinkedList<DraggableBox>) tiles.clone())
			d.draw(canvas);

	}
	
	//Contains method. Returns true is point is within bounds of drop box
	public boolean contains(float x, float y)
	{
		return dragBorder.contains(x, y);
	}
}
