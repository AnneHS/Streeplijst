package com.example.anneh.streeplijst;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class StreepDatabase extends SQLiteOpenHelper {

    private static StreepDatabase instance;
    SQLiteDatabase db;

    // Constructor
    private StreepDatabase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create products table (id, name, price)
        String createProducts = "CREATE TABLE products(_id INTEGER PRIMARY KEY, name TEXT, price REAL)";
        db.execSQL(createProducts);

        // Create users table (id, name, costs)
        String createUsers = "CREATE TABLE users(_id INTEGER PRIMARY KEY, name TEXT, costs REAL)";
        db.execSQL(createUsers);

        // Create transactions table


    }

    // If you make changes to the schema of your database (like adding columns), you need to force a call to onUpgrade().
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop and reload tables
        db.execSQL("DROP TABLE products");
        onCreate(db);

        db.execSQL("DROP TABLE users");
        onCreate(db);

//        db.execSQL("DROP TABLE transactions");
//        onCreate(db);
    }

    // Getter: Returns instance of StreepDatabase if it exists, else creates one
    public static StreepDatabase getInstance(Context context) {

        if (instance == null) {
            return instance = new StreepDatabase(context, "streep_db", null, 1);
        }
        else {
            return instance;
        }

    }

    // Get cursor for products table
    public Cursor selectProducts() {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor productsCursor = db.rawQuery("SELECT * FROM products", null);
        return productsCursor;


    }

    // Get cursor for users table
    public Cursor selectUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor usersCursor = db.rawQuery("SELECT * FROM users", null);
        return usersCursor;
    }
    // TODO: transactionsCursor

    // Insert product into products table
    public void insertProduct(Product product) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("name", product.getName());
        cv.put("price", product.getPrice());

        db.insert("products", null, cv);
    }

    // Insert user into users table
    public void insertUser(User user) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("name", user.getName());
        cv.put("costs", 0); // New user = 0 costs

        db.insert("users", null, cv);
    }


}
