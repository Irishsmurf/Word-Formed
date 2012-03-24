package com.google.corrigan.owen.wordformed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class HiScoreDataSource {

	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_NAME, MySQLiteHelper.COLUMN_SCORE };
	private String sort = "SELECT " + MySQLiteHelper.COLUMN_NAME
			+ MySQLiteHelper.COLUMN_SCORE
			+ "FROM " + MySQLiteHelper.TABLE_HISCORES
			+ "ORDER BY " + MySQLiteHelper.COLUMN_SCORE;

	public HiScoreDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public HiScore createHiScore(String name, int score) {
		Log.d("HISCORES", "In data source, score = " + score);
		ContentValues values = new ContentValues(3);
		values.put(MySQLiteHelper.COLUMN_NAME, name);
		values.put(MySQLiteHelper.COLUMN_SCORE, score);
		Log.d("HISCORES", "dataSource, score in value: " + values.get(MySQLiteHelper.COLUMN_SCORE));
		long insertId = database.insert(MySQLiteHelper.TABLE_HISCORES, null,
				values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_HISCORES,
				allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		HiScore newHiScore = cursorToHiScore(cursor);
		cursor.close();
		return newHiScore;
	}

	public void deleteHiScore(HiScore hiScore) {
		long id = hiScore.getId();
		System.out.println("Comment deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_HISCORES, MySQLiteHelper.COLUMN_ID
				+ " = " + id, null);
	}

	public List<HiScore> getAllHiScores() {
		Log.d("HISCORES", "DataSource, getting all hiScores");
		
		List<HiScore> hiScores = new ArrayList<HiScore>();
		
		Cursor cursor = database.query(MySQLiteHelper.TABLE_HISCORES,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			HiScore hiScore = cursorToHiScore(cursor);
			hiScores.add(hiScore);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		
		//Sort HiScores
		Log.d("HISCORES", "Sorting HiScores");
		Collections.sort(hiScores);
		for(int i = 0; i < hiScores.size(); i++)
			hiScores.get(i).setName((i + 1) + ". " + hiScores.get(i).getName());
		if(hiScores.size() > 25)
			hiScores = hiScores.subList(0, 25);
		return hiScores;
	}

	private HiScore cursorToHiScore(Cursor cursor) {
		HiScore hiScore = new HiScore();
		hiScore.setId(cursor.getLong(0));
		hiScore.setName(cursor.getString(1));
		hiScore.setScore(Integer.parseInt(cursor.getString(2)));
		return hiScore;
	}
}
