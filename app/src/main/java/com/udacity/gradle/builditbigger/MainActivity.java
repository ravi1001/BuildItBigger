package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.udacity.gradle.builditbigger.displayjokes.DisplayJokes;
import com.udacity.gradle.builditbigger.jokesbackend.myApi.MyApi;

import java.io.IOException;

public class MainActivity extends ActionBarActivity {

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
//        Toast.makeText(this, "derp", Toast.LENGTH_SHORT).show();
//        String joke = (new SupplyJokes()).getJoke();
//        Toast.makeText(this, joke, Toast.LENGTH_SHORT).show();

        new EndpointsAsyncTask().execute();

    }


    class EndpointsAsyncTask extends AsyncTask<Void, Void, String> {
        private Context context;

        @Override
        protected String doInBackground(Void... params) {
            if(mMyApiService == null) {  // Only do this once
                // Get the project id for the backend from string resources.
                final String projectId = getString(R.string.backend_project_id);

                // Compose the root url.
                final String rootUrl = "https://" + projectId + ".appspot.com/_ah/api/";

                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl(rootUrl);

                mMyApiService = builder.build();
            }

            try {
                return mMyApiService.getJoke().execute().getData();

            } catch (IOException e) {
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Intent intent = new Intent(MainActivity.this, DisplayJokes.class);
            intent.putExtra(DisplayJokes.JOKE_EXTRA, result);
            startActivity(intent);

        }
    }


}
