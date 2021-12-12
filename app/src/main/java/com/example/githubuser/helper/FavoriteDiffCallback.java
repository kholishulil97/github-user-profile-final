package com.example.githubuser.helper;

import androidx.recyclerview.widget.DiffUtil;

import com.example.githubuser.database.Favorite;

import java.util.List;

public class FavoriteDiffCallback extends DiffUtil.Callback {
    private final List<Favorite> mOldFavoriteList;
    private final List<Favorite> mNewFavoriteList;

    public FavoriteDiffCallback(List<Favorite> mOldFavoriteList, List<Favorite> mNewFavoriteList) {
        this.mOldFavoriteList = mOldFavoriteList;
        this.mNewFavoriteList = mNewFavoriteList;
    }

    @Override
    public int getOldListSize() {
        return mOldFavoriteList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewFavoriteList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldFavoriteList.get(oldItemPosition).getId() == mNewFavoriteList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final Favorite oldEmployee = mOldFavoriteList.get(oldItemPosition);
        final Favorite newEmployee = mNewFavoriteList.get(newItemPosition);
        return oldEmployee.getLogin().equals(newEmployee.getLogin()) && oldEmployee.getAvatarUrl().equals(newEmployee.getAvatarUrl()) && oldEmployee.getType().equals(newEmployee.getType());
    }
}
