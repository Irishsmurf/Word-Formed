package com.google.corrigan.owen.wordformed;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;


import android.content.Context;
import android.util.Log;

public class Dictionary
{
  private static ArrayList<String> data;

  public Dictionary(Context context) {
    doLoad(context);
  }

  public static boolean isWord(String word) {
    int index = Collections.binarySearch(data, word);
    return index >= 0;
  }

  @SuppressWarnings("unchecked")
  private void doLoad(Context context) {
    InputStream fileIn;
    ObjectInputStream in;
    try {
      fileIn = context.getResources().openRawResource(R.raw.words);
      in = new ObjectInputStream(fileIn);

      data = (ArrayList<String>) in.readObject();
      System.out.println(data.size());
      in.close();
      fileIn.close();

    }
    catch(Exception e) {
      Log.d("Dictionary", e.getCause()+"");
    }
  }

  public static int size() {
    return data.size();
  }

  public void doSave(Context context) {
    try {
      InputStream raw = context.getResources().openRawResource(R.raw.word_list);
      Scanner in = new Scanner(raw);
      FileOutputStream out = new FileOutputStream("words.dict");
      ObjectOutput obj = new ObjectOutputStream(out);
      Log.d("Dictionary", "" + raw.available());

      while(in.hasNextLine()) {
        String line = in.nextLine();
        data.add(line);
        Log.d("Dictionary", line + " added to ArrayList Total: " + data.size());
      }

      try {
        obj.writeObject(data);
      }
      finally {
        obj.close();
      }
    } catch(IOException e) {
      Log.d("Dictionary", "Nope, not saving Either: " + e.getCause());
    }
  }
}
