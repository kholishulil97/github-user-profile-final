package com.example.githubuser;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import io.reactivex.rxjava3.annotations.NonNull;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final SettingPreferences pref;

    public ViewModelFactory(SettingPreferences dataStore) {
        this.pref = dataStore;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SettingViewModel.class)) {
            return (T) new SettingViewModel(pref);
        } else if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(pref);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
