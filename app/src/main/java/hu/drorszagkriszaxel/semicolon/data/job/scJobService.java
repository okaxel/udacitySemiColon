package hu.drorszagkriszaxel.semicolon.data.job;

import android.content.Intent;
import android.support.annotation.Nullable;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import hu.drorszagkriszaxel.semicolon.data.listener.CodeListener;
import hu.drorszagkriszaxel.semicolon.data.listener.QuoteListener;
import hu.drorszagkriszaxel.semicolon.data.local.LocalData;
import hu.drorszagkriszaxel.semicolon.data.model.Code;
import hu.drorszagkriszaxel.semicolon.data.model.Quote;
import hu.drorszagkriszaxel.semicolon.data.model.Repo;
import hu.drorszagkriszaxel.semicolon.data.task.ProjectTask;
import hu.drorszagkriszaxel.semicolon.data.task.QuoteTask;
import hu.drorszagkriszaxel.semicolon.data.task.RepoTask;
import hu.drorszagkriszaxel.semicolon.ui.widget.SemiColonWidget;

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
 * Job services to update home-screen widget and get freshness-information about libraries
 *
 */
public class scJobService extends JobService {

    public static final String JOB_GET_QUOTE = "getquote";
    public static final String JOB_UPDATE_PROJECTS = "updateprojects";
    public static final String JOB_UPDATE_REPOS = "updaterepos";

    /**
     * Handles the job itself.
     *
     * @param job Standard input about job information, I use Tag only
     * @return    Corresponds to the extended component's documentation
     */
    @Override
    public boolean onStartJob(final JobParameters job) {

        // I decided to name this property that way to don't forget the essential question?
        Boolean doesThisStillSomeWork = true;

        switch (job.getTag()) {

            case JOB_GET_QUOTE: {

                getQuoteFromApi(job);

                doesThisStillSomeWork = true;

                break;

            }

            case JOB_UPDATE_PROJECTS: {

                CodeListener projectListener = new CodeListener() {

                    @Override
                    public void onResult(@Nullable Code[] codes) {

                        if (codes != null) updateCodeThings(codes,job);
                        else jobFinished(job,false);

                    }

                };

                ProjectTask projectTask = new ProjectTask(projectListener);
                projectTask.execute();

                doesThisStillSomeWork = true;

                break;

            }

            case JOB_UPDATE_REPOS: {

                Repo[] repos = LocalData.getRepos(getContentResolver());

                if (repos != null) {

                    CodeListener repoListener = new CodeListener() {

                        @Override
                        public void onResult(@Nullable Code[] codes) {

                            if (codes != null) updateCodeThings(codes,job);
                            else jobFinished(job,false);

                        }

                    };
                    RepoTask repoTask = new RepoTask(repoListener, repos);
                    repoTask.execute();

                } else doesThisStillSomeWork = false;

            }

            default: {

                doesThisStillSomeWork = false;

            }

        }

        return doesThisStillSomeWork;

    }

    /**
     * Handles if job need to be stopped. I don't need it.
     *
     * @param job Required but not used
     * @return    Corresponds to the extended component's documentation
     */
    @Override
    public boolean onStopJob(JobParameters job) {

        return false;

    }

    /**
     * Gets a quote if don't recalls itself. (Required because of bad output.)
     *
     * @param jobParameters Not used but needed to keep cycle
     */
    private void getQuoteFromApi(final JobParameters jobParameters) {

        QuoteListener quoteListener = new QuoteListener() {

            @Override
            public void onResult(@Nullable Quote quote) {

                if (quote != null) updateQuoteThings(quote,jobParameters);
                else jobFinished(jobParameters,true);

            }

        };

        QuoteTask quoteTask = new QuoteTask(quoteListener);
        quoteTask.execute();

    }

    /**
     * Updates listed libraries.
     *
     * @param codes         List of downloaded codes
     * @param jobParameters Not used but needed to keep cycle
     */
    private void updateCodeThings(Code[] codes, JobParameters jobParameters) {

        LocalData.updateCodes(getContentResolver(),codes);

        if (LocalData.getFreshCodes(getContentResolver()) != null) {

            LocalData.updateWidgetRecord(getContentResolver(), Code.VERSION_NEW);
            updateWidget();

        }

        jobFinished(jobParameters,false);

    }

    /**
     * Handles downloaded quote.
     *
     * @param quote         The new quote
     * @param jobParameters Not used but needed to keep cycle
     */
    private void updateQuoteThings(Quote quote, JobParameters jobParameters) {

        Quote validated = LocalData.validateQuote(getContentResolver(),quote);

        if (validated != null) {

            LocalData.updateWidgetRecord(getContentResolver(),quote);
            LocalData.increaseQuoteCount(getContentResolver());
            updateWidget();
            jobFinished(jobParameters,false);

        } else getQuoteFromApi(jobParameters);

    }

    /**
     * Simple private method to update widget.
     */
    private void updateWidget() {

        Intent intent = new Intent(SemiColonWidget.ACTION_WIDGET_UPDATE);
        sendBroadcast(intent);

    }

}
