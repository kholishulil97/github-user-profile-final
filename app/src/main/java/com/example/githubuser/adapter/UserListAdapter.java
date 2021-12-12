package com.example.githubuser.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.githubuser.databinding.ItemUserBinding;
import com.example.githubuser.model.ItemUserResponse;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ListViewHolder> {

    private List<ItemUserResponse> itemUserResponses;

    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public interface OnItemClickCallback {
        void onItemClicked(ItemUserResponse data);
    }

    public void setData(List<ItemUserResponse> itemUserResponses) {
        this.itemUserResponses = itemUserResponses;
    }

    @NonNull
    @Override
    public UserListAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        ItemUserBinding binding = ItemUserBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        return new ListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.ListViewHolder holder, int position) {
        holder.binding.txtName.setText(itemUserResponses.get(position).getLogin());
        holder.binding.txtUsername.setText(itemUserResponses.get(position).getType());
        Glide.with(holder.itemView.getContext())
                .load(itemUserResponses.get(position).getAvatarUrl())
                .into(holder.binding.imgAvatar);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickCallback.onItemClicked(itemUserResponses.get(holder.getBindingAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemUserResponses.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {

        ItemUserBinding binding;

        public ListViewHolder(ItemUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
