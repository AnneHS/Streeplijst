/*
Anne Hoogerduijn Strating
12441163

Activity where mail address can be entered (to database).
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MailActivity extends AppCompatActivity {

    EditText mailET;
    Button submitBtn;
    StreepDatabase db;
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

    // Open AlertDialog when 'Submit Button' clicked.
    public void submitClicked(View view) {

        // Get email if entered, else quit.
        if (!mailET.getText().toString().equals("") && mailET.getText().toString().length() > 0) {
            address = mailET.getText().toString();
        }
        else {

            // Custom Toast: Enter address.
            Toast toast = new Toast(getApplicationContext());
            View customToastView = getLayoutInflater().inflate(
                    R.layout.activity_toast_custom_simple, null);
            TextView toastTV = (TextView) customToastView.findViewById(
                    R.id.toastText);
            toastTV.setText("Voer e-mailadres in.");
            toast.setView(customToastView);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0,0);
            toast.show();

            return;
        }

        // Get prompts.xml view (Custom AlertDialog).
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.prompts, null);

        // Create builder for AlertDialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(promptView);

        // Get reference to EditText.
        pinET = promptView.findViewById(R.id.pinET);

        // Set dialog window.
        builder.setMessage("Instellen als mail-adres?")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        // Check if PIN has been set yey.
                        Cursor pinCursor = db.getPin();
                        if (pinCursor != null & pinCursor.moveToFirst()) {

                            int PIN = pinCursor.getInt(0);

                            try {
                                int enteredPin = Integer.parseInt(pinET.getText().toString());

                                // Compare entered PIN to set PIN.
                                if (enteredPin == PIN) {

                                    // Add address to db if PIN matches.
                                    db.insertMail(address);

                                    // Custom Toast: E-mail address has been set.
                                    Toast toast = new Toast(getApplicationContext());
                                    View customToastView = getLayoutInflater().inflate(
                                            R.layout.activity_toast_custom_simple, null);
                                    TextView toastTV = (TextView) customToastView.findViewById(
                                            R.id.toastText);
                                    toastTV.setText("E-mail ingesteld.");
                                    toast.setView(customToastView);
                                    toast.setDuration(Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0,0);
                                    toast.show();

                                    // Return to ProductsActivity.
                                    Intent intent = new Intent(MailActivity.this,
                                            ExportActivity.class);
                                    startActivity(intent);
                                }
                                else {

                                    // Custom Toast: Wrong PIN entered.
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

                                    // Cancel Dialog.
                                    dialog.cancel();
                                }
                            }
                            catch (Exception e) {

                                e.printStackTrace();

                                // Custom Toast: No PIN entered.
                                Toast toast = new Toast(getApplicationContext());
                                View customToastView = getLayoutInflater().inflate(
                                        R.layout.activity_toast_custom_simple, null);
                                TextView toastTV = (TextView) customToastView.findViewById(
                                        R.id.toastText);
                                toastTV.setText("Voer PIN in.");
                                toast.setView(customToastView);
                                toast.setDuration(Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0,0);
                                toast.show();
                            }
                        }
                        else {

                            // Custom Toast: No PIN set yet.
                            Toast toast = new Toast(getApplicationContext());
                            View customToastView = getLayoutInflater().inflate(
                                    R.layout.activity_toast_custom_simple, null);
                            TextView toastTV = (TextView) customToastView.findViewById(
                                    R.id.toastText);
                            toastTV.setText("Stel eerst een PIN in.");
                            toast.setView(customToastView);
                            toast.setDuration(Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0,0);
                            toast.show();

                            dialog.cancel();
                        }
                    }
                })

                // Cancel Dialog when user clicks Cancel.
                .setNegativeButton("Annuleren", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // Create dialog box and show.
        AlertDialog alert = builder.create();
        alert.show();
    }

}
