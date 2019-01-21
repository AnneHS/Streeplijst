package com.example.anneh.streeplijst;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
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
    Button removeBtn;
    int transactionID;

    // AlertDialog
    AlertDialog.Builder builder;

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
        ImageView userIV = (ImageView) findViewById(R.id.userImg);
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

        // Get total costs from database & set TV.
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

        //TODO: Ask for pin
        // Open AlertDialog when remove button is clicked.
        // https://www.javatpoint.com/android-alert-dialog-example
        removeBtn = (Button) findViewById(R.id.remove);
        builder = new AlertDialog.Builder(this);
        removeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                // Ask for confirmation,
                builder.setMessage("Verwijder gebruiker?")
                         .setCancelable(false)

                         // Remove user when user confirms.
                         .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int id) {

                                 // Remove user from database.
                                 db.removeUser(userID);

                                 // Confirm removal through toast
                                 Toast toast = Toast.makeText(getApplicationContext(), "Gebruiker verwijderd", Toast.LENGTH_SHORT);
                                 toast.show();

                                 // Return to ProductsActivity
                                 Intent intent = new Intent(ProfileActivity.this, ProductsActivity.class);
                                 startActivity(intent);
                             }
                         })

                        // Cancel if user does not want to remove user.
                        .setNegativeButton("Nee", new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int id) {
                                 dialog.cancel();
                             }
                         }) ;

                 // Create dialog box & show.
                 AlertDialog alert = builder.create();
                 alert.show();
            }
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks: go to corresponding activity.
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
