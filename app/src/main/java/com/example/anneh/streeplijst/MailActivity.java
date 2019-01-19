package com.example.anneh.streeplijst;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);

        // Get reference to EditText & Button.
        mailET = findViewById(R.id.mail);
        submitBtn = findViewById(R.id.submit);

        // Get db.
        db = StreepDatabase.getInstance(getApplicationContext());

        // Open AlertDialog when button is clicked.
        builder = new AlertDialog.Builder(this);
        submitBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                // Get username if entered, else quit.
                if (!mailET.getText().toString().equals("") && mailET.getText().toString().length() > 0) {
                    address = mailET.getText().toString();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Geef e-mailadres", Toast.LENGTH_SHORT).show();
                    return;
                }

                // AlertDialog builder to confirm e-mail address.
                builder.setMessage("Dit adres toevoegen?") //TODO: adres weergeven
                        .setCancelable(false)
                        .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                // Add address to db.
                                db.insertMail(address);

                                // Confirm.
                                Toast toast = Toast.makeText(getApplicationContext(), "E-mailadres toegevoegd", Toast.LENGTH_SHORT);
                                toast.show();

                                // Return to ProductsActivity.
                                Intent intent = new Intent(MailActivity.this, ExportActivity.class);
                                startActivity(intent);
                            }
                        })

                        // Cancel if user does not want to add the address.
                        .setNegativeButton("Nee", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }) ;

                // Creating dialog box & show.
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
}
