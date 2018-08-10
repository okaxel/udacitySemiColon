package hu.drorszagkriszaxel.semicolon.data.local;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

import hu.drorszagkriszaxel.semicolon.data.model.Code;
import hu.drorszagkriszaxel.semicolon.data.model.Quote;
import hu.drorszagkriszaxel.semicolon.data.model.Repo;
import hu.drorszagkriszaxel.semicolon.data.model.UiRecord;
import hu.drorszagkriszaxel.semicolon.data.model.WidgetRecord;
import hu.drorszagkriszaxel.semicolon.data.provider.CodeTable;
import hu.drorszagkriszaxel.semicolon.data.provider.QuoteTable;
import hu.drorszagkriszaxel.semicolon.data.provider.UiTable;
import hu.drorszagkriszaxel.semicolon.data.provider.WidgetTable;
import hu.drorszagkriszaxel.semicolon.data.provider.scProvider;

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
 * A final PoJo class with static methods to handle local data-flow
 *
 */
public final class LocalData {

    private final static int VALUE_NOT_SET = -1;

    private final static String DEFAULT_QUOTE_ID = "defa1234";
    private final static String DEFAULT_QUOTE_TEXT = "Messages from deep under the hood confuses everyone.";
    private final static String DEFAULT_QUOTE_AUTHOR = "LocalData Placeholder";

    private final static String SELECTION_HELPER = "=?";

    private final static int DEFAULT_QUOTE_STATE = Quote.STATE_DEFAULT;

    private final static String[] codeProjection = {CodeTable._ID, CodeTable.TYPE, CodeTable.NAME,
            CodeTable.HOME, CodeTable.PAGE, CodeTable.VERSION, CodeTable.VERSION_STATE, CodeTable.STATE};

    private final static String[] quoteProjection = {QuoteTable._ID, QuoteTable.ID, QuoteTable.TEXT,
            QuoteTable.AUTHOR, QuoteTable.STATE};

    private final static String[] uiProjection = {UiTable._ID, UiTable.CODE_COUNT, UiTable.CODE_LIKED,
            UiTable.CODE_FRESH, UiTable.QUOTE_COUNT, UiTable.QUOTE_LIKED, UiTable.QUOTE_HIDDEN};

    private final static  String[] widgetProjection = {WidgetTable._ID, WidgetTable.ID,
            WidgetTable.TEXT, WidgetTable.AUTHOR, WidgetTable.QUOTE_STATE, WidgetTable.CODE_STATE};

    // ================= //
    // Code data methods //
    // ================= //

    /**
     * Adds a code.
     *
     * @param contentResolver Required parameter to handle Content Provider
     * @param code            Code to add
     */
    private static void add(ContentResolver contentResolver, Code code) {

        updateCode(contentResolver,code);

    }

    /**
     * To spare some code this method converts cursors to codes.
     *
     * @param cursor The cursor input
     * @return       The code output, it can be null
     */
    private static Code[] convertCursorToCode(@Nullable Cursor cursor) {

        Code[] codes = null;

        if (cursor != null) {

            if (cursor.getCount()>0) {

                codes = new Code[cursor.getCount()];
                int arrayHelper = 0;

                while (cursor.moveToNext()) {

                    int type = cursor.getInt(cursor.getColumnIndex(CodeTable.TYPE));
                    String name = cursor.getString(cursor.getColumnIndex(CodeTable.NAME));
                    String home = cursor.getString(cursor.getColumnIndex(CodeTable.HOME));
                    String page = cursor.getString(cursor.getColumnIndex(CodeTable.PAGE));
                    String version = cursor.getString(cursor.getColumnIndex(CodeTable.VERSION));
                    int versionState = cursor.getInt(cursor.getColumnIndex(CodeTable.VERSION_STATE));
                    int state = cursor.getInt(cursor.getColumnIndex(CodeTable.STATE));

                    codes[arrayHelper] = new Code(type, name, home, page, version, versionState, state);
                    arrayHelper++;

                }

            }

            cursor.close();

        }

        return codes;

    }

    /**
     * Deletes a code.
     *
     * @param contentResolver Required parameter to handle Content Provider
     * @param code            Code to delete
     */
    public static void delete(ContentResolver contentResolver, Code code) {

        int deletedLines = contentResolver
                .delete(scProvider.Code.withHome(code.getHome()),null,null);

    }

