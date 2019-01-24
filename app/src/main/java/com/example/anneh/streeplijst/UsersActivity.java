package com.example.anneh.streeplijst;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FilterQueryProvider;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UsersActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private StreepDatabase db;
    Cursor usersCursor;
    private UserAdapter adapter;
    HashMap<Integer,Integer> selectedMap = new HashMap<Integer,Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        // Enable home button
        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);

        // TODO:  home icon veranderen
        // actionbar.setNavigationIcon(R.drawable.home_icon);

        // Get cursor for users table from StreepDatabase.
        db = StreepDatabase.getInstance(getApplicationContext());
        usersCursor = db.selectUsers();

        // Get all user id's.
        // https://stackoverflow.com/questions/12481595/how-to-get-all-ids-from-a-sqlite-database
        ArrayList<Integer> users = new ArrayList<Integer>();
        if (usersCursor.moveToFirst()) {
           do {
               users.add(usersCursor.getInt(usersCursor.getColumnIndex("_id")));
           } while (usersCursor.moveToNext());
        }

        // Put user id's in selectedMap to keep track of click count.
        for (int i = 0; i < users.size(); i++) {
            int id = users.get(i);
            selectedMap.put(id, 0);
        }

        // Set UserAdapter to userGrid.
        adapter = new UserAdapter(this, usersCursor, selectedMap);
        GridView userGrid = (GridView) findViewById(R.id.userGrid);
        userGrid.setAdapter(adapter);

        // Change GridView/Adapter based on search query.
        // https://coderwall.com/p/zpwrsg/add-search-function-to-list-view-in-android
        userGrid.setTextFilterEnabled(true);
        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {

                // Show all users when search bar is empty or closed.
                Cursor userCursor;
                if (TextUtils.isEmpty(constraint)) {
                    userCursor = db.selectUsers();
                }

                // Else show users with searched name (+ "%" to search substring).
                else {
                    SQLiteDatabase SQLdb = db.getWritableDatabase();
                    userCursor = SQLdb.rawQuery("SELECT * FROM users WHERE name LIKE ?",
                            new String[] {constraint.toString() + "%"});
                }

                return userCursor;

            }
        });

        // Set listeners for userGrid.
        userGrid.setOnItemClickListener(new UsersActivity.GridViewClickListener());
        userGrid.setOnItemLongClickListener(new GridViewLongClickListener());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds a search menu to the action bar if it is present
        getMenuInflater().inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks: go to corresponding activity.
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

    // https://coderwall.com/p/zpwrsg/add-search-function-to-list-view-in-android
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        adapter.getFilter().filter(newText);
        adapter.notifyDataSetChanged();

        return true;
    }

    // Keep track of selected users.
    // https://stackoverflow.com/questions/18030384/get-listview-item-clicked-count-in-android
    private class GridViewClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            // Get userID of clicked user.
            Cursor clickedUser = (Cursor) parent.getItemAtPosition(position);
            int userID =  clickedUser.getInt(clickedUser.getColumnIndex("_id"));

            // Get current count for clicked user.
            int count = selectedMap.get(userID);

            // Update count.
            int updatedCount = count + 1;
            selectedMap.put(userID,updatedCount);

            // Display count.
            String amount = Integer.toString(updatedCount);
            TextView amountTV = (TextView) view.findViewById(R.id.amount);
            amountTV.setText(amount);

            // Change background color for clicked user.
            LinearLayout user = view.findViewById(R.id.userLL);
            user.setBackgroundResource(R.color.colorPrimaryDark);
        }
    }



    // LongClick: Go to user profile (ProfileActivity).
    private class GridViewLongClickListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            // Get selected username & ID and pass to ProfileActivity
            Intent intent = new Intent(UsersActivity.this, ProfileActivity.class);
            Cursor clickedUser = (Cursor) parent.getItemAtPosition(position);
            intent.putExtra("user_id",
                    clickedUser.getInt(clickedUser.getColumnIndex("_id")));
            intent.putExtra("user_name",
                    clickedUser.getString(clickedUser.getColumnIndex("name")));
            intent.putExtra("img_path",
                    clickedUser.getString(clickedUser.getColumnIndex("imgPath")));
            intent.putExtra("img_name",
                    clickedUser.getString(clickedUser.getColumnIndex("imgName")));
            startActivity(intent);
            return true;
        }
    }

    // Add when streepBtn clicked.
    public void streepClicked(View view) {

        // Get product id, name & price from intent.
        Intent intent = getIntent();
        int productID = (int) intent.getSerializableExtra("product_id");
        String productName = (String) intent.getSerializableExtra("product_name");
        float productPrice = (float) intent.getSerializableExtra("product_price");

        // Get userID('s) & selected count from selectedMap.
        for (Map.Entry<Integer, Integer> entry : selectedMap.entrySet()) {
            int userID = entry.getKey();
            int amount = entry.getValue();

            // If selected at least once, add transaction.
            if (amount > 0) {
                // Update tables for current userID.
                // https://stackoverflow.com/questions/10244222/android-database-cursorindexoutofboundsexception-index-0-requested-with-a-size
                Cursor userCursor = db.selectUser(userID);
                if (userCursor != null && userCursor.moveToFirst()) {

                    // Get username from db.
                    String username = userCursor.getString(userCursor.getColumnIndex("name"));
                    userCursor.close();

                    // Update transactions table.
                    Transaction transaction = new Transaction(userID, username, productName, productPrice, amount);
                    db.insertTransaction(transaction);

                    // Update portfolio table.
                    db.updatePortfolio(transaction);

                    // Update users table.
                    db.streep(userID, transaction.getTotal());
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Er gaat iets fout", Toast.LENGTH_SHORT);
                    toast.show();

                }
            }
        }

        // Confirm success with toast.
        Toast toast = Toast.makeText(getApplicationContext(), "Gestreept!", Toast.LENGTH_SHORT);
        toast.show();

        // Return to ProductsActivity.
        Intent productsIntent = new Intent(UsersActivity.this, ProductsActivity.class);
        startActivity(productsIntent);
    }
}
