package com.google.corrigan.owen.wordformed

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.MotionEvent

class DraggableBox(private val context: Context, topX: Float, topY: Float) {
    private var rectX: Float = topX
    private var rectY: Float = topY
    private var startX: Float = topX
    private var startY: Float = topY
    private var rectSize = 45
    private val borderSize = 5
    private var rect: RectF = RectF(rectX, rectY, rectX + rectSize, rectY + rectSize)
    private var dragging = false
    private val letter: Char = TileGenerator.nextTile()
    private val value: Int = TileGenerator.getValue(letter)
    private var tile: Bitmap? = null
    private val paint: Paint = Paint()
    private val speed = 5f
    private var velX = 0f
    private var velY = 0f
    private var color: Int = 0
    private var flinging = false
    private var transparency = 255
    private var movingLeft = false
    private var movingRight = false
    private var targetX = 0f
    var notMoved = true

    init {
        val box = context.resources.getDrawable(R.drawable.tile, null)
        tile = (box as BitmapDrawable).bitmap
        
        color = when {
            value == 1 -> Color.BLACK
            value == 2 -> Color.rgb(60, 0, 0)
            value == 3 -> Color.rgb(120, 0, 0)
            value == 4 -> Color.rgb(180, 0, 0)
            value > 4 -> Color.rgb(255, 0, 0)
            else -> Color.BLACK
        }

        paint.color = color
        paint.textSize = 30f
        paint.typeface = Typeface.MONOSPACE
    }

    companion object {
        private var loaded = false
        private var soundPool: SoundPool? = null
        var popID: Int = 0
        var submitID: Int = 0
        var enteredID: Int = 0
        var finishID: Int = 0

        fun initSound(context: Context) {
            if (soundPool != null) return

            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()

            soundPool = SoundPool.Builder()
                .setMaxStreams(10)
                .setAudioAttributes(audioAttributes)
                .build()

            soundPool?.setOnLoadCompleteListener { _, _, _ -> loaded = true }

            popID = soundPool?.load(context, R.raw.pop_sound, 1) ?: 0
            submitID = soundPool?.load(context, R.raw.submit, 1) ?: 0
            enteredID = soundPool?.load(context, R.raw.entered, 1) ?: 0
            finishID = soundPool?.load(context, R.raw.finish, 1) ?: 0
        }

        fun playSound(context: Context, soundID: Int) {
            if (loaded) {
                val am = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                val curVol = am.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat()
                val maxVol = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC).toFloat()
                val vol = curVol / maxVol
                soundPool?.play(soundID, vol, vol, 1, 0, 1f)
            }
        }
    }

    fun getLetter(): Char = letter

    @Synchronized
    fun draw(canvas: Canvas) {
        paint.alpha = transparency
        tile?.let {
            canvas.drawBitmap(it, null, rect, paint)
        }
        
        if (!dragging && !flinging) {
            canvas.drawText(letter.toString(), rectX + 15, rectY + 35, paint)
        } else {
            canvas.drawText(letter.toString(), rectX + 15 - 35, rectY + 35 - 10, paint)
        }
    }

    @Synchronized
    fun onTouchEvent(event: MotionEvent, drop: Dropbox, answer: Dropbox, create: CreateBox): Boolean {
        val mouseX = event.x
        val mouseY = event.y
        
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = mouseX
                startY = mouseY
                if (rect.contains(mouseX, mouseY) && !flinging) {
                    vibrate(60)
                    playSound(context, popID)
                    dragging = true
                    rectSize = 80
                    paint.textSize = 60f
                    rectX = mouseX
                    rectY = mouseY
                    updateRect(true)
                } else {
                    dragging = false
                }
            }
            MotionEvent.ACTION_UP -> {
                if (dragging) {
                    velY = mouseY - startY
                    velX = mouseX - startX
                    val factor = Math.sqrt((velX * velX + velY * velY).toDouble()).toFloat() / speed
                    velX /= factor
                    velY /= factor

                    drop.remove(this)
                    answer.remove(this)
                    if (create.contains(this)) {
                        create.remove(this)
                    }

                    if (drop.contains(rectX, rectY)) {
                        playSound(context, popID)
                        drop.add(this)
                        notMoved = false
                        rectSize = 45
                        paint.textSize = 30f
                        updateRect(false)
                    } else if (answer.contains(rectX, rectY)) {
                        paint.textSize = 30f
                        rectSize = 45
                        playSound(context, popID)
                        answer.add(this)
                        updateRect(false)
                    } else {
                        flinging = true
                    }
                }
                dragging = false
            }
            MotionEvent.ACTION_MOVE -> {
                if (dragging) {
                    rectX = mouseX
                    rectY = mouseY
                    updateRect(true)
                }
            }
        }
        return true
    }

    private fun updateRect(isOffset: Boolean) {
        val offset = if (isOffset) 40f else 0f
        rect = RectF(rectX - offset, rectY - offset, rectX + rectSize - offset, rectY + rectSize - offset)
    }

    private fun vibrate(duration: Long) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            v.vibrate(duration)
        }
    }

    fun anim() {
        if (flinging) {
            rectX += velX
            rectY += velY
            updateRect(true)
            if (transparency > 0) {
                transparency -= 5
            }
        } else if (movingLeft || movingRight) {
            rectX += velX
            if ((movingLeft && rectX <= targetX) || (movingRight && rectX >= targetX)) {
                rectX = targetX
                movingLeft = false
                movingRight = false
                velX = 0f
            }
            updateRect(false)
        }
    }

    fun getValue(): Int = value
    fun isSelected(): Boolean = dragging

    fun move(x: Float, y: Float) {
        targetX = x
        rectY = y
        if (rectX > x) {
            movingLeft = true
            movingRight = false
            velX = -speed
        } else {
            movingRight = true
            movingLeft = false
            velX = speed
        }
        updateRect(false)
    }
}
