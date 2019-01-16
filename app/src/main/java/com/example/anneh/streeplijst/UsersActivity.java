package com.example.anneh.streeplijst;

import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UsersActivity extends AppCompatActivity {

    private StreepDatabase db;
    Cursor usersCursor;
    private UserAdapter adapter;
    Map<Integer,Integer> selectedMap = new HashMap<Integer,Integer>();

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

        // Get all user id's
        // https://stackoverflow.com/questions/12481595/how-to-get-all-ids-from-a-sqlite-database
        ArrayList<Integer> users = new ArrayList<Integer>();
        if (usersCursor.moveToFirst()) {
           do {
               users.add(usersCursor.getInt(usersCursor.getColumnIndex("_id")));
           } while (usersCursor.moveToNext());
        }

        // Initialize value to 0 for every userID = click count
        for (int i = 0; i < users.size(); i++) {
            int id = users.get(i);
            selectedMap.put(id, 0);
        }

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
    // https://stackoverflow.com/questions/18030384/get-listview-item-clicked-count-in-android
    private class GridViewClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            // Get user ID
            Cursor clickedUser = (Cursor) parent.getItemAtPosition(position);
            int userID =  clickedUser.getInt(clickedUser.getColumnIndex("_id"));

            // Default value for count
            int count = selectedMap.get(userID);

            // Display how many times user is selected
            int updatedCount = count + 1;
            String amount = Integer.toString(updatedCount);
            TextView amountTV = (TextView) view.findViewById(R.id.amount);
            amountTV.setText(amount);

            // Update count
            selectedMap.put(userID,(updatedCount));

        }
    }

    // LongClick --> Go to profile
    private class GridViewLongClickListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            // Get selected user name and pass to ProfileActivity
            Intent intent = new Intent(UsersActivity.this, ProfileActivity.class);
            Cursor clickedUser = (Cursor) parent.getItemAtPosition(position);
            intent.putExtra("user_id", clickedUser.getInt(clickedUser.getColumnIndex("_id")));
            intent.putExtra("user_name", clickedUser.getString(clickedUser.getColumnIndex("name")));
            startActivity(intent);
            return true;
        }
    }

    // Strepen
    public void streepClicked(View view) {

        // Get product id, name & price
        Intent intent = getIntent();
        int productID = (int) intent.getSerializableExtra("product_id");
        String productName = (String) intent.getSerializableExtra("product_name");
        float productPrice = (float) intent.getSerializableExtra("product_price");


        // For each key (userID) in selectedMap
        for (Map.Entry<Integer, Integer> entry : selectedMap.entrySet()) {
            int userID = entry.getKey();
            int amount = entry.getValue();

            // Check
            System.out.println(entry.getKey() + " = " + entry.getValue());

            // TODO: wat doet dit?
            // https://stackoverflow.com/questions/10244222/android-database-cursorindexoutofboundsexception-index-0-requested-with-a-size
            Cursor userCursor = db.selectUser(userID);
            if (userCursor != null && userCursor.moveToFirst()) {

                // Get username
                String username = userCursor.getString(userCursor.getColumnIndex("name"));
                userCursor.close();

                // Create transaction and insert into DB.
                Transaction transaction = new Transaction(userID, username, productName, productPrice, amount);
                db.insertTransaction(transaction);

                // Update user table.
                db.streep(userID, transaction.getTotal());
            }
            else {
                Toast toast = Toast.makeText(getApplicationContext(), "Er gaat iets fout", Toast.LENGTH_SHORT);
                toast.show();

            }

        }

        // reset background color
        Toast toast = Toast.makeText(getApplicationContext(), "Gestreept!", Toast.LENGTH_SHORT);
        toast.show();
    }
}
