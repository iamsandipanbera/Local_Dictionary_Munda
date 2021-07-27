package com.devsan.loctionary;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.util.*;
import java.io.*;
//import android.se.omapi.*;

//DevSan : Sandipan
//Created on 28.06.2021

public class DatabaseHelper extends SQLiteOpenHelper
{

    private String DB_PATH = null;
    private static String DB_NAME = "word_base.db";
    private SQLiteDatabase myDataBase;
    private final Context myContext;

    public DatabaseHelper(Context context)
	{
        super(context, DB_NAME, null, 3);
        this.myContext = context;
		this.DB_PATH = "/data/data/" + context.getPackageName() + "/" + "databases/";
		//this.DB_PATH = "/data/data/" + context.getPackageName() + "/" + "databases/?useUnicode=true&amp;characterEncoding=UTF-8";
        Log.e("Path 1", DB_PATH);

    }

    public void createDataBase() throws IOException
	{
        boolean dbExist = checkDataBase();
        if (!dbExist)
		{

            this.getReadableDatabase();
            try
			{
                myContext.deleteDatabase(DB_NAME);
                copyDataBase();

            }
			catch (IOException e)
			{
                throw new Error("Error copying database");
            }

        }

    }

    public boolean checkDataBase()
	{
        File dbFile = myContext.getDatabasePath(DB_NAME);
		if (dbFile.exists()) return true;
		if (!dbFile.getParentFile().exists())
		{
			dbFile.getParentFile().mkdirs();
		}
		return false;
	}

	private void copyDataBase() throws IOException
	{

        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0)
		{
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
        Log.i("copyDataBase", "Database copied");


    }

    public void openDataBase() throws SQLException
	{
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

		myDataBase = this.getReadableDatabase();
		String count = "SELECT count(*) FROM words";
		Cursor mcursor = myDataBase.rawQuery(count, null);
		mcursor.moveToFirst();
		int icount = mcursor.getInt(0);
		if (icount != 2257)
		{
			throw new NullPointerException("Database Error");
		}
    }


    @Override
    public synchronized void close()
	{
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }




    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
	{

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
	{
        try
		{
            this.getReadableDatabase();
			myDataBase.execSQL("DROP TABLE IF EXISTS words");
			onCreate(myDataBase);
            myContext.deleteDatabase(DB_NAME);
            copyDataBase();
        }
		catch (IOException e)
		{
            e.printStackTrace();

        }
    }

    public Cursor getMeaning(String text)
    {
        Cursor c= myDataBase.rawQuery("SELECT definition FROM words WHERE en_word==UPPER('" + text + "')", null);
        return c;
    }

    public Cursor getSuggestions(String text)
    {
        Cursor c= myDataBase.rawQuery("SELECT _id, en_word FROM words WHERE en_word LIKE '" + text + "%' LIMIT 40", null);
        return c;
    }
}
