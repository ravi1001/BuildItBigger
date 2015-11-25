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

package com.udacity.gradle.builditbigger.displayjokes;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Displays the joke passed in through the intent extra.
 */
public class DisplayJokesFragment extends Fragment {

    public DisplayJokesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the root view.
        View root = inflater.inflate(R.layout.fragment_display_jokes, container, false);

        // Inflate the text view displaying the joke.
        TextView jokeTextView = (TextView) root.findViewById(R.id.joke_content_textview);

        // Extract the joke and set it onto the view.
        jokeTextView.setText(getJoke());

        return root;
    }

    /*
     * Extracts and returns the joke from the intent.
     */
    private String getJoke() {
        // Get the intent from the activity.
        Intent intent = getActivity().getIntent();

        // Joke string to be returned
        String joke = null;

        // Extract the joke from the intent.
        if(intent != null) {
            joke = intent.getStringExtra(DisplayJokes.JOKE_EXTRA);
        }

        // Set error message if joke could not be found.
        if(joke == null || joke.isEmpty()) {
            joke = getString(R.string.error_no_joke);
        }

        return joke;
    }
}
