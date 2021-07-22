package com.devsan.loctionary;
import android.os.*;
import android.app.*;
import com.google.android.gms.ads.*;

public class AboutActivity extends Activity 
{

	private InterstitialAd mInterstitialAd;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_ui);
		
		mInterstitialAd = new InterstitialAd(this);  

        // set the ad unit ID  
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");  

        AdRequest adRequest = new AdRequest.Builder().build();  

        // Load ads into Interstitial Ads  
        mInterstitialAd.loadAd(adRequest);  

        mInterstitialAd.setAdListener(new AdListener() {  
				public void onAdLoaded() {  
					showInterstitial();  
				}  
			});  

}
	private void showInterstitial() {  
        if (mInterstitialAd.isLoaded()) {  
            mInterstitialAd.show();  
        }  
    }  
}
