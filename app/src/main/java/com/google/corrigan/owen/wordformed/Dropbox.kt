package com.google.corrigan.owen.wordformed

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.Log
import java.util.LinkedList

class Dropbox(x: Int, y: Int, width: Int, height: Int) {
    private val border = 5
    private val dragBorder: RectF = RectF(x.toFloat(), y.toFloat(), (x + width).toFloat(), (y + height).toFloat())
    private val dragFill: RectF = RectF((x + border).toFloat(), (y + border).toFloat(), (x + width - border).toFloat(), (y + height - border).toFloat())
    private val tiles = LinkedList<DraggableBox>()
    private val paint = Paint()
    private val instructions: String

    var onTilesChanged: (() -> Unit)? = null

    init {
        instructions = if (y == 325) {
            "Save letters here"
        } else {
            "Form words here"
        }
        paint.color = Color.BLACK
        paint.textSize = 30f
        paint.typeface = Typeface.MONOSPACE
        paint.alpha = 150
    }

    fun tilesToString(): String {
        val word = StringBuilder()
        for (tile in tiles) {
            word.append(tile.getLetter())
        }
        val result = word.toString()
        Log.d("DropBox", result)
        return result
    }

    fun getScore(): Int {
        var valSum = 0
        for (tile in tiles) {
            valSum += tile.getValue()
        }
        return valSum
    }

    fun add(d: DraggableBox) {
        if (tiles.size == 7) {
            val tmp = getFirst()
            tmp.move(-100f, dragBorder.bottom - 75f)
            tiles.remove(tmp)
        }
        tiles.add(d)
        onTilesChanged?.invoke()
        updatePositions()
    }

    fun updatePositions() {
        for (i in tiles.indices) {
            tiles[i].move(i * 65f + 20f, dragBorder.bottom - 75f)
        }
    }

    fun remove(d: DraggableBox) {
        tiles.remove(d)
        onTilesChanged?.invoke()
        updatePositions()
    }

    @Synchronized
    fun removeAll() {
        val clone = LinkedList(tiles)
        for (tile in clone) {
            val tmp = getFirst()
            tmp.move(-100f, dragBorder.bottom - 60f)
            tiles.remove(tmp)
        }
        onTilesChanged?.invoke()
        updatePositions()
    }

    fun getFirst(): DraggableBox {
        return tiles.first
    }

    fun draw(canvas: Canvas) {
        val dragRectangle = Paint()
        dragRectangle.color = Color.BLACK
        canvas.drawRect(dragBorder, dragRectangle)
        dragRectangle.color = Color.rgb(68, 89, 108)
        canvas.drawRect(dragFill, dragRectangle)
        canvas.drawText(instructions, dragBorder.left + 80, dragBorder.top + 60, paint)
    }

    fun contains(x: Float, y: Float): Boolean {
        return dragBorder.contains(x, y)
    }
}
