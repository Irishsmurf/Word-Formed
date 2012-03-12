package com.google.corrigan.owen.wordformed;


import java.util.Collections;
import java.util.LinkedList;

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
import android.view.View;
import android.view.WindowManager;

public class SinglePlayerGameView extends SurfaceView implements SurfaceHolder.Callback
{
	private final String TAG = "SINGLEPLAYERGAMEVIEW";
	
	private RectF holdBox;	
	private CreateBox create = new CreateBox(10, 200, 460, 70);
	private Dropbox drop = new Dropbox(10, 350, 460, 70);
	private Dropbox answer = new Dropbox(10, 500, 460, 70);
	private Display display;
	LinkedList<DraggableBox> db = new LinkedList<DraggableBox>();
	
	
	private GameThread thread;
	
	public SinglePlayerGameView(Context context)
	{
		super(context);
		display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		holdBox = new RectF(0, 0, display.getWidth(), display.getHeight());
		
		thread = new GameThread(getHolder(), this);
		
		setFocusable(true);
		getHolder().addCallback(this);
		//create = new CreateBox(10, 50, 460, 70, context, db);
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
		
		//Draw background for dragboxes
		drop.draw(canvas);
		answer.draw(canvas);
		create.draw(canvas);
		
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

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0)
	{
		// TODO Auto-generated method stub
		thread.setRunning(true);
		thread.start();
	}

	@Override
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
		try{
		//Draw the background...
		Paint background = new Paint();
		background.setColor(getResources().getColor(
				R.color.background));
		canvas.drawRect(0, 0, getWidth(), getHeight(), background);
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();
		}
		//Draw background for dragboxes
		drop.draw(canvas);
		answer.draw(canvas);
		create.draw(canvas);
			
		synchronized (db) {
			for(DraggableBox d: db)
			{
				d.draw(canvas);
			}
		}
	}
}