    /**
     * Simple helper method to compare code arrays.
     *
     * @param codes Array of codes
     * @param home  Home value to find
     * @return      The key's position in the array
     */
    private static int findIdInCode(Code[] codes, String home) {

        int arrayHelper = 0;

        for (Code code:codes) {

            if (code.getHome().equals(home)) return arrayHelper;

                arrayHelper++;

        }

        return VALUE_NOT_SET;

    }

    /**
     * Simple helper method to compare code arrays - designed for repo codes.
     *
     * @param codes The array
     * @param home  Home value to find
     * @param page  Page value to find
     * @return      The match's position in the array
     */
    private static int findIdInCode(Code[] codes, String home, String page) {

        int arrayHelper = 0;

        for (Code code:codes) {

            if (code.getHome().equals(home) && code.getPage().equals(page)) return arrayHelper;

            arrayHelper++;

        }

        return VALUE_NOT_SET;

    }

    /**
     * Gets a code.
     *
     * @param contentResolver Required parameter to handle Content Provider
     * @param homeId          The code's home id
     * @return                Code that matches
     */
    private static Code getCode(ContentResolver contentResolver, String homeId) {

        Code code = null;

        Cursor cursor = contentResolver.query(scProvider.Code.withHome(homeId),
                codeProjection, null, null, null);

        if (cursor != null) {

            if (cursor.getCount() > 0) {

                cursor.moveToFirst();

                int type = cursor.getInt(cursor.getColumnIndex(CodeTable.TYPE));
                String name = cursor.getString(cursor.getColumnIndex(CodeTable.NAME));
                String home = cursor.getString(cursor.getColumnIndex(CodeTable.HOME));
                String page = cursor.getString(cursor.getColumnIndex(CodeTable.PAGE));
                String version = cursor.getString(cursor.getColumnIndex(CodeTable.VERSION));
                int versionState = cursor.getInt(cursor.getColumnIndex(CodeTable.VERSION_STATE));
                int state = cursor.getInt(cursor.getColumnIndex(CodeTable.STATE));

                code = new Code(type, name, home, page, version, versionState, state);

            }

            cursor.close();

        }

        return code;
    }

    /**
     * Gets all codes.
     *
     * @param contentResolver Required parameter to handle Content Provider
     * @return                Each code from the database
     */
    public static Code[] getCodes(ContentResolver contentResolver) {

        return convertCursorToCode(contentResolver.query(scProvider.Code.CODES,
                codeProjection,null,null,null));

    }

    /**
     * Gets codes with new version.
     *
     * @param contentResolver Required parameter to handle Content Provider
     * @return                Every fresh code from the database
     */
    public static Code[] getFreshCodes(ContentResolver contentResolver) {

        String[] selectionArgs = {String.valueOf(Code.VERSION_NEW)};

        return convertCursorToCode(contentResolver.query(scProvider.Code.CODES,codeProjection,
                CodeTable.VERSION_STATE + SELECTION_HELPER,selectionArgs,null));

    }

    /**
     * Gets all codes liked by the user.
     *
     * @param contentResolver Required parameter to handle Content Provider
     * @return                Every liked code from the database
     */
    public static Code[] getLikedCodes(ContentResolver contentResolver) {

        String[] selectionArgs = {String.valueOf(Code.STATE_LIKED)};

        return convertCursorToCode(contentResolver.query(scProvider.Code.CODES,codeProjection,
                CodeTable.STATE + SELECTION_HELPER,selectionArgs,null));

    }

    /**
     * Gets originally listed codes.
     *
     * @param contentResolver Required parameter to handle Content Provider
     * @return                Every code from the database listed by the Verse API
     */
    public static Code[] getProjectCodes(ContentResolver contentResolver) {

        String[] selectionArgs = {String.valueOf(Code.CODE_PROJECT)};

        return convertCursorToCode(contentResolver.query(scProvider.Code.CODES,codeProjection,
                CodeTable.TYPE + SELECTION_HELPER,selectionArgs,null));

    }

    /**
     * Gets every repository codes in form of Repo objects.
     *
     * @param contentResolver Required parameter to handle Content Provider
     * @return                Every code from the database that is a GitHub repo in fact
     */
    public static Repo[] getRepos(ContentResolver contentResolver) {

        Repo[] repos = null;
        Code[] codes = getReposAsCodes(contentResolver);

        if (codes != null && codes.length > 0) {

            repos = new Repo[codes.length];
            int arrayHelper = 0;

            for (Code code:codes) {

                String owner = code.getPage();
                String home = code.getHome();

                repos[arrayHelper] = new Repo(owner,home);
                arrayHelper++;

            }

        }

        return repos;

    }

