package com.example.anneh.streeplijst;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import java.text.Format;
import java.text.NumberFormat;

public class TransactionAdapter extends ResourceCursorAdapter {

    // Constructor
    public TransactionAdapter(Context context, Cursor cursor) {
        super(context, R.layout.transaction, cursor);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Get reference to TextViews.
        TextView nameTV = (TextView) view.findViewById(R.id.productName);
        TextView priceTV = (TextView) view.findViewById(R.id.productPrice);
        TextView amountTV = (TextView) view.findViewById(R.id.amount);
        TextView totalTV = (TextView) view.findViewById(R.id.total);
        TextView dateTV = (TextView) view.findViewById(R.id.date);

        // Extract transaction info from transactions table.
        String productName = cursor.getString(cursor.getColumnIndex("productName"));
        Float productPrice = cursor.getFloat(cursor.getColumnIndex("productPrice"));
        String productAmount = cursor.getString(cursor.getColumnIndex("amount"));
        Float total = cursor.getFloat(cursor.getColumnIndex("total"));
        String date = cursor.getString(cursor.getColumnIndex("timestamp"));
        int removed = cursor.getInt(cursor.getColumnIndex("removed"));

        // Get formatter for devices default currency.
        // https://stackoverflow.com/questions/7131922/how-to-format-a-float-value-with-the-device-currency-format
        Format format = NumberFormat.getCurrencyInstance();

        // Set text color to black.
        nameTV.setTextColor(Color.BLACK);
        priceTV.setTextColor(Color.BLACK);
        amountTV.setTextColor(Color.BLACK);
        totalTV.setTextColor(Color.BLACK);
        dateTV.setTextColor(Color.BLACK);

        // Populate TextViews with transaction info.
        nameTV.setText(productName);
        priceTV.setText(format.format(productPrice));
        amountTV.setText(productAmount);
        totalTV.setText(format.format(total));
        dateTV.setText(date);

        // Display transaction in red if removed.
        if (removed == 1) {

            // Populate views with extracted properties.
            nameTV.setTextColor(Color.RED);
            priceTV.setTextColor(Color.RED);
            amountTV.setTextColor(Color.RED);
            totalTV.setTextColor(Color.RED);
            dateTV.setTextColor(Color.RED);
        }
    }
}
