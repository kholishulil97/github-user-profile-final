package com.example.githubuser.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Favorite favorite);
    @Update()
    void update(Favorite favorite);
    @Delete()
    void delete(Favorite favorite);
    @Query("SELECT * from favorite ORDER BY id ASC")
    LiveData<List<Favorite>> getAllFavorites();
    @Query("SELECT * from favorite WHERE login = :username")
    LiveData<Favorite> checkUsername(String username);
}
