package com.example.githubuser;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.githubuser.api.ApiConfig;
import com.example.githubuser.database.Favorite;
import com.example.githubuser.databinding.ItemUserBinding;
import com.example.githubuser.helper.FavoriteDiffCallback;
import com.example.githubuser.model.UserDetailResponse;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private final ArrayList<Favorite> listFavorites = new ArrayList<>();

    void setListFavorites(List<Favorite> listFavorites) {
        final FavoriteDiffCallback diffCallback = new FavoriteDiffCallback(this.listFavorites, listFavorites);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        this.listFavorites.clear();
        this.listFavorites.addAll(listFavorites);
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUserBinding binding = ItemUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FavoriteViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        holder.bind(listFavorites.get(position));
    }

    @Override
    public int getItemCount() {
        return listFavorites.size();
    }

    static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        final ItemUserBinding binding;
        FavoriteViewHolder(ItemUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(Favorite favorite) {
            binding.txtName.setText(favorite.getLogin());
            binding.txtUsername.setText(favorite.getDate());
            Glide.with(binding.getRoot().getContext())
                    .load(favorite.getAvatarUrl())
                    .into(binding.imgAvatar);
            binding.parentItem.setOnClickListener(v -> {
                String apiKey = BuildConfig.API_KEY;
                Call<UserDetailResponse> client = ApiConfig.getApiService().getUserDetail(apiKey, favorite.getLogin());
                client.enqueue(new Callback<UserDetailResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<UserDetailResponse> call, @NotNull Response<UserDetailResponse> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                Intent intent = new Intent(v.getContext(), UserProfileActivity.class);
                                intent.putExtra(UserProfileActivity.EXTRA_USER, response.body());
                                intent.putExtra(UserProfileActivity.EXTRA_FAVORITE, favorite);
                                v.getContext().startActivity(intent);
                            }
                        } else {
                            if (response.body() != null) {
                                Toast.makeText(v.getContext(), "Gagal menerima respon!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    @Override
                    public void onFailure(@NotNull Call<UserDetailResponse> call, @NotNull Throwable t) {
                        Log.e(TAG, "onFailure: " + t.getMessage());
                        Toast.makeText(v.getContext(), "Terjadi kesalahan jaringan!", Toast.LENGTH_LONG).show();
                    }
                });
            });
        }
    }
}
