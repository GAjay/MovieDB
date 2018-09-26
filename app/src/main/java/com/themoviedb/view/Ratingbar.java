package com.themoviedb.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.RatingBar;

public class Ratingbar extends android.support.v7.widget.AppCompatRatingBar {
    public Ratingbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Ratingbar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Ratingbar(Context context) {
        super(context);
    }
}
