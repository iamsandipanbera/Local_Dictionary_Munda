package com.devsan.loctionary;

import android.content.*;
import android.os.*;
import android.support.v7.app.*;
import android.view.*;
import java.io.*;

// Void, Void, Boolean --> maps out in doInBackground, onProgressUpdate, onPostExecute
public class LoadDatabaseAsync extends AsyncTask<Void, Void, Boolean>
{

    private Context context;
    private AlertDialog mAlertDialog;
    private DatabaseHelper myDbHelper;

    // Constructor
    public LoadDatabaseAsync(Context context)
	{
        this.context = context;

    }

    @Override
    protected void onPreExecute()
	{
        super.onPreExecute();

        AlertDialog.Builder d = new AlertDialog.Builder(context, R.style.MyDialogTheme);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.alert_dialog_database_copying, null);
        d.setTitle("Loading Database...");
        d.setView(dialogView);
        mAlertDialog = d.create();

        mAlertDialog.setCancelable(false);
        mAlertDialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... params)
	{

        myDbHelper = new DatabaseHelper(context);

        try
		{
            myDbHelper.createDataBase();
        }
		catch (IOException e)
		{
            throw new Error("Database was not created");
        }
        myDbHelper.close();
        return null;

    }

    protected void onProgressUpdate(Void... values)
	{
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Boolean result)
	{
        super.onPostExecute(result);
        mAlertDialog.dismiss();
        MainActivity.openDatabase();
    }
}
