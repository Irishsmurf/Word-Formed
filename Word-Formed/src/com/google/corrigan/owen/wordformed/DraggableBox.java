package com.google.corrigan.owen.wordformed;

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
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.SystemClock;
import android.os.Vibrator;
import android.view.MotionEvent;

public class DraggableBox
{
	//Top left X and Y coordinates of draggable box
	
	private static SoundPool sound;
	static int popID;
	static int submitID;
	static int enteredID;
	static int finishID;
	private static boolean loaded;

	
	private float rectX;
	float rectY;
	//Hold starting position when the box is picked up
	private float startX;
	private float startY;
	//Length of rectangle
	private int rectSize = 45;
	//Size of border
	private int borderSize = 5;
	//Outer and inner rectangles used for dragging
	private RectF rect;
	//Boolean to say if box is being dragged
	private boolean dragging = false;
	//Letter stored in tile
	private char letter;
	private static Context context;
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
	private int value;
	
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
		//Randomly choose a character
		letter = TileGenerator.nextTile();
		value = TileGenerator.getValue(letter);
		context = context0;	
		tile = BitmapFactory.decodeResource(context.getResources(), R.drawable.tile);
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setTextSize(30);
		paint.setTypeface(Typeface.MONOSPACE);
		
		sound = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		sound.setOnLoadCompleteListener(new OnLoadCompleteListener() 
		{
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) 
			{
				loaded = true;
			}
		});
		popID = sound.load(context, R.raw.pop_sound, 1);
		submitID = sound.load(context, R.raw.submit, 1);
		enteredID = sound.load(context, R.raw.entered, 1);
		finishID = sound.load(context, R.raw.finish, 1);
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
		tile = ((BitmapDrawable)box).getBitmap();
		paint.setAlpha(transperancy);
		canvas.drawBitmap(tile, null, rect, paint);
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
					playSound(popID);	
					Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
					v.vibrate(60);
					dragging = true;
					rectSize = 80;
					paint.setTextSize(60);
					//Update position immediately to prevent letter inside from jumping
					rectX = mouseX;
					rectY = mouseY;
					rect = new RectF(rectX - 40, rectY - 40, rectX+rectSize - 40, rectY+rectSize - 40);
					new RectF(rectX + borderSize - 40, rectY + borderSize - 40, 
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
						playSound(popID);
						drop.add(this);
						notMoved = false;
						rectSize = 45;
						paint.setTextSize(30);
						rect = new RectF(rectX, rectY, rectX+rectSize, rectY+rectSize);
						new RectF(rectX + borderSize, rectY + borderSize, 
								rectX + rectSize - borderSize, rectY + rectSize - borderSize);
					}
					else if(answer.contains(rectX, rectY))
					{
						playSound(popID);
						answer.add(this);
						rectSize = 45;
						paint.setTextSize(30);
						rect = new RectF(rectX, rectY, rectX+rectSize, rectY+rectSize);
						new RectF(rectX + borderSize, rectY + borderSize, 
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
					new RectF(rectX + borderSize - 40, rectY + borderSize - 40, 
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
			new RectF(rectX + borderSize - 40, rectY + borderSize - 40, 
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
			new RectF(rectX + borderSize, rectY + borderSize, 
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
			new RectF(rectX + borderSize, rectY + borderSize, 
					rectX + rectSize - borderSize, rectY + rectSize - borderSize);
		}
	}
	
	public int getValue()
	{
		return value;
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
		new RectF(rectX + borderSize, rectY + borderSize, 
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
		new RectF(rectX + borderSize, rectY + borderSize, 
			rectX + rectSize - borderSize, rectY + rectSize - borderSize);
	}
	
	public void move(float x, float y)
	{
		if(rectX > x)
			moveLeft(x, y);
		else
			moveRight(x, y);
	}
	
	public static void playSound(int soundID)
	{
		if(loaded)
		{
			AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
			float curVol = (float) am.getStreamVolume(AudioManager.STREAM_MUSIC);
			float maxVol = (float) am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			float vol = curVol/maxVol;
			int waitCounter = 0;
			int waitLimit = 20;
			while(sound.play(soundID, vol, vol, 1, 0, 1.f) == 0 && waitCounter < waitLimit)
			{
				waitCounter++;
				SystemClock.sleep(5);
			}
		}				
	}
}
