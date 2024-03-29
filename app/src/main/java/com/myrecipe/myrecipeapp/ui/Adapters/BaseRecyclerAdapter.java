/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 24/04/20 00:55
 */

package com.myrecipe.myrecipeapp.ui.Adapters;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.ui.Fragments.BaseListsFragment;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter {

    static final int VIEW_TYPE_EMPTY = 1;
    static final int VIEW_TYPE_LOADING = 2;

    boolean isLoadingMore = false;
    private boolean isFirstLoading = false;
    public ArrayList<T> list = new ArrayList<>();
    Context context;
    private RecyclerView recyclerView;
    BaseListsFragment fragment;
    private int offset;
    private int count;
    private int lastPosition = -1;

    BaseRecyclerAdapter(Context context, BaseListsFragment fragment, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.fragment = fragment;
    }

    @Override
    public int getItemCount() {
        return list.size() != 0 || !isFirstLoading ? list.size() : getLoadingItemsNum();
    }

    private int getLoadingItemsNum() {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float parentHeight = recyclerView.getHeight() / displayMetrics.density;

        return (int) (parentHeight / getLoadingItemHeight());
    }

    abstract int getLoadingItemHeight();

    void startAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.recyclerview_animation);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        holder.itemView.clearAnimation();
    }

    public T get(int position) {
        return list.get(position);
    }

    public void add(T item) {
        recyclerView.post(() -> {
            list.add(item);
            notifyItemInserted(list.size() - 1);
        });
    }

    public void addAll(List<T> objects) {
        if (list.size() != 0) {
            recyclerView.post(() -> {
                list.addAll(objects);
                notifyItemRangeInserted(list.size() - objects.size(),
                        objects.size());
            });
        } else {
            recyclerView.post(() -> {
                list.addAll(objects);
                notifyDataSetChanged();
            });
        }
    }

    public void clear() {
        recyclerView.post(() -> {
            list.clear();
            notifyDataSetChanged();
        });
    }

    public boolean contains(T item) {
        return list.contains(item);
    }

    public int indexOf(T item) {
        return list.indexOf(item);
    }

    public void remove(int position) {
        recyclerView.post(() -> {
            list.remove(position);
            notifyItemRemoved(position);
            if (isEmpty()) {
                if (fragment.getView() != null) {
                    fragment.recyclerView.setVisibility(View.INVISIBLE);
                    fragment.errorLabel.setText(R.string.favourites_empty);
                    fragment.errorLabel.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void update(T item, int position) {
        recyclerView.post(() -> {
            list.set(position, item);
            notifyItemChanged(position);
        });
    }


    public void addLoadingFooter() {
        isLoadingMore = true;
        recyclerView.post(() -> {
            list.add(null);
            notifyItemInserted(list.size() - 1);
        });
    }

    public void removeLoadingFooter() {
        if (isLoadingMore) {
            isLoadingMore = false;
            int position = list.size() - 1;
            remove(position);
        }
    }

    public boolean isLoadingMore() {
        return isLoadingMore;
    }

    public boolean isLastPage() {
        return offset >= count;
    }

    public boolean isFirstLoading() {
        return isFirstLoading;
    }

    public void setFirstLoading(boolean firstLoading) {
        isFirstLoading = firstLoading;
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
        return list.isEmpty();
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
}
