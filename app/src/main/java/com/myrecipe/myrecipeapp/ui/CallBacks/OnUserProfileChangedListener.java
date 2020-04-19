/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 19/04/20 22:04
 */

package com.myrecipe.myrecipeapp.ui.CallBacks;

import com.myrecipe.myrecipeapp.models.UserModel;

public interface OnUserProfileChangedListener {

    void onUserProfileChanged(UserModel user, boolean isCurrentUser);
}
