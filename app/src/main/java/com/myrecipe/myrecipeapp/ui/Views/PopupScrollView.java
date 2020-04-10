/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 11/04/20 01:56
 */

package com.myrecipe.myrecipeapp.ui.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class PopupScrollView extends ScrollView {

    private static final int MAX_MARGIN = 350;
    private static final int MIN_MARGIN = 200;
    float y;
    int topMargin;

    public PopupScrollView(Context context) {
        super(context);
    }

    public PopupScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private boolean isScrollable(boolean scrollUp) {
        if (topMargin == 0) {
            int topMarginInPX = ((ConstraintLayout.LayoutParams) getLayoutParams()).topMargin;
            topMargin = (int) (topMarginInPX / getContext().getResources().getDisplayMetrics().density);
        }

        if (scrollUp)
            return topMargin == MIN_MARGIN;
        return topMargin < MAX_MARGIN && computeVerticalScrollOffset() != 0;
    }

    private boolean moveView(float dy) {
        ConstraintLayout.LayoutParams layoutParams = ((ConstraintLayout.LayoutParams) getLayoutParams());

        float density = getContext().getResources().getDisplayMetrics().density;

        if (layoutParams.topMargin - dy > MAX_MARGIN * density)
            layoutParams.topMargin = (int) (MAX_MARGIN * density);

        else if (layoutParams.topMargin - dy < MIN_MARGIN * density)
            layoutParams.topMargin = (int) (MIN_MARGIN * density);

        else
            layoutParams.topMargin -= dy;

        topMargin = (int) (layoutParams.topMargin / density);

        setLayoutParams(layoutParams);

        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                y = ev.getRawY();
                return true;

            case MotionEvent.ACTION_MOVE:
                float newY = ev.getRawY();

                float dy = y - newY;
                y = newY;

                if (isScrollable(dy > 0)) {
                    return super.onTouchEvent(ev);
                }

                return moveView(dy);

            default:
                return false;
        }
    }
}
