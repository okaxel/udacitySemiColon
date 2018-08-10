package hu.drorszagkriszaxel.semicolon.data.task;

import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.crashlytics.android.Crashlytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import hu.drorszagkriszaxel.semicolon.data.listener.QuoteListener;
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
 * Classical AsyncTask class to get random quote the API
 *
 */
public class QuoteTask extends AsyncTask<Void,Void,Quote> {

    private static final String API_PROTOCOL = "http://";
    private static final String API_HOST = "quotes.stormconsultancy.co.uk";
    private static final String API_PATH = "/random.json"; // "/api/1.0/"

    private static final String JSON_TAG_TEXT = "quote"; // "quoteText"
    private static final String JSON_TAG_AUTHOR = "author"; // "quoteAuthor"
    private static final String JSON_TAG_ID = "id"; // "quoteLink"

    private QuoteListener quoteListener;

    /**
     * Constructor that adds the listener to handle data sending.
     *
     * @param quoteListener Listener
     */
    public QuoteTask(QuoteListener quoteListener) {

        this.quoteListener = quoteListener;

    }


    /**
     * Downloads and validates data.
     *
     * @param voids This type is the best to work with. :-)
     * @return      Quote or null
     */
    @Override
    protected Quote doInBackground(Void... voids) {

        Quote quote = null;

        try {

            URL url = new URL(API_PROTOCOL + API_HOST + API_PATH);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.connect();
            InputStream inputStream = connection.getInputStream();
            connection.disconnect();

            if (inputStream != null) {

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while (true) {

                    final String tryLine = bufferedReader.readLine();
                    if (tryLine == null) break;
                    stringBuilder.append(tryLine).append("\n");

                }

                bufferedReader.close();

                if (stringBuilder.length() > 0) {

                    String jsonString = stringBuilder.toString();
                    JSONObject jsonObject = new JSONObject(jsonString);

                    String text = jsonObject.optString(JSON_TAG_TEXT,"");

                    String author = jsonObject.optString(JSON_TAG_AUTHOR,"");

                    String id = jsonObject.optString(JSON_TAG_ID,"");

                    quote = new Quote(id,text,author);

                }

            }

        } catch (IOException|JSONException e) {

            Crashlytics.logException(e);
            e.printStackTrace();

        }

        return quote;

    }

    /**
     * Sends data to requester.
     *
     * @param quote Quote
     */
    @Override
    protected void onPostExecute(@Nullable Quote quote) {

        super.onPostExecute(quote);
        quoteListener.onResult(quote);

    }

}
