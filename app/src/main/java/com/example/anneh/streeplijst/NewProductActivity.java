package com.example.anneh.streeplijst;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class NewProductActivity extends AppCompatActivity {

    StreepDatabase db;


    EditText nameET;
    EditText priceET;
    String productName;
    float productPrice;
    Button addBtn;
    AlertDialog.Builder builder;

    // https://stackoverflow.com/questions/27810945/select-video-from-gallery-cannot-find-symbol-result-load-image
    private static int RESULT_LOAD_IMAGE = 1;
    private static final int PICK_FROM_GALLERY = 1;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        // Enable home button in actionbar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get db.
        db = StreepDatabase.getInstance(getApplicationContext());

        // Get reference to EditTexts.
        nameET = (EditText) findViewById(R.id.name);
        priceET = (EditText) findViewById(R.id.price);

        // TODO: Ask for pin
        // Open AlertDialog when add button is clicked.
        // https://www.javatpoint.com/android-alert-dialog-example
        addBtn = (Button) findViewById(R.id.addProduct);
        builder = new AlertDialog.Builder(this);
        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                // Get product name if entered, else return.
                if (!nameET.getText().toString().equals("") && nameET.getText().toString().length() > 0) {
                    productName = nameET.getText().toString();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Geef productnaam", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Get price if entered, else return.
                if(!nameET.getText().toString().equals("") && nameET.getText().toString().length() > 0) {
                    productPrice = Float.valueOf(priceET.getText().toString());
                }
                else {
                    Toast.makeText(getApplicationContext(), "Geef prijs", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Return if no image uploaded.
                if(bitmap == null) {
                    Toast.makeText(getApplicationContext(), "Upload afbeelding", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Ask for confirmation.
                builder.setMessage("Product toevoegen?")
                        .setCancelable(false)
                        .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                // Save bitmap to internal storage.
                                // https://android--code.blogspot.com/2015/09/android-how-to-save-image-to-internal.html

                                // Make image name.
                                String imgName = productName + ".jpg";

                                // Create a file to save the image.
                                ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
                                File directory = wrapper.getDir("Product_Images", MODE_PRIVATE);
                                File mypath = new File(directory, imgName);

                                try {

                                    // Create OutputStream to write bytes to the file.
                                    OutputStream stream = null;
                                    stream = new FileOutputStream(mypath);

                                    // Write compressed version of the bitmap to the OutputStream.
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                                    stream.flush();
                                    stream.close();

                                    // Path
                                    String imgPath = directory.getAbsolutePath();

                                    // Add product to products table (StreepDatabase)
                                    Product newProduct = new Product(productName, productPrice, imgPath, imgName);
                                    db.insertProduct(newProduct);

                                    // Confirm with toast.
                                    Toast toast = Toast.makeText(getApplicationContext(), "Product toegevoegd", Toast.LENGTH_SHORT);
                                    toast.show();

                                    // Return to ProductsActivity.
                                    Intent intent = new Intent(NewProductActivity.this, ProductsActivity.class);
                                    startActivity(intent);

                                }catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        })

                        // Cancel if user does not want to add the product.
                        .setNegativeButton("Nee", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }) ;

                // Creating dialog box and show.
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }


    // Open Gallery when upload Button clicked.
    // https://stackoverflow.com/questions/39866869/how-to-ask-permission-to-access-gallery-on-android-m/39866945
    public void uploadClicked (View view) {


        try {
            // Ask for permission if not yet given.
            if (ActivityCompat.checkSelfPermission(NewProductActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(NewProductActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
            } else {

                // Intent to open Image Gallery.
                Intent uploadIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                // Open Gallery.
                startActivityForResult(uploadIntent, RESULT_LOAD_IMAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Request -->
    // https://stackoverflow.com/questions/39866869/how-to-ask-permission-to-access-gallery-on-android-m/39866945
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case PICK_FROM_GALLERY:

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Geef toestemming", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
        }
    }

    // Get bitmap for selected image.
    // http://viralpatel.net/blogs/pick-image-from-galary-android-app/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor imageCursor = getContentResolver().query(selectedImage, filePathColumn,
                    null, null, null);

            if (imageCursor.moveToFirst()) {

                // Get bitmap.
                int columnIndex = imageCursor.getColumnIndex(filePathColumn[0]);
                String picturePath = imageCursor.getString(columnIndex);
                imageCursor.close();
                bitmap = BitmapFactory.decodeFile(picturePath);

                ImageView upload = (ImageView) findViewById(R.id.uploadPic);
                upload.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks: go to corresponding activity.
        int id = item.getItemId();
        if (id == R.id.overview) {
            Intent intent = new Intent(NewProductActivity.this, OverviewActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.addProduct) {
            Intent intent = new Intent(NewProductActivity.this, NewProductActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.addUser) {
            Intent intent = new Intent(NewProductActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
        else if (id == android.R.id.home) {
            Intent intent = new Intent(NewProductActivity.this, ProductsActivity.class);
            startActivity(intent);
        }

        return true;
    }
}
