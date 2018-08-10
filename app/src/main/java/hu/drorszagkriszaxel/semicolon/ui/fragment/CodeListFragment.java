package hu.drorszagkriszaxel.semicolon.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.drorszagkriszaxel.semicolon.R;
import hu.drorszagkriszaxel.semicolon.data.local.LocalData;
import hu.drorszagkriszaxel.semicolon.data.model.Code;
import hu.drorszagkriszaxel.semicolon.ui.AddRepoActivity;
import hu.drorszagkriszaxel.semicolon.ui.CodeDetailsActivity;

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
 * Class to provide fragments for Code List Activity
 *
 */public class CodeListFragment extends Fragment {

    private static final String TAB_ID = "tabid";

    @BindView(R.id.code_list_recycler) RecyclerView recyclerView;
    @BindView(R.id.tv_no_record) TextView tvNoRecord;
    @BindView(R.id.fab_code_list) FloatingActionButton floatingActionButton;
    @BindView(R.id.code_fragment_coordinator) CoordinatorLayout coordinatorLayout;

    private int tabId;
    String msgText = "";

    /**
     * This is a required empty public constructor.
     */
    public CodeListFragment() {
    }

    /**
     * A tricky method to give a new style for the classical instance-creation-process.
     *
     * @param tabId The number of the selected tab
     * @return      A new instance of this fragment
     */
    public static CodeListFragment newInstance(int tabId) {

        CodeListFragment fragment = new CodeListFragment();
        Bundle args = new Bundle();

        args.putInt(TAB_ID, tabId);
        fragment.setArguments(args);
        return fragment;

    }

    /**
     * Creates the fragment.
     *
     * @param savedInstanceState State of instances
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(TAB_ID)) {

            this.tabId = getArguments().getInt(TAB_ID);

        }

    }

    /**
     * Creates views.
     *
     * @param inflater           Inflater to inflate
     * @param container          Container to contain
     * @param savedInstanceState State of instances
     * @return                   Views to be viewed by the user
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_code_list, container, false);
        ButterKnife.bind(this,view);

        Code[] codes = null;
        String noDataMsg = getString(R.string.tv_no_code_placeholder);

        if (getContext() != null) {

            switch (tabId) {

                case 0: {

                    codes = LocalData.getLikedCodes(getContext().getContentResolver());
                    if (codes != null) {

                        msgText = getString(R.string.fab_code_list_send_liked,
                                makeLikedList(codes));

                        floatingActionButton.setContentDescription(getString(
                                R.string.cd_code_list_fab_liked));

                    } else {

                        msgText = getString(R.string.fab_code_list_send_liked_empty);

                        floatingActionButton.setContentDescription(getString(
                                R.string.cd_code_list_fab_liked_empty));

                    }

                    floatingActionButton.setImageResource(R.drawable.ic_share);

                    floatingActionButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {


                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("text/plain");

                            intent.putExtra(Intent.EXTRA_TEXT, msgText);

                            startActivity(Intent.createChooser(intent,getText(R.string.text_fabe_send_with)));

                        }

                    });

                    noDataMsg = getString(R.string.tv_no_code_liked);

                    break;

                }

                case 1: {

                    codes = LocalData.getCodes(getContext().getContentResolver());
                    noDataMsg = getString(R.string.tv_no_code_all);

                    final Intent repoIntent = new Intent(getContext(),AddRepoActivity.class);

                    floatingActionButton.setImageResource(R.drawable.ic_add);

                    floatingActionButton.setContentDescription(getString(
                            R.string.cd_code_list_fab_codes));

                    floatingActionButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {


                            startActivity(repoIntent);

                        }

                    });

                    break;

                }

                case 2: {

                    codes = LocalData.getFreshCodes(getContext().getContentResolver());

                    if (codes != null) {

                        msgText = getString(R.string.fab_code_list_send_fresh,
                                makeFreshList(codes));

                        floatingActionButton.setContentDescription(getString(
                                R.string.cd_code_list_fab_fresh));

                    } else {

                        msgText = getString(R.string.fab_code_list_send_fresh_empty);

                        floatingActionButton.setContentDescription(getString(
                                R.string.cd_code_list_fab_fresh_empty));


                    }

                    noDataMsg = getString(R.string.tv_no_code_fresh);

                    floatingActionButton.setImageResource(R.drawable.ic_share);

                    floatingActionButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {


                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("text/plain");

                            intent.putExtra(Intent.EXTRA_TEXT, msgText);

                            startActivity(Intent.createChooser(intent,getText(R.string.text_fabe_send_with)));

                        }

                    });

                    break;

                }
            }

        }

        if (codes != null) {

            recyclerView.setLayoutManager(new
                    LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

            recyclerView.setAdapter(new CodeListAdapter(getLayoutInflater(),codes));
            recyclerView.getAdapter().notifyDataSetChanged();

            tvNoRecord.setVisibility(View.GONE);

        } else {

            tvNoRecord.setText(noDataMsg);
            tvNoRecord.setVisibility(View.VISIBLE);

        }

        return view;

    }

    /**
     * Formats list of fresh codes to share.
     *
     * @param codes Codes
     * @return      String
     */
    private String makeFreshList(Code[] codes) {

        StringBuilder stringBuilder = new StringBuilder();

        for(Code code: codes) {

            if (code.getType() == Code.CODE_PROJECT) {

                stringBuilder.append(code.getName());
                stringBuilder.append(getString(R.string.simple_space));
                stringBuilder.append(code.getVersion());
                stringBuilder.append(getString(R.string.fab_code_list_send_at));
                stringBuilder.append(code.getPage());
                stringBuilder.append("\n");


            } else {

                if (code.getName().equals(""))
                    stringBuilder.append(getString(R.string.text_repo_name,code.getHome(),code.getPage()));
                else stringBuilder.append(code.getName());

                stringBuilder.append(getString(R.string.simple_space));
                stringBuilder.append(code.getVersion());
                stringBuilder.append(getString(R.string.fab_code_list_send_at));
                stringBuilder.append(getString(R.string.repo_link_helper,code.getPage(),code.getHome()));
                stringBuilder.append("\n");

            }

        }

        return stringBuilder.toString();

    }

    /**
     * Formats list of liked codes to share.
     *
     * @param codes Codes
     * @return      String
     */
    private String makeLikedList(Code[] codes) {

        String returnString = "";

        StringBuilder stringBuilder = new StringBuilder();

        for(Code code: codes) {

            if (code.getType() == Code.CODE_PROJECT) {

                stringBuilder.append(code.getName());
                stringBuilder.append(getString(R.string.fab_code_list_send_at));
                stringBuilder.append(code.getPage());
                stringBuilder.append("\n");


            } else {

                if (code.getName().equals(""))
                    stringBuilder.append(getString(R.string.text_repo_name,code.getHome(),code.getPage()));
                else stringBuilder.append(code.getName());

                stringBuilder.append(getString(R.string.fab_code_list_send_at));
                stringBuilder.append(getString(R.string.repo_link_helper,code.getPage(),code.getHome()));
                stringBuilder.append("\n");

            }

        }

        return stringBuilder.toString();

    }

    /**
     * Adapter class to show quote cards.
     */
    class CodeListAdapter extends RecyclerView.Adapter<CodeListAdapter.ViewHolder> {

        private LayoutInflater layoutInflater;
        private Code[] codes;

        /**
         * Constructs the adapter.
         *
         * @param layoutInflater Inflater to inflate
         * @param codes          Data set to show
         */
        CodeListAdapter (LayoutInflater layoutInflater, Code[] codes) {

            this.layoutInflater = layoutInflater;
            this.codes = codes;

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

            View view = layoutInflater.inflate(R.layout.recyclerview_code_item,
                    parent,false);

            return new ViewHolder(view);

        }

        /**
         * Sets up the views of the holder.
         *
         * @param holder   Holder
         * @param position Position
         */
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            if (codes[position].getType() == Code.CODE_PROJECT) {

                holder.tvCodeName.setText(codes[position].getName());

            } else {

                holder.tvCodeName.setText(getString(R.string.text_repo_name,
                        codes[position].getHome(),codes[position].getPage()));

            }

            if (codes[position].getVersion().equals(""))
                holder.tvCodeVersion.setText(getString(R.string.text_code_no_version));
            else holder.tvCodeVersion.setText(codes[position].getVersion());

            if (codes[position].getVersionState() != Code.VERSION_NEW)
                holder.ivCodeFresh.setVisibility(View.INVISIBLE);
            else holder.ivCodeFresh.setVisibility(View.VISIBLE);

            if (codes[position].getState() != Code.STATE_LIKED)
                holder.ivCodeLike.setVisibility(View.INVISIBLE);
            else holder.ivCodeLike.setVisibility(View.VISIBLE);

            holder.itemView.setOnClickListener(new CodeClickListener(codes[position]));

        }

        /**
         * Gets the count of the items.
         *
         * @return Count of items
         */
        @Override
        public int getItemCount() {

            return (codes != null) ? codes.length : 0;

        }

        /**
         * Class to instantiate ViewHolder appropriately.
         */
        class ViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.code_name) TextView tvCodeName;
            @BindView(R.id.code_version) TextView tvCodeVersion;
            @BindView(R.id.code_fresh) ImageView ivCodeFresh;
            @BindView(R.id.code_like) ImageView ivCodeLike;

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

        /**
         * ClickListener class handle card clicks.
         */
        class CodeClickListener implements View.OnClickListener {

            private Code code;

            /**
             * Constructor, initializes data to send.
             *
             * @param code Data to send when object clicked
             */
            CodeClickListener(Code code) {

                this.code = code;

            }

            /**
             * Handles click event.
             *
             * @param view Required but not used
             */
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), CodeDetailsActivity.class);

                intent.putExtra(getString(R.string.details_code_tag),code);

                startActivity(intent);

            }

        }

    }

}
