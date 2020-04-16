/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 16/04/20 23:56
 */

package com.myrecipe.myrecipeapp.ui.Adapters;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myrecipe.myrecipeapp.R;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter {

    boolean isLoadingAdded = false; // todo change loading names
    boolean isLoading = false;
    ArrayList<T> list = new ArrayList<>();
    Context context;
    RecyclerView recyclerView;
    private int offset;
    private int count;
    private int lastPosition = -1;

    BaseRecyclerAdapter(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
    }

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

    private void remove(int position) {
        recyclerView.post(() -> {
            list.remove(position);
            notifyItemRemoved(position);
        });
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        recyclerView.post(() -> {
            list.add(null);
            notifyItemInserted(list.size() - 1);
        });
    }

    public void removeLoadingFooter() {
        if (isLoadingAdded) {
            isLoadingAdded = false;
            int position = list.size() - 1;
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
