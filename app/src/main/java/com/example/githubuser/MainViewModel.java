package com.example.githubuser;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
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

public class MainViewModel extends ViewModel {

    private String apiKey = "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";

    private final MutableLiveData<List<ItemUserResponse>> _userList = new MutableLiveData<>();

    public LiveData<List<ItemUserResponse>> getUserList() {
        return _userList;
    }

    private final MutableLiveData<List<ItemUserResponse>> _itemSearchResult = new MutableLiveData<List<ItemUserResponse>>();

    public LiveData<List<ItemUserResponse>> getSearchResult() {
        return _itemSearchResult;
    }

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();

    public LiveData<Boolean> isLoading() {
        return _isLoading;
    }

    private final MutableLiveData<Event<String>> _responseStatus = new MutableLiveData<>();

    public LiveData<Event<String>> responseStatus() {
        return _responseStatus;
    }


    private  static  final String TAG = "MainViewModel";

    private final SettingPreferences pref;

    public MainViewModel(SettingPreferences pref) {
        this.pref = pref;
    }

    public LiveData<Boolean> getThemeSettings() {
        return LiveDataReactiveStreams.fromPublisher(pref.getThemeSetting());
    }
//    public MainViewModel() {
//        fetchUserList();
//    }

    private void fetchUserList() {
        _isLoading.setValue(true);
        Call<List<ItemUserResponse>> client = ApiConfig.getApiService().getUserList(apiKey);
        client.enqueue(new Callback<List<ItemUserResponse>>() {
            @Override
            public void onResponse(@NotNull Call<List<ItemUserResponse>> call, @NotNull Response<List<ItemUserResponse>> response) {
                _isLoading.setValue(false);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        _userList.setValue(response.body());
                    }
                } else {
                    if (response.body() != null) {
                        _responseStatus.setValue(new Event<>("Gagal menerima pesan!"));
                    }
                }
            }
            @Override
            public void onFailure(@NotNull Call<List<ItemUserResponse>> call, @NotNull Throwable t) {
                _isLoading.setValue(false);
                Log.e(TAG, "onFailure: " + t.getMessage());
                _responseStatus.setValue(new Event<>("Terjadi kesalahan jaringan!"));
            }
        });
    }

    public void fetchUserSearch(String key) {
        _isLoading.setValue(true);
        Call<UserResponse> client = ApiConfig.getApiService().getUserSearch(apiKey, key);
        client.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NotNull Call<UserResponse> call, @NotNull Response<UserResponse> response) {
                _isLoading.setValue(false);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        List<ItemUserResponse> itemUserResponses = response.body().getItems();
                        _itemSearchResult.setValue(itemUserResponses);
                    }
                } else {
                    if (response.body() != null) {
                        _responseStatus.setValue(new Event<>("Gagal menerima pesan!"));
                    }
                }
            }
            @Override
            public void onFailure(@NotNull Call<UserResponse> call, @NotNull Throwable t) {
                _isLoading.setValue(false);
                Log.e(TAG, "onFailure: " + t.getMessage());
                _responseStatus.setValue(new Event<>("Terjadi kesalahan jaringan!"));
            }
        });
    }
}
