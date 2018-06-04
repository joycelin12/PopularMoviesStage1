package com.example.android.popularmoviesstage1.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by joycelin12 on 6/3/18.
 */

public class Trailer implements Parcelable {

    String id;
    String key;
    String name;
    String size;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.key);
        dest.writeString(this.name);
        dest.writeString(this.size);
    }

    public Trailer(String id, String key, String name, String size) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.size = size;
    }

    protected Trailer(Parcel in) {
        this.id = in.readString();
        this.key = in.readString();
        this.name = in.readString();
        this.size = in.readString();
    }

    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel source) {
            return new Trailer(source);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
}
