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

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Set alternating background colors for rows in ListView.
        int position = cursor.getPosition();
        if (position % 2 == 1){
            view.setBackgroundColor(Color.parseColor("#e3f2fd"));
        }
        else {
            view.setBackgroundColor(Color.WHITE);
        }

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

        // Get formatter for devices default currency.
        Format format = NumberFormat.getCurrencyInstance();

        // Set text color to black.
        usernameTV.setTextColor(Color.BLACK);
        nameTV.setTextColor(Color.BLACK);
        priceTV.setTextColor(Color.BLACK);
        amountTV.setTextColor(Color.BLACK);
        totalTV.setTextColor(Color.BLACK);
        dateTV.setTextColor(Color.BLACK);

        // Populate views with extracted properties.
        usernameTV.setText(userName);
        nameTV.setText(productName);
        priceTV.setText(format.format(productPrice));
        amountTV.setText(productAmount);
        totalTV.setText(format.format(total));
        dateTV.setText(date);


        // Display transaction in red if removed.
        if (removed == 1) {

            usernameTV.setTextColor(Color.RED);
            nameTV.setTextColor(Color.RED);
            priceTV.setTextColor(Color.RED);
            amountTV.setTextColor(Color.RED);
            totalTV.setTextColor(Color.RED);
            dateTV.setTextColor(Color.RED);
        }
    }
}
