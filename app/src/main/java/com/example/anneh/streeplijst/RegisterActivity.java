package com.example.anneh.streeplijst;

import android.Manifest;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class RegisterActivity extends AppCompatActivity {

    StreepDatabase db;

    EditText usernameET;
    String username;
    Button addBtn;
    AlertDialog.Builder builder;

    // https://stackoverflow.com/questions/27810945/select-video-from-gallery-cannot-find-symbol-result-load-image
    private static int RESULT_LOAD_IMAGE = 1;
    private static final int PICK_FROM_GALLERY = 1;
    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Enable home button in actionbar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get db.
        db = StreepDatabase.getInstance(getApplicationContext());

        // Get reference to EditText.
        usernameET = findViewById(R.id.username);

        // TODO: Ask for pin
        // Open AlertDialog when add button is clicked
        // https://www.javatpoint.com/android-alert-dialog-example
        addBtn = (Button) findViewById(R.id.addUser);
        builder = new AlertDialog.Builder(this);
        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                // Get username if entered, else cancel.
                if (!usernameET.getText().toString().equals("") && usernameET.getText().toString().length() > 0) {
                    username = usernameET.getText().toString();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Geef naam", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Return if no image uploaded.
                if(bitmap == null) {
                    Toast.makeText(getApplicationContext(), "Upload afbeelding", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Ask for confirmation.
                builder.setMessage("Gebruiker toevoegen?")
                        .setCancelable(false)

                        // Add user when confirmed.
                        .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                // Save bitmap to internal storage.
                                // https://android--code.blogspot.com/2015/09/android-how-to-save-image-to-internal.html

                                // Make image name.
                                String imgName = username + ".jpg";

                                // Create a file to save the image.
                                ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
                                File directory = wrapper.getDir("User_Images", MODE_PRIVATE);
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

                                    // Create user and add to users table (StreepDatabase)
                                    User newUser = new User(username, imgPath, imgName);
                                    db.insertUser(newUser);

                                    // Confirm with toast.
                                    Toast toast = Toast.makeText(getApplicationContext(), "Gebruiker toegevoegd", Toast.LENGTH_SHORT);
                                    toast.show();

                                    // Return to ProductsActivity.
                                    Intent intent = new Intent(RegisterActivity.this, ProductsActivity.class);
                                    startActivity(intent);

                                }catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        })

                        // Cancel if user does not want to add user.
                        .setNegativeButton("Nee", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }) ;

                // Create dialog box & show.
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    // Open Gallery when upload Button clicked.
    // https://stackoverflow.com/questions/39866869/how-to-ask-permission-to-access-gallery-on-android-m/39866945
    public void uploadClicked (View view) {


        try {

            // If permission to access Image Gallery not yet given, ask for permission.
            if (ActivityCompat.checkSelfPermission(RegisterActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(RegisterActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
            }

            // Else, open Image Gallery.
            else {

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


    // Request for permission result.
    // https://stackoverflow.com/questions/39866869/how-to-ask-permission-to-access-gallery-on-android-m/39866945
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case PICK_FROM_GALLERY:

                // Open Image Gallery if permission granted
                // (request cancelled -> result arrays are empty)
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
                }

                // Else display toast: permission needed.
                else {
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

                // Set ImageView to bitmap (uploaded image).
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
            Intent intent = new Intent(RegisterActivity.this, OverviewActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.addProduct) {
            Intent intent = new Intent(RegisterActivity.this, NewProductActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.addUser) {
            Intent intent = new Intent(RegisterActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
        else if (id == android.R.id.home) {
            Intent intent = new Intent(RegisterActivity.this, ProductsActivity.class);
            startActivity(intent);
        }

        return true;
    }

}
