/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 18/04/20 18:12
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
import com.myrecipe.myrecipeapp.ui.Fragments.RecipeDetailFragment;

import java.util.List;

public class RecipesRecyclerAdapter extends BaseRecyclerAdapter<RecipeModel> {

    public static final int VIEW_TYPE_RECIPE = 0;
    private static final int RECIPE_LOADING_ITEM_HEIGHT = 280;


    protected RecipesRecyclerAdapter(Context context, Fragment fragment, RecyclerView recyclerView) {
        super(context, fragment, recyclerView);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_RECIPE)
            return new RecipesRecyclerAdapter.RecipeViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_item, parent, false));
        if (viewType == VIEW_TYPE_LOADING)
            return new RecipesRecyclerAdapter.LoadingViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.loading_item, parent, false));
        return new RecipesRecyclerAdapter.EmptyViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.recipe_empty_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) != VIEW_TYPE_RECIPE)
            return;

        RecipeModel recipe = list.get(position);

        if (recipe == null)
            return;

        RecipeViewHolder viewHolder = (RecipeViewHolder) holder;

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

        viewHolder.recipeItem.setOnClickListener(v -> {
            RecipeDetailFragment recipeFragment = new RecipeDetailFragment(get(position));
            fragment.getActivity().getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit)
                    .add(fragment.getView().getId(), recipeFragment)
                    .addToBackStack(null)
                    .commit();
        });

        startAnimation(viewHolder.itemView, position);
    }

    @Override
    int getLoadingItemHeight() {
        return RECIPE_LOADING_ITEM_HEIGHT;
    }

    @Override
    public int getItemViewType(int position) {
        if (list.size() == 0)
            return VIEW_TYPE_EMPTY;
        if (position == list.size() - 1 && isLoadingMore)
            return VIEW_TYPE_LOADING;
        return VIEW_TYPE_RECIPE;
    }


    public void removeRecipe(String slug) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == null)
                continue;

            if (list.get(i).getSlug().equals(slug)) {
                int j = i;
                recyclerView.post(() -> {
                    list.remove(j);
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

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == null)
                continue;

            if (list.get(i).getSlug().equals(slug)) {
                int j = i;
                recyclerView.post(() -> {
                    list.set(j, recipe);
                    notifyItemChanged(j);
                });
                break;
            }
        }
    }


    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        public ImageButton favourite;
        public TextView favourites_count;
        CardView recipeItem;
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
