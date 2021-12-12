package com.example.githubuser.tablayout;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.githubuser.MainActivity;
import com.example.githubuser.UserFollowViewModel;
import com.example.githubuser.UserProfileActivity;
import com.example.githubuser.adapter.UserListAdapter;
import com.example.githubuser.api.ApiConfig;
import com.example.githubuser.databinding.FragmentFollowBinding;
import com.example.githubuser.model.ItemUserResponse;
import com.example.githubuser.model.UserDetailResponse;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private String apiKey = "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";

    private String username;

    private FragmentFollowBinding binding;

    private UserFollowViewModel userFollowViewModel;

    public static FollowFragment newInstance(int index) {
        FollowFragment fragment = new FollowFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFollowBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        UserProfileActivity activity = (UserProfileActivity) getActivity();

        Bundle results = activity.getMyData();
        username = results.getString("val1");

        userFollowViewModel = new ViewModelProvider((ViewModelStoreOwner) getViewLifecycleOwner()).get(UserFollowViewModel.class);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
            if (index == 1) {
                userFollowViewModel.fetchUserFollowers(username);
            } else {
                userFollowViewModel.fetchUserFollowing(username);
            }
            observeLiveData();
            userFollowViewModel.isLoadingList().observe(getViewLifecycleOwner(), this::showLoading);
        }
    }

    private void observeLiveData() {
        userFollowViewModel.isLoading().observe(getViewLifecycleOwner(), this::showLoading);

        userFollowViewModel.responseStatus().observe(getViewLifecycleOwner(), s -> {
            String toastText = s.getContentIfNotHandled();
            if (toastText != null)
                Toast.makeText(getActivity(),toastText,Toast.LENGTH_LONG).show();
        });

        userFollowViewModel.getUserFollowersList().observe(getViewLifecycleOwner(), userResponses -> {
            setRecyclerView(userResponses);
        });

        userFollowViewModel.getUserFollowingList().observe(getViewLifecycleOwner(), userResponses -> {
            setRecyclerView(userResponses);
        });
    }

    private void setRecyclerView(List<ItemUserResponse> itemUserResponse) {
        UserListAdapter adapter = new UserListAdapter();
        adapter.setData(itemUserResponse);
        binding.rvFollower.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvFollower.setHasFixedSize(true);
        binding.rvFollower.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickCallback(new UserListAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(ItemUserResponse data) {
                showSelectedUser(data);
            }
        });
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
        }
    }

    private void showSelectedUser(ItemUserResponse data) {showLoading(true);
        Call<UserDetailResponse> client = ApiConfig.getApiService().getUserDetail(apiKey, data.getLogin());
        client.enqueue(new Callback<UserDetailResponse>() {
            @Override
            public void onResponse(@NotNull Call<UserDetailResponse> call, @NotNull Response<UserDetailResponse> response) {
                showLoading(false);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Intent intent = new Intent(getActivity(), UserProfileActivity.class);
                        intent.putExtra(UserProfileActivity.EXTRA_USER, response.body());
                        startActivity(intent);
                    }
                } else {
                    if (response.body() != null) {
                        Toast.makeText(getActivity(), "Gagal menerima respon!", Toast.LENGTH_LONG).show();
                    }
                }
            }
            @Override
            public void onFailure(@NotNull Call<UserDetailResponse> call, @NotNull Throwable t) {
                showLoading(false);
                Log.e(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(getActivity(), "Gagal menerima respon!", Toast.LENGTH_LONG).show();
            }
        });
    }
}