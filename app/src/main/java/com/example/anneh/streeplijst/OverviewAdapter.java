package com.example.anneh.streeplijst;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import java.text.Format;
import java.text.NumberFormat;

public class OverviewAdapter extends ResourceCursorAdapter {

    // Constructor
    public OverviewAdapter(Context context, Cursor cursor) {
        super(context, R.layout.transactions_overview, cursor);
    }

    // TODO: laatste transacties bovenaan
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Get reference to TextViews.
        TextView usernameTV = (TextView) view.findViewById(R.id.username);
        TextView nameTV = (TextView) view.findViewById(R.id.productName);
        TextView priceTV = (TextView) view.findViewById(R.id.productPrice);
        TextView amountTV = (TextView) view.findViewById(R.id.amount);
        TextView totalTV = (TextView) view.findViewById(R.id.total);
        TextView dateTV = (TextView) view.findViewById(R.id.date);

        // Extract properties from cursor.
        String userName = cursor.getString(cursor.getColumnIndex("username"));
        String productName = cursor.getString(cursor.getColumnIndex("productName"));
        Float productPrice = cursor.getFloat(cursor.getColumnIndex("productPrice"));
        String productAmount = cursor.getString(cursor.getColumnIndex("amount"));
        Float total = cursor.getFloat(cursor.getColumnIndex("total"));
        String date = cursor.getString(cursor.getColumnIndex("timestamp"));
        int removed = cursor.getInt(cursor.getColumnIndex("removed"));

        // Get formatter for devices default currency
        Format format = NumberFormat.getCurrencyInstance();

        // Populate views with extracted properties.
        usernameTV.setText(userName);
        nameTV.setText(productName);
        priceTV.setText(format.format(productPrice));
        amountTV.setText(productAmount);
        totalTV.setText(format.format(total));
        dateTV.setText(date);

        // Display transaction in red if removed
        if (removed == 1) {
            // Populate views with extracted properties.
            usernameTV.setTextColor(Color.RED);
            nameTV.setTextColor(Color.RED);
            priceTV.setTextColor(Color.RED);
            amountTV.setTextColor(Color.RED);
            totalTV.setTextColor(Color.RED);
            dateTV.setTextColor(Color.RED);
        }

    }
}
