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
 * Simple model class with implementation of Parcelable to provide quote records
 *
 */
public class Quote implements Parcelable {

    private String id;
    private String text;
    private String author;
    private int state;

    public static final int STATE_DEFAULT = 0;
    public static final int STATE_LIKED = 1;
    public static final int STATE_HIDDEN = 2;

    /**
     * The most basic constructor
     */
    public Quote() {
    }

    /**
     * Alternative constructor with given and default parameters as well.
     *
     * @param id     Id
     * @param text   Text
     * @param author Author
     */
    public Quote(String id, String text, String author) {

        this.id = id;
        this.text = text;
        this.author = author;
        this.state = STATE_DEFAULT;

    }

    /**
     * Te most advanced constructor with all given parameters.
     *
     * @param id     Id
     * @param text   Text
     * @param author Author
     * @param state  State
     */
    public Quote(String id, String text, String author, int state) {

        this.id = id;
        this.text = text;
        this.author = author;
        this.state = state;

    }

    /**
     * Simple getter method.
     *
     * @return Id
     */
    public String getId() {

        return id;

    }

    /**
     * Simple setter method.
     *
     * @param id Id
     */
    public void setId(String id) {

        this.id = id;

    }

    /**
     * Simple getter method.
     *
     * @return Text
     */
    public String getText() {

        return text;

    }

    /**
     * Simple setter method.
     *
     * @param text Text
     */
    public void setText(String text) {

        this.text = text;

    }

    /**
     * Simple getter method.
     *
     * @return Author
     */
    public String getAuthor() {

        return author;

    }

    /**
     * Simple setter method.
     *
     * @param author Author
     */
    public void setAuthor(String author) {

        this.author = author;

    }

    /**
     * Simple getter method.
     *
     * @return State
     */
    public int getState() {

        return state;

    }

    /**
     * Simple setter method.
     *
     * @param state State
     */
    public void setState(int state) {

        this.state = state;

    }

    /**
     * Reads from parcel. Surprisingly it needs a suppress parameter.
     *
     * @param in Parcel
     */
    @SuppressWarnings("WeakerAccess")
    protected Quote(Parcel in) {

        id = in.readString();
        text = in.readString();
        author = in.readString();
        state = in.readInt();

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

        parcel.writeString(id);
        parcel.writeString(text);
        parcel.writeString(author);
        parcel.writeInt(state);

    }

    /**
     * Static creator class for arrays and parcelables.
     */
    public static final Creator<Quote> CREATOR = new Creator<Quote>() {

        /**
         * Creates the object from parcel.
         *
         * @param in Parcel
         * @return   A new instance
         */
        @Override
        public Quote createFromParcel(Parcel in) {

            return new Quote(in);

        }

        /**
         * Creates a new array.
         *
         * @param size Size
         * @return     Array
         */
        @Override
        public Quote[] newArray(int size) {

            return new Quote[size];

        }

    };

}
