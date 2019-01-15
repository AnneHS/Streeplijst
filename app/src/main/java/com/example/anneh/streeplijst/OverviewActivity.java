package com.example.anneh.streeplijst;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

public class OverviewActivity extends AppCompatActivity {

    private StreepDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        // Enable home button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get total costs from database & set TV
        db = StreepDatabase.getInstance(getApplicationContext());
        String total = Float.toString(db.getTotalCosts());
        TextView totalCosts = (TextView) findViewById(R.id.totalCosts);
        totalCosts.setText(total);

        // Transactions
        Cursor transactionsCursor = db.selectTransactions();
        ListView transactionsLV = (ListView) findViewById(R.id.transactionsLV);
        OverviewAdapter adapter = new OverviewAdapter(this, transactionsCursor);
        transactionsLV.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks --> go to corresponding activity
        int id = item.getItemId();

        if (id == R.id.overview) {
            Intent intent = new Intent(OverviewActivity.this, OverviewActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.addProduct) {
            Intent intent = new Intent(OverviewActivity.this, NewProductActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.addUser) {
            Intent intent = new Intent(OverviewActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
        else if (id == android.R.id.home) {
            Intent intent = new Intent(OverviewActivity.this, ProductsActivity.class);
            startActivity(intent);
        }

        return true;
    }
}
