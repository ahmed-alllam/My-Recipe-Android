/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 24/04/20 00:55
 */

package com.myrecipe.myrecipeapp.ui.Fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.myrecipe.myrecipeapp.CallBacks.OnRecipeDataChangedListener;
import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.models.RecipeModel;
import com.myrecipe.myrecipeapp.ui.Adapters.RecipesRecyclerAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BaseRecipesFragment extends BaseListsFragment<RecipeModel> implements OnRecipeDataChangedListener {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recipesRecyclerView);
        errorLabel = view.findViewById(R.id.errorLabel);
        swipeRefreshLayout = view.findViewById(R.id.swipeLayout);

        adapter = new RecipesRecyclerAdapter(getActivity(), this, recyclerView);

        super.onViewCreated(view, savedInstanceState);
    }

    public static class emptyCallBack implements Callback<Void> {
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {

        }

        @Override
        public void onFailure(Call<Void> call, Throwable t) {

        }
    }

    @Override
    public void onRecipeChanged(RecipeModel recipe) {
        int position = adapter.indexOf(recipe);
        if (position >= 0)
            adapter.update(recipe, position);
    }
}
