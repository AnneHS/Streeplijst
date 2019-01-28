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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class ProfileAdapter extends ResourceCursorAdapter {

    private UserAdapter instance;
    HashMap<Integer,Integer> selectedMap;

    // Constructor
    public ProfileAdapter(Context context, Cursor cursor, HashMap<Integer,Integer> selected) {
        super(context, R.layout.profile, cursor);
        this.selectedMap = selected;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Change color
        LinearLayout product = view.findViewById(R.id.userLL);
        product.setBackgroundResource(R.color.colorPrimary);

        // Get reference to views from user.xml.
        TextView usernameTV = view.findViewById(R.id.user);
        ImageView userIV = view.findViewById(R.id.userImg);
        LinearLayout userLL = view.findViewById(R.id.userLL);

        // Get username from users table & set TextView.
        String userName = cursor.getString(cursor.getColumnIndex("name"));
        usernameTV.setText(userName);

        // Get name & path profile pic.
        String imgName = cursor.getString(cursor.getColumnIndex("imgName"));
        String imgPath = cursor.getString(cursor.getColumnIndex("imgPath"));

        // Load file into ImageView
        Picasso.get().load(new File(imgPath, imgName)).into(userIV);
    }
}
