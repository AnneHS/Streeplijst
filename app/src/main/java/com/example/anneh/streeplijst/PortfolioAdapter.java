package com.example.anneh.streeplijst;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.example.anneh.streeplijst.R;

import java.text.Format;
import java.text.NumberFormat;

public class PortfolioAdapter extends ResourceCursorAdapter {

    // Constructor
    public PortfolioAdapter(Context context, Cursor cursor) {
        super(context, R.layout.portfolio_product, cursor);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Set alternating background colors for ListView rows.
        int position = cursor.getPosition();
        if (position % 2 == 1){
            view.setBackgroundColor(Color.parseColor("#4f83cc"));
        }
        else {
            view.setBackgroundColor(Color.WHITE);
        }


        // Get reference to TextViews from portfolio_product.xml.
        TextView nameTV = (TextView) view.findViewById(R.id.productName);
        TextView priceTV = (TextView) view.findViewById(R.id.productPrice);
        TextView amountTV = (TextView) view.findViewById(R.id.productAmount);
        TextView totalTV = (TextView) view.findViewById(R.id.totalCosts);

        // Extract properties from cursor.
        String productName = cursor.getString(cursor.getColumnIndex("productName"));
        Float productPrice = cursor.getFloat(cursor.getColumnIndex("productPrice"));
        String productAmount = cursor.getString(cursor.getColumnIndex("amount"));
        Float total = cursor.getFloat(cursor.getColumnIndex("total"));

        // Get formatter for devices default currency.
        Format format = NumberFormat.getCurrencyInstance();

        // Set text for TextViews.
        nameTV.setText(productName);
        priceTV.setText(format.format(productPrice));
        amountTV.setText(productAmount);
        totalTV.setText(format.format(total));
    }
}