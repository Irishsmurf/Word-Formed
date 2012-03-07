package com.google.corrigan.owen.wordformed;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class DraggableBox
{
	private static final String TAG = "DRAGGABLEBOX";
	//Top left X and Y coordinates of draggable box
	private float rectX;
	private float rectY;
	//Hold starting position when the box is picked up
	private float startX;
	private float startY;
	//Length of rectangle
	private int rectSize = 45;
	//Size of border
	private int borderSize = 5;
	//Outer and inner rectangles used for dragging
	private RectF rect, rect2;
	//Boolean to say if box is being dragged
	private boolean dragging = false;
	//Letter stored in tile
	private char letter;
	private Context context;
	private Bitmap tile;
	boolean notMoved = true;
	//Constructor. Takes starting position as parameters
	public DraggableBox(Context context0, float topX, float topY)
	{
		//Set coordinates of box
		rectX = topX;
		rectY = topY;
		//Set coordinates in case box needs to jump back to this position
		startX = topX;
		startY = topY;
		//Calculate size and position of outer and inner box
		rect = new RectF(rectX, rectY, rectX+rectSize, rectY+rectSize);
		rect2 = new RectF(rectX + borderSize, rectY + borderSize, 
				rectX + rectSize - borderSize, rectY + rectSize - borderSize);
		//Randomly choose a character
		//TODO: Find a better distribution
		Random r = new Random();
		letter = (char)(r.nextInt(26) + 'A');
		context = context0;
		tile = BitmapFactory.decodeFile("images/tile");

	}
	
	public DraggableBox(int letter)
	{
		/*
		 * TO DO: Single Constructor and placer
		 * letter = letter + 'A';
		 */
	}
	
	//Draw method. Takes canvas as parameter
	public void draw(Canvas canvas)
	{
		//Draw Outer Rectangle
		Paint color = new Paint();
		color.setColor(Color.BLUE);
		canvas.drawRect(rect, color);
		
		//Draw inner Rectangle
		color.setColor(Color.WHITE);
		canvas.drawRect(rect2, color);
		//canvas.drawBitmap(tile, null, rect, color);
		//Draw letter
		Paint font = new Paint();
		font.setColor(Color.BLACK);
		font.setTextSize(30);
		font.setTypeface(Typeface.MONOSPACE);
		//If being dragged, account for offset
		if(!dragging)
			canvas.drawText(letter+"", rectX + 17, rectY + 30, font);
		else
			canvas.drawText(letter+"", rectX + 17 - 40, rectY + 30 - 40, font);
	}
	
	//On touch event. Takes MotionEvent and Dropbox as parameter
	public boolean onTouchEvent(MotionEvent event, Dropbox drop, Dropbox answer, CreateBox create)
	{
		//Store mouse x and y coordinates
		float mouseX = event.getX();
		float mouseY = event.getY();
		switch(event.getAction())
		{
			//On mouse down, check if touched within area. If so start dragging
			case MotionEvent.ACTION_DOWN:
				if(rect.contains(mouseX, mouseY))
				{
					Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
					v.vibrate(60);
					dragging = true;
				}
				else dragging = false;
				break;
			/* On mouse up drop tile in place if in valid location.
			 * If it landed on another tile swap that tile back to it's place
			 * Or be deleted otherwise
			*/
			case MotionEvent.ACTION_UP:
				if(dragging)
				{
					drop.remove(this);
					answer.remove(this);
					create.remove(this);
					//if within drop zone snap to grid
					if(drop.contains(rectX, rectY))
					{
						drop.add(this);
						notMoved = false;
					}
					else if(answer.contains(rectX, rectY))
					{
						answer.add(this);
					}
					//If box not within dropbox, remove from board
					//TODO: return null reference to prevent memory leak
					else
					{
						//remove box
						rect = new RectF();
						rect2 = new RectF();
						letter = ' ';
					}
				}
				//Fixes bug for some reason
				//TODO: figure out what next line does
				dragging = false;
				break;
			//If this is being dragged, set position to same position as mouse
			//TODO: Instead of default, find specific dragging case
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
	
	public boolean isSelected()
	{
		return dragging;
	}
	
	//Move this tile to a specific x and y position. Takes x and y position as parameters
	public void move(float x, float y)
	{
		rectX = x;
		rectY = y;
		rect = new RectF(rectX, rectY, rectX + rectSize, rectY + rectSize);
		rect2 = new RectF(rectX + borderSize, rectY + borderSize, 
			rectX + rectSize - borderSize, rectY + rectSize - borderSize);
	}
}
