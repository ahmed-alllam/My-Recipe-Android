/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 20/04/20 21:11
 */

package com.myrecipe.myrecipeapp.ui.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class PopupScrollView extends ScrollView {

    private static final int MAX_MARGIN = 300;
    private static final int MIN_MARGIN = 150;
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
            int topMarginInPX = ((RelativeLayout.LayoutParams) ((View) getParent()).getLayoutParams()).topMargin;
            topMargin = (int) (topMarginInPX / getContext().getResources().getDisplayMetrics().density);
        }

        if (scrollUp)
            return topMargin == MIN_MARGIN;
        return topMargin < MAX_MARGIN && computeVerticalScrollOffset() != 0;
    }

    private boolean moveView(float dy) {
        RelativeLayout.LayoutParams layoutParams = ((RelativeLayout.LayoutParams) ((View) getParent()).getLayoutParams());

        float density = getContext().getResources().getDisplayMetrics().density;

        if (layoutParams.topMargin - dy > MAX_MARGIN * density)
            layoutParams.topMargin = (int) (MAX_MARGIN * density);

        else if (layoutParams.topMargin - dy < MIN_MARGIN * density)
            layoutParams.topMargin = (int) (MIN_MARGIN * density);

        else
            layoutParams.topMargin -= dy;

        topMargin = (int) (layoutParams.topMargin / density);

        ((View) getParent()).setLayoutParams(layoutParams);

        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // todo: buggy
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
