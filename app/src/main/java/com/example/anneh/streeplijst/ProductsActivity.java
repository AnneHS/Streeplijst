package com.example.anneh.streeplijst;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

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

        // Set navigation drawer.
        // https://medium.com/quick-code/android-navigation-drawer-e80f7fc2594f
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

        // Set listeners for producrGrid.
        productGrid.setOnItemClickListener(new GridViewClickListener());
        productGrid.setOnItemLongClickListener(new ProductsActivity.GridViewLongClickListener());
    }


    private class NavigationViewClickListener implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            // Handle action bar item clicka: go to corresponding activity.
            int id = item.getItemId();

            if (id == R.id.overview) {
                Intent intent = new Intent(ProductsActivity.this, OverviewActivity.class);
                startActivity(intent);
            }
            else if (id == R.id.addProduct) {
                Intent intent = new Intent(ProductsActivity.this, NewProductActivity.class);
                startActivity(intent);
            }
            else if (id == R.id.addUser) {
                Intent intent = new Intent(ProductsActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
            else if (id == R.id.export) {
                Intent intent = new Intent(ProductsActivity.this, ExportActivity.class);
                startActivity(intent);
            }
            else if (id == R.id.pin) {
                Intent intent = new Intent(ProductsActivity.this, PinActivity.class);
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



    // TODO: Sorteer producten op meest gestreept.
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
}
