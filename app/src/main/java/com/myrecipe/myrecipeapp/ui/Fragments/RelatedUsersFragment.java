/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 22/04/20 17:56
 */

package com.myrecipe.myrecipeapp.ui.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.myrecipe.myrecipeapp.CallBacks.OnUserProfileChangedListener;
import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.data.RelatedUsersViewModel;
import com.myrecipe.myrecipeapp.models.UserModel;
import com.myrecipe.myrecipeapp.ui.Activities.MainActivity;
import com.myrecipe.myrecipeapp.ui.Adapters.RelatedUsersAdapter;
import com.myrecipe.myrecipeapp.util.PreferencesManager;


public class RelatedUsersFragment extends BaseListsFragment<UserModel> implements OnUserProfileChangedListener {
    static final int FOLLOWERS_TYPE = 0;
    static final int FOLLOWINGS_TYPE = 1;
    private int type;
    private String username;
    private RelatedUsersViewModel viewModel;


    RelatedUsersFragment(int fragmentType, String username) {
        this.type = fragmentType;
        this.username = username;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_related_users, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        view.findViewById(R.id.backButton).setOnClickListener(v -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .remove(this)
                    .commit();
        });

        TextView relatedUsersLabel = view.findViewById(R.id.relatedUsersLabel);
        if (type == FOLLOWINGS_TYPE)
            relatedUsersLabel.setText(R.string.followings);
        else if (type == FOLLOWERS_TYPE)
            relatedUsersLabel.setText(R.string.followers);

        viewModel = new ViewModelProvider(this)
                .get(RelatedUsersViewModel.class);

        recyclerView = view.findViewById(R.id.usersRecyclerView);
        adapter = new RelatedUsersAdapter(getContext(), this, recyclerView);
        errorLabel = view.findViewById(R.id.errorLabel);
        swipeRefreshLayout = view.findViewById(R.id.swipeLayout);

        viewModel.users.observe(getViewLifecycleOwner(), users -> onNewData(users.getUsers(), users.getCount()));

        viewModel.error.observe(getViewLifecycleOwner(), this::onError);

        super.onViewCreated(view, savedInstanceState);
    }

    void callViewModel(int position) {
        if (type == FOLLOWERS_TYPE) {
            viewModel.getUsersFollowers(getContext(), username, limitPerRequest, position);
        } else if (type == FOLLOWINGS_TYPE) {
            viewModel.getUsersFollowings(getContext(), username, limitPerRequest, position);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) getActivity()).removeFragment(this);
    }

    @Override
    public void onUserProfileChanged(UserModel user, boolean isCurrentUser) {
        UserModel me = PreferencesManager.getStoredUser(getContext());
        int requiredType;
        UserModel requiredUser;

        if (me.getUsername().equals(username)) {
            requiredType = FOLLOWINGS_TYPE;
            requiredUser = user;
        } else {
            requiredType = FOLLOWERS_TYPE;
            requiredUser = me;
        }

        if (type == requiredType && !isCurrentUser) {
            int position = adapter.indexOf(requiredUser);

            if (!user.isFollowedByUser()) {
                if (position >= 0)
                    adapter.remove(position);
            } else {
                if (position >= 0) {
                    errorLabel.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter.add(requiredUser);
                } else
                    adapter.update(requiredUser, position);
            }
        }
    }
}
