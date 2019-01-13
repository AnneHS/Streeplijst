package com.example.anneh.streeplijst;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewProductActivity extends AppCompatActivity {

    StreepDatabase db;


    EditText nameET;
    EditText priceET;
    String productName;
    float productPrice;
    Button addBtn;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        // Enable home button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get db
        db = StreepDatabase.getInstance(getApplicationContext());

        // Get reference to EditTexts
        nameET = (EditText) findViewById(R.id.name);
        priceET = (EditText) findViewById(R.id.price);


        // TODO: Ask for pin
        // Open AlertDialog when add button is clicked
        // https://www.javatpoint.com/android-alert-dialog-example
        addBtn = (Button) findViewById(R.id.addProduct);
        builder = new AlertDialog.Builder(this);
        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                // Get product name if entered, else return
                if (!nameET.getText().toString().equals("") && nameET.getText().toString().length() > 0) {
                    productName = nameET.getText().toString();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Geef productnaam", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Get price if entered, else return
                if(!nameET.getText().toString().equals("") && nameET.getText().toString().length() > 0) {
                    productPrice = Float.valueOf(priceET.getText().toString());
                }
                else {
                    Toast.makeText(getApplicationContext(), "Geef prijs", Toast.LENGTH_SHORT).show();
                    return;
                }
                builder.setMessage("Product toevoegen?")
                        .setCancelable(false)
                        .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                // Add product to products table (StreepDatabase)
                                Product newProduct = new Product(productName, productPrice);
                                db.insertProduct(newProduct);

                                Toast toast = Toast.makeText(getApplicationContext(), "Product toegevoegd", Toast.LENGTH_SHORT);
                                toast.show();

                                // Return to ProductsActivity
                                Intent intent = new Intent(NewProductActivity.this, ProductsActivity.class);
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

        // Handle action bar item clicks --> go to corresponding activity
        int id = item.getItemId();

        if (id == R.id.overview) {
            Intent intent = new Intent(NewProductActivity.this, OverviewActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.addProduct) {
            Intent intent = new Intent(NewProductActivity.this, NewProductActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.addUser) {
            Intent intent = new Intent(NewProductActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
        else if (id == android.R.id.home) {
            Intent intent = new Intent(NewProductActivity.this, ProductsActivity.class);
            startActivity(intent);
        }

        return true;
    }
}
