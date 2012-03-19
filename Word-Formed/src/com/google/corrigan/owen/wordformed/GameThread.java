package com.google.corrigan.owen.wordformed;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

//Thread for the Game to run on.
public class GameThread extends Thread
{
	//Constants to maintain a smooth rate of play
	private final static int MAX_FPS = 50;
	private final static int MAX_FRAME_SKIPS = 5;
	private final static int FRAME_PERIOD = 1000/MAX_FPS;
	private static final String TAG = GameThread.class.getSimpleName(); //Logcat
	private boolean running;
	private SurfaceHolder surfaceHolder; //Holders the SurfaceView
	private SinglePlayerGameView gamePanel;//Main Game
		
	public GameThread(SurfaceHolder surfaceHolder, SinglePlayerGameView gamePanel)
	{
		super();
		this.surfaceHolder = surfaceHolder;
		this.gamePanel = gamePanel;
	}
	public void setRunning(boolean running)
	{
		this.running = running;
	}
	
	@Override
	public synchronized void run()
	{
		//Boilerplate Initializations
		Canvas canvas;
		long beginTime;
		long timeDiff;
		int sleepTime;
		int framesSkipped;
		
		long ticks = 0L;
		while(running) //While Game runs
		{
			canvas = null;
			try {
				canvas = this.surfaceHolder.lockCanvas();//Lock the Canvas
				synchronized (surfaceHolder) {
					beginTime = System.currentTimeMillis();//Begin timing
					framesSkipped = 0;
					//this.gamePanel.update(); 
					this.gamePanel.render(canvas); //Render Canvas
					timeDiff = System.currentTimeMillis() - beginTime;//Time for rendering
					sleepTime = (int)(FRAME_PERIOD - timeDiff);
					
					if(sleepTime > 0)
					{
						try {
							Thread.sleep(sleepTime);
						} catch (InterruptedException e)
						{}
					}
					
					while(sleepTime < 0 && framesSkipped == MAX_FRAME_SKIPS)
					{
						this.gamePanel.update();
						sleepTime += FRAME_PERIOD;
						framesSkipped++;
					}
				}
			} finally {
				if(canvas != null) {
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
			ticks++;
			
			
		}
		Log.d(TAG, "Game loops "+ticks+" times.");
	}
}
