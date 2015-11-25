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

package com.udacity.gradle.builditbigger.supplyjokes;

/**
 * Supplies jokes to the client.
 */
public class SupplyJokes {

    // Constant string containing a joke.
    private static final String JOKE = "How many programmers does it take to change a light bulb? \n" +
            "None. It's a hardware problem.";

    /**
     * Returns a joke as a string.
     */
    public String getJoke() {
        return JOKE;
    }
}
