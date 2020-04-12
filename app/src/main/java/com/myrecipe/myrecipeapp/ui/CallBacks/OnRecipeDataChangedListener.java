/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 12/04/20 22:50
 */

package com.myrecipe.myrecipeapp.ui.CallBacks;

import com.myrecipe.myrecipeapp.models.RecipeModel;

public interface OnRecipeDataChangedListener {

    void onRecipeChanged(RecipeModel recipe);
}
