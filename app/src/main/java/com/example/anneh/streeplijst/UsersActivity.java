/*
Anne Hoogerduijn Strating
12441163

Activity that shows a GridView of the users.
 */

package com.example.anneh.streeplijst;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FilterQueryProvider;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UsersActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    private StreepDatabase db;
    Cursor usersCursor;
    private UserAdapter adapter;
    HashMap<Integer,Integer> selectedMap = new HashMap<Integer,Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        /* Set navigation drawer.
        https://medium.com/quick-code/android-navigation-drawer-e80f7fc2594f
         */
        drawer = (DrawerLayout)findViewById(R.id.activity_users);
        toggle = new ActionBarDrawerToggle(this, drawer, R.string.Open, R.string.Close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set listener for Navigation Drawer.
        navigationView = (NavigationView)findViewById(R.id.nv);
        navigationView.setNavigationItemSelectedListener(
                new UsersActivity.NavigationViewClickListener());

        // Get cursor for users table from StreepDatabase.
        db = StreepDatabase.getInstance(getApplicationContext());
        usersCursor = db.selectUsers();

        /* Get all user id's.
        https://stackoverflow.com/questions/12481595/how-to-get-all-ids-from-a-sqlite-database
         */
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

        /* Change GridView/Adapter based on search query.
        https://coderwall.com/p/zpwrsg/add-search-function-to-list-view-in-android
         */
        userGrid.setTextFilterEnabled(true);
        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {

                // Show all users when search bar is empty or closed.
                Cursor userCursor;
                if (TextUtils.isEmpty(constraint)) {
                    userCursor = db.selectUsers();
                }

                // Else show users with searched name ("%" + "%" to search substring).
                else {
                    SQLiteDatabase SQLdb = db.getWritableDatabase();
                    userCursor = SQLdb.rawQuery("SELECT * FROM users WHERE name LIKE ?",
                            new String[] {"%" + constraint.toString() + "%"});
                }

                return userCursor;

            }
        });

        // Set listeners for userGrid.
        userGrid.setOnItemClickListener(new UsersActivity.GridViewClickListener());
        userGrid.setOnItemLongClickListener(new GridViewLongClickListener());
    }


    private class NavigationViewClickListener implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Class nextActivity = null;

            // Get next activity.
            switch(item.getItemId()) {
                case R.id.overview:     nextActivity = OverviewActivity.class;
                                        break;
                case R.id.users:        nextActivity = UserProfilesActivity.class;
                                        break;
                case R.id.addProduct:   nextActivity = NewProductActivity.class;
                                        break;
                case R.id.addUser:      nextActivity = RegisterActivity.class;
                                        break;
                case R.id.export:       nextActivity = ExportActivity.class;
                                        break;
                case R.id.pin:          nextActivity = PinActivity.class;
                                        break;
                case R.id.csv:          openCSVFolder();
                                        break;
            }

            if (nextActivity != null){

                // Go to next activity.
                Intent intent = new Intent(UsersActivity.this, nextActivity);
                startActivity(intent);
            }

            // Close drawer without animation.
            drawer.closeDrawer(Gravity.START, false);

            return true;
        }
    }

    public void openCSVFolder() {

        // Create URI for CSV-folder path.
        Uri selectedUri = Uri.parse(Environment.getExternalStorageDirectory() +
                "/Streeplijst/");

        /* Open folder (file explorer needed).
        https://stackoverflow.com/questions/17165972/android-how-to-open-a-specific-folder-via-intent-and-show-its-content-in-a-file
         */
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(selectedUri, "resource/folder");
        if (intent.resolveActivityInfo(getPackageManager(), 0) != null) {
            startActivity(intent);
        }
        else {

            // Display toast if user has not installed file explorer.
            Toast toast = Toast.makeText(getApplicationContext(), "Installeer een " +
                    "file explorer om verder te gaan.", Toast.LENGTH_SHORT);
            toast.show();
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

        // Inflate the menu; this adds a search menu to the action bar if it is present.
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
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    /* Notify UserAdapter when search function is used.
    https://coderwall.com/p/zpwrsg/add-search-function-to-list-view-in-android
     */
    @Override
    public boolean onQueryTextChange(String newText) {

        adapter.getFilter().filter(newText);
        adapter.notifyDataSetChanged();

        return true;
    }

    /* Keep track of selected users.
    https://stackoverflow.com/questions/18030384/get-listview-item-clicked-count-in-android
     */
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

    // LongClick: Open AlertDialog to change selected count.
    private class GridViewLongClickListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            // Get reference to LinearLayout
            final LinearLayout userLL = view.findViewById(R.id.userLL);

            // Get current count for clicked user.
            Cursor clickedUser = (Cursor) parent.getItemAtPosition(position);
            final int userID =  clickedUser.getInt(clickedUser.getColumnIndex("_id"));
            int currentCount = selectedMap.get(userID);

            /* Set values for NumberPicker
            https://stackoverflow.com/questions/40162539/display-a-numberpicker-on-an-alertdialog
             */
            final NumberPicker numberPicker = new NumberPicker(getApplicationContext());
            numberPicker.setMaxValue(100);
            numberPicker.setMinValue(0);
            numberPicker.setValue(currentCount);

            // AlertDialog with NumberPicker
            AlertDialog.Builder builder = new AlertDialog.Builder(UsersActivity.this)
                    .setTitle("Strepen")
                    .setMessage("Kies het aantal strepen: ")
                    .setView(numberPicker)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {

                           // Get updated count and save in selectedMap.
                           int updatedCount = numberPicker.getValue();
                           selectedMap.put(userID,updatedCount);

                           // Get reference to TextView.
                           TextView amountTV = (TextView) userLL.findViewById(R.id.amount);

                           // Display updated count.
                           if (updatedCount > 0) {
                               String amount = Integer.toString(updatedCount);
                               amountTV.setText(amount);
                           }

                           // Return background color to normal and TextView to "" if 0.
                           else {
                               amountTV.setText("");
                               userLL.setBackgroundResource(R.color.colorPrimary);
                           }

                       }
                    })
                    .setNegativeButton("Annuleren", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            // Create dialog box & show.
            AlertDialog alert = builder.create();
            alert.show();

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

                /* Update tables for current userID.
                https://stackoverflow.com/questions/10244222/android-database-cursorindexoutofboundsexception-index-0-requested-with-a-size
                 */
                Cursor userCursor = db.selectUser(userID);
                if (userCursor != null && userCursor.moveToFirst()) {

                    // Get username from db.
                    String username = userCursor.getString(userCursor.getColumnIndex("name"));
                    userCursor.close();

                    // Update transactions table.
                    Transaction transaction = new Transaction(userID, username, productID,
                            productName, productPrice, amount);
                    db.insertTransaction(transaction);

                    // Update portfolio table.
                    db.updatePortfolio(transaction);

                    // Update users & products table.
                    db.streep(userID, transaction.getTotal(), productID, amount);

                } else {

                    /* Custom Toast: something went wrong.
                    https://www.dev2qa.com/android-custom-toast-example/
                     */
                    Toast toast = new Toast(getApplicationContext());
                    View customToastView = getLayoutInflater().inflate(
                            R.layout.activity_toast_custom_simple, null);
                    TextView toastTV = (TextView) customToastView.findViewById(
                            R.id.toastText);
                    toastTV.setText("ERROR: er kon niet worden gestreept.");
                    toast.setView(customToastView);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0,0);
                    toast.show();
                }
            }
        }

        // Custom Toast: toast with image to confirm.
        Toast toast = new Toast(getApplicationContext());
        View customToastView = getLayoutInflater().inflate(R.layout.activity_toast_custom_view, null);
        toast.setView(customToastView);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0,0);
        toast.show();

        // Return to ProductsActivity.
        Intent productsIntent = new Intent(UsersActivity.this, ProductsActivity.class);
        startActivity(productsIntent);
        finish();
    }
}
