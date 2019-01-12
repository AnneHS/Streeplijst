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
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    Button removeBtn;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Enable home button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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

                                 // TODO: Verwijder gebruiker
                                 finish();
                                 Toast toast = Toast.makeText(getApplicationContext(), "Gebruiker verwijderd", Toast.LENGTH_SHORT);
                                 toast.show();
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
