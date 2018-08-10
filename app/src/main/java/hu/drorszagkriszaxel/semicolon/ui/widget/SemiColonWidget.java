package hu.drorszagkriszaxel.semicolon.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;

import hu.drorszagkriszaxel.semicolon.R;
import hu.drorszagkriszaxel.semicolon.data.local.LocalData;
import hu.drorszagkriszaxel.semicolon.data.model.Code;
import hu.drorszagkriszaxel.semicolon.data.model.WidgetRecord;
import hu.drorszagkriszaxel.semicolon.ui.CodeListActivity;
import hu.drorszagkriszaxel.semicolon.ui.QuotesActivity;
import hu.drorszagkriszaxel.semicolon.utils.BitmapUtils;

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
 * This class provides Widget's resources and flow
 *
 */
public class SemiColonWidget extends AppWidgetProvider {

    public static final String ACTION_WIDGET_UPDATE = "android.appwidget.action.APPWIDGET_UPDATE";

    /**
     * Updates Widget
     *
     * @param context          Context is one of the most common partner of developers here
     * @param appWidgetManager Manager
     * @param appWidgetId      Widget's id
     */
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.quote_placeholder);

        // Construct the RemoteViews object

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.semi_colon_widget);

        Bitmap background = BitmapUtils.getBackground(context);
        if (background != null) views.setImageViewBitmap(R.id.widget_background,background);

        WidgetRecord widgetRecord = LocalData.getWidgetRecord(context.getContentResolver());
        if (widgetRecord != null) {

            views.setTextViewText(R.id.widget_quote_text,
                    context.getString(R.string.quote_formatted,widgetRecord.getQuote().getText()));

            views.setTextViewText(R.id.widget_quote_author,widgetRecord.getQuote().getAuthor());
            if (widgetRecord.getCodeState() == Code.VERSION_NEW)
                views.setTextViewText(R.id.widget_code_state,context.getString(R.string.widget_updates_available));
            else views.setTextViewText(R.id.widget_code_state,context.getString(R.string.widget_no_updates));

        } else
            views.setTextViewText(R.id.widget_quote_text,
                    context.getString(R.string.quote_formatted,widgetText));

        Intent codeIntent = new Intent(context, CodeListActivity.class);
        Intent quoteIntent = new Intent(context, QuotesActivity.class);

        PendingIntent pendingCode = PendingIntent.getActivity(context,0,codeIntent,0);
        PendingIntent pendingQuote = PendingIntent.getActivity(context,0,quoteIntent,0);

        views.setOnClickPendingIntent(R.id.widget_code_state,pendingCode);
        views.setOnClickPendingIntent(R.id.widget_quote_text,pendingQuote);
        views.setOnClickPendingIntent(R.id.widget_quote_author,pendingQuote);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    /**
     * Handles Widget's update.
     *
     * @param context          Context to send
     * @param appWidgetManager Manager to send
     * @param appWidgetIds     Ide
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {

            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    /**
     * Handles receive event and updates Widget if needed.
     *
     * @param context Goog old context to share with other methods
     * @param intent  Intent to work with
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction() != null && intent.getAction().equals(ACTION_WIDGET_UPDATE)) {

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            onUpdate(context,appWidgetManager,appWidgetManager.getAppWidgetIds(new ComponentName(context,SemiColonWidget.class)));

        }

        super.onReceive(context, intent);

    }

    /**
     * Empty event handler.
     *
     * @param context Context
     */
    @Override
    public void onEnabled(Context context) {
    }

    /**
     * Empty event handler.
     *
     * @param context Context what else?
     */
    @Override
    public void onDisabled(Context context) {
    }

}
