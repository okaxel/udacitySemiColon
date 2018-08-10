package hu.drorszagkriszaxel.semicolon.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Repo implements Parcelable{

    private String owner;
    private String name;

    public Repo() {
    }

    public Repo(String owner, String name) {

        this.owner = owner;
        this.name = name;

    }

    public String getOwner() {

        return owner;

    }

    public void setOwner(String owner) {

        this.owner = owner;

    }

    public String getName() {

        return name;

    }

    public void setName(String name) {

        this.name = name;

    }

    @SuppressWarnings("WeakerAccess")
    protected Repo(Parcel in) {

        owner = in.readString();
        name = in.readString();

    }

    @Override
    public int describeContents() {

        return 0;

    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(owner);
        parcel.writeString(name);

    }

    public static final Creator<Repo> CREATOR = new Creator<Repo>() {

        @Override
        public Repo createFromParcel(Parcel in) {

            return new Repo(in);

        }

        @Override
        public Repo[] newArray(int size) {

            return new Repo[size];

        }

    };

}
