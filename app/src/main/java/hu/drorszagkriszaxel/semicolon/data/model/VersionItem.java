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
 * Simple model class with implementation of Parcelable to provide version items for version
 * history records
 *
 */
public class VersionItem implements Parcelable {

    private String family;
    private String number;

    /**
     * The most basic constructor
     */
    public VersionItem() {
    }

    /**
     * Te most advanced constructor with all given parameters.
     *
     * @param family Version family
     * @param number Effective version number
     */
    public VersionItem(String family, String number) {

        this.family = family;
        this.number = number;

    }

    /**
     * Simple getter method.
     *
     * @return Version family
     */
    public String getFamily() {

        return family;

    }

    /**
     * Simple setter method.
     *
     * @param family Version family
     */
    public void setFamily(String family) {

        this.family = family;

    }

    /**
     * Simple getter method.
     *
     * @return Effective version number
     */
    public String getNumber() {

        return number;

    }

    /**
     * Simple setter method.
     *
     * @param number Effective version number
     */
    public void setNumber(String number) {

        this.number = number;

    }

    /**
     * Reads from parcel. Surprisingly it needs a suppress parameter.
     *
     * @param in Parcel
     */
    @SuppressWarnings("WeakerAccess")
    protected VersionItem(Parcel in) {

        family = in.readString();
        number = in.readString();

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

        parcel.writeString(family);
        parcel.writeString(number);

    }

    /**
     * Static creator class for arrays and parcelables.
     */
    public static final Creator<VersionItem> CREATOR = new Creator<VersionItem>() {

        /**
         * Creates the object from parcel.
         *
         * @param in Parcel
         * @return   A new instance
         */
        @Override
        public VersionItem createFromParcel(Parcel in) {

            return new VersionItem(in);

        }

        /**
         * Creates a new array.
         *
         * @param size Size
         * @return     Array
         */
        @Override
        public VersionItem[] newArray(int size) {

            return new VersionItem[size];

        }

    };

}
