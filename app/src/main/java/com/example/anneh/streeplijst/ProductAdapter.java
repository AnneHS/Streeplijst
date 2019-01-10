package com.example.anneh.streeplijst;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ProductAdapter extends ArrayAdapter<Product> {
    private ArrayList products;
    Product product;

    // Constructor
    public ProductAdapter(@NonNull Context context, int resource, ArrayList<Product> productsList) {
        super(context, resource, productsList);
        products = productsList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.product, parent, false);
        }

        // Get reference to button
        TextView productTV = (TextView) convertView.findViewById(R.id.product);

        // Get product info for given position
        Product product = (Product) products.get(position);
        String name = product.getName();
        float price = product.getPrice();

        // Set text for button
        productTV.setText(product.getName());


        // Return view
        return convertView;
    }
}
