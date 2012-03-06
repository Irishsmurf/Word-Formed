package com.google.corrigan.owen.wordformed;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

public class SinglePlayerGameView extends SurfaceView implements SurfaceHolder.Callback
{
	
	Dropbox drop = new Dropbox(10, 250, 460, 70);
	Dropbox answer = new Dropbox(10, 450, 460, 70);
	ArrayList<DraggableBox> db = new ArrayList<DraggableBox>();
	CreateBox create;
	
	private GameThread thread;
	
	public SinglePlayerGameView(Context context)
	{
		super(context);
		
		thread = new GameThread(getHolder(), this);
		
		setFocusable(true);
		getHolder().addCallback(this);
		create = new CreateBox(10, 50, 460, 70, context);
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
	public boolean onTouchEvent(MotionEvent event)
	{
		int selected = 0;
		for(DraggableBox d: db)
		{
			if(d.isSelected()) selected = db.indexOf(d);
		}
		Collections.swap(db, db.size() - 1, selected);
		
		for(DraggableBox d: db)
		{
			d.onTouchEvent(event, drop, answer, create);
		}
		invalidate();
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

	public void render(Canvas canvas)
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

