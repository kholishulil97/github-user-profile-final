package com.example.githubuser.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDetailResponse implements Parcelable {
    @SerializedName("login")
    @Expose
    private String login;
    @SerializedName("avatar_url")
    @Expose
    private String avatarUrl;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("company")
    @Expose
    private String company;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("bio")
    @Expose
    private String bio;
    @SerializedName("public_repos")
    @Expose
    private Integer publicRepos;
    @SerializedName("followers")
    @Expose
    private Integer followers;
    @SerializedName("following")
    @Expose
    private Integer following;

    protected UserDetailResponse(Parcel in) {
        login = in.readString();
        avatarUrl = in.readString();
        name = in.readString();
        company = in.readString();
        location = in.readString();
        bio = in.readString();
        if (in.readByte() == 0) {
            publicRepos = null;
        } else {
            publicRepos = in.readInt();
        }
        if (in.readByte() == 0) {
            followers = null;
        } else {
            followers = in.readInt();
        }
        if (in.readByte() == 0) {
            following = null;
        } else {
            following = in.readInt();
        }
    }

    public static final Creator<UserDetailResponse> CREATOR = new Creator<UserDetailResponse>() {
        @Override
        public UserDetailResponse createFromParcel(Parcel in) {
            return new UserDetailResponse(in);
        }

        @Override
        public UserDetailResponse[] newArray(int size) {
            return new UserDetailResponse[size];
        }
    };

    public String getLogin() {
        return login;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getName() {
        return name;
    }

    public String getCompany() {
        return company;
    }

    public String getLocation() {
        return location;
    }

    public String getBio() {
        return bio;
    }

    public Integer getPublicRepos() {
        return publicRepos;
    }

    public Integer getFollowers() {
        return followers;
    }

    public Integer getFollowing() {
        return following;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(login);
        parcel.writeString(avatarUrl);
        parcel.writeString(name);
        parcel.writeString(company);
        parcel.writeString(location);
        parcel.writeString(bio);
        if (publicRepos == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(publicRepos);
        }
        if (followers == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(followers);
        }
        if (following == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(following);
        }
    }
}
