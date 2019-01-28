package com.example.anneh.streeplijst;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FilterQueryProvider;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserProfilesActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    private StreepDatabase db;
    Cursor usersCursor;
    private ProfileAdapter adapter;
    HashMap<Integer,Integer> selectedMap = new HashMap<Integer,Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profiles);

        // Set navigation drawer.
        // https://medium.com/quick-code/android-navigation-drawer-e80f7fc2594f
        drawer = (DrawerLayout)findViewById(R.id.activity_user_profiles);
        toggle = new ActionBarDrawerToggle(this, drawer, R.string.Open, R.string.Close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set listener for Navigation Drawer.
        navigationView = (NavigationView)findViewById(R.id.nv);
        navigationView.setNavigationItemSelectedListener(new UserProfilesActivity.NavigationViewClickListener());

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

        // Set UserAdapter to userGrid.
        adapter = new ProfileAdapter(this, usersCursor, selectedMap);
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
                            new String[] {"%" + constraint.toString() + "%"});
                }

                return userCursor;

            }
        });

        // Set listeners for userGrid.
        userGrid.setOnItemClickListener(new UserProfilesActivity.GridViewClickListener());
    }


    private class NavigationViewClickListener implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {


            // TODO: Case.
            // Handle action bar item clicks: go to corresponding activity.
            int id = item.getItemId();

            if (id == R.id.overview) {
                Intent intent = new Intent(UserProfilesActivity.this, OverviewActivity.class);
                startActivity(intent);
            }
            else if (id == R.id.addProduct) {
                Intent intent = new Intent(UserProfilesActivity.this, NewProductActivity.class);
                startActivity(intent);
            }
            else if (id == R.id.addUser) {
                Intent intent = new Intent(UserProfilesActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
            else if (id == R.id.export) {
                Intent intent = new Intent(UserProfilesActivity.this, ExportActivity.class);
                startActivity(intent);
            }
            else if (id == R.id.pin) {
                Intent intent = new Intent(UserProfilesActivity.this, PinActivity.class);
                startActivity(intent);
            }

            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(toggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

            // Change background color for clicked user.
            LinearLayout user = view.findViewById(R.id.userLL);
            user.setBackgroundResource(R.color.colorPrimaryDark);

            // Get selected username & ID and pass to ProfileActivity
            Intent intent = new Intent(UserProfilesActivity.this, ProfileActivity.class);
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
        }
    }
}
