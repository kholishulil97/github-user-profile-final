package com.example.githubuser;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.githubuser.database.Favorite;
import com.example.githubuser.databinding.ActivityUserProfileBinding;
import com.example.githubuser.helper.DateHelper;
import com.example.githubuser.model.UserDetailResponse;
import com.example.githubuser.tablayout.FollowFragment;
import com.example.githubuser.tablayout.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

public class UserProfileActivity extends AppCompatActivity {

    private UserProfileViewModel userProfileViewModel;
    private ActivityUserProfileBinding binding;

    public static final String EXTRA_USER = "extra_user";

    public static final String EXTRA_FAVORITE = "extra_favorite";
    private final int ALERT_DIALOG_CLOSE = 10;
    private final int ALERT_DIALOG_DELETE = 20;

    private Favorite favorite;

    @StringRes
    private final int[] TAB_TITLES = new int[]{
            R.string.tab_follower,
            R.string.tab_following
    };

    private String out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        UserDetailResponse userDetailResponse = getIntent().getParcelableExtra(EXTRA_USER);
        out = userDetailResponse.getLogin();

        Bundle bundle = new Bundle();
        bundle.putString("username", out);
        FollowFragment fragobj = new FollowFragment();
        fragobj.setArguments(bundle);


        binding.tvName.setText(userDetailResponse.getName());
        binding.tvUsername.setText(userDetailResponse.getLogin());
        binding.tvLocation.setText(userDetailResponse.getLocation());
        binding.tvBio.setText(userDetailResponse.getBio());
        binding.tvCompany.setText(userDetailResponse.getCompany());
        binding.tvRepository.setText(Integer.toString(userDetailResponse.getPublicRepos()));
        binding.tvFollowing.setText(Integer.toString(userDetailResponse.getFollowing()));
        binding.tvFollowers.setText(Integer.toString(userDetailResponse.getFollowers()));
        Glide.with(UserProfileActivity.this)
                .load(userDetailResponse.getAvatarUrl())
                .into(binding.ivAvatar);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this);
        binding.viewPager.setAdapter(sectionsPagerAdapter);
        new TabLayoutMediator(binding.tabs, binding.viewPager,
                (tab, position) -> tab.setText(getResources().getString(TAB_TITLES[position]))
        ).attach();

        userProfileViewModel = obtainViewModel(UserProfileActivity.this);

        favorite = getIntent().getParcelableExtra(EXTRA_FAVORITE);

        if (favorite != null) {
            binding.btnFavorite.setText(getString(R.string.delete_favorite));
        } else {
            userProfileViewModel.checkUsername(userDetailResponse.getLogin()).observe(this, favorites -> {
                if (favorites != null) {
                    binding.btnFavorite.setText(getString(R.string.delete_favorite));
                    favorite = favorites;
                } else {
                    favorite = new Favorite();
                }
            });
        }

        binding.btnFavorite.setOnClickListener(view -> {
            String login = userDetailResponse.getLogin();
            String avatarUrl = userDetailResponse.getAvatarUrl();

            favorite.setLogin(login);
            favorite.setAvatarUrl(avatarUrl);

            if (binding.btnFavorite.getText().equals(getString(R.string.delete_favorite))) {
                userProfileViewModel.delete(favorite);
                showToast(getString(R.string.deleted));
            } else {
                favorite.setDate(DateHelper.getCurrentDate());
                userProfileViewModel.insert(favorite);
                showToast(getString(R.string.added));
            }

            finish();
        });

        if(getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setTitle(userDetailResponse.getLogin());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @NonNull
    private static UserProfileViewModel obtainViewModel(AppCompatActivity activity) {
        com.example.githubuser.helper.ViewModelFactory factory = com.example.githubuser.helper.ViewModelFactory.getInstance(activity.getApplication());
        return new ViewModelProvider(activity, factory).get(UserProfileViewModel.class);
    }

    public Bundle getMyData() {
        Bundle hm = new Bundle();
        hm.putString("val1",out);
        return hm;
    }
}
