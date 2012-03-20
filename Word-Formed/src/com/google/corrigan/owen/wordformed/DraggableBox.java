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
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
	private Paint paint;
	private float speed = 5;
	private float velX;
	private float velY;
	//True when user lets go
	private boolean flinging = false;
	private int transperancy = 255;
	private boolean movingLeft = false;
	private boolean movingRight = false;
	private float targetX = 0;
	
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
		tile = BitmapFactory.decodeResource(context.getResources(), R.drawable.tile);
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setTextSize(30);
		paint.setTypeface(Typeface.MONOSPACE);
	}
	
	public DraggableBox(int letter)
	{
		/*
		 * TO DO: Single Constructor and placer
		 * letter = letter + 'A';
		 */
	}
	
	public char getLetter()
	{
		return letter;
	}
	
	//Draw method. Takes canvas as parameter
	public synchronized void draw(Canvas canvas)
	{
		//Draw Outer Rectangle
		//Draw inner Rectangle
		Drawable box = context.getResources().getDrawable(R.drawable.tile);
		Bitmap bitmap = ((BitmapDrawable)box).getBitmap();
		paint.setAlpha(transperancy);
		canvas.drawBitmap(bitmap, null, rect, paint);
		//Draw letter
		if(!dragging && !flinging)
			canvas.drawText(letter+"", rectX + 15, rectY + 35, paint);
		else
			canvas.drawText(letter+"", rectX + 15 - 35, rectY + 35 - 10, paint); //COME BACK TO THIS
	}
	
	//On touch event. Takes MotionEvent and Dropbox as parameter
	public synchronized boolean onTouchEvent(MotionEvent event, Dropbox drop, Dropbox answer, CreateBox create)
	{
		//Store mouse x and y coordinates
		float mouseX = event.getX();
		float mouseY = event.getY();
		switch(event.getAction())
		{
			//On mouse down, check if touched within area. If so start dragging
			case MotionEvent.ACTION_DOWN:
				startX = mouseX;
				startY = mouseY;
				if(rect.contains(mouseX, mouseY) && !flinging)
				{
					Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
					v.vibrate(60);
					dragging = true;
					rectSize = 80;
					paint.setTextSize(60);
					//Update position immediately to prevent letter inside from jumping
					rectX = mouseX;
					rectY = mouseY;
					rect = new RectF(rectX - 40, rectY - 40, rectX+rectSize - 40, rectY+rectSize - 40);
					rect2 = new RectF(rectX + borderSize - 40, rectY + borderSize - 40, 
							rectX + rectSize - borderSize - 40, rectY + rectSize - borderSize - 40);
				}
				else dragging = false;
				break;
			/* On mouse up drop tile in place if in valid location.
			 * If it landed on another tile swap that tile back to it's place
			 * Or be deleted otherwise
			*/
			case MotionEvent.ACTION_UP:
				//angle = Math.atan(mouseY - startY  / mouseX - startX);
				if(dragging)
				{
					velY = (mouseY - startY);
					velX = (mouseX - startX);
					float factor = (float)Math.sqrt(velX * velX + velY * velY) / speed;
					velX /= factor;
					velY /= factor;
					
					drop.remove(this);
					answer.remove(this);
					if(create.contains(this))
						create.remove(this);
					
					//if within drop zone snap to grid
					if(drop.contains(rectX, rectY))
					{
						drop.add(this);
						notMoved = false;
						rectSize = 45;
						paint.setTextSize(30);
						rect = new RectF(rectX, rectY, rectX+rectSize, rectY+rectSize);
						rect2 = new RectF(rectX + borderSize, rectY + borderSize, 
								rectX + rectSize - borderSize, rectY + rectSize - borderSize);
					}
					else if(answer.contains(rectX, rectY))
					{
						answer.add(this);
						rectSize = 45;
						paint.setTextSize(30);
						rect = new RectF(rectX, rectY, rectX+rectSize, rectY+rectSize);
						rect2 = new RectF(rectX + borderSize, rectY + borderSize, 
								rectX + rectSize - borderSize, rectY + rectSize - borderSize);
						
					}
					//If box not within dropbox, remove from board
					//TODO: return null reference to prevent memory leak
					else
					{
						flinging = true;
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
	
	public void anim()
	{
		if(flinging)
		{
			rectX += velX;
			rectY += velY;
			rect = new RectF(rectX - 40, rectY - 40, rectX+rectSize - 40, rectY+rectSize - 40);
			rect2 = new RectF(rectX + borderSize - 40, rectY + borderSize - 40, 
					rectX + rectSize - borderSize - 40, rectY + rectSize - borderSize - 40);
			if(transperancy > 0)
				transperancy -= 5;
		}
		else if(movingLeft)
		{
			if(rectX <= targetX)
			{
				rectX = targetX;
				movingLeft = false;
				velX = 0;
			}
			rectX += velX;
			rect = new RectF(rectX, rectY, rectX + rectSize, rectY + rectSize);
			rect2 = new RectF(rectX + borderSize, rectY + borderSize, 
					rectX + rectSize - borderSize, rectY + rectSize - borderSize);
		}
		else if(movingRight)
		{
			if(rectX >= targetX)
			{
				rectX = targetX;
				movingRight = false;
				velX = 0;
			}
			rectX += velX;
			rect = new RectF(rectX, rectY, rectX + rectSize, rectY + rectSize);
			rect2 = new RectF(rectX + borderSize, rectY + borderSize, 
					rectX + rectSize - borderSize, rectY + rectSize - borderSize);
		}
	}
	
	public boolean isSelected()
	{
		return dragging;
	}
	
	//Move this tile to a specific x and y position. Takes x and y position as parameters
	private void moveLeft(float x, float y)
	{
		targetX = x;
		movingLeft = true;
		
		velX = -1 * speed;
		//rectX = x;
		rectY = y;
		rect = new RectF(rectX, rectY, rectX + rectSize, rectY + rectSize);
		rect2 = new RectF(rectX + borderSize, rectY + borderSize, 
			rectX + rectSize - borderSize, rectY + rectSize - borderSize);
	}
	
	private void moveRight(float x, float y)
	{
		targetX = x;
		movingRight = true;
		
		velX = speed;
		//rectX = x;
		rectY = y;
		rect = new RectF(rectX, rectY, rectX + rectSize, rectY + rectSize);
		rect2 = new RectF(rectX + borderSize, rectY + borderSize, 
			rectX + rectSize - borderSize, rectY + rectSize - borderSize);
	}
	
	public void move(float x, float y)
	{
		if(rectX > x)
			moveLeft(x, y);
		else
			moveRight(x, y);
	}
}