    /**
     * Gets every repository codes.
     *
     * @param contentResolver Required parameter to handle Content Provider
     * @return                Every code from the database that is a GitHub repo in fact
     */
    private static Code[] getReposAsCodes(ContentResolver contentResolver) {

        String[] selectionArgs = {String.valueOf(Code.CODE_REPO)};

        return convertCursorToCode(contentResolver.query(scProvider.Code.CODES,codeProjection,
                CodeTable.TYPE + SELECTION_HELPER,selectionArgs,null));

    }

    /**
     * Updates a code.
     *
     * @param contentResolver Required parameter to handle Content Provider
     * @param code            Code to update, it holds the new information in it
     */
    public static void updateCode(ContentResolver contentResolver, Code code) {

        ContentValues values = new ContentValues();

        values.put(CodeTable.TYPE,code.getType());
        values.put(CodeTable.NAME,code.getName());
        values.put(CodeTable.HOME,code.getHome());
        values.put(CodeTable.PAGE,code.getPage());
        values.put(CodeTable.VERSION,code.getVersion());
        values.put(CodeTable.VERSION_STATE,code.getVersionState());
        values.put(CodeTable.STATE,code.getState());

        if (getCode(contentResolver,code.getHome()) != null) {

            int updatedRows = contentResolver.update(scProvider.Code.withHome(code.getHome()),
                    values,null,null);

        } else {

            Uri uri = contentResolver.insert(scProvider.Code.CODES,values);

        }

        updateUiRecord(contentResolver);

    }

    /**
     * Updates multiple codes.
     *
     * @param contentResolver Required parameter to handle Content Provider
     * @param codes           Can be null to simplify code-flow
     */
    public static void updateCodes(ContentResolver contentResolver, @Nullable Code[] codes) {

        if (codes != null) {

            Code[] storedCodes = getCodes(contentResolver);
            Code[] newCodes = null;

            if (storedCodes != null) {

                ArrayList<Code> helperArrayList = new ArrayList<Code>();

                for (Code code: codes) {

                    int position = VALUE_NOT_SET;

                    if (code.getType() == Code.CODE_PROJECT)
                        position = findIdInCode(storedCodes,code.getHome());
                    else position = findIdInCode(storedCodes,code.getHome(),code.getPage());

                    if (position == VALUE_NOT_SET) {

                        helperArrayList.add(code);

                    } else {

                        if (!code.getVersion().equals(storedCodes[position].getVersion())) {

                            storedCodes[position].setVersion(code.getVersion());
                            storedCodes[position].setVersionState(Code.VERSION_NEW);

                        }

                    }

                }

                if (helperArrayList.size() > 0) {

                    ArrayList<Code> finalArrayList = new ArrayList<Code>();

                    finalArrayList.addAll(Arrays.asList(storedCodes));
                    finalArrayList.addAll(helperArrayList);

                    newCodes = (Code[]) finalArrayList.toArray();

                } else {

                    newCodes = storedCodes;

                }

            } else newCodes = codes;

            writeCodes(contentResolver,newCodes);

        }

    }

    /**
     * Writes multiple codes at the same time.
     *
     * @param contentResolver Required parameter to handle Content Provider
     * @param codes           Codes to write
     */
    private static void writeCodes(ContentResolver contentResolver, Code[] codes) {

        if (codes != null) {

            ContentValues[] values = new ContentValues[codes.length];
            int arrayHelper = 0;

            for (Code code: codes) {

                values[arrayHelper] = new ContentValues();

                values[arrayHelper].put(CodeTable.TYPE,code.getType());
                values[arrayHelper].put(CodeTable.NAME,code.getName());
                values[arrayHelper].put(CodeTable.HOME,code.getHome());
                values[arrayHelper].put(CodeTable.PAGE,code.getPage());
                values[arrayHelper].put(CodeTable.VERSION,code.getVersion());
                values[arrayHelper].put(CodeTable.VERSION_STATE,code.getVersionState());
                values[arrayHelper].put(CodeTable.STATE,code.getState());

                arrayHelper++;

            }

            int deletedRows = contentResolver.delete(scProvider.Code.CODES,null, null);
            int insertedRows = contentResolver.bulkInsert(scProvider.Code.CODES,values);

        }

    }

    // ================== //
    // Quote data methods //
    // ================== //

