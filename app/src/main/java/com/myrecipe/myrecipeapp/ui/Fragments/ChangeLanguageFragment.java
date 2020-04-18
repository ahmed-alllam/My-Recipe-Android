/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 18/04/20 16:08
 */

package com.myrecipe.myrecipeapp.ui.Fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.data.PreferencesManager;
import com.myrecipe.myrecipeapp.ui.Activities.BaseActivity;


public class ChangeLanguageFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        TextView myMsg = new TextView(getContext());
        myMsg.setText(R.string.change_language);
        myMsg.setGravity(Gravity.CENTER_VERTICAL);
        myMsg.setTextSize(23);
        myMsg.setTextColor(Color.BLACK);
        myMsg.setPadding(55, 55, 55, 55);

        builder.setCustomTitle(myMsg)
                .setItems(R.array.languages, (dialog, which) -> {
                    String language = "";
                    switch (which) {
                        case 0:
                            language = PreferencesManager.LANGUAGE_ENGLISH;
                            break;
                        case 1:
                            language = PreferencesManager.LANGUAGE_ARABIC;
                    }
                    int result = PreferencesManager.changeLanguage(getContext(), language);
                    if (result == 0)
                        ((BaseActivity) getActivity()).changeLanguage(language, true);
                });
        return builder.create();
    }
}
