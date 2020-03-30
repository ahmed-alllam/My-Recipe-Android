/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 30/03/20 23:47
 */

package com.myrecipe.myrecipeapp.ui.Adapters;

import android.annotation.SuppressLint;
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

    private List<RecipeModel> recipesList = new ArrayList<>();
    private Context context;

    public RecipesFeedRecyclerAdapter(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecipeViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // todo create empty place holder and recipe item layout

        RecipeModel recipe = recipesList.get(position);
        RecipeViewHolder viewHolder = (RecipeViewHolder) holder;

        Glide.with(context).load(recipe.getMain_image()).into(viewHolder.mainImage);  // add placeholder
        viewHolder.name.setText(recipe.getName());
        viewHolder.description.setText(recipe.getDescription());
        viewHolder.favourites_count.setText(String.valueOf(recipe.getFavourites_count())); // ()
        // viewHolder.rating.setRating(recipe.getRating());
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

    @Override
    public int getItemCount() {
        return recipesList.size();
    }

    public void add(List<RecipeModel> recipeModels) {
        recipesList.addAll(recipeModels);
        notifyDataSetChanged();
    }

    public void clear() {
        recipesList.clear();
        notifyDataSetChanged();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {

        ImageView mainImage;
        ImageButton favourite;
        // RatingBar rating;
        TextView favourites_count, name, description,
                timeToFinish, tags;

        RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            mainImage = itemView.findViewById(R.id.mainImage);
            favourite = itemView.findViewById(R.id.favourite);
            // rating = itemView.findViewById(R.id.rating);
            favourites_count = itemView.findViewById(R.id.favourites_count);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            timeToFinish = itemView.findViewById(R.id.timeToFinish);
            tags = itemView.findViewById(R.id.tags);
        }
    }
}
