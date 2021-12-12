package com.example.githubuser.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserResponse {
    @SerializedName("items")
    @Expose
    private List<ItemUserResponse> items = null;

    public List<ItemUserResponse> getItems() {
        return items;
    }
}
