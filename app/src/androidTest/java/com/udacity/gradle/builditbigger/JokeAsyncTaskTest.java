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

import android.test.AndroidTestCase;
import android.test.UiThreadTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Tests whether a valid joke is returned from the async task.
 */
public class JokeAsyncTaskTest extends AndroidTestCase implements IJokeFetchListener {
    // Specify a valid project id for the gce server.
    private final String PROJECT_ID = "project-id";
    JokeFetchAsync mJokeFetchAsync;
    CountDownLatch mCountDownLatch;
    String mJoke;

    protected void setUp() throws Exception {
        super.setUp();

        mCountDownLatch = new CountDownLatch(1);
        mJokeFetchAsync = new JokeFetchAsync(this, PROJECT_ID);
    }

    @UiThreadTest
    public void testDownload() throws InterruptedException
    {
        mJokeFetchAsync.fetchJoke();
        mCountDownLatch.await(30, TimeUnit.SECONDS);

        assertTrue("A valid joke is returned", (mJoke != null && !mJoke.isEmpty()));
    }

    @Override
    public void jokeFetchCompleted(String joke) {
        mJoke = joke;
        mCountDownLatch.countDown();
    }
}