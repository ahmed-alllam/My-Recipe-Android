/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 18/04/20 15:41
 */

package com.myrecipe.myrecipeapp.ui.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.models.UserModel;


public class RelatedUsersAdapter extends BaseRecyclerAdapter<UserModel> {

    private static final int VIEW_TYPE_USER = 0;
    private static final int USER_LOADING_ITEM_HEIGHT = 100;

    public RelatedUsersAdapter(Context context, RecyclerView recyclerView) {
        super(context, recyclerView);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_EMPTY)
            return new RelatedUsersAdapter.EmptyViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.user_empty_item, parent, false));

        if (viewType == VIEW_TYPE_LOADING)
            return new RelatedUsersAdapter.LoadingViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.loading_item, parent, false));

        return new RelatedUsersAdapter.UserViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.user_item, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        if (list.size() == 0)
            return VIEW_TYPE_EMPTY;
        if (position == list.size() - 1 && isLoadingMore)
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
        // todo add listner for opening user fragment

        startAnimation(viewHolder.itemView, position);
    }

    @Override
    int getLoadingItemHeight() {
        return USER_LOADING_ITEM_HEIGHT;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView userPhoto;
        TextView userName;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);

            userPhoto = itemView.findViewById(R.id.userPhoto);
            userName = itemView.findViewById(R.id.userName);
        }
    }
}
