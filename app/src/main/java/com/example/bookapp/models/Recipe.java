package com.example.bookapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Recipe implements Parcelable {

    private String name;
    private String imageUrl;

    public Recipe(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    protected Recipe(Parcel in) {
        name = in.readString();
        imageUrl = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(imageUrl);
    }
}


