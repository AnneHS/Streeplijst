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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    EditText pinET;
    // AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

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
    }

    public void removeClicked(View view) {

        // Get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.prompts, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(promptView);

        // Get reference to EditText
        pinET = promptView.findViewById(R.id.pinET);

        // Set dialog window.
        builder.setMessage("Product verwijderen?")
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

                                    // Remove product from database.
                                    db.removeProduct(productID);

                                    // Toast.
                                    Toast toast = Toast.makeText(getApplicationContext(), "Product verwijderd", Toast.LENGTH_SHORT);
                                    toast.show();

                                    // Return to ProductsActivity.
                                    Intent intent = new Intent(ProductActivity.this, ProductsActivity.class);
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
}
