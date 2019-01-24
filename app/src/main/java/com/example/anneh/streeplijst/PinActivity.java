package com.example.anneh.streeplijst;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PinActivity extends AppCompatActivity {

    String submittedPIN;
    StreepDatabase db;
    EditText pinET;
    int savedPIN;
    int newPIN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
    }

    public void submitClicked (View view) {

        // Get reference to EditText
        EditText newPinET = (EditText) findViewById(R.id.enteredPin);
        String submittedPIN = newPinET.getText().toString();

        // Check if (correct) PIN entered.
        if (submittedPIN.length() != 4) {
            Toast toast = Toast.makeText(getApplicationContext(), "PIN moet bestaan uit 4 cijfers.", Toast.LENGTH_SHORT);
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
                                // TODO: *****
                                int enteredPIN = Integer.parseInt(pinET.getText().toString());

                                // Compare
                                if (enteredPIN == savedPIN) {

                                    // Remove product from database.
                                    db.insertPin(newPIN);

                                    // Toast.
                                    Toast toast = Toast.makeText(getApplicationContext(), "PIN aangepast", Toast.LENGTH_SHORT);
                                    toast.show();

                                    // Return to ProductsActivity.
                                    Intent intent = new Intent(PinActivity.this, ProductsActivity.class);
                                    startActivity(intent);
                                }
                                else {
                                    Toast toast = Toast.makeText(getApplicationContext(), "PIN onjuist", Toast.LENGTH_SHORT);
                                    toast.show();
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
            else {

                // If no PIN.
                builder.setMessage("PIN Instellen?")
                        .setCancelable(false)
                        .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                // Enter DB
                                db.insertPin(newPIN);

                                // TOAST
                                // Confirm.
                                Toast toast = Toast.makeText(getApplicationContext(), "PIN Toegevoegd", Toast.LENGTH_SHORT);
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

                //Create dialog box and show.
                AlertDialog alert = builder.create();
                alert.show();
            }
        }

    }
}
