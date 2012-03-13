package com.google.corrigan.owen.wordformed;


import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.os.CountDownTimer;


public class SinglePlayerGameView extends SurfaceView implements SurfaceHolder.Callback
{
	public void draw(Canvas canvas)
	{
		Paint paint = new Paint();
		paint.setColor(Color.rgb(42, 63, 82));
		canvas.drawRect(holdBox, paint);
	}
	
	private final String TAG = "SINGLEPLAYERGAMEVIEW";
	
	private RectF holdBox;	
	private CreateBox create = new CreateBox(10, 200, 460, 70);
	private Dropbox drop = new Dropbox(10, 350, 460, 70);
	private Dropbox answer = new Dropbox(10, 500, 460, 70);
	private Display display;
	private Button submit = new Button(150, 650, 200, 100);
	private int score = 0;
	
	
	//private Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
	private String timeLeft = "3:00";
	
	private CountDownTimer clock = new CountDownTimer(180000, 1000)
	{
		public void onTick(long millisUntilFinished)
		{
			timeLeft = "Time: "+ String.format("%d:%02d", 
						TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
						TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - 
						TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
			Log.d(TAG, timeLeft);
		}
		
		public void onFinish()
		{
			//TODO
			timeLeft = "Done!";
		}
	};
	
	LinkedList<DraggableBox> db = new LinkedList<DraggableBox>();
	
	private GameThread thread;
	
	public SinglePlayerGameView(Context context)
	{
		super(context);
		display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		holdBox = new RectF(0, 0, display.getWidth(), 180);
		Log.d(TAG, "Width = " + display.getWidth() + ", Height = " + display.getHeight());
		thread = new GameThread(getHolder(), this);
		
		clock.start();
		setFocusable(true);
		getHolder().addCallback(this);
		create.setContext(context);
		create.setRef(db);
		
		for(int i = 0; i < 7; i++)
		{
			DraggableBox d = new DraggableBox(context, i * 65 + 20, 60);
			db.add(d);
			create.add(d);
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		
		//Draw the background...
		Paint background = new Paint();
		background.setColor(getResources().getColor(
				R.color.background));
		canvas.drawRect(0, 0, getWidth(), getHeight(), background);
		
		clock.start();
		Log.d(TAG, "LOLOLOL");
		//Draw background for dragboxes
		drop.draw(canvas);
		answer.draw(canvas);
		create.draw(canvas);
		this.draw(canvas); //Draw Top Rectangle
		
		submit.draw(canvas);
		
		for(DraggableBox d: db)
		{
			d.draw(canvas);
		}
	}

	@Override
	public synchronized boolean onTouchEvent(MotionEvent event)
	{
		int selected = 0;
		for(DraggableBox d: db)
		{
			if(d.isSelected()) selected = db.indexOf(d);
		}
		Collections.swap(db, db.size() - 1, selected);
		
		score += submit.onTouchEvent(event);
		
		try
		{
			for(DraggableBox d: db)
			{
				d.onTouchEvent(event, drop, answer, create);
			}
			invalidate();
		}
		catch(Exception e)
		{
			String out = e.toString();
			Log.d(TAG, out);
		}
		return true;
	}

	
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3)
	{
		// TODO Auto-generated method stub
		
	}

	
	public void surfaceCreated(SurfaceHolder arg0)
	{
		// TODO Auto-generated method stub
		thread.setRunning(true);
		thread.start();
	}

	public void surfaceDestroyed(SurfaceHolder arg0)
	{
		boolean retry = true;
		while(retry)
		{
			try {
				thread.setRunning(false);
				((Activity)getContext()).finish();
				thread.join();
				retry = false;
			}
			catch (InterruptedException e){
				thread.stop();
			}
		}		
	}

	public void update()
	{
		// TODO Auto-generated method stub
		
	}

	public synchronized void render(Canvas canvas)
	{	
		Paint background = new Paint();
		try{
		//Draw the background...
		background.setColor(getResources().getColor(
				R.color.background));
		canvas.drawRect(0, 0, getWidth(), getHeight(), background);
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();
		}
		//Draw background for dragboxes
		
		background.setColor(Color.WHITE);
		drop.draw(canvas);
		answer.draw(canvas);
		create.draw(canvas);
		this.draw(canvas);
		background.setTextSize(45);
		
		canvas.drawText(timeLeft, 130, 80, background);
		canvas.drawText("Score: " + score, 150, 150, background);
		
		submit.draw(canvas);
		synchronized (db) {
			for(DraggableBox d: db)
			{
				d.draw(canvas);
			}
		}
	}
}

