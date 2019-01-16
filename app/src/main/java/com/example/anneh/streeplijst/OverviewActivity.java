package com.example.anneh.streeplijst;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class OverviewActivity extends AppCompatActivity {

    private StreepDatabase db;
    int transactionID;

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

        // Set listener for transactions.
        transactionsLV.setOnItemLongClickListener(new OverviewActivity.ListViewLongClickListener());

    }

    // LongClick --> Remove transaction?
    private class ListViewLongClickListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            // Get transaction ID
            Cursor clickedTransaction = (Cursor) parent.getItemAtPosition(position);
            transactionID = clickedTransaction.getInt(clickedTransaction.getColumnIndex("_id"));

            AlertDialog.Builder builder = new AlertDialog.Builder(OverviewActivity.this);
            builder.setMessage("Transactie verwijderen?")
                    .setCancelable(false)
                    .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            // Remove transaction from database
                            db.removeTransaction(transactionID);


                            // TODO: Verwijderen weergeven in transacties & opslaan

                            // Confirm removal through toast
                            Toast toast = Toast.makeText(getApplicationContext(), "Transactie verwijderd", Toast.LENGTH_SHORT);
                            toast.show();

                            // Return to ProductsActivity
                            Intent intent = new Intent(OverviewActivity.this, ProductsActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Nee", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }) ;

            // Creating dialog box
            AlertDialog alert = builder.create();
            alert.show();

            return true;
        }
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
