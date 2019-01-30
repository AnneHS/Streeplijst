/*
Anne Hoogerduijn Strating
12441163

Custom ImageView Class to create an ImageView with rounded corners.
https://stackoverflow.com/questions/18229358/bitmap-in-imageview-with-rounded-corners
 */

package com.example.anneh.streeplijst;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

public class CustomImageView extends android.support.v7.widget.AppCompatImageView {

    // Radius for rounded corners.
    public static float radius = 18.0f;

    public CustomImageView(Context context) {
        super(context);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        // Create rounded corners.
        Path clipPath = new Path();
        RectF rect = new RectF(0, 0, this.getWidth(), this.getHeight());
        clipPath.addRoundRect(rect, radius, radius, Path.Direction.CW);
        canvas.clipPath(clipPath);

        super.onDraw(canvas);
    }

    /* Resize image.
    https://rogcg.github.io/articles/2013-11/gridview-and-auto-resized-images-android
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }
}

