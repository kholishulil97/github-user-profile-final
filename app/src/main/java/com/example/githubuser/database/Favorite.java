package com.example.githubuser.database;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Favorite implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "login")
    private String login;

    @ColumnInfo(name = "avatarUrl")
    private String avatarUrl;

    @ColumnInfo(name = "user")
    private String type;

    @ColumnInfo(name = "date")
    private String date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Ignore
    public Favorite() {}

    public Favorite(int id, String login, String avatarUrl, String type, String date) {
        this.id = id;
        this.login = login;
        this.avatarUrl = avatarUrl;
        this.type = type;
        this.date = date;
    }

    protected Favorite(Parcel in) {
        id = in.readInt();
        login = in.readString();
        avatarUrl = in.readString();
        type = in.readString();
        date = in.readString();
    }

    public static final Creator<Favorite> CREATOR = new Creator<Favorite>() {
        @Override
        public Favorite createFromParcel(Parcel in) {
            return new Favorite(in);
        }

        @Override
        public Favorite[] newArray(int size) {
            return new Favorite[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(login);
        parcel.writeString(avatarUrl);
        parcel.writeString(type);
        parcel.writeString(date);
    }
}
