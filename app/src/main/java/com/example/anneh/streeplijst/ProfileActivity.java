package com.example.anneh.streeplijst;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.Format;
import java.text.NumberFormat;

public class ProfileActivity extends AppCompatActivity {

    StreepDatabase db;
    private TransactionAdapter adapter;
    Cursor transactionCursor;
    int userID;
    int transactionID;

    // AlertDialog
    AlertDialog.Builder builder;
    EditText pinET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Enable home button in actionbar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get user info from intent
        Intent intent = getIntent();
        String username = (String) intent.getSerializableExtra("user_name");
        userID = (int) intent.getSerializableExtra("user_id");
        String imgPath = (String) intent.getSerializableExtra("img_path");
        String imgName = (String) intent.getSerializableExtra("img_name");

        // Set username.
        TextView nameTV = (TextView) findViewById(R.id.username);
        nameTV.setText(username);

        // Set user image.
        CustomImageView userIV = (CustomImageView) findViewById(R.id.userImg);
        Bitmap imgBitmap;
        FileInputStream fis;

        try {

            File file = new File(imgPath, imgName);
            imgBitmap = BitmapFactory.decodeStream(new FileInputStream(file));

            // Set image.
            userIV.setImageBitmap(imgBitmap);
        }
        catch (FileNotFoundException e) {
            Log.d("Error: ", "file not found");
            e.printStackTrace();
        }

        // Get formatter for devices default currency.
        Format format = NumberFormat.getCurrencyInstance();

        // Get total costs from database & set text.
        db = StreepDatabase.getInstance(getApplicationContext());
        Float costs = db.getUserCosts(userID);
        TextView totalCosts = (TextView) findViewById(R.id.totalCosts);
        totalCosts.setText(format.format(costs));

        // Get cursor for transactions from given user.
        transactionCursor = db.selectUserTransactions(userID);

        // Set adapter to transaction ListView.
        ListView transactionLV = (ListView) findViewById(R.id.transactionsLV);
        adapter = new TransactionAdapter(this, transactionCursor);
        transactionLV.setAdapter(adapter);

        // Set listener for transactions.
        transactionLV.setOnItemLongClickListener(new ProfileActivity.ListViewLongClickListener());
    }

    // Go to PortfolioActivity when portfolioBtn clicked.
    public void portfolioClicked(View view) {

        // Pass user ID to PortfolioActivity
        Intent portfolioIntent = new Intent(ProfileActivity.this, PortfolioActivity.class);
        portfolioIntent.putExtra("user_id", userID);
        startActivity(portfolioIntent);
    }

    // LongClick transaction: Remove transaction?
    private class ListViewLongClickListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            // Get transaction ID
            Cursor clickedTransaction = (Cursor) parent.getItemAtPosition(position);
            transactionID = clickedTransaction.getInt(clickedTransaction.getColumnIndex("_id"));
            int removed = clickedTransaction.getInt(clickedTransaction.getColumnIndex("removed"));

            // Cancel if transaction already removed.
            if (removed == 1) {
                Toast toast = Toast.makeText(getApplicationContext(), "Transactie is al verwijderd", Toast.LENGTH_SHORT);
                toast.show();
                return false;
            }

            // Confirm removal with AlertDialog.
            builder = new AlertDialog.Builder(ProfileActivity.this);
            builder.setMessage("Transactie verwijderen?")
                    .setCancelable(false)
                    .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            // Remove transaction from database (users, portfolio, transactions).
                            db.removeTransaction(transactionID);

                            // Confirm removal through toast.
                            Toast toast = Toast.makeText(getApplicationContext(), "Transactie verwijderd", Toast.LENGTH_SHORT);
                            toast.show();

                            // Return to ProductsActivity.
                            Intent intent = new Intent(ProfileActivity.this, ProductsActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Nee", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }) ;

            // Create dialog box & show.
            AlertDialog alert = builder.create();
            alert.show();

            return true;
        }
    }

    // AlertDialog: open when remove clicked
    public void removeClicked(View view) {

        // Get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.prompts, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(promptView);

        // Get reference to EditText
        pinET = promptView.findViewById(R.id.pinET);

        // Set dialog window.
        builder.setMessage("Gebruiker verwijderen?")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        // Check PIN
                        Cursor pinCursor = db.getPin();
                        if (pinCursor != null & pinCursor.moveToFirst()) {

                            // If PIN.
                            int PIN = pinCursor.getInt(0);

                            // Get pin
                            // TODO: exception???
                            try {
                                int enteredPin = Integer.parseInt(pinET.getText().toString());

                                // Compare
                                if (enteredPin == PIN) {

                                    // Remove user from database.
                                    db.removeUser(userID);

                                    // Toast.
                                    Toast toast = Toast.makeText(getApplicationContext(), "Gebruiker verwijderd", Toast.LENGTH_SHORT);
                                    toast.show();

                                    // Return to ProductsActivity.
                                    Intent intent = new Intent(ProfileActivity.this, ProductsActivity.class);
                                    startActivity(intent);
                                }
                                else {
                                    Toast toast = Toast.makeText(getApplicationContext(), "PIN onjuist", Toast.LENGTH_SHORT);
                                    toast.show();
                                    dialog.cancel();
                                }
                            }
                            catch (Exception e) {

                                e.printStackTrace();

                                // Toast.
                                Toast toast = Toast.makeText(getApplicationContext(), "Voer PIN in", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                        else {

                            // Toast.
                            Toast toast = Toast.makeText(getApplicationContext(), "Nog geen PIN ingesteld", Toast.LENGTH_SHORT);
                            toast.show();
                            dialog.cancel();
                        }
                    }
                })
                .setNegativeButton("Annuleren", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        //Create dialog box and show.
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks: go to corresponding activity.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent intent = new Intent(ProfileActivity.this, ProductsActivity.class);
            startActivity(intent);
            finish();
        }

        return true;
    }
}
