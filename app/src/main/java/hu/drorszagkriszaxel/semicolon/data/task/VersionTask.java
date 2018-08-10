package hu.drorszagkriszaxel.semicolon.data.task;

import android.os.AsyncTask;

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

import hu.drorszagkriszaxel.semicolon.data.listener.VersionListener;
import hu.drorszagkriszaxel.semicolon.data.model.Code;
import hu.drorszagkriszaxel.semicolon.data.model.VersionHistory;
import hu.drorszagkriszaxel.semicolon.data.model.VersionItem;

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
 * Classical AsyncTask class to get version information about codes
 *
 */
public class VersionTask extends AsyncTask<Void,Void,VersionHistory> {

    private static final String API_PROTOCOL = "https://";
    private static final String API_HOST = "verse.pawelad.xyz";
    private static final String API_PATH_PROJECT = "/projects/";
    private static final String API_PATH_REPO = "/gh/";
    private static final String API_PATH_MAJOR = "/major/";
    private static final String API_PATH_MINOR = "/minor/";
    private static final String API_PARAMS = "?format=json";

    private VersionListener versionListener;
    private Code code;

    /**
     * Constructor that adds the listener to handle data sending and a codes to check.
     *
     * @param versionListener Listener
     * @param code            Codes
     */
    public VersionTask(VersionListener versionListener, Code code) {

        this.versionListener = versionListener;
        this.code = code;

    }

    /**
     * Downloads and validates data.
     *
     * @param voids This type is the best to work with. :-)
     * @return      Version informations or null
     */
    @Override
    protected VersionHistory doInBackground(Void... voids) {

        VersionHistory versionHistory = null;

        try {

            String pathHelper = null;

            switch (code.getType()) {

                case Code.CODE_PROJECT: {

                    pathHelper = API_PATH_PROJECT + code.getHome();
                    break;

                }
                case Code.CODE_REPO: {

                    pathHelper = API_PATH_REPO + code.getPage() + "/" + code.getHome();
                    break;

                }

            }

            if (pathHelper != null) {

                URL urlMajor = new URL(API_PROTOCOL + API_HOST +
                        pathHelper + API_PATH_MAJOR + API_PARAMS);

                URL urlMinor = new URL(API_PROTOCOL + API_HOST +
                        pathHelper + API_PATH_MINOR + API_PARAMS);

                VersionItem[] versionMajors = getItems(urlMajor);
                VersionItem[] versionMinors = getItems(urlMinor);

                if (versionMajors != null && versionMinors != null)
                    versionHistory = new VersionHistory(versionMajors,versionMinors);

            }

        } catch (IOException|JSONException e) {

            Crashlytics.logException(e);
            e.printStackTrace();

        }

        return versionHistory;

    }

    /**
     * Sends data to requester.
     *
     * @param versionHistory Array of version history data or null
     */
    @Override
    protected void onPostExecute(VersionHistory versionHistory) {

        super.onPostExecute(versionHistory);
        versionListener.onResult(versionHistory);

    }

    /**
     * Handles the effective download.
     *
     * @param url            Location to download data from it
     * @return               Version history information
     * @throws IOException   In case if I/O crashes
     * @throws JSONException In case if JSON isn't valid
     */
    private VersionItem[] getItems(URL url) throws IOException,JSONException {

        VersionItem[] versionItems = null;

        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        connection.connect();
        InputStream inputStream = connection.getInputStream();
        connection.disconnect();

        if (inputStream != null) {

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {

                stringBuilder.append(line).append("\n");

            }

            bufferedReader.close();

            if (stringBuilder.length() > 0) {

                String jsonString = stringBuilder.toString();
                JSONObject jsonObject = new JSONObject(jsonString);

                Iterator<String> iterator = jsonObject.keys();

                versionItems = new VersionItem[jsonObject.length()];

                int arrayHelper = 0;

                while (iterator.hasNext()) {

                    String family = iterator.next();
                    String number = jsonObject.optString(family,"");

                    versionItems[arrayHelper] = new VersionItem(family,number);
                    arrayHelper++;

                }


            }

        }

        return versionItems;

    }

}
