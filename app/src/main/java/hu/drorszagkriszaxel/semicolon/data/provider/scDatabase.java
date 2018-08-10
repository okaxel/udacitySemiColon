package hu.drorszagkriszaxel.semicolon.data.provider;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

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
 * Database data model class for the Schematic Content Provider generator. This contains the
 * structure of the tables in the database
 *
 */
@Database(version = scDatabase.VERSION)
public final class scDatabase {

    static final int VERSION = 1;

    @Table(CodeTable.class) public static final String CODES = "codes";
    @Table(QuoteTable.class) public static final String QUOTES = "quotes";
    @Table(UiTable.class) public static final String UI = "ui";
    @Table(WidgetTable.class) public static final String WIDGET = "widget";

}
