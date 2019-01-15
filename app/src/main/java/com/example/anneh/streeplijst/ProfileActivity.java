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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    StreepDatabase db;
    private TransactionAdapter adapter;
    Cursor transactionCursor;
    int userID;
    Button removeBtn;
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

        // Get transactionCursor for given user
        db = StreepDatabase.getInstance(getApplicationContext());
        transactionCursor = db.selectUserTransactions(userID);

        // Set adapter to transaction listview
        ListView transactionLV = (ListView) findViewById(R.id.transactionsLV);
        adapter = new TransactionAdapter(this, transactionCursor);
        transactionLV.setAdapter(adapter);


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