    /**
     * To spare some code this method converts cursors to quotes.
     *
     * @param cursor The cursor input
     * @return       The quote output, it can be null
     */
    private static Quote[] convertCursorToQuotes(Cursor cursor) {

        Quote[] quotes = null;

        if (cursor != null) {

            quotes = new Quote[cursor.getCount()];
            int arrayHelper = 0;

            while (cursor.moveToNext()) {

                String id = cursor.getString(cursor.getColumnIndex(QuoteTable.ID));
                String text = cursor.getString(cursor.getColumnIndex(QuoteTable.TEXT));
                String author = cursor.getString(cursor.getColumnIndex(QuoteTable.AUTHOR));
                int state = cursor.getInt(cursor.getColumnIndex(QuoteTable.STATE));

                quotes[arrayHelper] = new Quote(id, text, author, state);
                arrayHelper++;

            }

            cursor.close();

        }

        return quotes;
    }

    /**
     * Gets every quote that the user hided.
     *
     * @param contentResolver Required parameter to handle Content Provider
     * @return                Every hidden quote from the database
     */
    public static Quote[] getHiddenQuotes(ContentResolver contentResolver) {

        String[] selectionArgs = {String.valueOf(Quote.STATE_HIDDEN)};

        return convertCursorToQuotes(contentResolver.query(scProvider.Quote.QUOTES,quoteProjection,
                QuoteTable.STATE + SELECTION_HELPER,selectionArgs,null));

    }

    /**
     * Gets every quote that the user liked.
     *
     * @param contentResolver Required parameter to handle Content Provider
     * @return                Every liked quote from the database
     */
    public static Quote[] getLikedQuotes(ContentResolver contentResolver) {

        String[] selectionArgs = {String.valueOf(Quote.STATE_LIKED)};

        return convertCursorToQuotes(contentResolver.query(scProvider.Quote.QUOTES,quoteProjection,
                QuoteTable.STATE + SELECTION_HELPER,selectionArgs,null));

    }

    /**
     * Gets every quote.
     *
     * @param contentResolver Required parameter to handle Content Provider
     * @return                Every quote from the database
     */
    private static Quote[] getQuotes(ContentResolver contentResolver) {

        return convertCursorToQuotes(contentResolver.query(scProvider.Quote.QUOTES,
                quoteProjection,null,null, null));

    }

    /**
     * Gets a quote with the given id.
     *
     * @param contentResolver Required parameter to handle Content Provider
     * @param id              Id to search
     * @return                The quote
     */
    private static Quote getQuoteById(ContentResolver contentResolver, String id) {

        Quote returnQuote = null;

        Cursor cursor = contentResolver.query(scProvider.Quote.withId(id),
                quoteProjection,null,null,null);

        if (cursor != null) {

            if (cursor.getCount()>0) {

                cursor.moveToFirst();

                String db_id = cursor.getString(cursor.getColumnIndex(QuoteTable.ID));
                String text = cursor.getString(cursor.getColumnIndex(QuoteTable.TEXT));
                String author = cursor.getString(cursor.getColumnIndex(QuoteTable.AUTHOR));
                int quoteState = cursor.getInt(cursor.getColumnIndex(QuoteTable.STATE));

                returnQuote = new Quote(db_id,text,author,quoteState);

            }

            cursor.close();

        }

        return returnQuote;

    }

    /**
     * Updates a quote's state.
     *
     * @param contentResolver Required parameter to handle Content Provider
     * @param quote           Quote to update
     * @param newState        New state
     */
    public static void updateQuoteState(ContentResolver contentResolver, Quote quote, int newState) {

        Quote storedQuote = getQuoteById(contentResolver,quote.getId());

        if (storedQuote != null) {

            switch (newState) {

                case Quote.STATE_DEFAULT: {

                    int deleted = contentResolver.delete(scProvider.Quote.withId(quote.getId()),
                            null,null);
                    break;

                }

                case Quote.STATE_HIDDEN:
                case Quote.STATE_LIKED: {

                    ContentValues values = new ContentValues();
                    values.put(QuoteTable.ID,storedQuote.getId());
                    values.put(QuoteTable.TEXT,storedQuote.getText());
                    values.put(QuoteTable.AUTHOR,storedQuote.getAuthor());
                    values.put(QuoteTable.STATE,newState);

                    String[] whereArgs = {storedQuote.getId()};

                    int updatedRows = contentResolver.update(scProvider.Quote.withId(quote.getId()),
                            values,null,null);
                    break;

                }

            }

        } else {

            if (newState == Quote.STATE_LIKED || newState == Quote.STATE_HIDDEN) {

                ContentValues values = new ContentValues();
                values.put(QuoteTable.ID,quote.getId());
                values.put(QuoteTable.TEXT,quote.getText());
                values.put(QuoteTable.AUTHOR,quote.getAuthor());
                values.put(QuoteTable.STATE,newState);

                Uri uri = contentResolver.insert(scProvider.Quote.QUOTES,values);

            }

        }

    }

