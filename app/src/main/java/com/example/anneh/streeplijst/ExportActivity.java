package com.example.anneh.streeplijst;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class ExportActivity extends AppCompatActivity {

    String mailAddress;

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

        // Get e-mail address from db.
        StreepDatabase db = StreepDatabase.getInstance(getApplicationContext());
        mailAddress = db.getMail();

        // Display address if given.
        if (mailAddress != "") {
            TextView mailTV = findViewById(R.id.mail);
            mailTV.setText(mailAddress);
        }
        else {
            Toast toast = Toast.makeText(getApplicationContext(), "Geen mail gevonden", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    public void exportClicked(View view) {

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

        // WRITE CSV FILE users
        // https://stackoverflow.com/questions/14049323/android-program-to-convert-the-sqlite-database-to-excel
        File exportDir = new File(Environment.getExternalStorageDirectory(), "");

        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        File usersFile = new File(exportDir, "gebruikers.csv");

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
            File portfolioFile = new File(exportDir, "portfolio.csv");

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

            }
            catch(Exception sqlEx) {

                Log.e("Error: ", sqlEx.getMessage(), sqlEx);
                Toast toast = Toast.makeText(getApplicationContext(), "Niet gelukt!", Toast.LENGTH_SHORT);
                toast.show();
            }

        }
        catch(Exception sqlEx) {

            Log.e("Error: ", sqlEx.getMessage(), sqlEx);
            Toast toast = Toast.makeText(getApplicationContext(), "Niet gelukt!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    // Go to MailActivity
    public void mailClicked(View view) {

        Intent intent = new Intent(ExportActivity.this, MailActivity.class);
        startActivity(intent);
    }
}
