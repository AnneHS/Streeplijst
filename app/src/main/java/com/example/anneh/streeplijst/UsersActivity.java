package com.example.anneh.streeplijst;

import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class UsersActivity extends AppCompatActivity {

    private StreepDatabase db;
    Cursor usersCursor;
    private UserAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        // Enable home button
        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);

        // TODO:  home icon veranderen
        // actionbar.setNavigationIcon(R.drawable.home);

        // Get cursor for users table from StreepDatabase
        db = StreepDatabase.getInstance(getApplicationContext());
        usersCursor = db.selectUsers();

        // Set adapter to productGrid
        adapter = new UserAdapter(this, usersCursor);
        GridView userGrid = (GridView) findViewById(R.id.userGrid);
        userGrid.setAdapter(adapter);

        // Set listeners
        userGrid.setOnItemClickListener(new UsersActivity.GridViewClickListener());
        userGrid.setOnItemLongClickListener(new GridViewLongClickListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle actian bar item clicks --> go to corresponding activity
        int id = item.getItemId();

        if (id == R.id.overview) {
            Intent intent = new Intent(UsersActivity.this, OverviewActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.addProduct) {
            Intent intent = new Intent(UsersActivity.this, NewProductActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.addUser) {
            Intent intent = new Intent(UsersActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
        else if (id == android.R.id.home) {
            Intent intent = new Intent(UsersActivity.this, ProductsActivity.class);
            startActivity(intent);
        }

        return true;
    }

    // Streep
    private class GridViewClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Toast toast = Toast.makeText(getApplicationContext(), "Gestreept!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    // LongClick --> Go to profile
    private class GridViewLongClickListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            Intent intent = new Intent(UsersActivity.this, ProfileActivity.class);
            startActivity(intent);
            return true;
        }
    }

}
