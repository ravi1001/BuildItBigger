package com.udacity.gradle.builditbigger.free;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.udacity.gradle.builditbigger.IJokeFetchListener;
import com.udacity.gradle.builditbigger.JokeFetchAsync;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.displayjokes.DisplayJokes;
import com.udacity.gradle.builditbigger.jokesbackend.myApi.MyApi;

public class MainActivity extends ActionBarActivity implements IJokeFetchListener {

    private static MyApi mMyApiService = null;

    // Interstitial ad object.
    private InterstitialAd mInterstitialAd;

    // Spinner.
    ProgressBar mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the interstitial ad and set the ad unit id.
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));

        // Set the interstitial ad listener.
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Start loading the ad again.
                requestNewInterstitial();
            }
        });

        // Start loading the ad asynchronously.
        requestNewInterstitial();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void tellJoke(View view){
        // Show the spinner.
        mSpinner = (ProgressBar) findViewById(R.id.spinner);
        if(mSpinner != null) {
            mSpinner.setVisibility(View.VISIBLE);
        }

        // Display the interstitial ad if it's loaded.
        if(mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }

        // Start the async fetch of joke from gce server.
        new JokeFetchAsync(this, getString(R.string.backend_project_id)).fetchJoke();
    }

    @Override
    public void jokeFetchCompleted(String joke) {
        // Launch the joke displaying activity in the android library.
        Intent intent = new Intent(MainActivity.this, DisplayJokes.class);
        intent.putExtra(DisplayJokes.JOKE_EXTRA, joke);
        startActivity(intent);

        // Hide the spinner.
        if(mSpinner != null) {
            mSpinner.setVisibility(View.GONE);
        }
    }

    // Requests a new interstitial ad.
    private void requestNewInterstitial() {
        // Check if the ad is not being loaded or already loaded, only then request for new ad.
        if(mInterstitialAd != null && !mInterstitialAd.isLoading() && !mInterstitialAd.isLoaded()) {
            // Get an ad request object.
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(getString(R.string.test_device_id))
                    .build();

            // Request for new ad.
            mInterstitialAd.loadAd(adRequest);
        }
    }
}
