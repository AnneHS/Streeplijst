package com.example.anneh.streeplijst;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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


    // Storage Permissions
    // https://stackoverflow.com/questions/16360763/permission-denied-when-creating-new-file-on-external-storage
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        // TODO: FileProvider i.p.v. URI & dit verwijderen (exposed beyond app through ClipData.Item.getUri())
        // https://stackoverflow.com/questions/42251634/android-os-fileuriexposedexception-file-jpg-exposed-beyond-app-through-clipdata-item-geturi
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        // Enable home button in actionbar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get e-mail address from db.
        StreepDatabase db = StreepDatabase.getInstance(getApplicationContext());
        mailAddress = db.getMail();

        // Display address if given.
        if (mailAddress != "") {
            TextView mailTV = findViewById(R.id.mailTV);
            mailTV.setText(mailAddress);
        }
        else {
            Toast toast = Toast.makeText(getApplicationContext(), "Geen mail gevonden", Toast.LENGTH_SHORT);
            toast.show();
        }

        emptyBtn = (Button) findViewById(R.id.emptyBtn);
        emptyBtn.setEnabled(false);

    }

    public void exportClicked(View view) {

        // Get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.prompts, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(promptView);

        // Get reference to EditText
        pinET = promptView.findViewById(R.id.pinET);

        // Set dialog window.
        builder.setMessage("Streeplijst exporteren?")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        // Check PIN
                        StreepDatabase db = StreepDatabase.getInstance(getApplicationContext());
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

                                    exportCSV();
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
                                Toast toast = Toast.makeText(getApplicationContext(), "Geen PIN ingevoerd", Toast.LENGTH_SHORT);
                                toast.show();
                                return;
                            }
                        }
                        else {

                            // Toast.
                            Toast toast = Toast.makeText(getApplicationContext(), "Nog geen PIN ingesteld", Toast.LENGTH_SHORT);
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

        //Create dialog box and show.
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void exportCSV() {

        // Prompt user for permission if not yet granted
        // https://stackoverflow.com/questions/16360763/permission-denied-when-creating-new-file-on-external-storage
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }

        // Get db
        StreepDatabase db = StreepDatabase.getInstance(getApplicationContext());

        // Create "Streeplijst" Directory in phone storage, if not yet exists.
        File exportDir = new File(Environment.getExternalStorageDirectory(), "Streeplijst");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        // Get current date
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String todayString = formatter.format(todayDate);

        // WRITE CSV FILE users
        // https://stackoverflow.com/questions/14049323/android-program-to-convert-the-sqlite-database-to-excel
        File usersFile = new File(exportDir, "gebruikers(" + todayString + ").csv");

        try {
            usersFile.createNewFile();
            CSVWriter writer = new CSVWriter(new FileWriter(usersFile));
            SQLiteDatabase sqlDB = db.getReadableDatabase();
            Cursor usersDB = db.selectUsers();
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
                sqlDB = db.getReadableDatabase();
                Cursor portfolioDB = db.selectPortfolios();
                writer.writeNext(portfolioDB.getColumnNames());

                while (portfolioDB.moveToNext()) {

                    // 0: userID, 1: ProductName, 2: ProductPrice, 3: amount, 4: total
                    String row[] = {portfolioDB.getString(0),
                            portfolioDB.getString(1), portfolioDB.getString(2),
                            portfolioDB.getString(3), portfolioDB.getString(4)};
                    writer.writeNext(row);
                }
                writer.close();
                portfolioDB.close();

                // Confirm
                Toast toast = Toast.makeText(getApplicationContext(), "Csv bestanden gemaakt!", Toast.LENGTH_SHORT);
                toast.show();

                // Open e-mail app to send csv file
                //  https://stackoverflow.com/questions/18415202/not-able-to-send-csv-file-with-email-in-android
                final Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);

                // Add all info needed.
                emailIntent.setType("application/csv");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]  {mailAddress}); // array met 1 emailadres //
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, new String[] {"Streeplijst"});
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Streeplijst in bijlage.");

                // Add both csv files as attachments.
                Uri U = Uri.fromFile(usersFile);
                Uri U2 = Uri.fromFile(portfolioFile);
                ArrayList<Uri> uris = new ArrayList<Uri>();
                uris.add(U);
                uris.add(U2);
                emailIntent.putExtra(Intent.EXTRA_STREAM, uris);

                // Open e-mail
                startActivity(Intent.createChooser(emailIntent, "Send Mail"));

                // Enable empty button.
                emptyBtn.setEnabled(true);

            }
            catch(Exception sqlEx) {

                //TODO: error in toast?
                Log.e("Error: ", sqlEx.getMessage(), sqlEx);
                Toast toast = Toast.makeText(getApplicationContext(), "Niet gelukt!", Toast.LENGTH_SHORT);
                toast.show();
            }

        }
        catch(Exception sqlEx) {

            // TODO: error in toast?
            Log.e("Error: ", sqlEx.getMessage(), sqlEx);
            Toast toast = Toast.makeText(getApplicationContext(), "Niet gelukt!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void emptyClicked(View view) {

        // Get prompts.xml view
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

                        // Check PIN
                        StreepDatabase db = StreepDatabase.getInstance(getApplicationContext());
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

                                    // Empty transactions & portfolio table + reset users costs to 0.
                                    db = StreepDatabase.getInstance(getApplicationContext());
                                    db.emptyDB();

                                    Toast toast = Toast.makeText(getApplicationContext(), "Streeplijst geleegd.", Toast.LENGTH_SHORT);
                                    toast.show();
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
                                Toast toast = Toast.makeText(getApplicationContext(), "Geen PIN ingevoerd", Toast.LENGTH_SHORT);
                                toast.show();
                                return;
                            }
                        }
                        else {

                            // Toast.
                            Toast toast = Toast.makeText(getApplicationContext(), "Nog geen PIN ingesteld", Toast.LENGTH_SHORT);
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

        //Create dialog box and show.
        AlertDialog alert = builder.create();
        alert.show();
    }

    // Go to MailActivity
    public void mailClicked(View view) {

        Intent intent = new Intent(ExportActivity.this, MailActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar clicks: return to ProductsActivity.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent intent = new Intent(ExportActivity.this, ProductsActivity.class);
            startActivity(intent);
        }

        return true;
    }
}
