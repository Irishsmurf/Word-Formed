package com.google.corrigan.owen.wordformed;

import java.util.LinkedList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.Log;

public class Dropbox {
	private int border = 5;
	private RectF dragBorder, dragFill;
	private LinkedList<DraggableBox> tiles = new LinkedList<DraggableBox>();
	@SuppressWarnings("unused")
	private final String TAG = "DRAGGABLEBOX";

	Paint paint = new Paint();
	String instructions;

	public Dropbox(int x, int y, int width, int height) {
		dragBorder = new RectF(x, y, x + width, y + height);
		dragFill = new RectF(x + border, y + border, x + width - border, y + height - border);
		if(y == 325) {
		    instructions = "Save letters here";
		} else {
			instructions = "Form words here";
		}
		paint.setColor(R.color.black);
		paint.setTextSize(30);
		paint.setTypeface(Typeface.MONOSPACE);
		paint.setAlpha(150);
	}

	public String tilesToString() {
		String word = "";
		for(DraggableBox tile: tiles) {
			word += tile.getLetter();
		}
		Log.d("DropBox", word);
		return word;
	}

	public int getScore() {
		int val = 0;
		for(DraggableBox tile: tiles) {
			val += tile.getValue();
		}
		return val;
	}

	//Method for Adding tiles to the Dropbox
	public void add(DraggableBox d) {
		//size of 7 tiles held.
		if(tiles.size() == 7) {
			DraggableBox tmp = getFirst();
			tmp.move(-100, dragBorder.bottom - 75);
			tiles.remove(tmp);
		}
		tiles.add(d);
		Button.checkWord();
		updatePositions();
	}

	public void updatePositions() {
		for(int i = 0; i < tiles.size(); i++) {
			tiles.get(i).move(i*65+20, dragBorder.bottom - 75);
        }
	}

	public void remove(DraggableBox d) {
		tiles.remove(d);
		Button.checkWord();
		updatePositions();
	}

	@SuppressWarnings("unchecked")
	public synchronized void removeAll() {
		LinkedList<DraggableBox> clone = (LinkedList<DraggableBox>) tiles.clone();
		for(DraggableBox tile: clone) {
			DraggableBox tmp = getFirst();
			tmp.move(-100, dragBorder.bottom - 60);
			tiles.remove(tmp);		
		}
		Button.opaque();
		updatePositions();
	}

	public DraggableBox getFirst() {
		return tiles.get(0);
	}

	public void draw(Canvas canvas) {
		Paint dragRectangle = new Paint();
		dragRectangle.setColor(Color.BLACK);
		canvas.drawRect(dragBorder, dragRectangle);
		dragRectangle.setColor(Color.rgb(68, 89, 108));
		canvas.drawRect(dragFill, dragRectangle);
		canvas.drawText(instructions, dragBorder.left + 80, dragBorder.top + 60, paint);
	}

	public boolean contains(float x, float y) {
		return dragBorder.contains(x, y);
	}
}
