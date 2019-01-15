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
        String createTransactions = "CREATE TABLE transactions(_id INTEGER PRIMARY KEY, " +
                "userID INTEGER, username TEXT, productName TEXT, productPrice REAL, amount INTEGER," +
                " total REAL, gestreept BOOLEAN, timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(createTransactions);

        // TODO: portfolio (zie finance)


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

    // Return cursor for product with given id
    public Cursor selectProduct(int id) {

        String productID = Integer.toString(id);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor productCursor = db.rawQuery("SELECT * FROM products WHERE _id = ?", new String[] {productID});
        return productCursor;

    }

    // Get cursor for users table
    public Cursor selectUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor usersCursor = db.rawQuery("SELECT * FROM users", null);
        return usersCursor;
    }
    // TODO: transactionsCursor


    // Insert transaction into transactions table
    public void insertTransaction(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("userID", transaction.getUserID());
        cv.put("username", transaction.getUsername());
        cv.put("productName", transaction.getProductName());
        cv.put("productPrice", transaction.getPrice());
        cv.put("amount", transaction.getAmount());
        cv.put("total", transaction.getTotal());
        cv.put("gestreept", true);

        db.insert("transactions", null, cv);
    }


    // Update costs in users table
    public void streep(int userId, float total) {
        //TODO: op handigere manier

        SQLiteDatabase db = this.getWritableDatabase();

        String userID = Integer.toString(userId);

        Cursor usernameCursor = db.rawQuery("SELECT costs FROM users WHERE _id = ?", new String[] {userID});
        float formerCosts = usernameCursor.getFloat(usernameCursor.getColumnIndex("costs"));
        float updatedCosts = formerCosts + total;

        ContentValues cv = new ContentValues();
        cv.put("costs", updatedCosts);

        db.update("users", cv, "id = ?", new String[] {userID});
    }


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

    // Get username for given id
    public String getUsername(int userId){

        SQLiteDatabase db = this.getWritableDatabase();
        String userID = Integer.toString(userId);
        Cursor usernameCursor = db.rawQuery("SELECT username FROM users WHERE _id = ?", new String[] {userID});
        String username = usernameCursor.getString(usernameCursor.getColumnIndex("name"));
        return username;
    }

    // Remove product from products table
    public void removeProduct(int productId) {

        SQLiteDatabase db = this.getWritableDatabase();
        String productID = Integer.toString(productId);
        db.delete("products", "_id=?", new String[] {productID});
    }

    // Remove user from users table
    public void removeUser(int userId) {

        SQLiteDatabase db = this.getWritableDatabase();
        String userID = Integer.toString(userId);
        db.delete("users", "_id=?", new String[] {userID});

    }


}
