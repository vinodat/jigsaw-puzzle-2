package com.yqg.puzzle;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.google.ads.InterstitialAd;
import com.google.ads.AdRequest.ErrorCode;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class AdActivity extends Activity {
	//private InterstitialAd interstitialAd;
	private AdView adView;
	
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      this.setContentView(R.layout.main);
      
   // Create the adView
      adView = new AdView(this, AdSize.BANNER, "a14eddc19acf9b7");
      LinearLayout layout = (LinearLayout) findViewById(R.id.imageslayout);
      layout.addView(adView);
      adView.loadAd(new AdRequest());
      // Create an ad.
      //interstitialAd = new InterstitialAd(this, "a14eddc19acf9b7");

      // Create an ad request.
      //AdRequest adRequest = new AdRequest();
      // Fill out ad request.

      // Register an AdListener.
      //interstitialAd.setAdListener(this);

      // Start loading the ad in the background.
      //interstitialAd.loadAd(adRequest);
    }

    public void onDestroy() {
      // Stop loading the ad.
      //interstitialAd.stopLoading();

      super.onDestroy();
    }

    public void onReceiveAd(Ad ad) {
      // Be sure to check that it is an InterstitialAd that triggered this
      // callback. Also, if there are multiple InterstitialAds, make sure it
      // is the correct one.
      //if (ad == interstitialAd) {
        // For best performance, make sure you are not performing
        // processor-intensive or media-intensive operations while showing
        // interstitial ads.
        //interstitialAd.show();
      //}
    }

	
}
