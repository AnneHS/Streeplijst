package com.example.anneh.streeplijst;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MailActivity extends AppCompatActivity {

    EditText mailET;
    Button submitBtn;
    StreepDatabase db;
    AlertDialog.Builder builder;
    String address;
    EditText pinET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);

        // Get reference to EditText & Button.
        mailET = findViewById(R.id.mail);
        submitBtn = findViewById(R.id.submit);

        // Get db.
        db = StreepDatabase.getInstance(getApplicationContext());
    }

    // AlertDialog: open when submit clicked
    public void submitClicked(View view) {

        // Get email if entered, else quit.
        if (!mailET.getText().toString().equals("") && mailET.getText().toString().length() > 0) {
            address = mailET.getText().toString();
        }
        else {
            Toast.makeText(getApplicationContext(), "Geef e-mailadres", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.prompts, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(promptView);

        // Get reference to EditText
        pinET = promptView.findViewById(R.id.pinET);

        // Set dialog window.
        builder.setMessage("Instellen als mail-adres?")
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

                                    // Add address to db.
                                    db.insertMail(address);

                                    // Toast.
                                    Toast toast = Toast.makeText(getApplicationContext(), "E-mail ingesteld", Toast.LENGTH_SHORT);
                                    toast.show();

                                    // Return to ProductsActivity.
                                    Intent intent = new Intent(MailActivity.this, ExportActivity.class);
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
