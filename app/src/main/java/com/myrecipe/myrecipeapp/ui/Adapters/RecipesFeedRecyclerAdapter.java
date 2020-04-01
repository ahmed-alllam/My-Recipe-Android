/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 02/04/20 01:16
 */

package com.myrecipe.myrecipeapp.ui.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.models.RecipeModel;

import java.util.ArrayList;
import java.util.List;

public class RecipesFeedRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_RECIPE = 1;
    private static final int VIEW_TYPE_LOADING = 2;
    private int offset;
    private int count;
    private boolean isLoadingAdded = false;
    private boolean isLoading = false;

    private List<RecipeModel> recipesList = new ArrayList<>();
    private Context context;

    public RecipesFeedRecyclerAdapter(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_RECIPE)
            return new RecipeViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_item, parent, false));
        if (viewType == VIEW_TYPE_LOADING)
            return new LoadingViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.loading_item, parent, false)); //todo not showing
        return new EmptyViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.empty_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (VIEW_TYPE_RECIPE == getItemViewType(position)) {
            RecipeModel recipe = recipesList.get(position);
            RecipeViewHolder viewHolder = (RecipeViewHolder) holder;

            Glide.with(context)
                    .load(recipe.getMain_image())
                    .placeholder(R.drawable.placeholder)
                    .into(viewHolder.mainImage);

            viewHolder.name.setText(recipe.getName());
            viewHolder.description.setText(recipe.getDescription());
            viewHolder.favourites_count.setText(String.valueOf(recipe.getFavourites_count())); // ()
            viewHolder.rating.setText(String.valueOf(recipe.getRating()));
            viewHolder.timeToFinish.setText(recipe.getTime_to_finish() + " min");

            StringBuilder sb = new StringBuilder();
            List<String> tags = recipe.getTags();
            for (int i = 0; i < tags.size(); i++) {
                if (i != 0) {
                    sb.append(" · ");
                }
                sb.append(tags.get(i));
            }
            viewHolder.tags.setText(sb.toString());

            viewHolder.favourite.setOnClickListener(v -> {  // todo

            });
        }
    }

    @Override
    public int getItemCount() {
        if (recipesList.size() != 0)
            return recipesList.size();
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (recipesList.size() == 0)
            return VIEW_TYPE_EMPTY;
        if (position == recipesList.size() - 1 && isLoadingAdded)
            return VIEW_TYPE_LOADING;
        return VIEW_TYPE_RECIPE;
    }

    public void add(RecipeModel recipe) {
        recipesList.add(recipe);
        notifyItemInserted(recipesList.size() - 1);
    }

    public void addAll(List<RecipeModel> recipeModels) {
        recipesList.addAll(recipeModels);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        recipesList.remove(position);
        notifyItemRemoved(position);
    }

    public void clear() {
        isLoadingAdded = false;
        recipesList.clear();
        notifyDataSetChanged();
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(null);
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = recipesList.size() - 1;
        remove(position);
    }

    public boolean isLastPage() {
        return offset >= count;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setCount(int count) {
        this.count = count;
    }


    class LoadingViewHolder extends RecyclerView.ViewHolder {
        LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    class EmptyViewHolder extends RecyclerView.ViewHolder {
        EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {

        ImageView mainImage;
        ImageButton favourite;
        TextView favourites_count, name, description,
                timeToFinish, tags, rating;

        RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            mainImage = itemView.findViewById(R.id.mainImage);
            favourite = itemView.findViewById(R.id.favourite);
            rating = itemView.findViewById(R.id.rating);
            favourites_count = itemView.findViewById(R.id.favourites_count);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            timeToFinish = itemView.findViewById(R.id.timeToFinish);
            tags = itemView.findViewById(R.id.tags);
        }
    }
}
