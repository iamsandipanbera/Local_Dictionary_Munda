package com.devsan.loctionary;
import android.app.*;
import android.os.*;
import android.content.*;
import android.net.*;
import android.view.*;
import com.google.android.gms.ads.*;

public class ContributionActivity extends Activity 
{

	private AdView adView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contribution_ui);
		
		MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        adView = (AdView)findViewById(R.id.adView);
        AdRequest request = new AdRequest.Builder().build();
        adView.loadAd(request);
	}

	public void contribution_mail(View v) {

		Intent e_Intent = new Intent(Intent.ACTION_SENDTO);
		e_Intent.setData(Uri.parse("mailto:ssing9797@gmail.com"));
		e_Intent.putExtra(Intent.EXTRA_SUBJECT, "Contribution");
		startActivity(Intent.createChooser(e_Intent, "Contribute"));

	}

	}
