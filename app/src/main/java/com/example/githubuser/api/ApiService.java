package com.example.githubuser.api;

import com.example.githubuser.model.ItemUserResponse;
import com.example.githubuser.model.UserDetailResponse;
import com.example.githubuser.model.UserResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("users")
    Call<List<ItemUserResponse>> getUserList(
            @Header("Authorization") String auth
            );

    @GET("users/{username}")
    Call<UserDetailResponse> getUserDetail(
            @Header("Authorization") String auth,
            @Path("username") String username
    );

    @GET("users/{username}/followers")
    Call<List<ItemUserResponse>> getUserFollowers(
            @Header("Authorization") String auth,
            @Path("username") String username
    );

    @GET("users/{username}/following")
    Call<List<ItemUserResponse>> getUserFollowing(
            @Header("Authorization") String auth,
            @Path("username") String username
    );

    @GET("search/users")
    Call<UserResponse> getUserSearch(
            @Header("Authorization") String auth,
            @Query("q") String key
    );
}
