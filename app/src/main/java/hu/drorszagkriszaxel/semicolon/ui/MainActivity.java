package hu.drorszagkriszaxel.semicolon.ui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.drorszagkriszaxel.semicolon.BuildConfig;
import hu.drorszagkriszaxel.semicolon.R;
import hu.drorszagkriszaxel.semicolon.data.job.scJobService;
import hu.drorszagkriszaxel.semicolon.data.listener.CodeListener;
import hu.drorszagkriszaxel.semicolon.data.listener.QuoteListener;
import hu.drorszagkriszaxel.semicolon.data.local.LocalData;
import hu.drorszagkriszaxel.semicolon.data.model.Code;
import hu.drorszagkriszaxel.semicolon.data.model.Quote;
import hu.drorszagkriszaxel.semicolon.data.provider.UiTable;
import hu.drorszagkriszaxel.semicolon.data.provider.scProvider;
import hu.drorszagkriszaxel.semicolon.data.task.ProjectTask;
import hu.drorszagkriszaxel.semicolon.data.task.QuoteTask;
import hu.drorszagkriszaxel.semicolon.utils.Connection;

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
 * This class provides MainActivity's functionality
 *
 */
public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    // AdMob keys with self explaining names...
    private static final String ADMOB_APP_KEY = BuildConfig.ADMOB_APP_KEY;
    private static final String ADMOB_TEST_DEVICE_ID = BuildConfig.ADMOB_TEST_DEVICE_ID;

    @BindView(R.id.main_coordinator) CoordinatorLayout coordinatorLayout;

    @BindView(R.id.card_code) CardView cardCode;
    @BindView(R.id.tv_code_count) TextView tvCodeCount;
    @BindView(R.id.tv_code_liked) TextView tvCodeLiked;
    @BindView(R.id.tv_code_fresh) TextView tvCodeFresh;

    @BindView(R.id.card_quote) CardView cardQuote;
    @BindView(R.id.tv_quote_count) TextView tvQuoteCount;
    @BindView(R.id.tv_quote_liked) TextView tvQuoteLiked;
    @BindView(R.id.tv_quote_hidden) TextView tvQuoteHidden;

    @BindView(R.id.main_ad) AdView adView;

    private Toolbar toolbar;

    private FirebaseAnalytics firebaseAnalytics;

    /**
     * Creates the activity's instances.
     *
     * @param savedInstanceState Instances state if any
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        getSupportLoaderManager().initLoader(0,null,this);

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        MobileAds.initialize(this,ADMOB_APP_KEY);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(ADMOB_TEST_DEVICE_ID)
                .build();
        adView.loadAd(adRequest);
        adView.setAdListener(new MyAdListener());

        handleWelcome();
        jobHandler();

    }

    /**
     * Creates loader to deal with Content Provider.
     *
     * @param id   Id of loader to identify
     * @param args Arguments if any
     * @return     CursorLoader
     */
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        final String[] uiProjection = {UiTable._ID, UiTable.CODE_COUNT, UiTable.CODE_LIKED,
                UiTable.CODE_FRESH, UiTable.QUOTE_COUNT, UiTable.QUOTE_LIKED, UiTable.QUOTE_HIDDEN};

        return new CursorLoader(this,
                scProvider.Ui.UI,uiProjection,null,null,null);

    }

    /**
     * Handles the result.
     *
     * @param loader Required but not used
     * @param data   Cursor to work with
     */
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

        data.moveToFirst();

        if (data.isAfterLast()) {

            firstRun();

        } else {

            tvCodeCount.setText(data.getString(data.getColumnIndex(UiTable.CODE_COUNT)));
            tvCodeLiked.setText(data.getString(data.getColumnIndex(UiTable.CODE_LIKED)));
            tvCodeFresh.setText(data.getString(data.getColumnIndex(UiTable.CODE_FRESH)));
            tvQuoteCount.setText(data.getString(data.getColumnIndex(UiTable.QUOTE_COUNT)));
            tvQuoteLiked.setText(data.getString(data.getColumnIndex(UiTable.QUOTE_LIKED)));
            tvQuoteHidden.setText(data.getString(data.getColumnIndex(UiTable.QUOTE_HIDDEN)));

        }

    }

    /**
     * Empty handler
     *
     * @param loader Loader
     */
    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    }

    /**
     * Handles if user is interested about codes
     *
     * @param view Required but not used
     */
    public void codeClick(View view) {

        Bundle bundle = new Bundle();

        bundle.putString(FirebaseAnalytics.Param.CAMPAIGN, getString(R.string.fa_campaign_id));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, getString(R.string.fa_item_code_door));

        firebaseAnalytics.logEvent(getString(R.string.fa_first_start),bundle);

        Intent intent = new Intent(getApplicationContext(),CodeListActivity.class);
        startActivity(intent);

    }

    /**
     * Gets some data to enjoy the app's features.
     */
    private void firstRun() {

        Bundle bundle = new Bundle();

        bundle.putString(FirebaseAnalytics.Param.CAMPAIGN, getString(R.string.fa_campaign_id));

        firebaseAnalytics.logEvent(getString(R.string.fa_first_start),bundle);

        if (Connection.isConnected((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE))) {

            makeSnack(R.string.snack_first_run);

            CodeListener codeListener = new CodeListener() {

                @Override
                public void onResult(@Nullable Code[] codes) {

                    LocalData.updateCodes(getContentResolver(),codes);
                    LocalData.updateUiRecord(getContentResolver());
                    restartLoader();

                }

            };

            ProjectTask projectTask = new ProjectTask(codeListener);
            projectTask.execute();
            getQuoteAnyWay(null);

        } else {

            makeSnack(R.string.snack_first_run_connect, getString(R.string.snack_action_retry),
                    new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    restartLoader();

                }

            });

        }

    }

    /**
     * Gets quote anyway.
     *
     * @param quote Quote or null
     */
    void getQuoteAnyWay(@Nullable Quote quote) {

        if (quote != null) {

            LocalData.updateWidgetRecord(getContentResolver(),quote);
            LocalData.increaseQuoteCount(getContentResolver());
            restartLoader();

        } else {

            QuoteListener quoteListener = new QuoteListener() {

                @Override
                public void onResult(@Nullable Quote quote) {

                    Quote validQuote = null;

                    if (quote != null) validQuote = LocalData.validateQuote(getContentResolver(),quote);

                    getQuoteAnyWay(validQuote);

                }
            };

            QuoteTask quoteTask = new QuoteTask(quoteListener);
            quoteTask.execute();

        }

    }

    /**
     * Gently thanks the user to visit the app from a homepage.
     */
    private void handleWelcome() {

        Intent intent = getIntent();

        if (intent != null) {

            String action = intent.getAction();
            Uri whereFrom = intent.getData();
            if (Intent.ACTION_VIEW.equals(action) && whereFrom != null) {

                makeSnack(getString(R.string.snack_welcome_format,whereFrom.toString()));

            }

        }

    }

    /**
     * Initializes and schedules JobDispatcher's jobs.
     */
    private void jobHandler() {

        FirebaseJobDispatcher firebaseJobDispatcher =
                new FirebaseJobDispatcher(new GooglePlayDriver(getApplicationContext()));

        Job quoteJob = firebaseJobDispatcher.newJobBuilder()
                .setService(scJobService.class)
                .setTag(scJobService.JOB_GET_QUOTE)
                .setRecurring(true)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(3300,3900))
                .setReplaceCurrent(true)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .build();

        Job projectJob = firebaseJobDispatcher.newJobBuilder()
                .setService(scJobService.class)
                .setTag(scJobService.JOB_UPDATE_PROJECTS)
                .setRecurring(true)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(82800,90000))
                .setReplaceCurrent(true)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setConstraints(Constraint.ON_UNMETERED_NETWORK)
                .build();

        Job repoJob = firebaseJobDispatcher.newJobBuilder()
                .setService(scJobService.class)
                .setTag(scJobService.JOB_UPDATE_REPOS)
                .setRecurring(true)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(82800,90000))
                .setReplaceCurrent(true)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setConstraints(Constraint.ON_UNMETERED_NETWORK)
                .build();

        firebaseJobDispatcher.mustSchedule(quoteJob);
        firebaseJobDispatcher.mustSchedule(projectJob);
        firebaseJobDispatcher.mustSchedule(repoJob);

    }

    /**
     * Makes a SnackBar message.
     *
     * @param resId Id of text resource
     */
    private void makeSnack(int resId) {

        Snackbar snackbar = Snackbar.make(coordinatorLayout,resId,Snackbar.LENGTH_LONG);
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        TextView textView = snackbarLayout.findViewById(android.support.design.R.id.snackbar_text);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            textView.setTextColor(getColor(R.color.colorTextLight));
            snackbarLayout.setBackgroundColor(getColor(R.color.colorPrimaryDark));

        } else {

            textView.setTextColor(Color.rgb(255,255,255));
            snackbarLayout.setBackgroundColor(Color.rgb(40,53,147));

        }

        snackbar.show();

    }

    /**
     * Makes a SnackBar message.
     *
     * @param string String to display
     */
    private void makeSnack(String string) {

        Snackbar snackbar = Snackbar.make(coordinatorLayout, string,Snackbar.LENGTH_LONG);
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        TextView textView = snackbarLayout.findViewById(android.support.design.R.id.snackbar_text);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            textView.setTextColor(getColor(R.color.colorTextLight));
            snackbarLayout.setBackgroundColor(getColor(R.color.colorPrimaryDark));

        } else {

            textView.setTextColor(Color.rgb(255,255,255));
            snackbarLayout.setBackgroundColor(Color.rgb(40,53,147));

        }

        snackbar.show();

    }

    /**
     * Makes a SnackBar message with action.
     *
     * @param resId    Id for text resource
     * @param action   Name of the action
     * @param listener Listener to handle click
     */
    private void makeSnack(int resId, String action, View.OnClickListener listener) {

        Snackbar snackbar = Snackbar.make(coordinatorLayout,resId,Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(action,listener);

        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        TextView textView = snackbarLayout.findViewById(android.support.design.R.id.snackbar_text);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            textView.setTextColor(getColor(R.color.colorTextLight));
            snackbarLayout.setBackgroundColor(getColor(R.color.colorPrimaryDark));
            snackbar.setActionTextColor(getColor(R.color.colorAccent));

        } else {

            textView.setTextColor(Color.rgb(255,255,255));
            snackbarLayout.setBackgroundColor(Color.rgb(40,53,147));
            snackbar.setActionTextColor(Color.rgb(255,234,0));

        }

        snackbar.show();

    }

    /**
     * Handles if user interested about quotes.
     *
     * @param view Required but not used
     */
    public void quoteClick(View view) {

        Bundle bundle = new Bundle();

        bundle.putString(FirebaseAnalytics.Param.CAMPAIGN, getString(R.string.fa_campaign_id));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, getString(R.string.fa_item_quote_door));

        firebaseAnalytics.logEvent(getString(R.string.fa_first_start),bundle);

        Intent intent = new Intent(getApplicationContext(),QuotesActivity.class);
        startActivity(intent);

    }

    /**
     * Restarts loader.
     */
    private void restartLoader() {

        getSupportLoaderManager().restartLoader(0,null,this);

    }

    /**
     * AdListener class
     */
    class MyAdListener extends AdListener {

        /**
         * Handles if user clicks on an Ad.
         */
        @Override
        public void onAdClicked() {

            Bundle bundle = new Bundle();

            bundle.putString(FirebaseAnalytics.Param.CAMPAIGN, getString(R.string.fa_campaign_id));

            firebaseAnalytics.logEvent(getString(R.string.fa_ad_click_activity_main),bundle);

            super.onAdClicked();

        }

    }

}
