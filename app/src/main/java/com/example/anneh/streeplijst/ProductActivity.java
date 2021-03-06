/*
Anne Hoogerduijn Strating
12441163

This Activity shows the 'profile page' of a product: product name, image, price and the related
transactions. Here a product can be removed as well.
 */

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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.Format;
import java.text.NumberFormat;

public class ProductActivity extends AppCompatActivity {

    StreepDatabase db;
    int productID;
    int transactionID;
    EditText pinET;
    Cursor transactionCursor;
    OverviewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        // Enable home button in actionbar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get db.
        db = StreepDatabase.getInstance(getApplicationContext());

        // Get product info.
        Intent intent = getIntent();
        productID = (int) intent.getSerializableExtra("product_id");
        String productName = (String) intent.getSerializableExtra("product_name");
        float productPrice = (float) intent.getSerializableExtra("product_price");
        int productStrepen = (int) intent.getSerializableExtra("product_strepen");
        float productTotal = (float) intent.getSerializableExtra("product_total");
        String imgPath = (String) intent.getSerializableExtra("img_path");
        String imgName = (String) intent.getSerializableExtra("img_name");

        // Get reference to views.
        TextView nameTV = (TextView) findViewById(R.id.productName);
        TextView priceTV = (TextView) findViewById(R.id.price);
        TextView strepenTV = (TextView) findViewById(R.id.streepTV);
        TextView totalTV = (TextView) findViewById(R.id.totalTV);
        CustomImageView productIV = (CustomImageView) findViewById(R.id.productImg);

        // Get formatter for devices default currency.
        Format format = NumberFormat.getCurrencyInstance();

        // Set text for TextViews.
        nameTV.setText(productName);
        priceTV.setText(format.format(productPrice));
        strepenTV.setText(Integer.toString(productStrepen));
        totalTV.setText(format.format(productTotal));

        // Load bitmap & set image.
        Bitmap imgBitmap;
        FileInputStream fis;

        try {

            File file = new File(imgPath, imgName);
            imgBitmap = BitmapFactory.decodeStream(new FileInputStream(file));

            // Set image.
            productIV.setImageBitmap(imgBitmap);
        }
        catch (FileNotFoundException e) {
            Log.d("Error: ", "file not found");
            e.printStackTrace();
        }

        // Get cursor for transactions from given user.
        transactionCursor = db.selectProductTransactions(productID);

        // Set adapter to transaction ListView.
        ListView transactionLV = (ListView) findViewById(R.id.transactionsLV);
        adapter = new OverviewAdapter(this, transactionCursor);
        transactionLV.setAdapter(adapter);

        // Set listener for transactions.
        transactionLV.setOnItemLongClickListener(new ProductActivity.ListViewLongClickListener());
    }

    // LongClick transaction: Remove transaction?
    private class ListViewLongClickListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            // Get transaction ID.
            Cursor clickedTransaction = (Cursor) parent.getItemAtPosition(position);
            transactionID = clickedTransaction.getInt(
                    clickedTransaction.getColumnIndex("_id"));
            int removed = clickedTransaction.getInt(
                    clickedTransaction.getColumnIndex("removed"));

            // Cancel if transaction already removed.
            if (removed == 1) {

                // Custom Toast: Transaction has already been removed.
                Toast toast = new Toast(getApplicationContext());
                View customToastView = getLayoutInflater().inflate(
                        R.layout.activity_toast_custom_simple, null);
                TextView toastTV = (TextView) customToastView.findViewById(
                        R.id.toastText);
                toastTV.setText("Transactie is al verwijderd.");
                toast.setView(customToastView);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0,0);
                toast.show();

                return false;
            }

            // Confirm removal with AlertDialog.
            AlertDialog.Builder builder = new AlertDialog.Builder(ProductActivity.this);
            builder.setMessage("Transactie verwijderen?")
                    .setCancelable(false)
                    .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            // Remove transaction from database (users, portfolio, transactions).
                            db.removeTransaction(transactionID);

                            // Custom Toast: Transaction removed.
                            Toast toast = new Toast(getApplicationContext());
                            View customToastView = getLayoutInflater().inflate(
                                    R.layout.activity_toast_custom_simple, null);
                            TextView toastTV = (TextView) customToastView.findViewById(
                                    R.id.toastText);
                            toastTV.setText("Transactie verwijderd.");
                            toast.setView(customToastView);
                            toast.setDuration(Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0,0);
                            toast.show();

                            // Return to ProductsActivity.
                            Intent intent = new Intent(ProductActivity.this,
                                    ProductsActivity.class);
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


    public void removeClicked(View view) {

        // Get prompts.xml view (Custom AlertDialog).
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.prompts, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(promptView);

        // Get reference to EditText.
        pinET = promptView.findViewById(R.id.pinET);

        // Set dialog window: ask for PIN.
        builder.setMessage("Product verwijderen?")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        // Check if PIN set yet.
                        Cursor pinCursor = db.getPin();
                        if (pinCursor != null & pinCursor.moveToFirst()) {

                            int PIN = pinCursor.getInt(0);

                            try {
                                int enteredPin = Integer.parseInt(pinET.getText().toString());

                                // Remove PIN if entered PIN matches.
                                if (enteredPin == PIN) {

                                    // Remove product from database.
                                    db.removeProduct(productID);

                                    // Custom Toast: Product removed.
                                    Toast toast = new Toast(getApplicationContext());
                                    View customToastView = getLayoutInflater().inflate(
                                            R.layout.activity_toast_custom_simple, null);
                                    TextView toastTV = (TextView) customToastView.findViewById(
                                            R.id.toastText);
                                    toastTV.setText("Product verwijderd.");
                                    toast.setView(customToastView);
                                    toast.setDuration(Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0,0);
                                    toast.show();

                                    // Return to ProductsActivity.
                                    Intent intent = new Intent(ProductActivity.this,
                                            ProductsActivity.class);
                                    startActivity(intent);
                                }
                                else {

                                    // Custom Toast: Wrong PIN.
                                    Toast toast = new Toast(getApplicationContext());
                                    View customToastView = getLayoutInflater().inflate(
                                            R.layout.activity_toast_custom_simple, null);
                                    TextView toastTV = (TextView) customToastView.findViewById(
                                            R.id.toastText);
                                    toastTV.setText("PIN onjuist");
                                    toast.setView(customToastView);
                                    toast.setDuration(Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0,0);
                                    toast.show();

                                    dialog.cancel();
                                }
                            }
                            catch (Exception e) {

                                e.printStackTrace();

                                // Custom Toast: Enter PIN.
                                Toast toast = new Toast(getApplicationContext());
                                View customToastView = getLayoutInflater().inflate(
                                        R.layout.activity_toast_custom_simple, null);
                                TextView toastTV = (TextView) customToastView.findViewById(
                                        R.id.toastText);
                                toastTV.setText("Voer PIN in.");
                                toast.setView(customToastView);
                                toast.setDuration(Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0,0);
                                toast.show();
                            }
                        }
                        else {

                            // Custom Toast: No PIN set yet.
                            Toast toast = new Toast(getApplicationContext());
                            View customToastView = getLayoutInflater().inflate(
                                    R.layout.activity_toast_custom_simple, null);
                            TextView toastTV = (TextView) customToastView.findViewById(
                                    R.id.toastText);
                            toastTV.setText("Nog geen PIN ingesteld");
                            toast.setView(customToastView);
                            toast.setDuration(Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0,0);
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

        // Return to main activity (ProductsActivity) when home button is pressed.
        Intent intent = new Intent(ProductActivity.this, ProductsActivity.class);
        startActivity(intent);
        finish();

        return true;
    }
}
