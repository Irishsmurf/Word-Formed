package com.google.corrigan.owen.wordformed

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.media.AudioManager
import android.os.CountDownTimer
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.WindowManager
import android.widget.EditText
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Collections
import java.util.LinkedList
import java.util.concurrent.TimeUnit

class SinglePlayerGameView(context: Context) : SurfaceView(context), SurfaceHolder.Callback {
    private val TAG = "SINGLEPLAYERGAMEVIEW"
    private val input: EditText = EditText(context)
    
    private var holdBox: RectF
    private var scoreBox: RectF
    private val create = CreateBox(10, 200, 460, 100)
    private val drop = Dropbox(10, 325, 460, 100)
    private val answer = Dropbox(10, 450, 460, 100)
    private val submit: Button
    private val db = LinkedList<DraggableBox>()
    private var thread: GameThread? = null
    
    var finished = false
    private var timeLeft = "3:00"

    companion object {
        @JvmStatic
        var score = 0
            private set
        
        @JvmStatic
        var wordsByScore: List<Word> = emptyList()
            private set
    }

    private val anim = object : CountDownTimer(180000, 10) {
        override fun onTick(millisUntilFinished: Long) {
            synchronized(db) {
                for (d in db) d.anim()
            }
        }
        override fun onFinish() {}
    }

    private val clock = object : CountDownTimer(180000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
            val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - 
                          TimeUnit.MINUTES.toSeconds(minutes)
            timeLeft = "Time: ${String.format("%d:%02d", minutes, seconds)}"
        }

        override fun onFinish() {
            onGameFinished()
        }
    }

    init {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val width = display.width.toFloat()
        
        scoreBox = RectF(30f, 580f, width - 30f, 640f)
        holdBox = RectF(0f, 0f, width, 180f)
        
        submit = Button(context, answer, 150, 650, 200, 100)
        thread = GameThread(holder, this)
        
        DraggableBox.initSound(context)
        
        clock.start()
        anim.start()
        isFocusable = true
        holder.addCallback(this)
        
        create.setContext(context)
        create.setRef(db)

        score = 0
        for (i in 0 until 7) {
            val d = DraggableBox(context, i * 65f + 20f, 60f)
            db.add(d)
            create.add(d)
        }
    }

    private fun onGameFinished() {
        wordsByScore = ArrayList(SinglePlayerGame.wordList).apply {
            sortWith { o1, o2 -> o2.score - o1.score }
        }
        
        timeLeft = "Done!"
        finished = true

        val builder = AlertDialog.Builder(context)
            .setTitle("Game Over")
            .setCancelable(false)

        if (score > 0) {
            builder.setMessage("Congratulations, you achieved a score of $score
Please enter your tag:")
                .setView(input)
                .setNeutralButton("Ok") { _, _ ->
                    val name = input.text.toString()
                    if (name.isNotEmpty()) {
                        saveHighScore(name, score)
                    }
                    visibility = GONE
                    context.startActivity(Intent(context, GameOverActivity::class.java))
                }
        } else {
            builder.setMessage("You didn't make a single word. Your failures offend me.")
                .setNeutralButton("Ok") { _, _ ->
                    visibility = GONE
                    (context as? Activity)?.finish()
                }
        }
        builder.show()
        DraggableBox.playSound(context, DraggableBox.finishID)
    }

    private fun saveHighScore(name: String, score: Int) {
        findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
            withContext(Dispatchers.IO) {
                val db = AppDatabase.getDatabase(context)
                db.hiScoreDao().insert(HiScore(name = name, score = score))
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        val background = Paint().apply {
            color = context.getColor(R.color.background)
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), background)

        drop.draw(canvas)
        answer.draw(canvas)
        create.draw(canvas)
        
        val paint = Paint().apply { color = Color.rgb(42, 63, 82) }
        canvas.drawRect(holdBox, paint)
        paint.color = Color.rgb(42, 73, 82)
        canvas.drawRect(scoreBox, paint)

        submit.draw(canvas)

        synchronized(db) {
            for (d in db) d.draw(canvas)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        synchronized(db) {
            var selectedIndex = -1
            for (i in db.indices) {
                if (db[i].isSelected()) {
                    selectedIndex = i
                    break
                }
            }
            if (selectedIndex != -1) {
                Collections.swap(db, db.size - 1, selectedIndex)
            }
        }

        score += submit.onTouchEvent(event)

        try {
            synchronized(db) {
                for (d in db) {
                    d.onTouchEvent(event, drop, answer, create)
                }
            }
            invalidate()
        } catch (e: Exception) {
            Log.e(TAG, "Error handling touch event", e)
        }

        return true
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        thread?.setRunning(true)
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        thread?.setRunning(false)
        (context as? Activity)?.finish()
    }

    fun render(canvas: Canvas) {
        val background = Paint().apply {
            color = context.getColor(R.color.background)
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), background)

        background.color = Color.WHITE
        drop.draw(canvas)
        answer.draw(canvas)
        create.draw(canvas)
        
        val paint = Paint().apply { color = Color.rgb(42, 63, 82) }
        canvas.drawRect(holdBox, paint)
        
        background.textSize = 45f
        canvas.drawText(timeLeft, 130f, 80f, background)
        canvas.drawText("Score: $score", 150f, 150f, background)

        submit.draw(canvas)
        synchronized(db) {
            for (d in db) {
                d.draw(canvas)
            }
        }
    }
}
