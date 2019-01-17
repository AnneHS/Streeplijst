package com.example.anneh.streeplijst;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
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
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

    }

    public void exportClicked(View view) {


        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        // Ask for permission
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }


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

            while(usersCSV.moveToNext()) {
                // 0: ID, 1: Name, 2: Costs
                String row[] ={usersCSV.getString(0), usersCSV.getString(1),
                        usersCSV.getString(2)};
                writer.writeNext(row);
            }
            writer.close();
            usersCSV.close();
            Toast toast = Toast.makeText(getApplicationContext(), "Gelukt!", Toast.LENGTH_SHORT);
            toast.show();
        }
        catch(Exception sqlEx) {
            Log.e("Error: ", sqlEx.getMessage(), sqlEx);
            Toast toast = Toast.makeText(getApplicationContext(), "Niet gelukt!", Toast.LENGTH_SHORT);
        }

    }
}
