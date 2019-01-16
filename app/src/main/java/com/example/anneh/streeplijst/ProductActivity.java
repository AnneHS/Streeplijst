package com.example.anneh.streeplijst;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ProductActivity extends AppCompatActivity {

    StreepDatabase db;
    int productID;
    Button removeBtn;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        // Get db instance
        db = StreepDatabase.getInstance(getApplicationContext());

        // Get product ID, name & price
        // TODO: alleen ID meegeven, rest ophalen uit database
        Intent intent = getIntent();
        productID = (int) intent.getSerializableExtra("product_id");
        String productName = (String) intent.getSerializableExtra("product_name");
        float productPrice = (float) intent.getSerializableExtra("product_price");

        TextView nameTV = (TextView) findViewById(R.id.productName);
        TextView priceTV = (TextView) findViewById(R.id.price);

        nameTV.setText(productName);
        priceTV.setText(Float.toString(productPrice));


        //TODO: Ask for pin
        // Open AlertDialog when remove button is clicked
        // https://www.javatpoint.com/android-alert-dialog-example
        removeBtn = (Button) findViewById(R.id.remove);
        builder = new AlertDialog.Builder(this);
        removeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                builder.setMessage("Verwijder product?")
                        .setCancelable(false)
                        .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                // Remove product from database
                                db.removeProduct(productID);

                                // Confirm removal with toast
                                Toast toast = Toast.makeText(getApplicationContext(), "Product verwijderd", Toast.LENGTH_SHORT);
                                toast.show();


                                // Return to ProductsActivity
                                Intent intent = new Intent(ProductActivity.this, ProductsActivity.class);
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

}
