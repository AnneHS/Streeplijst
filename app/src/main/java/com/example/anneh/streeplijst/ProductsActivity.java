package com.example.anneh.streeplijst;

import android.content.Intent;
import android.database.Cursor;
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

    private StreepDatabase db;
    Cursor productsCursor;
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        // Get cursor for products table from StreepDatabase
        db = StreepDatabase.getInstance(getApplicationContext());
        productsCursor = db.selectProducts();

        // Set adapter to productGrid
        adapter = new ProductAdapter(this, productsCursor);
        GridView productGrid = (GridView) findViewById(R.id.productGrid);
        productGrid.setAdapter(adapter);

        // set listener
        productGrid.setOnItemClickListener(new GridViewClickListener());
        productGrid.setOnItemLongClickListener(new ProductsActivity.GridViewLongClickListener());
    }


    // TODO: zorgen dat dit niet elke activiteit opnieuw moet
    // https://www.youtube.com/watch?v=o4WeEitmF9E
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds itemts to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle actian bar item clicks --> go to corresponding activity
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

        return true;
    }


    // Go to UserActivity when product is clicked
    private class GridViewClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            // Change color
            LinearLayout product = view.findViewById(R.id.productLL);
            product.setBackgroundResource(R.color.colorPrimaryDark);

            // Pass product id, name and price to UsersActivity
            Intent intent = new Intent(ProductsActivity.this, UsersActivity.class);
            Cursor clickedProduct = (Cursor) parent.getItemAtPosition(position);
            intent.putExtra("product_id", clickedProduct.getInt(clickedProduct.getColumnIndex("_id")));
            intent.putExtra("product_name", clickedProduct.getString(clickedProduct.getColumnIndex("name")));
            intent.putExtra("product_price", clickedProduct.getFloat(clickedProduct.getColumnIndex("price")));
            startActivity(intent);
        }
    }

    // Go to product (profile) page (ProductActivity)
    private class GridViewLongClickListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            // Get selected product name & price and pass to ProductActivity
            Intent intent = new Intent(ProductsActivity.this, ProductActivity.class);
            Cursor clickedProduct = (Cursor) parent.getItemAtPosition(position);
            intent.putExtra("product_id", clickedProduct.getInt(clickedProduct.getColumnIndex("_id")));
            intent.putExtra("product_name", clickedProduct.getString(clickedProduct.getColumnIndex("name")));
            intent.putExtra("product_price", clickedProduct.getFloat(clickedProduct.getColumnIndex("price")));
            startActivity(intent);
            return true;
        }
    }
}
