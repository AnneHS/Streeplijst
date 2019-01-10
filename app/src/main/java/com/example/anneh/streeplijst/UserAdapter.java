package com.example.anneh.streeplijst;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class UserAdapter extends ArrayAdapter<User> {
    private ArrayList users;
    Product user;

    // Constructor
    public UserAdapter(@NonNull Context context, int resource, ArrayList<User> usersList) {
        super(context, resource, usersList);
        users = usersList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user, parent, false);
        }

        // Get reference to button
        TextView userTV = (TextView) convertView.findViewById(R.id.user);

        // Get product info for given position
        User user = (User) users.get(position);

        // Set text for button
        userTV.setText(user.getName());


        // Return view
        return convertView;
    }
}
