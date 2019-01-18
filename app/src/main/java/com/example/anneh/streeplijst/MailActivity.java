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

        mailET = findViewById(R.id.mail);
        submitBtn = findViewById(R.id.submit);

        // Get db
        db = StreepDatabase.getInstance(getApplicationContext());

        builder = new AlertDialog.Builder(this);
        submitBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                // Get username if entered, else return
                if (!mailET.getText().toString().equals("") && mailET.getText().toString().length() > 0) {
                    address = mailET.getText().toString();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Geef e-mailadres", Toast.LENGTH_SHORT).show();
                    return;
                }
                builder.setMessage("Dit adres toevoegen?") //TODO: adres weergeven
                        .setCancelable(false)
                        .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                // TODO: Voeg e-mailadres toe aan database
                                // User newUser = new User(username);
                                db.insertMail(address);

                                Toast toast = Toast.makeText(getApplicationContext(), "E-mailadres toegevoegd", Toast.LENGTH_SHORT);
                                toast.show();

                                // Return to ProductsActivity
                                Intent intent = new Intent(MailActivity.this, ExportActivity.class);
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
