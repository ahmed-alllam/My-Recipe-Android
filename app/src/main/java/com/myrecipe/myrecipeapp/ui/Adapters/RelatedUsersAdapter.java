/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 21/04/20 17:01
 */

package com.myrecipe.myrecipeapp.ui.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.models.UserModel;
import com.myrecipe.myrecipeapp.ui.Activities.MainActivity;
import com.myrecipe.myrecipeapp.ui.Fragments.BaseListsFragment;
import com.myrecipe.myrecipeapp.ui.Fragments.GeneralUsersProfileFragment;


public class RelatedUsersAdapter extends BaseRecyclerAdapter<UserModel> {

    private static final int VIEW_TYPE_USER = 0;
    private static final int USER_LOADING_ITEM_HEIGHT = 100;

    public RelatedUsersAdapter(Context context, BaseListsFragment fragment, RecyclerView recyclerView) {
        super(context, fragment, recyclerView);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_EMPTY)
            return new EmptyViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.user_empty_item, parent, false));

        if (viewType == VIEW_TYPE_LOADING)
            return new LoadingViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.loading_item, parent, false));

        return new UserViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.user_item, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        if (list.size() == 0)
            return VIEW_TYPE_EMPTY;
        if ((position == list.size() - 1 && isLoadingMore) || list.get(position) == null)
            return VIEW_TYPE_LOADING;
        return VIEW_TYPE_USER;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) != VIEW_TYPE_USER)
            return;

        UserModel user = list.get(position);

        if (user == null)
            return;

        UserViewHolder viewHolder = (UserViewHolder) holder;

        Glide.with(context)
                .load(user.getImage())
                .placeholder(R.drawable.user)
                .into(viewHolder.userPhoto);

        viewHolder.userName.setText(user.getName());
        holder.itemView.setOnClickListener(v -> {
            GeneralUsersProfileFragment profileFragment = new GeneralUsersProfileFragment(user);
            FragmentTransaction ft = fragment.getChildFragmentManager().beginTransaction();
            ft.add(fragment.getView().getId(), profileFragment)
                    .addToBackStack(null)
                    .commit();
            ((MainActivity) fragment.getActivity()).addFragment(profileFragment);
        });

        startAnimation(viewHolder.itemView, position);
    }

    public void updateUser(UserModel user) {
        String username = user.getUsername();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == null)
                continue;

            if (list.get(i).getUsername().equals(username)) {
                int j = i;
                recyclerView.post(() -> {
                    list.set(j, user);
                    notifyItemChanged(j);
                });
                break;
            }
        }
    }

    public void removeUser(String slug) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == null)
                continue;

            if (list.get(i).getUsername().equals(slug)) {
                int j = i;
                recyclerView.post(() -> {
                    list.remove(j);
                    notifyItemRemoved(j);
                    if (isEmpty()) {
                        if (fragment.getView() != null) {
                            fragment.recyclerView.setVisibility(View.INVISIBLE);
                            fragment.errorLabel.setText(R.string.favourites_empty);
                            fragment.errorLabel.setVisibility(View.VISIBLE);
                        }
                    }
                });
                break;
            }
        }
    }

    @Override
    int getLoadingItemHeight() {
        return USER_LOADING_ITEM_HEIGHT;
    }

    private class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView userPhoto;
        TextView userName;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);

            userPhoto = itemView.findViewById(R.id.userPhoto);
            userName = itemView.findViewById(R.id.userName);
        }
    }
}
