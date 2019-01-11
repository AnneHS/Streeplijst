package com.example.anneh.streeplijst;

import android.content.Intent;
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

public class ProductsActivity extends AppCompatActivity {

    ArrayList<Product> products = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        Product bier = new Product("bier", 1);
        products.add(bier);
        Product fris = new Product("fris", 2);
        products.add(fris);
        Product wijn = new Product("wijn", 3);
        products.add(wijn);

        // Instantiate adapter for gridview
        ProductAdapter adapter = new ProductAdapter(this, R.layout.product, products);
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

        return true;
    }



    // Go to UserActivity when product is clicked
    private class GridViewClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            // Pass Product object to UserActivity
            // Product clickedProduct = (Product) parent.getItemAtPosition(position);
            Intent intent = new Intent(ProductsActivity.this, UsersActivity.class);
            startActivity(intent);
        }
    }

    // LongClick --> Product Profile
    private class GridViewLongClickListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            //TODO: toast met opties
            Intent intent = new Intent(ProductsActivity.this, ProductActivity.class);
            startActivity(intent);
            return true;
        }
    }
}
