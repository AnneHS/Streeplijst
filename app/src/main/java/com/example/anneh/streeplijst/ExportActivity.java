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
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ajts.androidmads.library.SQLiteToExcel;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;

public class ExportActivity extends AppCompatActivity {

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

        // TODO: FileProvider i.p.v. URI & dit verwijderen
        // https://stackoverflow.com/questions/42251634/android-os-fileuriexposedexception-file-jpg-exposed-beyond-app-through-clipdata-item-geturi
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

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

        // WRITE CSV FILE
        // https://stackoverflow.com/questions/14049323/android-program-to-convert-the-sqlite-database-to-excel

        // Get db
        StreepDatabase db = StreepDatabase.getInstance(getApplicationContext());

        File exportDir = new File(Environment.getExternalStorageDirectory(), "");

        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "streeplijst.csv");

        try {
            file.createNewFile();
            CSVWriter writer = new CSVWriter(new FileWriter(file));
            SQLiteDatabase sqlDB = db.getReadableDatabase();
            Cursor usersCSV = db.selectUsers();
            writer.writeNext(usersCSV.getColumnNames());

            while (usersCSV.moveToNext()) {
                // 0: ID, 1: Name, 2: Costs
                String row[] = {usersCSV.getString(0), usersCSV.getString(1),
                        usersCSV.getString(2)};
                writer.writeNext(row);
            }
            writer.close();
            usersCSV.close();
            Toast toast = Toast.makeText(getApplicationContext(), "Gelukt!", Toast.LENGTH_SHORT);
            toast.show();


            // Send e-mail
            //  https://stackoverflow.com/questions/18415202/not-able-to-send-csv-file-with-email-in-android
            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("application/csv");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, "annehoogerduijn@gmail.com");
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Streeplijst");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Streeplijst in bijlage: ");
            Uri U = Uri.fromFile(file);
            emailIntent.putExtra(Intent.EXTRA_STREAM, U);
            startActivity(Intent.createChooser(emailIntent, "Send Mail"));

        }
        catch(Exception sqlEx) {
            Log.e("Error: ", sqlEx.getMessage(), sqlEx);
            Toast toast = Toast.makeText(getApplicationContext(), "Niet gelukt!", Toast.LENGTH_SHORT);
        }

    }
}
