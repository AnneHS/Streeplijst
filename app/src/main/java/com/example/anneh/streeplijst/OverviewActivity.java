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

import java.text.Format;
import java.text.NumberFormat;

public class OverviewActivity extends AppCompatActivity {

    private StreepDatabase db;
    int transactionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        // Enable home button in actionbar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get formatter for devices default currency.
        Format format = NumberFormat.getCurrencyInstance();

        // Get total costs from database & set TV's.
        db = StreepDatabase.getInstance(getApplicationContext());
        Float total = db.getTotalCosts();
        TextView totalCosts = (TextView) findViewById(R.id.totalCosts);
        totalCosts.setText(format.format(total));

        // Set TransactionAdapter for ListView.
        Cursor transactionsCursor = db.selectTransactions();
        ListView transactionsLV = (ListView) findViewById(R.id.transactionsLV);
        OverviewAdapter adapter = new OverviewAdapter(this, transactionsCursor);
        transactionsLV.setAdapter(adapter);

        // Set listener for transactions in ListView.
        transactionsLV.setOnItemLongClickListener(new OverviewActivity.ListViewLongClickListener());

    }

    // LongClick transaction: Ask if user wants to remove the transaction.
    private class ListViewLongClickListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            // Get ID of clicked transaction.
            Cursor clickedTransaction = (Cursor) parent.getItemAtPosition(position);

            // Check if transaction has already been removed.
            transactionID = clickedTransaction.getInt(clickedTransaction.getColumnIndex("_id"));
            int removed = clickedTransaction.getInt(clickedTransaction.getColumnIndex("removed"));

            // Cancel if already removed and display toast.
            if (removed == 1) {

                Toast toast = Toast.makeText(getApplicationContext(), "Transactie is al verwijderd", Toast.LENGTH_SHORT);
                toast.show();
                return false;
            }

            // Create AlertDialog builder.
            AlertDialog.Builder builder = new AlertDialog.Builder(OverviewActivity.this);

            // Ask for confirmation.
            builder.setMessage("Transactie verwijderen?")
                    .setCancelable(false)

                    // Remove transaction when user confirms.
                    .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            // Remove transaction from database
                            db.removeTransaction(transactionID);

                            // Confirm removal through toast
                            Toast toast = Toast.makeText(getApplicationContext(), "Transactie verwijderd", Toast.LENGTH_SHORT);
                            toast.show();

                            // Return to ProductsActivity
                            Intent intent = new Intent(OverviewActivity.this, ProductsActivity.class);
                            startActivity(intent);
                        }
                    })

                    // Cancel AlertDialog if user does not want to remove transaction.
                    .setNegativeButton("Nee", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }) ;

            // Creating dialog box & show.
            AlertDialog alert = builder.create();
            alert.show();

            return true;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Return to main activity (ProductsActivity) when home button is pressed.
        Intent intent = new Intent(OverviewActivity.this, ProductsActivity.class);
        startActivity(intent);
        finish();

        return true;
    }
}
