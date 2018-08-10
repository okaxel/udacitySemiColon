package hu.drorszagkriszaxel.semicolon.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.drorszagkriszaxel.semicolon.R;
import hu.drorszagkriszaxel.semicolon.data.listener.VersionListener;
import hu.drorszagkriszaxel.semicolon.data.local.LocalData;
import hu.drorszagkriszaxel.semicolon.data.model.Code;
import hu.drorszagkriszaxel.semicolon.data.model.VersionHistory;
import hu.drorszagkriszaxel.semicolon.data.model.VersionItem;
import hu.drorszagkriszaxel.semicolon.data.task.VersionTask;
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
 * This class provides CodeDetailsActivity's functionality
 *
 */
public class CodeDetailsActivity extends AppCompatActivity {

    private final static String TAG_TRY_COUNT = "trycount";

    @BindView(R.id.code_details_coordinator) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.fab_code_details) FloatingActionButton floatingActionButton;

    @BindView(R.id.tv_code_details_name) TextView tvCodeName;
    @BindView(R.id.tv_code_details_version) TextView tvCodeVersion;
    @BindView(R.id.tv_code_details_homepage) TextView tvCodeHomepage;
    @BindView(R.id.ib_code_details_delete) ImageButton ibDelete;
    @BindView(R.id.ib_code_details_like) ImageButton ibLike;

    @BindView(R.id.tv_code_details_error) TextView tvError;
    @BindView(R.id.card_code_version) CardView cardVersion;

    @BindView(R.id.rv_version_major) RecyclerView rvMajor;
    @BindView(R.id.rv_version_minor) RecyclerView rvMinor;

    private int tryCount = 0;

    private Code code;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;

    /**
     * Creates the activity's instances.
     *
     * @param savedInstanceState Instances state if any
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_details);
        setSupportActionBar((Toolbar) findViewById(R.id.code_details_toolbar));

        if (savedInstanceState != null && savedInstanceState.containsKey(TAG_TRY_COUNT))
            tryCount = savedInstanceState.getInt(TAG_TRY_COUNT);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        ButterKnife.bind(this);

        final Intent inIntent = getIntent();

        if (inIntent != null && inIntent.hasExtra(getString(R.string.details_code_tag))) {

            code = inIntent.getParcelableExtra(getString(R.string.details_code_tag));

            if (code != null) {

                final String generatedName;
                final String generatedLink;

                if (!code.getName().equals(""))
                    getSupportActionBar().setTitle(getString(R.string.activity_code_details_label_format,code.getName()));

                if (code.getType() == Code.CODE_PROJECT) {

                    generatedName = code.getName();
                    generatedLink = code.getPage();


                } else {

                    if (code.getName().equals("")) generatedName =
                            getString(R.string.text_repo_name,code.getHome(),code.getPage());

                    else generatedName = code.getName();

                    generatedLink = getString(R.string.repo_link_helper,code.getPage(),code.getHome());

                }

                tvCodeName.setText(generatedName);
                tvCodeHomepage.setText(generatedLink);

                tvCodeHomepage.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(generatedLink)));

                    }

                });

                tvCodeHomepage.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        ClipboardManager clipboardManager =
                                (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

                        if (clipboardManager != null) {

                            makeSnack(getString(R.string.snack_uri_to_clipboard_format,generatedLink));
                            ClipData clipData = ClipData.newRawUri(getString(R.string.app_name), Uri.parse(generatedLink));
                            clipboardManager.setPrimaryClip(clipData);

                        }

                        return true;

                    }

                });

                if (!code.getVersion().equals("")) {

                    boolean needUpdate = false;

                    if (code.getVersionState() == Code.VERSION_NEW) {

                        tvCodeVersion.setText(getString(R.string.new_version_format,code.getVersion()));
                        tvCodeVersion.setTypeface(tvCodeVersion.getTypeface(), Typeface.BOLD);

                        code.setVersionState(Code.VERSION_UPDATED);
                        needUpdate = true;

                    } else {

                        tvCodeVersion.setText(code.getVersion());

                        if (code.getVersionState() == Code.VERSION_NEVER_CHECKED) {

                            code.setVersionState(Code.VERSION_UPDATED);
                            needUpdate = true;

                        }

                    }

                    if (needUpdate) {

                        LocalData.updateCode(getContentResolver(),code);
                        LocalData.updateUiRecord(getContentResolver());

                    }

                } else tvCodeVersion.setText(getString(R.string.text_code_no_version));

                if (code.getState() == Code.STATE_LIKED) {

                    ibLike.setImageResource(R.drawable.ic_star_border);
                    ibLike.setContentDescription(getString(R.string.cd_code_details_liked));

                }

                ibLike.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        if (code.getState() == Code.STATE_LIKED) {

                            code.setState(Code.STATE_DEFAULT);
                            ibLike.setImageResource(R.drawable.ic_star_border_inactive);
                            ibLike.setContentDescription(getString(R.string.cd_code_details_not_liked));

                        } else {

                            code.setState(Code.STATE_LIKED);
                            ibLike.setImageResource(R.drawable.ic_star_border);
                            ibLike.setContentDescription(getString(R.string.cd_code_details_liked));

                        }

                        LocalData.updateCode(getContentResolver(),code);
                        LocalData.updateUiRecord(getContentResolver());

                    }

                });

                alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle(R.string.dialog_delete_title);
                alertDialogBuilder.setMessage(getString(R.string.dialog_delete_format,generatedName));

                alertDialogBuilder.setPositiveButton(getString(R.string.dialog_button_positive),
                        new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        LocalData.delete(getContentResolver(),code);

                        Toast.makeText(CodeDetailsActivity.this,
                                getString(R.string.toast_deleted_format,generatedName),
                                Toast.LENGTH_SHORT).show();

                        finish();

                    }

                });

                alertDialogBuilder.setNegativeButton(getString(R.string.dialog_button_negative),
                        new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        alertDialog .onDetachedFromWindow();

                    }

                });

                if (code.getType() != Code.CODE_PROJECT) {

                    ibDelete.setVisibility(View.VISIBLE);

                    ibDelete.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            alertDialog = alertDialogBuilder.show();

                        }

                    });

                } else {

                    ibDelete.setVisibility(View.INVISIBLE);
                    ibDelete.setContentDescription(getString(R.string.cd_code_details_delete_hidden));

                }

                getVersionInfo();

                floatingActionButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");

                        intent.putExtra(Intent.EXTRA_TEXT,
                                getString(R.string.fab_code_details_send_content,generatedName,generatedLink));

                        startActivity(Intent.createChooser(intent,getText(R.string.text_fabe_send_with)));

                    }

                });

                getVersionInfo();

            }

        } else finish();

    }

    /**
     * Saves state of instances.
     *
     * @param outState State of instances
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putInt(TAG_TRY_COUNT,tryCount);

        super.onSaveInstanceState(outState);

    }

    /**
     * Loads version info for codes.
     */
    private void getVersionInfo() {

        if (Connection.isConnected((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE))) {

            VersionListener versionListener = new VersionListener() {

                @Override
                public void onResult(@Nullable VersionHistory versionHistory) {

                    if (versionHistory != null && versionHistory.getMajor().length >0
                            && versionHistory.getMinor().length > 0) {

                        cardVersion.setVisibility(View.VISIBLE);

                        rvMajor.setLayoutManager(new
                                LinearLayoutManager(getBaseContext(),
                                LinearLayoutManager.VERTICAL,false));

                        rvMajor.setAdapter(new VersionAdapter(getLayoutInflater(),
                                versionHistory.getMajor()));

                        rvMajor.getAdapter().notifyDataSetChanged();

                        rvMinor.setLayoutManager(new
                                LinearLayoutManager(getBaseContext(),
                                LinearLayoutManager.VERTICAL,false));

                        rvMinor.setAdapter(new VersionAdapter(getLayoutInflater(),
                                versionHistory.getMinor()));

                        rvMinor.getAdapter().notifyDataSetChanged();

                    } else {

                        tvError.setText(R.string.tv_code_details_no_plus_version_info);
                        tvError.setVisibility(View.VISIBLE);

                    }

                }

            };

            VersionTask versionTask = new VersionTask(versionListener,code);
            versionTask.execute();


        } else {

            Toast.makeText(CodeDetailsActivity.this, "tryCount: " +
                    String.valueOf(tryCount), Toast.LENGTH_SHORT).show();

            if (tryCount < 1) {

                makeSnack(R.string.snack_version_info_needs_network,
                        getString(R.string.snack_action_retry), new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {

                                tryCount++;
                                getVersionInfo();

                            }

                        });

            } else {

                tvError.setText(R.string.tv_code_details_no_net_no_info);
                tvError.setVisibility(View.VISIBLE);

            }

        }

    }

    /**
     * Makes a SnackBar message.
     *
     * @param resId Id of text resource
     */
    private void makeSnack(int resId) {

        makeSnack(getString(resId));

    }

    /**
     * Makes a SnackBar message.
     *
     * @param snackMessage String to show
     */
    private void makeSnack(String snackMessage) {

        Snackbar snackbar = Snackbar.make(coordinatorLayout, snackMessage,Snackbar.LENGTH_LONG);
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
     * Adapter class to show version info.
     */
    class VersionAdapter extends RecyclerView.Adapter<VersionAdapter.ViewHolder> {

        private LayoutInflater layoutInflater;
        private VersionItem[] versionItems;

        /**
         * Constructs the adapter.
         *
         * @param layoutInflater Inflater to inflate
         * @param versionItems   Version items
         */
        VersionAdapter(LayoutInflater layoutInflater, VersionItem[] versionItems) {

            this.layoutInflater = layoutInflater;
            this.versionItems = versionItems;

        }

        /**
         * Creates a new ViewHolder.
         *
         * @param parent   Parent instance
         * @param viewType Type of the view
         * @return         The ViewGroup itself
         */
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = layoutInflater.inflate(R.layout.recycler_version_info,parent,false);
            return new VersionAdapter.ViewHolder(view);

        }

        /**
         * Sets up the views of the holder.
         *
         * @param holder   Holder
         * @param position Position
         */
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            holder.tvVersionName.setText(versionItems[position].getFamily());
            holder.tvVersionNumber.setText(versionItems[position].getNumber());

        }

        /**
         * Gets the count of the items.
         *
         * @return Count of items
         */
        @Override
        public int getItemCount() {

            return (versionItems != null) ? versionItems.length : 0;

        }

        /**
         * Class to instantiate ViewHolder appropriately.
         */
        class ViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.tv_version_name) TextView tvVersionName;
            @BindView(R.id.tv_version_number) TextView tvVersionNumber;

            /**
             * Custom constructor from the first view but it binds unique views in fact.
             *
             * @param itemView ItemView to bind to
             */
            ViewHolder(View itemView) {

                super(itemView);

                ButterKnife.bind(this,itemView);

            }

        }

    }

}
