package com.example.anneh.streeplijst;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    public void removeClicked(View view) {

        //TODO: pop-up met opties
        Toast toast = Toast.makeText(getApplicationContext(), "Verwijder gebruiker?", Toast.LENGTH_SHORT);
        toast.show();
    }
}
