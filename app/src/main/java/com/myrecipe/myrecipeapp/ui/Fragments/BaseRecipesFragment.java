/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 07/04/20 18:25
 */

package com.myrecipe.myrecipeapp.ui.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
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
import com.myrecipe.myrecipeapp.models.RecipesResultModel;
import com.myrecipe.myrecipeapp.ui.Adapters.BaseRecipesAdapter;
import com.myrecipe.myrecipeapp.ui.Adapters.PaginationScrollListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BaseRecipesFragment extends Fragment {
    static List<BaseRecipesFragment> fragmentList = new ArrayList<>();
    MutableLiveData<RecipesResultModel> recipes;
    MutableLiveData<Integer> error;
    protected RecyclerView recyclerView;
    int limitPerRequest = 25;
    private BaseRecipesAdapter adapter;

    public BaseRecipesFragment() {
        fragmentList.add(this);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recipesRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new BaseRecipesAdapter(getContext(), this, recyclerView) {
            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                if (adapter.getItemViewType(position) == BaseRecipesAdapter.VIEW_TYPE_RECIPE) {
                    setOnFavouriteButtonPressed(holder, position, adapter, view);
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

    protected abstract void setOnFavouriteButtonPressed(RecyclerView.ViewHolder holder,
                                                        int position,
                                                        BaseRecipesAdapter adapter,
                                                        View view);

    protected class emptyCallBack implements Callback<Void> {
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {

        }

        @Override
        public void onFailure(Call<Void> call, Throwable t) {

        }
    }
}
