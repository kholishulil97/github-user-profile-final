package com.example.githubuser.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemUserResponse implements Parcelable {
    @SerializedName("login")
    @Expose
    private String login;
    @SerializedName("avatar_url")
    @Expose
    private String avatarUrl;
    @SerializedName("type")
    @Expose
    private String type;

    public static final Creator<ItemUserResponse> CREATOR = new Creator<ItemUserResponse>() {
        @Override
        public ItemUserResponse createFromParcel(Parcel in) {
            return new ItemUserResponse(in);
        }

        @Override
        public ItemUserResponse[] newArray(int size) {
            return new ItemUserResponse[size];
        }
    };

    public String getLogin() {
        return login;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getType() {
        return type;
    }

    protected ItemUserResponse(Parcel in) {
        login = in.readString();
        avatarUrl = in.readString();
        type = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(login);
        parcel.writeString(avatarUrl);
        parcel.writeString(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
