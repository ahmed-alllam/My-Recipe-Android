/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 14/04/20 23:38
 */

package com.myrecipe.myrecipeapp.ui.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.data.APIClient;
import com.myrecipe.myrecipeapp.data.APIInterface;
import com.myrecipe.myrecipeapp.data.PreferencesManager;
import com.myrecipe.myrecipeapp.models.RecipeModel;
import com.myrecipe.myrecipeapp.models.RecipesResultModel;
import com.myrecipe.myrecipeapp.ui.Adapters.BaseRecipesAdapter;
import com.myrecipe.myrecipeapp.ui.Adapters.PaginationScrollListener;
import com.myrecipe.myrecipeapp.ui.CallBacks.OnRecipeDataChangedListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BaseRecipesFragment extends Fragment {
    MutableLiveData<RecipesResultModel> recipes;
    MutableLiveData<Integer> error;
    int limitPerRequest = 25;
    protected BaseRecipesAdapter adapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recipesRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new BaseRecipesAdapter(getActivity(), this, recyclerView) {
            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                if (this.getItemViewType(position) == BaseRecipesAdapter.VIEW_TYPE_RECIPE) {

                    BaseRecipesAdapter.RecipeViewHolder viewHolder = (BaseRecipesAdapter.RecipeViewHolder) holder;

                    setOnFavouriteButtonPressed(viewHolder, position, this, view);

                    viewHolder.recipeItem.setOnClickListener(v -> {
                        RecipeDetailFragment recipeFragment = new RecipeDetailFragment(this.get(position));
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .add(view.getId(), recipeFragment)
                                .addToBackStack(null)
                                .commit();
                    });
                }
            }
        };
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            public void loadMoreRecipes() {
                adapter.setLoading(true);
                adapter.addLoadingFooter();
                callModelView(adapter.getOffset());
            }

            @Override
            public boolean isLastPage() {
                return adapter.isLastPage();
            }

            @Override
            public boolean isLoading() {
                return adapter.isLoading();
            }
        });

        TextView errorLabel = view.findViewById(R.id.errorLabel);
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipeLayout);

        int primaryColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        swipeRefreshLayout.setColorSchemeColors(primaryColor, Color.YELLOW, Color.GREEN);

        // todo: add checking for duplicates
        recipes.observe(getViewLifecycleOwner(), (recipes) -> {
            adapter.setOffset(adapter.getOffset() + limitPerRequest);
            adapter.setLoading(false);
            swipeRefreshLayout.setRefreshing(false);
            adapter.removeLoadingFooter();
            adapter.addAll(recipes.getRecipes());
            adapter.setCount(recipes.getCount());
        });
        error.observe(getViewLifecycleOwner(), (error) -> {
            adapter.setLoading(false);
            adapter.removeLoadingFooter();
            swipeRefreshLayout.setRefreshing(false);

            if (adapter.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                errorLabel.setVisibility(View.VISIBLE);
                errorLabel.setText(error);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (!adapter.isLoading()) {
                adapter.clear();
                adapter.setOffset(0);
                callModelView(0);
                adapter.setLoading(true);
                recyclerView.setVisibility(View.VISIBLE);
                errorLabel.setVisibility(View.GONE);
            } else
                swipeRefreshLayout.setRefreshing(false);
        });

        callModelView(0);
        adapter.setLoading(true);
    }

    protected abstract void callModelView(int offset);

    protected void setOnFavouriteButtonPressed(BaseRecipesAdapter.RecipeViewHolder holder, int position,
                                               BaseRecipesAdapter adapter, View view) {

        RecipeModel recipe = adapter.get(position);

        if (recipe.isFavouritedByUser())
            holder.favourite.setImageResource(R.drawable.favourite2);
        else
            holder.favourite.setImageResource(R.drawable.favourite_border);

        holder.favourite.setOnClickListener(v -> {
            String token = PreferencesManager.getToken(getContext());
            if (token.length() <= 0)
                return;
            token = "Token " + token;

            APIInterface APIInterface = APIClient.getClient().create(APIInterface.class);
            String slug = recipe.getSlug();


            if (!recipe.isFavouritedByUser()) {
                ((ImageButton) v).setImageResource(R.drawable.favourite2);
                recipe.setFavourites_count(recipe.getFavourites_count() + 1);
                recipe.setFavouritedByUser(true);

                APIInterface.addFavouriteRecipe(token, slug).enqueue(new emptyCallBack());
            } else {
                ((ImageButton) v).setImageResource(R.drawable.favourite_border);
                recipe.setFavourites_count(recipe.getFavourites_count() - 1);
                recipe.setFavouritedByUser(false);

                APIInterface.removeFavouriteRecipe(token, slug).enqueue(new emptyCallBack());
            }

            for (Fragment f : getActivity().getSupportFragmentManager().getFragments()) {
                if (f instanceof OnRecipeDataChangedListener && f != this) {
                    ((OnRecipeDataChangedListener) f).onRecipeChanged(recipe);
                }
            }

            holder.favourites_count.setText(String.valueOf(recipe.getFavourites_count()));
        });
    }

    protected class emptyCallBack implements Callback<Void> {
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {

        }

        @Override
        public void onFailure(Call<Void> call, Throwable t) {

        }
    }
}