    /**
     * Checks if the quote is hidden or not.
     *
     * @param contentResolver Required parameter to handle Content Provider
     * @param quote           Quote to check
     * @return                The quote itself if not hidden, null if hidden
     */
    public static Quote validateQuote(ContentResolver contentResolver, Quote quote) {

        Quote storedQuote = getQuoteById(contentResolver,quote.getId());

        if (storedQuote != null) {

            quote.setState(storedQuote.getState());

        }

        if (quote.getState() != Quote.STATE_HIDDEN) return quote; else return null;

    }

    // ================================= //
    // UI data methods except of loaders //
    // ================================= //

    /**
     * Gets the user interface record (used on the Main Activity. This method is private because
     * the application uses a loader now to fit rubrics.
     *
     * @param contentResolver Required parameter to handle Content Provider
     * @return                The UI record
     */
    private static UiRecord getUiRecord(ContentResolver contentResolver) {

        UiRecord uiRecord = null;

        Cursor cursor = contentResolver.query(scProvider.Ui.UI,uiProjection,
                null,null,null);

        if (cursor != null) {

            if (cursor.getCount() > 0) {

                cursor.moveToFirst();

                uiRecord = new UiRecord();

                uiRecord.setCodeCount(cursor.getInt(cursor.getColumnIndex(UiTable.CODE_COUNT)));
                uiRecord.setCodeLiked(cursor.getInt(cursor.getColumnIndex(UiTable.CODE_LIKED)));
                uiRecord.setCodeFresh(cursor.getInt(cursor.getColumnIndex(UiTable.CODE_FRESH)));

                uiRecord.setQuoteCount(cursor.getInt(cursor.getColumnIndex(UiTable.QUOTE_COUNT)));
                uiRecord.setQuoteLiked(cursor.getInt(cursor.getColumnIndex(UiTable.QUOTE_LIKED)));
                uiRecord.setQuoteHidden(cursor.getInt(cursor.getColumnIndex(UiTable.QUOTE_HIDDEN)));

            }

            cursor.close();

        }

        return uiRecord;

    }

    /**
     * Increases the count of shown quotes.
     *
     * @param contentResolver Required parameter to handle Content Provider
     */
    public static void increaseQuoteCount(ContentResolver contentResolver) {

        UiRecord uiRecord = getUiRecord(contentResolver);

        if (uiRecord == null) uiRecord = new UiRecord();

        uiRecord.setQuoteCount(uiRecord.getQuoteCount() + 1);

        writeUiRecord(contentResolver,uiRecord);

    }

    /**
     * Updates the UI record from database.
     *
     * @param contentResolver Required parameter to handle Content Provider
     */
    public static void updateUiRecord(ContentResolver contentResolver) {

        Code[] codes = getCodes(contentResolver);

        int codeCount = 0;
        int codeLiked = 0;
        int codeFresh = 0;

        if (codes != null) {

            codeCount = codes.length;

            for (Code code: codes) {

                if (code.getState() == Code.STATE_LIKED) codeLiked++;
                if (code.getVersionState() == Code.VERSION_NEW) codeFresh++;

            }

        }

        Quote[] quotes = getQuotes(contentResolver);

        int quoteLiked = 0;
        int quoteHidden = 0;

        if (quotes != null) {

            for (Quote quote: quotes) {

                switch (quote.getState()) {

                    case Quote.STATE_LIKED: {

                        quoteLiked++;
                        break;

                    }

                    case Quote.STATE_HIDDEN: {

                        quoteHidden++;
                        break;

                    }

                }

            }

        }

        UiRecord uiRecord = getUiRecord(contentResolver);

        if (uiRecord == null) uiRecord = new UiRecord();

        uiRecord.setCodeCount(codeCount);
        uiRecord.setCodeLiked(codeLiked);
        uiRecord.setCodeFresh(codeFresh);

        uiRecord.setQuoteLiked(quoteLiked);
        uiRecord.setQuoteHidden(quoteHidden);

        writeUiRecord(contentResolver,uiRecord);

    }

