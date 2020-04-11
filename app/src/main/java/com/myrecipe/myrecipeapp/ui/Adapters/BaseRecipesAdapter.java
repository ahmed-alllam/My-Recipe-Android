/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 11/04/20 23:44
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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.models.RecipeModel;

import java.util.ArrayList;
import java.util.List;

public class BaseRecipesAdapter extends RecyclerView.Adapter {

    public static final int VIEW_TYPE_RECIPE = 1;
    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_LOADING = 2;
    private int offset;
    private int count;
    private boolean isLoadingAdded = false;
    private boolean isLoading = false;

    private List<RecipeModel> recipesList = new ArrayList<>();
    private Context context;
    private Fragment fragment;
    private RecyclerView recyclerView;

    protected BaseRecipesAdapter(Context context, Fragment fragment, RecyclerView recyclerView) {
        this.context = context;
        this.fragment = fragment;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_RECIPE)
            return new BaseRecipesAdapter.RecipeViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_item, parent, false));
        if (viewType == VIEW_TYPE_LOADING)
            return new BaseRecipesAdapter.LoadingViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.loading_item, parent, false));
        return new BaseRecipesAdapter.EmptyViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.empty_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) != VIEW_TYPE_RECIPE)
            return;

        RecipeModel recipe = recipesList.get(position);

        if (recipe == null)
            return;

        BaseRecipesAdapter.RecipeViewHolder viewHolder = (BaseRecipesAdapter.RecipeViewHolder) holder;

        Glide.with(context)
                .load(recipe.getMain_image())
                .placeholder(R.drawable.placeholder)
                .into(viewHolder.mainImage);

        viewHolder.name.setText(recipe.getName());
        viewHolder.description.setText(recipe.getDescription());
        viewHolder.favourites_count.setText(String.valueOf(recipe.getFavourites_count()));
        viewHolder.rating.setText(String.valueOf(recipe.getRating()));
        viewHolder.timeToFinish.setText(String.format("%s %s", recipe.getTime_to_finish(), context.getResources().getString(R.string.minutes)));

        StringBuilder sb = new StringBuilder();
        List<String> tags = recipe.getTags();
        for (int i = 0; i < tags.size(); i++) {
            if (i != 0) {
                sb.append(" · ");
            }
            sb.append(tags.get(i));
        }
        viewHolder.tags.setText(sb.toString());
    }

    @Override
    public int getItemCount() {
        return recipesList.size() != 0 || !isLoading ? recipesList.size() : 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (recipesList.size() == 0)
            return VIEW_TYPE_EMPTY;
        if (position == recipesList.size() - 1 && isLoadingAdded)
            return VIEW_TYPE_LOADING;
        return VIEW_TYPE_RECIPE;
    }

    public RecipeModel get(int position) {
        return recipesList.get(position);
    }

    public void addAll(List<RecipeModel> recipeModels) {
        if (recipesList.size() != 0) {
            recyclerView.post(() -> {
                recipesList.addAll(recipeModels);
                notifyItemRangeInserted(recipesList.size() - recipeModels.size(),
                        recipeModels.size());
            });
        } else {
            recyclerView.post(() -> {
                recipesList.addAll(recipeModels);
                notifyDataSetChanged();
            });
        }
    }

    public void clear() {
        recyclerView.post(() -> {
            recipesList.clear();
            notifyDataSetChanged();
        });
    }

    private void remove(int position) {
        recyclerView.post(() -> {
            recipesList.remove(position);
            notifyItemRemoved(position);
        });
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        recyclerView.post(() -> {
            recipesList.add(null);
            notifyItemInserted(recipesList.size() - 1);
        });
    }

    public void removeLoadingFooter() {
        if (isLoadingAdded) {
            isLoadingAdded = false;
            int position = recipesList.size() - 1;
            remove(position);
        }
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

    public boolean isEmpty() {
        return recipesList.isEmpty();
    }

    public void removeRecipe(String slug) {
        for (int i = 0; i < recipesList.size(); i++) {
            if (recipesList.get(i) == null)
                continue;

            if (recipesList.get(i).getSlug().equals(slug)) {
                int j = i;
                recyclerView.post(() -> {
                    recipesList.remove(j);
                    notifyItemRemoved(j);
                    if (isEmpty()) {
                        if (fragment.getView() != null) {
                            TextView errorLabel = fragment.getView().findViewById(R.id.errorLabel);
                            fragment.getView().findViewById(R.id.recipesRecyclerView).setVisibility(View.INVISIBLE);
                            errorLabel.setText(R.string.favourites_empty);
                            errorLabel.setVisibility(View.VISIBLE);
                        }
                    }
                });
                break;
            }
        }
    }

    public void updateRecipe(RecipeModel recipe) {
        String slug = recipe.getSlug();

        for (int i = 0; i < recipesList.size(); i++) {
            if (recipesList.get(i) == null)
                continue;

            if (recipesList.get(i).getSlug().equals(slug)) {
                int j = i;
                recyclerView.post(() -> {
                    recipesList.set(j, recipe);
                    notifyItemChanged(j);
                });
                break;
            }
        }
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

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        public ImageButton favourite;
        public TextView favourites_count;
        public CardView recipeItem;
        private ImageView mainImage;
        private TextView name, description,
                timeToFinish, tags, rating;

        RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            recipeItem = itemView.findViewById(R.id.recipeItem);
            mainImage = itemView.findViewById(R.id.mainImage);
            favourite = itemView.findViewById(R.id.favourite);
            rating = itemView.findViewById(R.id.rating);
            favourites_count = itemView.findViewById(R.id.favouritesCount);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            timeToFinish = itemView.findViewById(R.id.timeToFinish);
            tags = itemView.findViewById(R.id.tags);
        }
    }
}
