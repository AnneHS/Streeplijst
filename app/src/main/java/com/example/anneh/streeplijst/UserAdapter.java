package com.example.anneh.streeplijst;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ResourceCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class UserAdapter extends ResourceCursorAdapter {

    private UserAdapter instance;

    // Constructor
    public UserAdapter(Context context, Cursor cursor) {
        super(context, R.layout.user, cursor);
    }

    // DB --> user.xml
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Get reference to views
        TextView userTV = view.findViewById(R.id.user);

        // Extract properties from cursor
        String userName = cursor.getString(cursor.getColumnIndex("name"));

        // Populate fields with extracted properties
        userTV.setText(userName);
    }

}
