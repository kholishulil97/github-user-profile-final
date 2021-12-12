package com.example.githubuser;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.githubuser.database.Favorite;
import com.example.githubuser.repository.FavoriteRepository;

import java.util.List;

public class UserProfileViewModel extends ViewModel {
    private final FavoriteRepository mFavoriteRepository;

    public UserProfileViewModel(Application application) {
        mFavoriteRepository = new FavoriteRepository(application);
    }
    public void insert(Favorite favorite) {
        mFavoriteRepository.insert(favorite);
    }

    public void delete(Favorite favorite) {
        mFavoriteRepository.delete(favorite);
    }

    LiveData<Favorite> checkUsername(String username) {
        return mFavoriteRepository.checkUsername(username);
    }
}
