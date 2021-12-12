package com.example.githubuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.githubuser.databinding.ActivityFavoriteBinding;

public class FavoriteActivity extends AppCompatActivity {

    private ActivityFavoriteBinding binding;
    private FavoriteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FavoriteViewModel favoriteViewModel = obtainViewModel(FavoriteActivity.this);
        favoriteViewModel.getAllFavorites().observe(this, favorites -> {
            if (favorites != null) {
                adapter.setListFavorites(favorites);
            }
        });

        adapter = new FavoriteAdapter();

        binding.rvFavorite.setLayoutManager(new LinearLayoutManager(this));
        binding.rvFavorite.setHasFixedSize(true);
        binding.rvFavorite.setAdapter(adapter);
    }

    @NonNull
    private static FavoriteViewModel obtainViewModel(AppCompatActivity activity) {
        com.example.githubuser.helper.ViewModelFactory factory = com.example.githubuser.helper.ViewModelFactory.getInstance(activity.getApplication());
        return new ViewModelProvider(activity, factory).get(FavoriteViewModel.class);
    }

    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }


}