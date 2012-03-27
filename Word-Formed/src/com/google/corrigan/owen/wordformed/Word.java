package com.google.corrigan.owen.wordformed;

public class Word
{
	public String word;
	public int score;
	
	Word(String word, int score)
	{
		this.word = word;
		this.score = score;
	}
	
	public String getWord()
	{
		return word;
	}
	
	public int getScore()
	{
		return score;
	}
}
