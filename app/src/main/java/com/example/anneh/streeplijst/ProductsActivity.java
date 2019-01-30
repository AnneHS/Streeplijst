/*
Anne Hoogerduijn Strating
12441163

Activity that shows a GridView of the available products. They can be selected and then passed to
the users activity.
 */

package com.example.anneh.streeplijst;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class ProductsActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private StreepDatabase db;
    Cursor productsCursor;
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        /* Set navigation drawer.
        https://medium.com/quick-code/android-navigation-drawer-e80f7fc2594f
         */
        drawer = (DrawerLayout)findViewById(R.id.activity_products);
        toggle = new ActionBarDrawerToggle(this, drawer, R.string.Open, R.string.Close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set listener for Navigation Drawer.
        navigationView = (NavigationView)findViewById(R.id.nv);
        navigationView.setNavigationItemSelectedListener(new NavigationViewClickListener());

        // Get cursor for products table.
        db = StreepDatabase.getInstance(getApplicationContext());
        productsCursor = db.selectProducts();

        // Set adapter to productGrid.
        adapter = new ProductAdapter(this, productsCursor);
        GridView productGrid = (GridView) findViewById(R.id.productGrid);
        productGrid.setAdapter(adapter);

        // Set listeners for productGrid.
        productGrid.setOnItemClickListener(new GridViewClickListener());
        productGrid.setOnItemLongClickListener(new ProductsActivity.GridViewLongClickListener());
    }


    private class NavigationViewClickListener implements
            NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Class nextActivity = null;

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
                Intent intent = new Intent(ProductsActivity.this, nextActivity);
                startActivity(intent);
            }

            // Close drawer without animation.
            drawer.closeDrawer(Gravity.START, false);

            return true;
        }
    }

    /* Open phone directory with the "Streeplijst" Csv files.
    https://stackoverflow.com/questions/17165972/android-how-to-open-a-specific-folder-via-intent-and-show-its-content-in-a-file
     */
    public void openCSVFolder() {

        // Create URI for CSV-folder path.
        Uri selectedUri = Uri.parse(Environment.getExternalStorageDirectory() +
                "/Streeplijst/");

        // Open folder (file explorer needed).
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(selectedUri, "resource/folder");
        if (intent.resolveActivityInfo(getPackageManager(), 0) != null) {
            startActivity(intent);
        }
        else {

            // Custom Toast: Install file explorer to show Csv files.
            Toast toast = new Toast(getApplicationContext());
            View customToastView = getLayoutInflater().inflate(
                    R.layout.activity_toast_custom_simple, null);
            TextView toastTV = (TextView) customToastView.findViewById(
                    R.id.toastText);
            toastTV.setText("Installeer een file explorer om Csv bestanden te bekijken.");
            toast.setView(customToastView);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0,0);
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

    // Go to UsersActivity when product is clicked
    private class GridViewClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            // Change color
            LinearLayout product = view.findViewById(R.id.productLL);
            product.setBackgroundResource(R.color.colorPrimaryDark);

            // Pass product id, name, price to UsersActivity.
            Intent intent = new Intent(ProductsActivity.this, UsersActivity.class);
            Cursor clickedProduct = (Cursor) parent.getItemAtPosition(position);
            intent.putExtra("product_id",
                    clickedProduct.getInt(clickedProduct.getColumnIndex("_id")));
            intent.putExtra("product_name",
                    clickedProduct.getString(clickedProduct.getColumnIndex("name")));
            intent.putExtra("product_price",
                    clickedProduct.getFloat(clickedProduct.getColumnIndex("price")));
            startActivity(intent);
        }
    }

    // Long Click product: Go to product page (ProductActivity).
    private class GridViewLongClickListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            // Change color
            LinearLayout product = view.findViewById(R.id.productLL);
            product.setBackgroundResource(R.color.colorPrimaryDark);

            // Get selected product info and pass to ProductActivity
            Intent intent = new Intent(ProductsActivity.this, ProductActivity.class);
            Cursor clickedProduct = (Cursor) parent.getItemAtPosition(position);
            intent.putExtra("product_id",
                    clickedProduct.getInt(clickedProduct.getColumnIndex("_id")));
            intent.putExtra("product_name",
                    clickedProduct.getString(clickedProduct.getColumnIndex("name")));
            intent.putExtra("product_price",
                    clickedProduct.getFloat(clickedProduct.getColumnIndex("price")));
            intent.putExtra("product_strepen",
                    clickedProduct.getInt(clickedProduct.getColumnIndex("strepen")));
            intent.putExtra("product_total",
                    clickedProduct.getFloat(clickedProduct.getColumnIndex("total")));
            intent.putExtra("img_path",
                    clickedProduct.getString(clickedProduct.getColumnIndex("imgPath")));
            intent.putExtra("img_name",
                    clickedProduct.getString(clickedProduct.getColumnIndex("imgName")));
            startActivity(intent);
            return true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}
