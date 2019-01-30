/*
Anne Hoogerduijn Strating
12441163

Adapter for product gridview in the ProductsActivity.
 */

package com.example.anneh.streeplijst;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ResourceCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class ProductAdapter extends ResourceCursorAdapter {

    // Constructor
    public ProductAdapter(Context context, Cursor cursor) {
        super(context, R.layout.product, cursor);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Get productName from database.
        String productName = cursor.getString(cursor.getColumnIndex("name"));

        // Change color
        LinearLayout product = view.findViewById(R.id.productLL);
        product.setBackgroundResource(R.color.colorPrimary);

        // Set text for TextView from product.xml.
        TextView productTV = (TextView) view.findViewById(R.id.product);
        productTV.setText(productName);

        // Get reference to ImageView & get image name and path from database.
        CustomImageView productIV = (CustomImageView) view.findViewById(R.id.productImg);
        String imgName = cursor.getString(cursor.getColumnIndex("imgName"));
        String imgPath = cursor.getString(cursor.getColumnIndex("imgPath"));

        // Load file into ImageView
        Picasso.get().load(new File(imgPath, imgName)).into(productIV);
    }

}
