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
    Button removeBtn;
    AlertDialog.Builder builder;

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
        String imgPath = (String) intent.getSerializableExtra("img_path");
        String imgName = (String) intent.getSerializableExtra("img_name");

        // Get reference to views.
        TextView nameTV = (TextView) findViewById(R.id.productName);
        TextView priceTV = (TextView) findViewById(R.id.price);
        CustomImageView productIV = (CustomImageView) findViewById(R.id.productImg);

        // Get formatter for devices default currency.
        Format format = NumberFormat.getCurrencyInstance();

        // Set text for TextViews.
        nameTV.setText(productName);
        priceTV.setText(format.format(productPrice));

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


        //TODO: Ask for pin
        // Open AlertDialog when remove button is clicked
        // https://www.javatpoint.com/android-alert-dialog-example
        removeBtn = (Button) findViewById(R.id.remove);
        builder = new AlertDialog.Builder(this);
        removeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                // Confirm removal.
                builder.setMessage("Verwijder product?")
                        .setCancelable(false)
                        .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                // Remove product from database.
                                db.removeProduct(productID);

                                // Confirm removal with toast.
                                Toast toast = Toast.makeText(getApplicationContext(), "Product verwijderd", Toast.LENGTH_SHORT);
                                toast.show();

                                // Return to ProductsActivity.
                                Intent intent = new Intent(ProductActivity.this, ProductsActivity.class);
                                startActivity(intent);

                            }
                        })

                        // Cancel if user does not want to remove product.
                        .setNegativeButton("Nee", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }) ;

                // Create dialog box and show.
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

}
