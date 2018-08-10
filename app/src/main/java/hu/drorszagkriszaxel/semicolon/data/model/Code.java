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
 * Simple model class with implementation of Parcelable to provide code records
 *
 */
public class Code implements Parcelable {

    private int type;
    private String name;
    private String home;
    private String page;
    private String version;
    private int versionState;
    private int state;


    public static final int CODE_PROJECT = 0;
    public static final int CODE_REPO = 1;

    public static final int VERSION_NEVER_CHECKED = 0;
    public static final int VERSION_UPDATED = 1;
    public static final int VERSION_NEW = 2;

    public static final int STATE_DEFAULT = 0;
    public static final int STATE_LIKED = 1;

    /**
     * The most basic constructor
     */
    public Code() {
    }

    /**
     * Alternative constructor with given and default parameters as well.
     *
     * @param type Type
     * @param name Name
     * @param home Home
     * @param page Page
     */
    public Code(int type, String name, String home, String page) {

        this.type = type;
        this.name = name;
        this.home = home;
        this.page = page;
        this.version = "";
        this.versionState = VERSION_NEVER_CHECKED;
        this.state = STATE_DEFAULT;

    }

    /**
     * Alternative constructor with given and default parameters as well.
     *
     * @param type    Type
     * @param name    Name
     * @param home    Home
     * @param page    Page
     * @param version Version
     */
    public Code(int type, String name, String home, String page, String version) {

        this.type = type;
        this.name = name;
        this.home = home;
        this.page = page;
        this.version = version;
        this.versionState = VERSION_NEVER_CHECKED;
        this.state = STATE_DEFAULT;

    }

    /**
     * Alternative constructor with given and default parameters as well.
     *
     * @param type         Type
     * @param name         Name
     * @param home         Home
     * @param page         Page
     * @param version      Version
     * @param versionState State of freshness
     */
    public Code(int type, String name, String home, String page, String version, int versionState) {

        this.type = type;
        this.name = name;
        this.home = home;
        this.page = page;
        this.version = version;
        this.versionState = versionState;
        this.state = STATE_DEFAULT;

    }

    /**
     * Te most advanced constructor with all given parameters.
     *
     * @param type         Type
     * @param name         Name
     * @param home         Home
     * @param page         Page
     * @param version      Version
     * @param versionState State of freshness
     * @param state        Liked or not
     */
    public Code(int type, String name, String home, String page, String version, int versionState, int state) {

        this.type = type;
        this.name = name;
        this.home = home;
        this.page = page;
        this.version = version;
        this.versionState = versionState;
        this.state = state;

    }

    /**
     * Simple getter method.
     *
     * @return Type
     */
    public int getType() {

        return type;

    }

    /**
     * Simple setter method.
     *
     * @param type Type
     */
    public void setType(int type) {

        this.type = type;

    }

    /**
     * Simple getter method.
     *
     * @return Name
     */
    public String getName() {

        return name;

    }

    /**
     * Simple setter method.
     *
     * @param name Name
     */
    public void setName(String name) {

        this.name = name;

    }

    /**
     * Simple getter method.
     *
     * @return Home
     */
    public String getHome() {

        return home;

    }

    /**
     * Simple setter method.
     *
     * @param home Home
     */
    public void setHome(String home) {

        this.home = home;

    }

    /**
     * Simple getter method.
     *
     * @return Page
     */
    public String getPage() {

        return page;

    }

    /**
     * Simple setter method.
     *
     * @param page Page
     */
    public void setPage(String page) {

        this.page = page;

    }

    /**
     * Simple getter method.
     *
     * @return Version
     */
    public String getVersion() {

        return version;

    }

    /**
     * Simple setter method.
     *
     * @param version Version
     */
    public void setVersion(String version) {

        this.version = version;

    }

    /**
     * Simple getter method.
     *
     * @return State of version
     */
    public int getVersionState() {

        return versionState;

    }

    /**
     * Simple setter method.
     *
     * @param versionState State of freshness
     */
    public void setVersionState(int versionState) {

        this.versionState = versionState;

    }

    /**
     * Simple getter method.
     *
     * @return State of popularity
     */
    public int getState() {

        return state;

    }

    /**
     * Simple setter method.
     *
     * @param state State of popularity
     */
    public void setState(int state) {

        this.state = state;

    }

    /**
     * Reads from parcel.
     *
     * @param in Parcel
     */
    protected Code(Parcel in) {

        type = in.readInt();
        name = in.readString();
        home = in.readString();
        page = in.readString();
        version = in.readString();
        versionState = in.readInt();
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

        parcel.writeInt(type);
        parcel.writeString(name);
        parcel.writeString(home);
        parcel.writeString(page);
        parcel.writeString(version);
        parcel.writeInt(versionState);
        parcel.writeInt(state);

    }

    /**
     * Static creator class for arrays and parcelables.
     */
    public static final Creator<Code> CREATOR = new Creator<Code>() {

        /**
         * Creates the object from parcel.
         *
         * @param in Parcel
         * @return   A new instance
         */
        @Override
        public Code createFromParcel(Parcel in) {

            return new Code(in);

        }

        /**
         * Creates a new array.
         *
         * @param size Size
         * @return     Array
         */
        @Override
        public Code[] newArray(int size) {

            return new Code[size];

        }

    };

}
