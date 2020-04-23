/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 24/04/20 00:55
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.myrecipe.myrecipeapp.CallBacks.OnRecipeDataChangedListener;
import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.data.APIClient;
import com.myrecipe.myrecipeapp.data.APIInterface;
import com.myrecipe.myrecipeapp.models.RecipeModel;
import com.myrecipe.myrecipeapp.ui.Activities.MainActivity;
import com.myrecipe.myrecipeapp.ui.Fragments.BaseListsFragment;
import com.myrecipe.myrecipeapp.ui.Fragments.BaseRecipesFragment;
import com.myrecipe.myrecipeapp.ui.Fragments.FavouritesFragment;
import com.myrecipe.myrecipeapp.ui.Fragments.RecipeDetailFragment;
import com.myrecipe.myrecipeapp.util.PreferencesManager;

import java.util.List;

public class RecipesRecyclerAdapter extends BaseRecyclerAdapter<RecipeModel> {

    private static final int VIEW_TYPE_RECIPE = 0;
    private static final int RECIPE_LOADING_ITEM_HEIGHT = 280;


    public RecipesRecyclerAdapter(Context context, BaseListsFragment fragment, RecyclerView recyclerView) {
        super(context, fragment, recyclerView);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_RECIPE)
            return new RecipeViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_item, parent, false));
        if (viewType == VIEW_TYPE_LOADING)
            return new LoadingViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.loading_item, parent, false));
        return new EmptyViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.recipe_empty_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (!(holder instanceof RecipeViewHolder))
            return;

        RecipeModel recipe = list.get(position);

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

        if (recipe.isFavouritedByUser())
            viewHolder.favourite.setImageResource(R.drawable.favourite2);
        else
            viewHolder.favourite.setImageResource(R.drawable.favourite_border);

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
        if ((position == list.size() - 1 && isLoadingMore) || list.get(position) == null)
            return VIEW_TYPE_LOADING;
        return VIEW_TYPE_RECIPE;
    }


    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageButton favourite;
        private TextView favourites_count;
        private ImageView mainImage;
        private TextView name, description,
                timeToFinish, tags, rating;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            mainImage = itemView.findViewById(R.id.mainImage);
            favourite = itemView.findViewById(R.id.favourite);
            rating = itemView.findViewById(R.id.rating);
            favourites_count = itemView.findViewById(R.id.favouritesCount);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            timeToFinish = itemView.findViewById(R.id.timeToFinish);
            tags = itemView.findViewById(R.id.tags);

            itemView.setOnClickListener(this);
            favourite.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.recipeItem:
                    RecipeDetailFragment recipeFragment = new RecipeDetailFragment(get(getLayoutPosition()));
                    fragment.getChildFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit)
                            .add(fragment.getView().getId(), recipeFragment)
                            .addToBackStack(null)
                            .commit();
                    ((MainActivity) fragment.getActivity()).addFragment(recipeFragment);
                    break;
                case R.id.favourite:
                    String token = PreferencesManager.getToken(context);
                    if (token.length() <= 0)
                        return;
                    token = "Token " + token;

                    APIInterface APIInterface = APIClient.getClient().create(APIInterface.class);

                    RecipeModel recipe = list.get(getLayoutPosition());
                    String slug = recipe.getSlug();


                    if (!recipe.isFavouritedByUser()) {
                        ((ImageButton) v).setImageResource(R.drawable.favourite2);
                        recipe.setFavourites_count(recipe.getFavourites_count() + 1);
                        recipe.setFavouritedByUser(true);

                        APIInterface.addFavouriteRecipe(token, slug).enqueue(new BaseRecipesFragment.emptyCallBack());
                    } else {
                        ((ImageButton) v).setImageResource(R.drawable.favourite_border);
                        recipe.setFavourites_count(recipe.getFavourites_count() - 1);
                        recipe.setFavouritedByUser(false);

                        if (fragment instanceof FavouritesFragment)
                            remove(indexOf(recipe));

                        APIInterface.removeFavouriteRecipe(token, slug).enqueue(new BaseRecipesFragment.emptyCallBack());
                    }

                    for (Fragment f : ((MainActivity) context).getFragments()) {
                        if (f instanceof OnRecipeDataChangedListener && f != fragment) {
                            ((OnRecipeDataChangedListener) f).onRecipeChanged(recipe);
                        }
                    }

                    favourites_count.setText(String.valueOf(recipe.getFavourites_count()));
                    break;
            }
        }
    }
}
