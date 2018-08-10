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
import hu.drorszagkriszaxel.semicolon.data.model.Repo;

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
 * Classical AsyncTask class to get information about GitHub repositores adde by the user
 *
 */
public class RepoTask extends AsyncTask<Void,Void,Code[]> {

    private static final String API_PROTOCOL = "https://";
    private static final String API_HOST = "verse.pawelad.xyz";
    private static final String API_PATH = "/gh/";
    private static final String API_PARAMS = "?format=json";

    private static final String JSON_TAG_VERSION = "latest";
    private static final String JSON_TAG_NOT_FOUND = "detail";

    private static final String JSON_VALUE_NOT_FOUND = "Not found.";

    private CodeListener codeListener;
    private Repo[] repos;

    /**
     * Constructor that adds the listener to handle data sending and array of repositories to check.
     *
     * @param codeListener Listener
     * @param repos        Repositories
     */
    public RepoTask(CodeListener codeListener, @Nullable Repo[] repos) {

        this.codeListener = codeListener;
        this.repos = repos;

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

        if (repos != null && repos.length > 0) {

            codes = new Code[repos.length];
            int arrayHelper = 0;

            try {

                for (Repo repo: repos) {

                    URL url = new URL(API_PROTOCOL + API_HOST + API_PATH +
                            repo.getOwner() + "/" + repo.getName() + "/" + API_PARAMS);

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

                            String version = jsonObject.optString(JSON_TAG_VERSION,"");

                            if (version.equals("")) {

                                Iterator<String> iterator = jsonObject.keys();

                                if (iterator.hasNext() && iterator.next().equals(JSON_TAG_NOT_FOUND)
                                        && jsonObject.optString(JSON_TAG_NOT_FOUND,"")
                                        .equals(JSON_VALUE_NOT_FOUND))

                                    Crashlytics.log(String.format
                                            ("Requested repository %s doesn't exists.",url.toString()));


                            } else {

                                codes[arrayHelper] = new Code(Code.CODE_REPO,
                                        "",repo.getName(),repo.getOwner(),
                                        version,Code.VERSION_UPDATED);
                                arrayHelper++;

                            }

                        }

                    }

                }

            } catch (IOException|JSONException e) {

                Crashlytics.logException(e);
                e.printStackTrace();

            }

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
