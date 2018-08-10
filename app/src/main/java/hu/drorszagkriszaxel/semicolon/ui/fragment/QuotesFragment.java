package hu.drorszagkriszaxel.semicolon.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.drorszagkriszaxel.semicolon.R;
import hu.drorszagkriszaxel.semicolon.data.listener.QuoteListener;
import hu.drorszagkriszaxel.semicolon.data.local.LocalData;
import hu.drorszagkriszaxel.semicolon.data.model.Quote;
import hu.drorszagkriszaxel.semicolon.data.model.WidgetRecord;
import hu.drorszagkriszaxel.semicolon.data.task.QuoteTask;
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
 * Class to provide fragments for Quotes Activity
 *
 */
public class QuotesFragment extends Fragment {

    private static final String TAB_ID = "tabid";

    private static final String KEY_QUOTE = "keyquote";
    private static final String KEY_RECYCLE_POSITION = "recyclepos";

    @BindView(R.id.quotes_coordinator) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.fab_quotes) FloatingActionButton floatingActionButton;

    @BindView(R.id.tv_no_quote) TextView tvNoQuote;
    @BindView(R.id.quotes_recycler) RecyclerView recyclerView;
    @BindView(R.id.quote_card) CardView cardQuote;

    @BindView(R.id.tv_quote_text) TextView tvQuoteText;
    @BindView(R.id.tv_quote_author) TextView tvQuoteAuthor;

    @BindView(R.id.ib_quote_like) ImageButton ibQuoteLike;
    @BindView(R.id.ib_quote_new) ImageButton ibQuoteNew;
    @BindView(R.id.ib_quote_hide) ImageButton ibQuoteHide;

    private int tabId;

    String msgText = "";

    private Quote quote = null;
    private Quote[] quotes = null;
    private Context context;
    private LinearLayoutManager linearLayoutManager;

    /**
     * This is a required empty public constructor.
     */
    public QuotesFragment() {
    }

    /**
     * A tricky method to give a new style for the classical instance-creation-process.
     *
     * @param tabId         The number of the selected tab
     * @return              A new instance of this fragment
     */
    public static QuotesFragment newInstance(int tabId) {

        QuotesFragment fragment = new QuotesFragment();
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

        if (tabId == 1 && savedInstanceState != null && savedInstanceState.containsKey(KEY_QUOTE)) {

            quote = savedInstanceState.getParcelable(KEY_QUOTE);

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

        View view = inflater.inflate(R.layout.fragment_quotes, container, false);
        ButterKnife.bind(this,view);

        context = getContext();

        if (context != null) {

            switch (tabId) {

                case 0: {

                    quotes = LocalData.getLikedQuotes(context.getContentResolver());

                    if (quotes == null) {

                        tvNoQuote.setText(R.string.tv_no_quote_liked);
                        tvNoQuote.setVisibility(View.VISIBLE);
                        msgText = getString(R.string.fab_quote_liked_empty);

                    } else msgText = getString(R.string.fab_quote_liked) + makeList(quotes);

                    break;

                }

                case 1: {

                    if (quote == null) {

                        WidgetRecord widgetRecord =
                                LocalData.getWidgetRecord(getContext().getContentResolver());

                        if (widgetRecord != null) quote = widgetRecord.getQuote();

                    }

                    if (quote != null) refreshQuoteTab();

                    tvQuoteText.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            updateWidget();

                        }
                    });

                    ibQuoteNew.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            getNewQuote(false);

                        }

                    });

                    ibQuoteLike.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            if (quote!= null) {

                                if (quote.getState() == Quote.STATE_LIKED)
                                    quote.setState(Quote.STATE_DEFAULT);
                                else quote.setState(Quote.STATE_LIKED);

                                LocalData.updateQuoteState(context.getContentResolver(),
                                        quote,quote.getState());

                                LocalData.updateWidgetRecord(context.getContentResolver(),quote);
                                LocalData.updateUiRecord(context.getContentResolver());
                                refreshQuoteTab();

                            }

                        }

                    });

                    ibQuoteHide.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            if (quote!= null) {

                                if (quote.getState() == Quote.STATE_HIDDEN)
                                    quote.setState(Quote.STATE_DEFAULT);
                                else quote.setState(Quote.STATE_HIDDEN);

                                LocalData.updateQuoteState(context.getContentResolver(),
                                        quote,quote.getState());

                                LocalData.updateWidgetRecord(context.getContentResolver(),quote);
                                LocalData.updateUiRecord(context.getContentResolver());
                                refreshQuoteTab();
                                getNewQuote(true);

                            }

                        }

                    });

                    break;

                }

                case 2: {

                    quotes = LocalData.getHiddenQuotes(context.getContentResolver());

                    if (quotes == null) {

                        tvNoQuote.setText(R.string.tv_no_quote_hidden);
                        tvNoQuote.setVisibility(View.VISIBLE);
                        msgText = getString(R.string.fab_quote_hidden_empty);

                    } else msgText = getString(R.string.fab_quote_hidden) + makeList(quotes);

                    break;

                }

            }

        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");

                intent.putExtra(Intent.EXTRA_TEXT, msgText);

                startActivity(Intent.createChooser(intent,getText(R.string.text_fabe_send_with)));

            }

        });

        if (quotes != null) {

            linearLayoutManager = new
                    LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);

            recyclerView.setLayoutManager(linearLayoutManager);

            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(new QuoteListAdapter(getLayoutInflater(),quotes));
            recyclerView.getAdapter().notifyDataSetChanged();

        }

        return view;

    }

    /**
     * Polishes the view when their states are restored.
     *
     * @param savedInstanceState State of instances
     */
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {

        if (savedInstanceState != null) {

            if (tabId == 1 && savedInstanceState.containsKey(KEY_QUOTE))
                quote = savedInstanceState.getParcelable(KEY_QUOTE);

            if (linearLayoutManager != null && savedInstanceState.containsKey(KEY_RECYCLE_POSITION)) {

                int position = savedInstanceState.getInt(KEY_RECYCLE_POSITION);
                linearLayoutManager.scrollToPosition(position);

            }

        }

        super.onViewStateRestored(savedInstanceState);

    }

    /**
     * Saves state of instances.
     *
     * @param outState States of instances
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        if (quote != null) outState.putParcelable(KEY_QUOTE,quote);

        if (linearLayoutManager != null) {

            int posiion = linearLayoutManager.findFirstVisibleItemPosition();
            outState.putInt(KEY_RECYCLE_POSITION,posiion);

        }

        super.onSaveInstanceState(outState);

    }

    /**
     * Gets a random quote from the internet
     *
     * @param withUpdate Determines whether update Widget or not
     */
    private void getNewQuote(final boolean withUpdate) {

        QuoteListener quoteListener = new QuoteListener() {

            @Override
            public void onResult(@Nullable Quote newQuote) {

                if (newQuote != null) {

                    quote = newQuote;
                    LocalData.increaseQuoteCount(context.getContentResolver());
                    refreshQuoteTab();
                    if (withUpdate) {

                        LocalData.updateWidgetRecord(context.getContentResolver(),quote);
                        updateWidget();

                    }

                } else getNewQuote(withUpdate);

            }
        };
        QuoteTask quoteTask = new QuoteTask(quoteListener);
        quoteTask.execute();

    }

    /**
     * Formats list of quotes to be shared.
     *
     * @param quotes Quotes
     * @return       String
     */
    private String makeList(Quote[] quotes) {

        StringBuilder stringBuilder = new StringBuilder();

        for(Quote quote: quotes) {

            if (getContext() != null) stringBuilder.
                    append(getContext().
                    getString(R.string.fab_quote_item_formatted,quote.getText(),quote.getAuthor()));

            stringBuilder.append("\n");

        }

        return stringBuilder.toString();

    }

    /**
     * Refreshes fragment's content.
     */
    private void refreshQuoteTab() {

        tvQuoteText.setText(getString(R.string.quote_formatted,quote.getText()));
        tvQuoteAuthor.setText(quote.getAuthor());

        msgText = getString(R.string.fab_quote_now,quote.getAuthor(),quote.getText());

        if (quote.getState() == Quote.STATE_LIKED)
            ibQuoteLike.setImageResource(R.drawable.ic_star_border);
        else ibQuoteLike.setImageResource(R.drawable.ic_star_border_inactive);

        if (quote.getState() == Quote.STATE_HIDDEN)
            ibQuoteHide.setImageResource(R.drawable.ic_hidden);
        else ibQuoteHide.setImageResource(R.drawable.ic_hidden_inactive);

        cardQuote.setVisibility(View.VISIBLE);

    }

    /**
     * Updates Widget.
     */
    private void updateWidget() {

        if (quote != null) {

            LocalData.updateWidgetRecord(context.getContentResolver(),quote);

            Intent intent = new Intent(SemiColonWidget.ACTION_WIDGET_UPDATE);

            if (getActivity() != null) {

                getActivity().sendBroadcast(intent);

            }

        }

    }

    /**
     * Adapter class to show quote cards.
     */
    class QuoteListAdapter extends RecyclerView.Adapter<QuoteListAdapter.ViewHolder> {

        private LayoutInflater layoutInflater;
        private Quote[] quotes;


        /**
         * Constructs the adapter.
         *
         * @param layoutInflater Inflater to inflate
         * @param quotes         Data set to show
         */
        QuoteListAdapter (LayoutInflater layoutInflater, Quote[] quotes) {

            this.layoutInflater = layoutInflater;
            this.quotes = quotes;

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

            View view = layoutInflater.inflate(R.layout.recycleview_quotes_item,
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

            holder.tvQuoteText.setText(getString(R.string.quote_formatted,quotes[position].getText()));
            holder.tvQuoteAuthor.setText(quotes[position].getAuthor());

        }

        /**
         * Gets the count of the items.
         *
         * @return Count of items
         */
        @Override
        public int getItemCount() {

            return (quotes!= null) ? quotes.length : 0;

        }

        /**
         * Class to instantiate ViewHolder appropriately.
         */
        class ViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.quote_item_text) TextView tvQuoteText;
            @BindView(R.id.quote_item_author) TextView tvQuoteAuthor;

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
