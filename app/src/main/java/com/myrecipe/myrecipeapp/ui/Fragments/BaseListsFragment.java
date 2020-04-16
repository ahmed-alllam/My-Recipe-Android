/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 16/04/20 23:47
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.ui.Adapters.BaseRecyclerAdapter;
import com.myrecipe.myrecipeapp.ui.Adapters.PaginationScrollListener;

import java.util.List;

public abstract class BaseListsFragment<T> extends Fragment {
    int limitPerRequest = 25;
    BaseRecyclerAdapter<T> adapter;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView errorLabel;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            public void loadMoreRecipes() {
                adapter.setLoading(true);
                adapter.addLoadingFooter();
                callViewModel(adapter.getOffset());
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


        int primaryColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        swipeRefreshLayout.setColorSchemeColors(primaryColor, Color.YELLOW, Color.GREEN);

        // todo: add checking for duplicates

        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (!adapter.isLoading()) {
                adapter.clear();
                adapter.setOffset(0);
                callViewModel(0);
                adapter.setLoading(true);
                recyclerView.setVisibility(View.VISIBLE);
                errorLabel.setVisibility(View.GONE);
            } else
                swipeRefreshLayout.setRefreshing(false);
        });

        callViewModel(0);
        adapter.setLoading(true);
    }

    abstract void callViewModel(int position);

    void onNewData(List<T> list, int count) {
        adapter.setOffset(adapter.getOffset() + limitPerRequest);
        adapter.setLoading(false);
        swipeRefreshLayout.setRefreshing(false);
        adapter.removeLoadingFooter();
        adapter.addAll(list);
        adapter.setCount(count);
    }

    void onError(int error) {
        adapter.setLoading(false);
        adapter.removeLoadingFooter();
        swipeRefreshLayout.setRefreshing(false);

        if (adapter.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            errorLabel.setVisibility(View.VISIBLE);
            errorLabel.setText(error);
        }
    }
}
