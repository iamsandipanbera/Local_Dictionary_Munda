package com.devsan.loctionary;

	import android.app.*;
	import android.os.*;
	import java.util.*;
	import android.content.*;
	import android.view.View.*;
	import android.util.*;
	import android.app.Activity;
	import android.content.Intent;
	import android.view.*;
	import android.widget.*;
	import android.net.*;
	import android.net.Uri;
//DevSan : Sandipan

	public class DeveloperActivity extends Activity 
	{

		@Override
		protected void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.developer);}

		public void sendfeed_email(View v) {

			Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
			emailIntent.setData(Uri.parse("mailto:devsancom@gmail.com"));
			emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
			startActivity(Intent.createChooser(emailIntent, "Send feedback"));

		}

	}
				

