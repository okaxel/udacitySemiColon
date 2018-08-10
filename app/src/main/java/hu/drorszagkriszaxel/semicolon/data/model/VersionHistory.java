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
 * Simple model class with implementation of Parcelable to provide version history records
 *
 */
public class VersionHistory implements Parcelable {

    private VersionItem[] major;
    private VersionItem[] minor;

    /**
     * The most basic constructor
     */
    public VersionHistory() {
    }

    /**
     * Te most advanced constructor with all given parameters.
     *
     * @param major Major version array
     * @param minor Minor version array
     */
    public VersionHistory(VersionItem[] major, VersionItem[] minor) {

        this.major = major;
        this.minor = minor;

    }

    /**
     * Simple getter method.
     *
     * @return Major versions
     */
    public VersionItem[] getMajor() {

        return major;

    }

    /**
     * Simple setter method.
     *
     * @param major Major versions
     */
    public void setMajor(VersionItem[] major) {

        this.major = major;

    }

    /**
     * Simple getter method.
     *
     * @return Minor versions
     */
    public VersionItem[] getMinor() {

        return minor;

    }

    /**
     * Simple setter method.
     *
     * @param minor Minor versions
     */
    public void setMinor(VersionItem[] minor) {

        this.minor = minor;

    }

    /**
     * Reads from parcel. Surprisingly it needs a suppress parameter.
     *
     * @param in Parcel
     */
    @SuppressWarnings("WeakerAccess")
    protected VersionHistory(Parcel in) {

        major = in.createTypedArray(VersionItem.CREATOR);
        minor = in.createTypedArray(VersionItem.CREATOR);

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

        parcel.writeTypedArray(major, i);
        parcel.writeTypedArray(minor, i);

    }

    /**
     * Static creator class for arrays and parcelables.
     */
    public static final Creator<VersionHistory> CREATOR = new Creator<VersionHistory>() {

        /**
         * Creates the object from parcel.
         *
         * @param in Parcel
         * @return   A new instance
         */
        @Override
        public VersionHistory createFromParcel(Parcel in) {

            return new VersionHistory(in);

        }

        /**
         * Creates a new array.
         *
         * @param size Size
         * @return     Array
         */
        @Override
        public VersionHistory[] newArray(int size) {

            return new VersionHistory[size];

        }

    };

}
