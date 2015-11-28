package com.udacity.gradle.builditbigger.free;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.udacity.gradle.builditbigger.IJokeFetchListener;
import com.udacity.gradle.builditbigger.JokeFetchAsync;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.displayjokes.DisplayJokes;

import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity implements IJokeFetchListener {

    // Key used to store/restore progress bar state during configuration changes.
    private static final String PROGRESS_BAR_VISIBLE_KEY = "ProgressBarVisibleKey";

    // Key used to store/restore joke button state during configuration changes.
    private static final String JOKE_BUTTON_ENABLED_KEY = "JokeButtonEnabledKey";

    // Counter to track the number of instances of this activity.
    private static AtomicInteger numActivitiesLaunched = new AtomicInteger(0);

    // Interstitial ad object.
    private InterstitialAd mInterstitialAd;

    // Circular progress bar.
    private ProgressBar mProgressBar;

    // Joke button.
    private Button mJokeButton;

    // Joke button state.
    private static boolean mIsJokeButtonEnabled = true;

    // Circular progress bar state.
    private static boolean mIsProgressBarVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Allow only a single instance of the activity as static flags are being used to maintain
        // state of joke button and progress bar. Not using singleInstance or singleTask as launch
        // mode in the manifest to prevent any alteration of the default behaviour for activities
        // and tasks.
        if(numActivitiesLaunched.incrementAndGet() > 1) { finish(); }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the progress bar and joke button.
        mProgressBar = (ProgressBar) findViewById(R.id.spinner);
        mJokeButton = (Button) findViewById(R.id.joke_button);

        // Create the interstitial ad and set the ad unit id.
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));

        // Set the interstitial ad listener.
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Start the async fetch of joke from server.
                new JokeFetchAsync(MainActivity.this, getString(R.string.backend_project_id)).fetchJoke();

                // Show the progress bar.
                showProgressBar();

                // Start loading the ad again.
                requestNewInterstitial();
            }
        });

        // Request new interstitial ad to be loaded and kept ready.
        requestNewInterstitial();

        // Check if there was a configuration change.
        if(savedInstanceState != null) {
            // Restore joke button state flag.
            if(savedInstanceState.containsKey(JOKE_BUTTON_ENABLED_KEY)) {
                mIsJokeButtonEnabled = savedInstanceState.getBoolean(JOKE_BUTTON_ENABLED_KEY);
            }

            // Restore progress bar state flag.
            if(savedInstanceState.containsKey(PROGRESS_BAR_VISIBLE_KEY)) {
                mIsProgressBarVisible = savedInstanceState.getBoolean(PROGRESS_BAR_VISIBLE_KEY);
            }
        }
    }

    @Override
    protected void onDestroy() {
        // Decrement the activity instance counter.
        numActivitiesLaunched.getAndDecrement();

        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the joke button state.
        outState.putBoolean(JOKE_BUTTON_ENABLED_KEY, mIsJokeButtonEnabled);

        // Save the progress bar state.
        outState.putBoolean(PROGRESS_BAR_VISIBLE_KEY, mIsProgressBarVisible);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Enable/disable button based on flag.
        if(mIsJokeButtonEnabled) {
            enableJokeButton();
        } else {
            disableJokeButton();
        }

        // Show/hide progress bar based on flag.
        if(mIsProgressBarVisible) {
            showProgressBar();
        } else {
            hideProgressBar();
        }
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

    @Override
    public void jokeFetchCompleted(String joke) {
        // Launch the joke displaying activity in the android library.
        Intent intent = new Intent(MainActivity.this, DisplayJokes.class);
        intent.putExtra(DisplayJokes.JOKE_EXTRA, joke);
        startActivity(intent);

        // Hide the progress bar.
        hideProgressBar();

        // Enable the joke button.
        enableJokeButton();
    }

    /**
     * Handles the tell joke button click event.
     */
    public void tellJoke(View view){
        // Disable the joke button.
        disableJokeButton();

        // Display the interstitial ad if it's loaded.
        if(mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            // Show interstitial ad.
            mInterstitialAd.show();
        } else {
            // Show the progress bar.
            showProgressBar();

            // Interstitial ad is not loaded yet, start async fetch of joke from server.
            new JokeFetchAsync(this, getString(R.string.backend_project_id)).fetchJoke();

            // Request new interstitial ad to be loaded and kept ready.
            requestNewInterstitial();
        }
    }

    // Requests a new interstitial ad.
    private void requestNewInterstitial() {
        // Check if the ad is not being loaded or not already loaded, only then request for new ad.
        if(mInterstitialAd != null && !mInterstitialAd.isLoading() && !mInterstitialAd.isLoaded()) {
            // Get an ad request object.
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(getString(R.string.test_device_id))
                    .build();

            // Request for new ad.
            mInterstitialAd.loadAd(adRequest);
        }
    }

    // Shows the progress bar.
    private void showProgressBar() {
        if(mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
            mIsProgressBarVisible = true;
        }
    }

    // Hides the progress bar.
    private void hideProgressBar() {
        if(mProgressBar != null) {
            mProgressBar.setVisibility(View.INVISIBLE);
            mIsProgressBarVisible = false;
        }
    }

    // Enables the joke button.
    private void enableJokeButton() {
        if (mJokeButton != null) {
            mJokeButton.setEnabled(true);
            mJokeButton.setClickable(true);
            mIsJokeButtonEnabled = true;
        }
    }

    // Disables the joke button.
    private void disableJokeButton() {
        if(mJokeButton != null) {
            mJokeButton.setEnabled(false);
            mJokeButton.setClickable(false);
            mIsJokeButtonEnabled = false;
        }
    }
}
