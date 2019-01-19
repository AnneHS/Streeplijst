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

public class RegisterActivity extends AppCompatActivity {

    StreepDatabase db;

    EditText usernameET;
    String username;
    Button addBtn;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Enable home button in actionbar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get db.
        db = StreepDatabase.getInstance(getApplicationContext());

        // Get reference to EditText.
        usernameET = findViewById(R.id.username);

        // TODO: Ask for pin
        // Open AlertDialog when add button is clicked
        // https://www.javatpoint.com/android-alert-dialog-example
        addBtn = (Button) findViewById(R.id.addUser);
        builder = new AlertDialog.Builder(this);
        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                // Get username if entered, else cancel.
                if (!usernameET.getText().toString().equals("") && usernameET.getText().toString().length() > 0) {
                    username = usernameET.getText().toString();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Geef naam", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Ask for confirmation.
                builder.setMessage("Gebruiker toevoegen?")
                        .setCancelable(false)

                        // Add user when confirmed.
                        .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                // Add user to db.
                                User newUser = new User(username);
                                db.insertUser(newUser);

                                // Confirm with toast.
                                Toast toast = Toast.makeText(getApplicationContext(), "Gebruiker toegevoegd", Toast.LENGTH_SHORT);
                                toast.show();

                                // Return to ProductsActivity.
                                Intent intent = new Intent(RegisterActivity.this, ProductsActivity.class);
                                startActivity(intent);
                            }
                        })

                        // Cancel if user does not want to add user.
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks: go to corresponding activity.
        int id = item.getItemId();

        if (id == R.id.overview) {
            Intent intent = new Intent(RegisterActivity.this, OverviewActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.addProduct) {
            Intent intent = new Intent(RegisterActivity.this, NewProductActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.addUser) {
            Intent intent = new Intent(RegisterActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
        else if (id == android.R.id.home) {
            Intent intent = new Intent(RegisterActivity.this, ProductsActivity.class);
            startActivity(intent);
        }

        return true;
    }

}
