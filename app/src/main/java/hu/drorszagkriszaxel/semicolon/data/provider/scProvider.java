package hu.drorszagkriszaxel.semicolon.data.provider;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

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
 * Content Provider model class for the Schematic Content Provider generator. This contains the
 * structure of the Content Provider's Uris
 *
 */
@ContentProvider(authority = scProvider.AUTHORITY, database = scDatabase.class)
public final class scProvider {

    /**
     * Although I don't access this field I think it's better to leave it public even if Lint thinks
     * differentl.
     */
    @SuppressWarnings("WeakerAccess")
    public static final String AUTHORITY = "hu.drorszagkriszaxel.semicolon.data.provider.provider";

    @TableEndpoint(table = scDatabase.CODES) public static class Code {

        @ContentUri(
                path = "code",
                type = "vnd.android.cursor.dir/list",
                defaultSort = CodeTable._ID + " ASC")

        public static final Uri CODES = Uri.parse("content://" + AUTHORITY + "/code");

        @InexactContentUri(
                path = "code/*",
                name = "CODE_ID",
                type = "vnd.android.cursor.dir/list",
                whereColumn = CodeTable.HOME,
                pathSegment = 1)

        public static Uri withHome(String home) {

            return Uri.parse("content://" + AUTHORITY + "/code/" + home);

        }
        
    }

    @TableEndpoint(table = scDatabase.QUOTES) public static class Quote {

        @ContentUri(
                path = "quote",
                type = "vnd.android.cursor.dir/list",
                defaultSort = QuoteTable._ID + " ASC")

        public static final Uri QUOTES = Uri.parse("content://" + AUTHORITY + "/quote");

        @InexactContentUri(
                path = "quote/*",
                name = "QUOTE_ID",
                type = "vnd.android.cursor.dir/list",
                whereColumn = QuoteTable.ID,
                pathSegment = 1)

        public static Uri withId(String id) {

            return Uri.parse("content://" + AUTHORITY + "/quote/" + id);

        }

    }

    @TableEndpoint(table = scDatabase.UI) public static class Ui {

        @ContentUri(
                path = "ui",
                type = "vnd.android.cursor.dir/list",
                defaultSort = UiTable._ID + " ASC")

        public static final Uri UI = Uri.parse("content://" + AUTHORITY + "/ui");

    }

    @TableEndpoint(table = scDatabase.WIDGET) public static class Widget {

        @ContentUri(
                path = "widget",
                type = "vnd.android.cursor.dir/list",
                defaultSort = WidgetTable._ID + " ASC")

        public static final Uri WIDGET = Uri.parse("content://" + AUTHORITY + "/widget");

    }

}
