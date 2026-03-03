package com.google.corrigan.owen.wordformed

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import java.util.LinkedList

class CreateBox(x: Int, y: Int, width: Int, height: Int) {
    private val border = 5
    private val dragBorder: RectF = RectF(x.toFloat(), y.toFloat(), (x + width).toFloat(), (y + height).toFloat())
    private val dragFill: RectF = RectF((x + border).toFloat(), (y + border).toFloat(), (x + width - border).toFloat(), (y + height - border).toFloat())
    private val tiles = LinkedList<DraggableBox>()
    private var ref: MutableList<DraggableBox>? = null
    private var context: Context? = null

    fun setContext(context: Context) {
        this.context = context
    }

    fun setRef(ref: MutableList<DraggableBox>) {
        this.ref = ref
    }

    fun add(d: DraggableBox) {
        if (tiles.size < 7) {
            tiles.add(d)
        }
        updatePositions()
    }

    fun updatePositions() {
        for (i in tiles.indices) {
            tiles[i].move(i * 65f + 20f, dragBorder.bottom - 75f)
        }
    }

    fun remove(d: DraggableBox) {
        context?.let { ctx ->
            val tmp = DraggableBox(ctx, 65f * 8 + 20f, 60f)
            tiles.remove(d)
            tiles.add(tmp)
            ref?.add(tmp)
            updatePositions()
        }
    }

    fun contains(d: DraggableBox): Boolean {
        return tiles.contains(d)
    }

    fun draw(canvas: Canvas) {
        val dragRectangle = Paint()
        dragRectangle.color = Color.BLACK
        canvas.drawRect(dragBorder, dragRectangle)
        dragRectangle.color = Color.rgb(68, 89, 108)
        canvas.drawRect(dragFill, dragRectangle)
    }

    fun contains(x: Float, y: Float): Boolean {
        return dragBorder.contains(x, y)
    }
}
