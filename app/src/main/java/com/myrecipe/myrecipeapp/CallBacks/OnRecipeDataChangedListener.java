/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 22/04/20 15:36
 */

package com.myrecipe.myrecipeapp.CallBacks;

import com.myrecipe.myrecipeapp.models.RecipeModel;

public interface OnRecipeDataChangedListener {

    void onRecipeChanged(RecipeModel recipe);
}
