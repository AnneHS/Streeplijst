package com.example.anneh.streeplijst;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class OptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
    }


    // Go to 'New product page' when "add product" is clicked
    public void addProductClicked(View view) {

        Intent intent = new Intent(OptionsActivity.this, NewProductActivity.class);
        startActivity(intent);
    }


    // Go to 'Register page' when "add user" is clicked
    public void addUserClicked(View view) {

        Intent intent = new Intent(OptionsActivity.this, RegisterActivity.class);
        startActivity(intent);
    }


}
