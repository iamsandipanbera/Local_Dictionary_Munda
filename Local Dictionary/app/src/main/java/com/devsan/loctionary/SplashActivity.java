package com.devsan.loctionary;

	import android.app.*;
	import android.os.*;
	import java.util.*;
	import android.content.*;
	import android.view.View.*;
	import android.util.*;
	import android.view.animation.*;
	import android.widget.*;
	public class SplashActivity extends Activity 
	{

		@Override
		protected void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.spalsh);
			final TextView splash = (TextView) findViewById(R.id.spalshTextView1);
			Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash_anim);
			animation.setAnimationListener(new Animation.AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						startActivity(new Intent(SplashActivity.this,MainActivity.class));
						// HomeActivity.class is the activity to go after showing the splash screen.
						finish();
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
					}
				});
			splash.startAnimation(animation);
		}}
