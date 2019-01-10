package com.example.anneh.streeplijst;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

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
        productGrid.setOnItemClickListener(new GridItemClickListener());
    }

    // Go to UserActivity when product is clicked
    private class GridItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            // Pass Product object to UserActivity
            // Product clickedProduct = (Product) parent.getItemAtPosition(position);
            Intent intent = new Intent(ProductsActivity.this, UsersActivity.class);
            startActivity(intent);
        }
    }
}
