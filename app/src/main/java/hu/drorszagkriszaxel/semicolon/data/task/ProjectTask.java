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
import java.net.URL;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import hu.drorszagkriszaxel.semicolon.data.listener.CodeListener;
import hu.drorszagkriszaxel.semicolon.data.model.Code;

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
 * Classical AsyncTask class to get information about projects listed by Verse
 *
 */
public class ProjectTask extends AsyncTask<Void,Void,Code[]> {

    private static final String API_PROTOCOL = "https://";
    private static final String API_HOST = "verse.pawelad.xyz";
    private static final String API_PATH = "/projects/";
    private static final String API_PARAMS = "?format=json";

    private static final String JSON_TAG_NAME = "name";
    private static final String JSON_TAG_PAGE = "homepage";
    private static final String JSON_TAG_VERSION = "latest";

    private CodeListener codeListener;

    /**
     * Constructor that adds the listener to handle data sending.
     *
     * @param codeListener Listener
     */
    public ProjectTask(CodeListener codeListener) {

        this.codeListener = codeListener;

    }

    /**
     * Downloads and validates data.
     *
     * @param voids This type is the best to work with. :-)
     * @return      Codes or null
     */
    @Override
    protected Code[] doInBackground(Void... voids) {

        Code[] codes = null;

        try {

            URL url = new URL(API_PROTOCOL + API_HOST + API_PATH + API_PARAMS);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            connection.connect();
            InputStream inputStream = connection.getInputStream();
            connection.disconnect();

            if (inputStream != null) {

                BufferedReader bufferedReader =
                        new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {

                    stringBuilder.append(line).append("\n");

                }

                bufferedReader.close();

                if (stringBuilder.length() > 0) {

                    String jsonString = stringBuilder.toString();
                    JSONObject jsonObject = new JSONObject(jsonString);

                    int arraySize = jsonObject.length();
                    codes = new Code[arraySize];

                    Iterator<String> iterator = jsonObject.keys();

                    int arrayHelper = 0;

                    while (iterator.hasNext()) {

                        String key = iterator.next();

                        if (arrayHelper < arraySize) {

                            JSONObject object = jsonObject.getJSONObject(key);

                            String name = object.optString(JSON_TAG_NAME,"");

                            String page = object.optString(JSON_TAG_PAGE,"");

                            codes[arrayHelper] = new Code(Code.CODE_PROJECT, name, key, page);
                            arrayHelper++;

                        }


                    }


                }

            }

            if (codes != null) {

                int arrayHelper = 0;

                for (Code code: codes) {

                    URL mUrl = new URL(API_PROTOCOL + API_HOST + API_PATH +
                            code.getHome() + "/" + API_PARAMS);

                    HttpsURLConnection mConnection = (HttpsURLConnection) mUrl.openConnection();

                    mConnection.connect();
                    InputStream mInputStream = mConnection.getInputStream();
                    mConnection.disconnect();

                    if (mInputStream != null) {

                        BufferedReader bufferedReader =
                                new BufferedReader(new InputStreamReader(mInputStream));

                        StringBuilder stringBuilder = new StringBuilder();
                        String line;

                        while ((line = bufferedReader.readLine()) != null) {

                            stringBuilder.append(line).append("\n");

                        }

                        bufferedReader.close();

                        if (stringBuilder.length() > 0) {

                            String jsonString = stringBuilder.toString();
                            JSONObject jsonObject = new JSONObject(jsonString);

                            String version = jsonObject.optString(JSON_TAG_VERSION,"");

                            codes[arrayHelper].setVersion(version);
                            codes[arrayHelper].setVersionState(Code.VERSION_UPDATED);
                            arrayHelper++;


                        }

                    }

                }

            }

        } catch (IOException|JSONException e) {

            Crashlytics.logException(e);
            e.printStackTrace();

        }

        return codes;

    }

    /**
     * Sends data to requester.
     *
     * @param codes Codes or null
     */
    @Override
    protected void onPostExecute(@Nullable Code[] codes) {

        super.onPostExecute(codes);
        codeListener.onResult(codes);

    }
}
