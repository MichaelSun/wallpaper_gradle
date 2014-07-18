package com.michael.wallpaper.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by michael on 14-7-18.
 */
public class STGVImageView extends ImageView {

    public int mWidth = 0;
    public int mHeight = 0;

    public STGVImageView(Context context) {
        this(context, null);
    }

    public STGVImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int heightC = width * mHeight / mWidth;

        setMeasuredDimension(width, heightC);
    }
}
