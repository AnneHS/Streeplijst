package com.example.anneh.streeplijst;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

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

        // Extract properties from cursor.
        String productName = cursor.getString(cursor.getColumnIndex("productName"));
        String productPrice = cursor.getString(cursor.getColumnIndex("productPrice"));
        String productAmount = cursor.getString(cursor.getColumnIndex("amount"));
        String total = cursor.getString(cursor.getColumnIndex("total"));
        String date = cursor.getString(cursor.getColumnIndex("timestamp"));

        // Populate views with extracted properties.
        nameTV.setText(productName);
        priceTV.setText(productPrice);
        amountTV.setText(productAmount);
        totalTV.setText(total);
        dateTV.setText(date);

    }
}
