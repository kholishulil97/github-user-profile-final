package com.example.githubuser;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.githubuser.adapter.UserListAdapter;
import com.example.githubuser.api.ApiConfig;
import com.example.githubuser.databinding.ActivityMainBinding;
import com.example.githubuser.model.ItemUserResponse;
import com.example.githubuser.model.UserDetailResponse;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private String apiKey = BuildConfig.API_KEY;

    private ActivityMainBinding binding;

    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
        observeLiveData();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvList.setLayoutManager(layoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        binding.rvList.addItemDecoration(itemDecoration);

        RxDataStore<Preferences> dataStore = new RxPreferenceDataStoreBuilder(this, "settings").build();
        SettingPreferences pref = SettingPreferences.getInstance(dataStore);

        mainViewModel = new ViewModelProvider(this, new ViewModelFactory(pref)).get(MainViewModel.class);
        mainViewModel.getThemeSettings().observe(this, isDarkModeActive -> {
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        //mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            binding.dialogLoading.dialogLoading.setVisibility(View.VISIBLE);
        } else {
            binding.dialogLoading.dialogLoading.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        if (searchManager != null) {
            SearchView searchView = (SearchView) (menu.findItem(R.id.search)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.search_hint));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchView.clearFocus();
                    searchView.setFocusable(false);
                    mainViewModel.fetchUserSearch(query);
                    observeLiveData();
                    return true;
                }
                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu1: {
                Intent i = new Intent(this, SettingActivity.class);
                startActivity(i);
                break;
            }
            case R.id.menu2: {
                Intent i = new Intent(this, FavoriteActivity.class);
                startActivity(i);
                break;
            }
        }
        return true;
    }

    private void observeLiveData() {
        mainViewModel.isLoading().observe(this, this::showLoading);

        mainViewModel.responseStatus().observe(this, s -> {
            String toastText = s.getContentIfNotHandled();
            if (toastText != null)
                Toast.makeText(MainActivity.this,toastText,Toast.LENGTH_LONG).show();
        });

        mainViewModel.getUserList().observe(this, userResponses -> {
            setRecyclerView(userResponses);
        });

        mainViewModel.getSearchResult().observe(MainActivity.this, userResponses -> {
            setRecyclerView(userResponses);
        });
    }

    private void setRecyclerView(List<ItemUserResponse> itemUserResponse) {
        UserListAdapter adapter = new UserListAdapter();
        adapter.setData(itemUserResponse);
        binding.rvList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickCallback(new UserListAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(ItemUserResponse data) {
                showSelectedUser(data);
            }
        });
    }

    private void showSelectedUser(ItemUserResponse data) {
        showLoading(true);
        Call<UserDetailResponse> client = ApiConfig.getApiService().getUserDetail(apiKey, data.getLogin());
        client.enqueue(new Callback<UserDetailResponse>() {
            @Override
            public void onResponse(@NotNull Call<UserDetailResponse> call, @NotNull Response<UserDetailResponse> response) {
                showLoading(false);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                        intent.putExtra(UserProfileActivity.EXTRA_USER, response.body());
                        startActivity(intent);
                    }
                } else {
                    if (response.body() != null) {
                        Toast.makeText(MainActivity.this, "Gagal menerima respon!", Toast.LENGTH_LONG).show();
                    }
                }
            }
            @Override
            public void onFailure(@NotNull Call<UserDetailResponse> call, @NotNull Throwable t) {
                showLoading(false);
                Log.e(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Terjadi kesalahan jaringan!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
