package com.example.anneh.streeplijst;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class UsersActivity extends AppCompatActivity {

    ArrayList<User> users = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        // Create users
        User vermee = new User("Vermee");
        users.add(vermee);
        User paard = new User("de Whâle");
        users.add(paard);
        User sjon = new User("Sjan");
        users.add(sjon);

        // Instantiate adapter for gridview
        UserAdapter adapter = new UserAdapter(this, R.layout.user, users);
        GridView userGrid = (GridView) findViewById(R.id.userGrid);
        userGrid.setAdapter(adapter);


        // Set listener
        userGrid.setOnItemClickListener(new UsersActivity.GridItemClickListener());
    }

    //
    private class GridItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Toast toast = Toast.makeText(getApplicationContext(), "Gestreept!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
