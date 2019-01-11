package com.example.anneh.streeplijst;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class NewProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);
    }

    // Add Product and confirm
    public void addProductClicked(View view) {

        Toast toast = Toast.makeText(getApplicationContext(), "Product toegevoegd!", Toast.LENGTH_SHORT);
        toast.show();
    }
}
