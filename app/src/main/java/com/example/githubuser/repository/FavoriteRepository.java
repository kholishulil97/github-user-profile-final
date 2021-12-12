package com.example.githubuser.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.githubuser.database.Favorite;
import com.example.githubuser.database.FavoriteDao;
import com.example.githubuser.database.FavoriteRoomDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FavoriteRepository {
    private final FavoriteDao mFavoritesDao;

    private final ExecutorService executorService;

    public FavoriteRepository(Application application) {
        executorService = Executors.newSingleThreadExecutor();
        FavoriteRoomDatabase db = FavoriteRoomDatabase.getDatabase(application);
        mFavoritesDao = db.favoriteDao();
    }

    public LiveData<List<Favorite>> getAllFavorites() {
        return mFavoritesDao.getAllFavorites();
    }

    public void insert(final Favorite favorite) {
        executorService.execute(() -> mFavoritesDao.insert(favorite));
    }

    public void delete(final  Favorite favorite) {
        executorService.execute(() -> mFavoritesDao.delete(favorite));
    }

    public LiveData<Favorite> checkUsername(String username) {
        return mFavoritesDao.checkUsername(username);
    }
}
