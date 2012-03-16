package com.google.corrigan.owen.wordformed;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.Serializable;

import android.app.Activity;
import android.util.Log;

class Trie
{
	public final static char DELIMITER = '\u0001';
	private Node root;
	private int size;
	private int maxDepth;
	
	private class Node implements Serializable
	{
		public int value;
		public Node firstChild;
		public Node nextSibling;
		
		public Node(int value)
		{
			this.value = value;
			firstChild = null;
			nextSibling = null;
		}
	}
	
	public Trie()
	{
		root = new Node('r');
		size = 0;
	}
	
	public boolean isEntry(String word)
	{
		if(word.length() == 0)
			throw new IllegalArgumentException("Words can't be empty");
		return
				isEntry(root, word + DELIMITER, 0);
	}
	
	private boolean isEntry(Node root, String word, int offset)
	{
		if(offset == word.length())
			return true;
		int c = word.charAt(offset);
		//Search for node to add to
		Node next = root.firstChild;
		while(next != null)
		{
			if(next.value < c)
				next = next.nextSibling;
			else if (next.value == c)
				return isEntry(next, word, offset + 1);
			else 
				return false;
		}
		return false;
	}
	
	public int size()
	{
		return size;
	}
	
}

public class Dictionary
{
	private Trie prefixTree;
	
	public Dictionary(Activity act)
	{
		try{
			InputStream file = act.getAssets().open("dict/words.dict"); //new FileInputStream("words.dict");
			InputStream buffer = new BufferedInputStream(file);
			ObjectInput input = new ObjectInputStream( buffer );
			try{
				prefixTree = (Trie) input.readObject();
			}
			finally{
				input.close();
			}
		}
		catch(Exception e){
			Log.d("Exceptions", ""+e.getCause());
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
