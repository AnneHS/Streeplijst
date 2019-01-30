/*
Anne Hoogerduijn Strating
12441163

Activity to convert the relevant database tables to Csv-files and e-mail them to a given e-mail
address. 
 */

package com.example.anneh.streeplijst;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ExportActivity extends AppCompatActivity {

    String mailAddress;
    StreepDatabase db;
    EditText pinET;
    Button emptyBtn;


    /* Storage Permissions
    https://stackoverflow.com/questions/16360763/permission-denied-when-creating-new-file-on-external-storage
     */
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        /* Ignore 'file:// Uri' exposure.
        https://stackoverflow.com/questions/42251634/android-os-fileuriexposedexception-file-jpg-exposed-beyond-app-through-clipdata-item-geturi
         */
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        // Enable home button in actionbar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get e-mail address from db.
        StreepDatabase db = StreepDatabase.getInstance(getApplicationContext());
        mailAddress = db.getMail();

        // Display e-mail address if given.
        if (mailAddress != "") {
            TextView mailTV = findViewById(R.id.mailTV);
            mailTV.setText(mailAddress);
        }
        else {

            /* Custom Toast: confirm saved e-mail address.
            https://www.dev2qa.com/android-custom-toast-example/
            */
            Toast toast = new Toast(getApplicationContext());
            View customToastView = getLayoutInflater().inflate(
                    R.layout.activity_toast_custom_simple, null);
            TextView toastTV = (TextView) customToastView.findViewById(R.id.toastText);
            toastTV.setText("Nog geen e-mailadres ingesteld.");
            toast.setView(customToastView);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0,0);
            toast.show();
        }

        // Disable button to empty database while CSV-files have not been e-mailed.
        emptyBtn = (Button) findViewById(R.id.emptyBtn);
        emptyBtn.setBackground(getApplicationContext().getDrawable(R.drawable.unabled_btn));
        emptyBtn.setEnabled(false);
    }

    // Ask for pin when export button clicked.
    public void exportClicked(View view) {

        // Get prompts.xml view (Custom AlertDialog).
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.prompts, null);

        // Create builder for AlertDialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(promptView);

        // Get reference to EditText in AlertDialog.
        pinET = promptView.findViewById(R.id.pinET);

        // Set dialog window.
        builder.setCancelable(false)

                // Check PIN if user confirms export.
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        // Cancel if no PIN has been set yet.
                        StreepDatabase db = StreepDatabase.getInstance(getApplicationContext());
                        Cursor pinCursor = db.getPin();

                        // If PIN entered, heck if PIN matches set PIN.
                        if (pinCursor != null & pinCursor.moveToFirst()) {
                            int PIN = pinCursor.getInt(0);
                            try {
                                int enteredPin = Integer.parseInt(pinET.getText().toString());

                                // Start export CSV-files if PIN matches.
                                if (enteredPin == PIN) {

                                    exportCSV();
                                }
                                else {

                                    // Custom Toast: wrong PIN Entered.
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

                                    dialog.cancel();
                                }
                            }
                            catch (Exception e) {

                                e.printStackTrace();

                                // Custom Toast: No PIN entered.
                                Toast toast = new Toast(getApplicationContext());
                                View customToastView = getLayoutInflater().inflate(
                                        R.layout.activity_toast_custom_simple, null);
                                TextView toastTV = (TextView)
                                        customToastView.findViewById(R.id.toastText);
                                toastTV.setText("Geen PIN ingevoerd.");
                                toast.setView(customToastView);
                                toast.setDuration(Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0,0);
                                toast.show();
                                return;
                            }
                        }
                        else {

                            // Custom Toast: No PIN set.
                            Toast toast = new Toast(getApplicationContext());
                            View customToastView = getLayoutInflater().inflate(
                                    R.layout.activity_toast_custom_simple, null);
                            TextView toastTV = (TextView)
                                    customToastView.findViewById(R.id.toastText);
                            toastTV.setText("Nog geen PIN ingesteld.");
                            toast.setView(customToastView);
                            toast.setDuration(Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0,0);
                            toast.show();

                            return;
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

    public void exportCSV() {

        /* Prompt user for permission to access storage if not yet granted.
        https://stackoverflow.com/questions/16360763/permission-denied-when-creating-new-file-on-external-storage
         */
        int permission = ActivityCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }

        // Get db.
        StreepDatabase db = StreepDatabase.getInstance(getApplicationContext());

        // Create "Streeplijst" Directory in phone storage, if it does not yet exist.
        File exportDir = new File(Environment.getExternalStorageDirectory(), "Streeplijst");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        // Get current date.
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String todayString = formatter.format(todayDate);

        /* WRITE CSV FILE users
        https://stackoverflow.com/questions/14049323/android-program-to-convert-the-sqlite-database-to-excel
         */
        File usersFile = new File(exportDir, "gebruikers(" + todayString + ").csv");

        try {
            usersFile.createNewFile();
            CSVWriter writer = new CSVWriter(new FileWriter(usersFile));
            Cursor usersDB = db.selectUsersCSV();
            writer.writeNext(usersDB.getColumnNames());

            while (usersDB.moveToNext()) {

                // 0: ID, 1: Name, 2: Costs
                String row[] = {usersDB.getString(0), usersDB.getString(1),
                        usersDB.getString(2)};
                writer.writeNext(row);
            }
            writer.close();
            usersDB.close();

            // WRITE CSV FILE PORTFOLIO
            File portfolioFile = new File(exportDir, "portfolio(" + todayString + ").csv");

            try {
                portfolioFile.createNewFile();
                writer = new CSVWriter(new FileWriter(portfolioFile));
                Cursor portfolioDB = db.selectPortfolios();
                writer.writeNext(portfolioDB.getColumnNames());

                while (portfolioDB.moveToNext()) {

                    // 0: userID, 1: ProductName, 2: ProductPrice, 3: amount, 4: total
                    String row[] = {portfolioDB.getString(0),
                            portfolioDB.getString(1),
                            portfolioDB.getString(2),
                            portfolioDB.getString(3),
                            portfolioDB.getString(4)};
                    writer.writeNext(row);
                }
                writer.close();
                portfolioDB.close();

                /* Create intent to open e-mail app.
                https://stackoverflow.com/questions/18415202/not-able-to-send-csv-file-with-email-in-android
                 */
                final Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);

                // Add all necessary info.
                emailIntent.setType("application/csv");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                        new String[]  {mailAddress});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, new String[] {"Streeplijst"});
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Streeplijst in bijlage.");

                // Add both csv files as attachments.
                Uri U = Uri.fromFile(usersFile);
                Uri U2 = Uri.fromFile(portfolioFile);
                ArrayList<Uri> uris = new ArrayList<Uri>();
                uris.add(U);
                uris.add(U2);
                emailIntent.putExtra(Intent.EXTRA_STREAM, uris);

                // Open e-mail.
                startActivity(Intent.createChooser(emailIntent, "Send Mail"));

                // Enable button to empty 'Streeplijst'.
                emptyBtn.setEnabled(true);
                emptyBtn.setBackground(getApplicationContext().getDrawable(R.drawable.buttonshape));

            }
            catch(Exception sqlEx) {

                Log.e("Error: ", sqlEx.getMessage(), sqlEx);

                /* Custom Toast: Error portfolio Csv.
                https://www.dev2qa.com/android-custom-toast-example/
                */
                Toast toast = new Toast(getApplicationContext());
                View customToastView = getLayoutInflater().inflate(
                        R.layout.activity_toast_custom_simple, null);
                TextView toastTV = (TextView) customToastView.findViewById(R.id.toastText);
                toastTV.setText("ERROR: portfolio bestand kon niet worden gemaakt.");
                toast.setView(customToastView);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0,0);
                toast.show();
            }

        }
        catch(Exception sqlEx) {

            Log.e("Error: ", sqlEx.getMessage(), sqlEx);

            // Custom Toast: Error users Csv.
            Toast toast = new Toast(getApplicationContext());
            View customToastView = getLayoutInflater().inflate(
                    R.layout.activity_toast_custom_simple, null);
            TextView toastTV = (TextView) customToastView.findViewById(R.id.toastText);
            toastTV.setText("ERROR: gebruikers bestand kon niet worden gemaakt.");
            toast.setView(customToastView);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0,0);
            toast.show();
        }
    }

    public void emptyClicked(View view) {

        // Get prompts.xml view (Custom AlertDialog).
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.prompts, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(promptView);

        // Get reference to EditText
        pinET = promptView.findViewById(R.id.pinET);

        // Set dialog window.
        builder.setMessage("Streeplijst legen?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        // Check if PIN has been set yey.
                        StreepDatabase db = StreepDatabase.getInstance(getApplicationContext());
                        Cursor pinCursor = db.getPin();
                        if (pinCursor != null & pinCursor.moveToFirst()) {

                            int PIN = pinCursor.getInt(0);

                            try {

                                int enteredPin = Integer.parseInt(pinET.getText().toString());

                                // If entered PIN matches
                                if (enteredPin == PIN) {

                                    /* Empty transactions & portfolio table & reset users costs to
                                    0.
                                    */
                                    db = StreepDatabase.getInstance(getApplicationContext());
                                    db.emptyDB();

                                    // Custom Toast: "Streeplijst" empty.
                                    Toast toast = new Toast(getApplicationContext());
                                    View customToastView = getLayoutInflater().inflate(
                                            R.layout.activity_toast_custom_simple, null);
                                    TextView toastTV = (TextView) customToastView.findViewById(
                                            R.id.toastText);
                                    toastTV.setText("Streeplijst geleegd.");
                                    toast.setView(customToastView);
                                    toast.setDuration(Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0,0);
                                    toast.show();
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

                                    dialog.cancel();
                                }
                            }
                            catch (Exception e) {

                                e.printStackTrace();

                                // Custom Toast: No pin entered.
                                Toast toast = new Toast(getApplicationContext());
                                View customToastView = getLayoutInflater().inflate(
                                        R.layout.activity_toast_custom_simple, null);
                                TextView toastTV = (TextView) customToastView.findViewById(
                                        R.id.toastText);
                                toastTV.setText("Geen PIN ingevoerd.");
                                toast.setView(customToastView);
                                toast.setDuration(Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0,0);
                                toast.show();

                                return;
                            }
                        }
                        else {

                            // Custom Toast: No PIN set.
                            Toast toast = new Toast(getApplicationContext());
                            View customToastView = getLayoutInflater().inflate(
                                    R.layout.activity_toast_custom_simple, null);
                            TextView toastTV = (TextView) customToastView.findViewById(
                                    R.id.toastText);
                            toastTV.setText("Nog geen PIN ingesteld.");
                            toast.setView(customToastView);
                            toast.setDuration(Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0,0);
                            toast.show();

                            return;
                        }
                    }
                })

                // Cancel AlertDialog when user clicks cancel.
                .setNegativeButton("Annuleren", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // Create dialog box and show.
        AlertDialog alert = builder.create();
        alert.show();
    }

    // Go to MailActivity.
    public void mailClicked(View view) {

        Intent intent = new Intent(ExportActivity.this, MailActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        /* Return to main activity (ProductsActivity) when home button is pressed and finish
        current activity.
         */
        Intent intent = new Intent(ExportActivity.this, ProductsActivity.class);
        startActivity(intent);
        finish();

        return true;
    }
}
