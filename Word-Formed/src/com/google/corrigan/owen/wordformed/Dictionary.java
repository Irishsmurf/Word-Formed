package com.google.corrigan.owen.wordformed;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

import android.app.Activity;
import android.util.Log;


public class Dictionary
{
	private Trie prefixTree;
	
	/*public Dictionary(Activity act)
	{
		prefixTree = new Trie();
		
		try{
				Scanner in = new Scanner(act.getResources().openRawResource(R.raw.word_list));
				while(in.hasNextLine())
				{
					String line = in.nextLine();
					prefixTree.add(line);
					Log.d("Loading", line + " added. Total: "+prefixTree.size());
				}
		}
		catch(Exception e)
		{
			Log.d("Dictionary", ""+e.getCause());
		}
	}*/
	public Dictionary(Activity act)
	{
		try{
			InputStream file = act.getResources().openRawResource(R.raw.words); //new FileInputStream("words.dict");
			InputStream buffer = new BufferedInputStream(file);
			ObjectInput input = new ObjectInputStream( buffer );
			try{
				Object obj = input.readObject();
				Log.d("Dictionary", "obj.toString: "+obj.toString());
				//prefixTree = input.readObject();
			}
			finally{
				input.close();
			}
		}
		catch(Exception e){
			e.getStackTrace();
			Log.d("Exceptions", "ObjectStream : "+ e.getCause());
		}
	}
	
	public boolean isWord(String token)
	{
		return prefixTree.isEntry(token);
	}
	
	public int size()
	{
		return prefixTree.size();
	}
}
