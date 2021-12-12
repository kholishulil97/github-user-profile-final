package com.example.githubuser;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.githubuser.api.ApiConfig;
import com.example.githubuser.database.Favorite;
import com.example.githubuser.model.ItemUserResponse;
import com.example.githubuser.model.UserDetailResponse;
import com.example.githubuser.model.UserResponse;
import com.example.githubuser.repository.FavoriteRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFollowViewModel extends ViewModel {
    private String apiKey = "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";

    private final MutableLiveData<List<ItemUserResponse>> _userFollowersList = new MutableLiveData<>();

    public LiveData<List<ItemUserResponse>> getUserFollowersList() {
        return _userFollowersList;
    }

    private final MutableLiveData<List<ItemUserResponse>> _userFollowingList = new MutableLiveData<>();

    public LiveData<List<ItemUserResponse>> getUserFollowingList() {
        return _userFollowingList;
    }

    private final MutableLiveData<Boolean> _isLoadingList = new MutableLiveData<>();

    public LiveData<Boolean> isLoadingList() {
        return _isLoadingList;
    }

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();

    public LiveData<Boolean> isLoading() {
        return _isLoading;
    }

    private final MutableLiveData<Event<String>> _responseStatus = new MutableLiveData<>();

    public LiveData<Event<String>> responseStatus() {
        return _responseStatus;
    }

    public void fetchUserFollowers(String username) {
        _isLoadingList.setValue(true);
        Call<List<ItemUserResponse>> client = ApiConfig.getApiService().getUserFollowers(apiKey, username);
        client.enqueue(new Callback<List<ItemUserResponse>>() {
            @Override
            public void onResponse(@NotNull Call<List<ItemUserResponse>> call, @NotNull Response<List<ItemUserResponse>> response) {
                _isLoadingList.setValue(false);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        _userFollowersList.setValue(response.body());
                    }
                } else {
                    if (response.body() != null) {
                        _responseStatus.setValue(new Event<>("Gagal menerima pesan!"));
                    }
                }
            }
            @Override
            public void onFailure(@NotNull Call<List<ItemUserResponse>> call, @NotNull Throwable t) {
                _isLoadingList.setValue(false);
                Log.e(TAG, "onFailure: " + t.getMessage());
                _responseStatus.setValue(new Event<>("Terjadi kesalahan jaringan!"));
            }
        });
    }

    public void fetchUserFollowing(String username) {
        _isLoadingList.setValue(true);
        Call<List<ItemUserResponse>> client = ApiConfig.getApiService().getUserFollowing(apiKey, username);
        client.enqueue(new Callback<List<ItemUserResponse>>() {
            @Override
            public void onResponse(@NotNull Call<List<ItemUserResponse>> call, @NotNull Response<List<ItemUserResponse>> response) {
                _isLoadingList.setValue(false);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        _userFollowingList.setValue(response.body());
                    }
                } else {
                    if (response.body() != null) {
                        _responseStatus.setValue(new Event<>("Gagal menerima pesan!"));
                    }
                }
            }
            @Override
            public void onFailure(@NotNull Call<List<ItemUserResponse>> call, @NotNull Throwable t) {
                _isLoadingList.setValue(false);
                Log.e(TAG, "onFailure: " + t.getMessage());
                _responseStatus.setValue(new Event<>("Terjadi kesalahan jaringan!"));
            }
        });
    }
}
