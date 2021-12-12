package com.example.githubuser;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.githubuser.database.Favorite;
import com.example.githubuser.repository.FavoriteRepository;

import java.util.List;

public class FavoriteViewModel extends ViewModel {
    private final FavoriteRepository mFavoriteRepository;

    public FavoriteViewModel(Application application) {
        mFavoriteRepository = new FavoriteRepository(application);
    }

    LiveData<List<Favorite>> getAllFavorites() {
        return mFavoriteRepository.getAllFavorites();
    }
}
