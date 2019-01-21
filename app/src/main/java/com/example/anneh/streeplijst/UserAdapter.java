package com.example.anneh.streeplijst;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ResourceCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class UserAdapter extends ResourceCursorAdapter {

    private UserAdapter instance;

    // Constructor
    public UserAdapter(Context context, Cursor cursor) {
        super(context, R.layout.user, cursor);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Get reference to views from user.xml.
        TextView userTV = view.findViewById(R.id.user);
        CustomImageView userIV = (CustomImageView) view.findViewById(R.id.userImg);

        // Get username from users table & set TextView.
        String userName = cursor.getString(cursor.getColumnIndex("name"));
        userTV.setText(userName);

        // Get name & path profile pic.
        String imgName = cursor.getString(cursor.getColumnIndex("imgName"));
        String imgPath = cursor.getString(cursor.getColumnIndex("imgPath"));

        // Load bitmap.
        Bitmap imgBitmap;
        FileInputStream fis;

        try {

            // Create new file for bitmap.
            File file = new File(imgPath, imgName);
            imgBitmap = BitmapFactory.decodeStream(new FileInputStream(file));

            // Set image.
            userIV.setImageBitmap(imgBitmap);
        }
        catch (FileNotFoundException e) {
            Log.d("Error: ", "file not found");
            e.printStackTrace();
        }
    }
}
