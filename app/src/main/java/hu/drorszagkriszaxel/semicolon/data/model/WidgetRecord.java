package hu.drorszagkriszaxel.semicolon.data.model;

import android.os.Parcel;
import android.os.Parcelable;

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
 * Simple model class with implementation of Parcelable to provide the widget records
 *
 */
public class WidgetRecord implements Parcelable{

    private Quote quote;
    private int codeState;

    /**
     * The most basic constructor
     */
    public WidgetRecord() {
    }

    /**
     * Te most advanced constructor with all given parameters.
     *
     * @param quote     Quote to the widget
     * @param codeState Freshness state of codes
     */
    public WidgetRecord(Quote quote, int codeState) {

        this.quote = quote;
        this.codeState = codeState;

    }

    /**
     * Simple getter method.
     *
     * @return Quote
     */
    public Quote getQuote() {

        return quote;

    }

    /**
     * Simple setter method.
     *
     * @param quote Quote of the widget
     */
    public void setQuote(Quote quote) {

        this.quote = quote;

    }

    /**
     * Simple getter method.
     *
     * @return Freshness state
     */
    public int getCodeState() {

        return codeState;

    }

    /**
     * Simple setter method.
     *
     * @param codeState Freshness state
     */
    public void setCodeState(int codeState) {

        this.codeState = codeState;

    }

    /**
     * Reads from parcel. Surprisingly it needs a suppress parameter.
     *
     * @param in Parcel
     */
    @SuppressWarnings("WeakerAccess")
    protected WidgetRecord(Parcel in) {

        quote = in.readParcelable(Quote.class.getClassLoader());
        codeState = in.readInt();

    }

    /**
     * Describes content - method with a straightforward behavior. :-)
     *
     * @return Always 0
     */
    @Override
    public int describeContents() {

        return 0;

    }

    /**
     * Writes to parcel
     *
     * @param parcel Parcel
     * @param i      Required but not used
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeParcelable(quote, i);
        parcel.writeInt(codeState);

    }

    /**
     * Static creator class for arrays and parcelables.
     */
    public static final Creator<WidgetRecord> CREATOR = new Creator<WidgetRecord>() {

        /**
         * Creates the object from parcel.
         *
         * @param in Parcel
         * @return   A new instance
         */
        @Override
        public WidgetRecord createFromParcel(Parcel in) {

            return new WidgetRecord(in);

        }

        /**
         * Creates a new array.
         *
         * @param size Size
         * @return     Array
         */
        @Override
        public WidgetRecord[] newArray(int size) {

            return new WidgetRecord[size];

        }

    };

}
