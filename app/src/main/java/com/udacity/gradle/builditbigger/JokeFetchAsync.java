/*
 * Copyright (C) 2015 Ravi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.udacity.gradle.builditbigger;

import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.udacity.gradle.builditbigger.jokesbackend.myApi.MyApi;

import java.io.IOException;

/**
 * Wrapper class for the async task to fetch jokes from the gce server.
 */
public class JokeFetchAsync {

    // Hold the reference to the listener object.
    private final IJokeFetchListener mJokeFetchListener;
    // Hold the project id of the gce server.
    private final String mProjectId;
    private static MyApi mMyApiService = null;

    public JokeFetchAsync(IJokeFetchListener jokeFetchListener, String projectId) {
        mJokeFetchListener = jokeFetchListener;
        mProjectId = projectId;
    }

    /**
     * Starts the async task to fetch joke from the gce server.
     */
    public void fetchJoke() {
        new EndpointsAsyncTask().execute();
    }

    private class EndpointsAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            if(mMyApiService == null) {  // Only do this once
                // Compose the root url.
                final String rootUrl = "https://" + mProjectId + ".appspot.com/_ah/api/";

                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null).setRootUrl(rootUrl);

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
            mJokeFetchListener.jokeFetchCompleted(result);
        }
    }
}
