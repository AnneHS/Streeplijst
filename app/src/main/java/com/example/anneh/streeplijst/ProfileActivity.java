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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.Format;
import java.text.NumberFormat;

public class ProfileActivity extends AppCompatActivity {

    StreepDatabase db;
    private TransactionAdapter adapter;
    Cursor transactionCursor;
    int userID;
    Button removeBtn;
    int transactionID;

    // AlertDialog
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Enable home button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get username and id
        Intent intent = getIntent();
        String username = (String) intent.getSerializableExtra("user_name");
        userID = (int) intent.getSerializableExtra("user_id");

        // Set username
        TextView nameTV = (TextView) findViewById(R.id.username);
        nameTV.setText(username);

        // Get formatter for devices default currency
        Format format = NumberFormat.getCurrencyInstance();

        // Get total costs from database & set TV
        db = StreepDatabase.getInstance(getApplicationContext());
        Float costs = db.getUserCosts(userID);
        TextView totalCosts = (TextView) findViewById(R.id.totalCosts);
        totalCosts.setText(format.format(costs));

        // Get transactionCursor for given user
        transactionCursor = db.selectUserTransactions(userID);

        // Set adapter to transaction listview
        ListView transactionLV = (ListView) findViewById(R.id.transactionsLV);
        adapter = new TransactionAdapter(this, transactionCursor);
        transactionLV.setAdapter(adapter);

        // Set listener for transactions.
        transactionLV.setOnItemLongClickListener(new ProfileActivity.ListViewLongClickListener());


        //TODO: Ask for pin
        // Open AlertDialog when remove button is clicked
        // https://www.javatpoint.com/android-alert-dialog-example
        removeBtn = (Button) findViewById(R.id.remove);
        builder = new AlertDialog.Builder(this);
        removeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                builder.setMessage("Verwijder gebruiker?")
                         .setCancelable(false)
                         .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int id) {

                                 // Remove user from database
                                 db.removeUser(userID);

                                 // Confirm removal through toast
                                 Toast toast = Toast.makeText(getApplicationContext(), "Gebruiker verwijderd", Toast.LENGTH_SHORT);
                                 toast.show();

                                 // Return to ProductsActivity
                                 Intent intent = new Intent(ProfileActivity.this, ProductsActivity.class);
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
            }
        });
    }


    // LongClick --> Remove transaction?
    private class ListViewLongClickListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            // Get transaction ID
            Cursor clickedTransaction = (Cursor) parent.getItemAtPosition(position);
            transactionID = clickedTransaction.getInt(clickedTransaction.getColumnIndex("_id"));
            int removed = clickedTransaction.getInt(clickedTransaction.getColumnIndex("removed"));

            // Cancel if already removed
            if (removed == 1) {
                Toast toast = Toast.makeText(getApplicationContext(), "Transactie is al verwijderd", Toast.LENGTH_SHORT);
                toast.show();
                return false;
            }

            // Check
            builder = new AlertDialog.Builder(ProfileActivity.this);
            builder.setMessage("Transactie verwijderen?")
                    .setCancelable(false)
                    .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            // Remove transaction from database
                            db.removeTransaction(transactionID);

                            // Confirm removal through toast
                            Toast toast = Toast.makeText(getApplicationContext(), "Transactie verwijderd", Toast.LENGTH_SHORT);
                            toast.show();

                            // Return to ProductsActivity
                            Intent intent = new Intent(ProfileActivity.this, ProductsActivity.class);
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

        // Handle actian bar item clicks --> go to corresponding activity
        int id = item.getItemId();

        if (id == R.id.overview) {
            Intent intent = new Intent(ProfileActivity.this, OverviewActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.addProduct) {
            Intent intent = new Intent(ProfileActivity.this, NewProductActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.addUser) {
            Intent intent = new Intent(ProfileActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
        else if (id == android.R.id.home) {
            Intent intent = new Intent(ProfileActivity.this, ProductsActivity.class);
            startActivity(intent);
        }

        return true;
    }
}
