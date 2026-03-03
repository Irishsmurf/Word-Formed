package com.google.corrigan.owen.wordformed

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.Log
import android.view.MotionEvent

class Button(
    private val context: Context,
    private val answer: Dropbox,
    x: Int,
    y: Int,
    width: Int,
    height: Int
) {
    private var clickable = false
    private val outer = RectF(x.toFloat(), y.toFloat(), (x + width).toFloat(), (y + height).toFloat())
    private val inner = RectF((x + 5).toFloat(), (y + 5).toFloat(), (x + width - 5).toFloat(), (y + height - 5).toFloat())
    
    private val borderColor = Paint()
    private val bgColor = Paint()
    private val textPaint = Paint()
    private var opaque = false

    init {
        borderColor.color = Color.BLACK
        bgColor.color = Color.WHITE
        textPaint.color = Color.BLACK
        textPaint.textSize = 40f
        
        answer.onTilesChanged = {
            checkWord()
        }
        
        fade()
    }

    fun checkWord() {
        if (Dictionary.isWord(answer.tilesToString())) {
            if (!opaque) {
                DraggableBox.playSound(context, DraggableBox.submitID)
            }
            opaque = true
            opaque()
        } else {
            opaque = false
            fade()
        }
    }

    private fun opaque() {
        bgColor.alpha = 255
        borderColor.alpha = 255
        textPaint.alpha = 255
        clickable = true
    }

    private fun fade() {
        bgColor.alpha = 50
        borderColor.alpha = 50
        textPaint.alpha = 50
        clickable = false
    }

    fun draw(canvas: Canvas) {
        canvas.drawRect(outer, borderColor)
        canvas.drawRect(inner, bgColor)

        canvas.drawText("Submit", inner.left + 30, inner.top + 60, textPaint)
        canvas.drawText("Word: " + answer.getScore(), inner.left + 20, inner.top - 35, textPaint)
    }

    fun onTouchEvent(event: MotionEvent): Int {
        var score = 0
        if (clickable) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (outer.contains(event.x, event.y)) {
                        bgColor.color = Color.parseColor("#CCCCCC")
                        val wordStr = answer.tilesToString()
                        if (Dictionary.isWord(wordStr)) {
                            score += answer.getScore()
                            val formattedWord = wordStr.lowercase().replaceFirstChar { it.uppercase() }
                            SinglePlayerGame.wordList.add(Word(formattedWord, answer.getScore()))
                            try {
                                answer.removeAll()
                                DraggableBox.playSound(context, DraggableBox.enteredID)
                            } catch (e: Exception) {
                                Log.d("ExceptionS", e.message ?: "Unknown error")
                            }
                        }
                    }
                }
                MotionEvent.ACTION_UP -> {
                    bgColor.color = Color.WHITE
                    fade()
                }
            }
        }
        return score
    }
}
