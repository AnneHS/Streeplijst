package com.example.anneh.streeplijst;

import android.content.Context;
import android.database.Cursor;
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

import java.util.ArrayList;

public class ProductAdapter extends ResourceCursorAdapter {

    // Constructor
    public ProductAdapter(Context context, Cursor cursor) {
        super(context, R.layout.product, cursor);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Get reference to TextView from product.xml.
        TextView productTV = (TextView) view.findViewById(R.id.product);

        // Get productName from database.
        String productName = cursor.getString(cursor.getColumnIndex("name"));

        // Set text.
        productTV.setText(productName);
    }

}
