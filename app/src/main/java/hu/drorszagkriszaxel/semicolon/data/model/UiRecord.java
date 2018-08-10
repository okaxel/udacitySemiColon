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
 * Simple model class with implementation of Parcelable to provide UI records
 *
 */
public class UiRecord implements Parcelable{

    private int codeCount;
    private int codeLiked;
    private int codeFresh;
    private int quoteCount;
    private int quoteLiked;
    private int quoteHidden;

    /**
     * The most basic constructor
     */
    public UiRecord() {

        this.codeCount = 0;
        this.codeLiked = 0;
        this.codeFresh = 0;
        this.quoteCount = 0;
        this.quoteLiked = 0;
        this.quoteHidden = 0;

    }

    /**
     * Simple getter method.
     *
     * @return Count of codes
     */
    public int getCodeCount() {

        return codeCount;

    }

    /**
     * Simple setter method.
     *
     * @param codeCount Count of codes
     */
    public void setCodeCount(int codeCount) {

        this.codeCount = codeCount;

    }

    /**
     * Simple getter method.
     *
     * @return Count of liked codes
     */
    public int getCodeLiked() {

        return codeLiked;

    }

    /**
     * Simple setter method.
     *
     * @param codeLiked Count of liked codes
     */
    public void setCodeLiked(int codeLiked) {

        this.codeLiked = codeLiked;

    }

    /**
     * Simple getter method.
     *
     * @return Count of fresh codes
     */
    public int getCodeFresh() {

        return codeFresh;

    }

    /**
     * Simple setter method.
     *
     * @param codeFresh Count of fresh codes
     */
    public void setCodeFresh(int codeFresh) {

        this.codeFresh = codeFresh;

    }

    /**
     * Simple getter method.
     *
     * @return Count of shown codes
     */
    public int getQuoteCount() {

        return quoteCount;

    }

    /**
     * Simple setter method.
     *
     * @param quoteCount Count of shown quotes
     */
    public void setQuoteCount(int quoteCount) {

        this.quoteCount = quoteCount;

    }

    /**
     * Simple getter method.
     *
     * @return Count of liked codes
     */
    public int getQuoteLiked() {

        return quoteLiked;

    }

    /**
     * Simple setter method.
     *
     * @param quoteLiked Count of liked codes
     */
    public void setQuoteLiked(int quoteLiked) {

        this.quoteLiked = quoteLiked;

    }

    /**
     * Simple getter method.
     *
     * @return Count of hidden quotes
     */
    public int getQuoteHidden() {

        return quoteHidden;

    }

    /**
     * Simple setter method.
     *
     * @param quoteHidden Count of hidden quotes
     */
    public void setQuoteHidden(int quoteHidden) {

        this.quoteHidden = quoteHidden;

    }

    /**
     * Reads from parcel. Surprisingly it needs a suppress parameter.
     *
     * @param in Parcel
     */
    @SuppressWarnings("WeakerAccess")
    protected UiRecord(Parcel in) {

        codeCount = in.readInt();
        codeLiked = in.readInt();
        codeFresh = in.readInt();
        quoteCount = in.readInt();
        quoteLiked = in.readInt();
        quoteHidden = in.readInt();

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

        parcel.writeInt(codeCount);
        parcel.writeInt(codeLiked);
        parcel.writeInt(codeFresh);
        parcel.writeInt(quoteCount);
        parcel.writeInt(quoteLiked);
        parcel.writeInt(quoteHidden);

    }

    /**
     * Static creator class for arrays and parcelables.
     */
    public static final Creator<UiRecord> CREATOR = new Creator<UiRecord>() {


        /**
         * Creates the object from parcel.
         *
         * @param in Parcel
         * @return   A new instance
         */
        @Override
        public UiRecord createFromParcel(Parcel in) {

            return new UiRecord(in);

        }

        /**
         * Creates a new array.
         *
         * @param size Size
         * @return     Array
         */
        @Override
        public UiRecord[] newArray(int size) {

            return new UiRecord[size];

        }

    };

}
