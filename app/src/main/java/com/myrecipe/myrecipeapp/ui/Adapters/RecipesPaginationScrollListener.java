/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 02/04/20 01:16
 */

package com.myrecipe.myrecipeapp.ui.Adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class RecipesPaginationScrollListener extends RecyclerView.OnScrollListener {
    private LinearLayoutManager layoutManager;

    protected RecipesPaginationScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        if (!isLastPage() && !isLoading()) {
            if ((visibleItemCount + firstVisibleItemPosition) >=
                    totalItemCount && firstVisibleItemPosition >= 0) { // todo verstehen
                loadMoreRecipes();
            }
        }
    }

    public abstract boolean isLastPage();

    public abstract boolean isLoading();

    public abstract void loadMoreRecipes();
}
