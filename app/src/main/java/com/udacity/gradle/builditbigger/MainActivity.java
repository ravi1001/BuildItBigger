package com.udacity.gradle.builditbigger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.udacity.gradle.builditbigger.displayjokes.DisplayJokes;
import com.udacity.gradle.builditbigger.jokesbackend.myApi.MyApi;

public class MainActivity extends ActionBarActivity implements IJokeFetchListener {

    private static MyApi mMyApiService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        // Start the async fetch of joke from gce server.
        new JokeFetchAsync(this, getString(R.string.backend_project_id)).fetchJoke();
    }

    @Override
    public void jokeFetchCompleted(String joke) {
        Intent intent = new Intent(MainActivity.this, DisplayJokes.class);
        intent.putExtra(DisplayJokes.JOKE_EXTRA, joke);
        startActivity(intent);
    }
}