    /**
     * Writes the UI record.
     *
     * @param contentResolver Required parameter to handle Content Provider
     * @param uiRecord        UI record
     */
    private static void writeUiRecord(ContentResolver contentResolver, UiRecord uiRecord) {

        ContentValues values = new ContentValues();

        values.put(UiTable.CODE_COUNT,uiRecord.getCodeCount());
        values.put(UiTable.CODE_LIKED,uiRecord.getCodeLiked());
        values.put(UiTable.CODE_FRESH,uiRecord.getCodeFresh());

        values.put(UiTable.QUOTE_COUNT,uiRecord.getQuoteCount());
        values.put(UiTable.QUOTE_LIKED,uiRecord.getQuoteLiked());
        values.put(UiTable.QUOTE_HIDDEN,uiRecord.getQuoteHidden());

        int deletedRows = contentResolver.delete(scProvider.Ui.UI,null, null);
        Uri uri = contentResolver.insert(scProvider.Ui.UI,values);

    }

    // =================== //
    // Widget data methods //
    // =================== //

    /**
     * Gets the widget's record.
     *
     * @param contentResolver Required parameter to handle Content Provider
     * @return                Widget's record
     */
    public static WidgetRecord getWidgetRecord(ContentResolver contentResolver) {

        WidgetRecord widgetRecord = null;

        Cursor cursor = contentResolver.query(scProvider.Widget.WIDGET, widgetProjection,
                null,null,null);

        if (cursor != null) {

            if (cursor.getCount()>0) {
                cursor.moveToFirst();

                String id = cursor.getString(cursor.getColumnIndex(WidgetTable.ID));
                String text = cursor.getString(cursor.getColumnIndex(WidgetTable.TEXT));
                String author = cursor.getString(cursor.getColumnIndex(WidgetTable.AUTHOR));
                int quoteState = cursor.getInt(cursor.getColumnIndex(WidgetTable.QUOTE_STATE));

                Quote quote = new Quote(id,text,author,quoteState);

                int codeState = cursor.getInt(cursor.getColumnIndex(WidgetTable.CODE_STATE));

                widgetRecord = new WidgetRecord(quote,codeState);


            }

            cursor.close();

        }

        return widgetRecord;

    }

    /**
     * Updates the widget's record with a new quote.
     *
     * @param contentResolver Required parameter to handle Content Provider
     * @param quote           Quote
     */
    public static void updateWidgetRecord(ContentResolver contentResolver, Quote quote) {

        updateWidgetRecord(contentResolver, quote, VALUE_NOT_SET);

    }

    /**
     * Updates the widget's record with a new state of codes.
     *
     * @param contentResolver Required parameter to handle Content Provider
     * @param codeState       State of codes
     */
    public static void updateWidgetRecord(ContentResolver contentResolver, int codeState) {

        updateWidgetRecord(contentResolver,null, codeState);

    }

    /**
     * Private method to handle the factual update.
     *
     * @param contentResolver Required parameter to handle Content Provider
     * @param quote           Quote if any
     * @param codeState       State of codes if any
     */
    private static void updateWidgetRecord(ContentResolver contentResolver, @Nullable Quote quote, int codeState) {

        WidgetRecord widgetRecord = getWidgetRecord(contentResolver);

        Quote newQuote;
        int newCodeState = VALUE_NOT_SET;

        if (widgetRecord != null) {

            if (quote != null) newQuote = quote; else newQuote = widgetRecord.getQuote();

            if (codeState == Code.VERSION_NEW || codeState == Code.VERSION_UPDATED)
                newCodeState = codeState; else newCodeState = widgetRecord.getCodeState();

        } else {

            if (quote == null) newQuote = new Quote(DEFAULT_QUOTE_ID,DEFAULT_QUOTE_TEXT,
                    DEFAULT_QUOTE_AUTHOR,DEFAULT_QUOTE_STATE);
            else newQuote = quote;

            if (codeState == VALUE_NOT_SET) newCodeState = Code.VERSION_UPDATED;
            else newCodeState = codeState;

        }

        int deleted = contentResolver.delete(scProvider.Widget.WIDGET,null,null);

        ContentValues values = new ContentValues();

        values.put(WidgetTable.ID,newQuote.getId());
        values.put(WidgetTable.TEXT,newQuote.getText());
        values.put(WidgetTable.AUTHOR,newQuote.getAuthor());
        values.put(WidgetTable.QUOTE_STATE,newQuote.getState());
        values.put(WidgetTable.CODE_STATE,newCodeState);

        Uri uri = contentResolver.insert(scProvider.Widget.WIDGET,values);

    }

}
