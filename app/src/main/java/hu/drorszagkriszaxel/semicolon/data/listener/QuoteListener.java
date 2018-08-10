package hu.drorszagkriszaxel.semicolon.data.listener;

import android.support.annotation.Nullable;

import hu.drorszagkriszaxel.semicolon.data.model.Quote;

/**
 *
 * SemiColon
 *
 * © 2018 by Axel Ország-Krisz Dr.
 *
 * @author  Axel Ország-Krisz Dr.
 * @version 1.0 - first try
 *
 * ---
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ---
 *
 * For more information and source check out:
 *
 * https://github.com/okaxel/udacitySemiColon
 *
 * ---
 *
 * Interface to help catch downloaded quote from the AsyncTask
 *
 */
public interface QuoteListener {

    /**
     * Called in the AsyncTask's onPostExecute method.
     *
     * @param quote The result if any
     */
    public void onResult(@Nullable Quote quote);

}
