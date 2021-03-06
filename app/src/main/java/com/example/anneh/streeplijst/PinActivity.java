/*
Anne Hoogerduijn Strating
12441163

Activity to set a PIN for the app.

 */

package com.example.anneh.streeplijst;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PinActivity extends AppCompatActivity {

    StreepDatabase db;
    EditText pinET;
    int savedPIN;
    int newPIN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        // Enable home button in actionbar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void submitClicked (View view) {

        // Get reference to EditText
        EditText newPinET = (EditText) findViewById(R.id.enteredPin);
        String submittedPIN = newPinET.getText().toString();

        // Check if (correct) PIN entered.
        if (submittedPIN.length() != 4) {

            /* Custom Toast: PIN has to consist of 4 numbers.
            https://www.dev2qa.com/android-custom-toast-example/
             */
            Toast toast = new Toast(getApplicationContext());
            View customToastView = getLayoutInflater().inflate(
                    R.layout.activity_toast_custom_simple, null);
            TextView toastTV = (TextView) customToastView.findViewById(
                    R.id.toastText);
            toastTV.setText("PIN moet bestaan uit 4 cijfers.");
            toast.setView(customToastView);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0,0);
            toast.show();
        }
        else {

            db = StreepDatabase.getInstance(getApplicationContext());
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            newPIN = Integer.parseInt(submittedPIN);

            // Check for existing PIN.
            Cursor pinCursor = db.getPin();
            if (pinCursor != null & pinCursor.moveToFirst()) {

                // If PIN.
                savedPIN = pinCursor.getInt(0);

                // Get prompts.xml view
                LayoutInflater layoutInflater = LayoutInflater.from(this);
                View promptView = layoutInflater.inflate(R.layout.prompts, null);
                pinET = promptView.findViewById(R.id.pinET);
                builder.setView(promptView);

                // Set dialog window.
                builder.setMessage("PIN vervangen? Voer oude PIN in om door te gaan.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                // Get pin
                                int enteredPIN = Integer.parseInt(pinET.getText().toString());

                                // Compare
                                if (enteredPIN == savedPIN) {

                                    // Remove product from database.
                                    db.insertPin(newPIN);

                                    // Custom Toast: new PIN.
                                    Toast toast = new Toast(getApplicationContext());
                                    View customToastView = getLayoutInflater().inflate(
                                            R.layout.activity_toast_custom_simple, null);
                                    TextView toastTV = (TextView) customToastView.findViewById(
                                            R.id.toastText);
                                    toastTV.setText("Nieuwe PIN ingesteld.");
                                    toast.setView(customToastView);
                                    toast.setDuration(Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0,0);
                                    toast.show();

                                    // Return to ProductsActivity.
                                    Intent intent = new Intent(PinActivity.this,
                                            ProductsActivity.class);
                                    startActivity(intent);
                                }
                                else {

                                    // Custom Toast: wrong PIN.
                                    Toast toast = new Toast(getApplicationContext());
                                    View customToastView = getLayoutInflater().inflate(
                                            R.layout.activity_toast_custom_simple, null);
                                    TextView toastTV = (TextView) customToastView.findViewById(
                                            R.id.toastText);
                                    toastTV.setText("PIN onjuist.");
                                    toast.setView(customToastView);
                                    toast.setDuration(Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0,0);
                                    toast.show();
                                }
                            }
                        })
                        .setNegativeButton("Annuleren", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                // Create dialog box and show.
                AlertDialog alert = builder.create();
                alert.show();
            }
            else {

                // If no PIN set yet.
                builder.setMessage("PIN Instellen?")
                        .setCancelable(false)
                        .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                // Enter DB
                                db.insertPin(newPIN);

                                // Custom Toast: PIN set.
                                Toast toast = new Toast(getApplicationContext());
                                View customToastView = getLayoutInflater().inflate(R.layout.activity_toast_custom_simple, null);
                                TextView toastTV = (TextView) customToastView.findViewById(R.id.toastText);
                                toastTV.setText("PIN Ingesteld.");
                                toast.setView(customToastView);
                                toast.setDuration(Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0,0);
                                toast.show();

                                // Return to ProductsActivity.
                                Intent intent = new Intent(PinActivity.this, ProductsActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Nee", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                // Create dialog box and show.
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Return to main activity (ProductsActivity) when home button is pressed.
        Intent intent = new Intent(PinActivity.this, ProductsActivity.class);
        startActivity(intent);
        finish();

        return true;
    }
}
