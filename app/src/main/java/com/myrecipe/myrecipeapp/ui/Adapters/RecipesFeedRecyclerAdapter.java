/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 29/03/20 23:54
 */

package com.myrecipe.myrecipeapp.ui.Adapters;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myrecipe.myrecipeapp.models.RecipeModel;

import java.util.List;

public class RecipesFeedRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<RecipeModel> recipesList;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // todo create empty place holder and recipe item layout
    }

    @Override
    public int getItemCount() {
        return recipesList.size();
    }

    public void add(List<RecipeModel> recipeModels) {
        recipesList.addAll(recipeModels);
    }
}
