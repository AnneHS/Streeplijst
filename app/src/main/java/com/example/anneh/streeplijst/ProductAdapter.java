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
import android.widget.TextView;

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

        // Set text for TextView from product.xml.
        TextView productTV = (TextView) view.findViewById(R.id.product);
        productTV.setText(productName);

        // Get reference to ImageView & get image name and path from database.
        ImageView productImg = (ImageView) view.findViewById(R.id.productImg);
        String imgName = cursor.getString(cursor.getColumnIndex("imgName"));
        String imgPath = cursor.getString(cursor.getColumnIndex("imgPath"));

        // Load bitmap.
        Bitmap imgBitmap;
        FileInputStream fis;

        try {

            File file = new File(imgPath, imgName);
            imgBitmap = BitmapFactory.decodeStream(new FileInputStream(file));

            // Set image.
            productImg.setImageBitmap(imgBitmap);
        }
        catch (FileNotFoundException e) {
            Log.d("Error: ", "file not found");
            e.printStackTrace();
        }
    }

}
