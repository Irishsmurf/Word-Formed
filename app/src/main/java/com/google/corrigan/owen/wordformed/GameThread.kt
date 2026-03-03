package com.google.corrigan.owen.wordformed

import android.graphics.Canvas
import android.util.Log
import android.view.SurfaceHolder
import kotlinx.coroutines.*

class GameThread(
    private val surfaceHolder: SurfaceHolder,
    private val gamePanel: SinglePlayerGameView
) {
    private val TAG = GameThread::class.java.simpleName
    private var running = false
    private var gameJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    companion object {
        private const val MAX_FPS = 50
        private const val FRAME_PERIOD = 1000 / MAX_FPS
    }

    fun setRunning(running: Boolean) {
        this.running = running
        if (running) {
            startLoop()
        } else {
            stopLoop()
        }
    }

    private fun startLoop() {
        if (gameJob?.isActive == true) return
        
        gameJob = scope.launch {
            var ticks = 0L
            while (running && !gamePanel.finished) {
                val beginTime = System.currentTimeMillis()
                
                var canvas: Canvas? = null
                try {
                    canvas = surfaceHolder.lockCanvas()
                    if (canvas != null) {
                        synchronized(surfaceHolder) {
                            gamePanel.render(canvas)
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error in game loop", e)
                } finally {
                    canvas?.let {
                        surfaceHolder.unlockCanvasAndPost(it)
                    }
                }

                val timeDiff = System.currentTimeMillis() - beginTime
                val sleepTime = FRAME_PERIOD - timeDiff

                if (sleepTime > 0) {
                    delay(sleepTime)
                }
                
                ticks++
            }
            Log.d(TAG, "Game loops $ticks times.")
        }
    }

    private fun stopLoop() {
        running = false
        gameJob?.cancel()
    }
}
