/*
Anne Hoogerduijn Strating
12441163

Activity that shows a users portfolio: the products the user has bought/"gestreept",
the amount, and theprice.
 */

package com.example.anneh.streeplijst;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.Format;
import java.text.NumberFormat;

public class PortfolioActivity extends AppCompatActivity {

    int userID;
    StreepDatabase db;
    Cursor portfolioCursor;
    PortfolioAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);

        // Get username and id.
        Intent intent = getIntent();
        userID = (int) intent.getSerializableExtra("user_id");

        // Get formatter for devices default currency.
        Format format = NumberFormat.getCurrencyInstance();

        // Get total costs from database & set TV.
        db = StreepDatabase.getInstance(getApplicationContext());
        Float costs = db.getUserCosts(userID);
        TextView totalCosts = (TextView) findViewById(R.id.totalCosts);
        totalCosts.setText(format.format(costs));

        // Get portfolio Cursor for given user.
        portfolioCursor = db.selectPortfolio(userID);

        // Set adapter to portfolio ListView.
        ListView portfolioLV = (ListView) findViewById(R.id.portfolioList);
        adapter = new PortfolioAdapter(this, portfolioCursor);
        portfolioLV.setAdapter(adapter);
    }
}
