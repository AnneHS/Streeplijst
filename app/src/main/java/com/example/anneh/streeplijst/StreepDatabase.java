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

        // Create products table.
        String createProducts = "CREATE TABLE products(_id INTEGER PRIMARY KEY, name TEXT, " +
                "price REAL, imgPath TEXT, imgName TEXT)";
        db.execSQL(createProducts);

        // Create users table.
        String createUsers = "CREATE TABLE users(_id INTEGER PRIMARY KEY, name TEXT, " +
                "costs FLOAT NOT NULL, imgPath TEXT, imgName TEXT)";
        db.execSQL(createUsers);

        // Create transactions table.
        String createTransactions = "CREATE TABLE transactions(_id INTEGER PRIMARY KEY, " +
                "userID INTEGER, username TEXT, productName TEXT, productPrice REAL, amount INTEGER," +
                " total REAL, removed BOOLEAN DEFAULT 0, timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(createTransactions);

        // Create portfolio table.
        String createPortfolio = "CREATE TABLE portfolio(_id INTEGER PRIMARY KEY, userID INTEGER, productName text, " +
                "productPrice REAL, amount INTEGER, total REAL)";
        db.execSQL(createPortfolio);

        // innerjoin

        // Create e-mail table.
        String createMail = "CREATE TABLE mail(_id INTEGER PRIMARY KEY, address TEXT)";
        db.execSQL(createMail);

        String createPin = "CREATE TABLE pin(_id INTEGER PRIMARY KEY, pinNumber INTEGER)";
        db.execSQL(createPin);
    }

    // TODO: ???
    // If you make changes to the schema of your database (like adding columns), you need to force a call to onUpgrade().
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop and reload tables
        db.execSQL("DROP TABLE products");
        db.execSQL("DROP TABLE users");
        db.execSQL("DROP TABLE transactions");
        db.execSQL("DROP TABLE mail");
        onCreate(db);
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

    // Return cursor for products table.
    public Cursor selectProducts() {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor productsCursor = db.rawQuery("SELECT * FROM products", null);
        return productsCursor;
    }

    // Return cursor for product with given id.
    public Cursor selectProduct(int id) {

        String productID = Integer.toString(id);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor productCursor = db.rawQuery("SELECT * FROM products WHERE _id = ?", new String[] {productID});
        return productCursor;

    }

    // Return cursor for users table.
    public Cursor selectUsers() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor usersCursor = db.rawQuery("SELECT * FROM users ORDER BY name ASC",
                null);
        return usersCursor;
    }

    // Return cursor for transactions from user with given userID.
    public Cursor selectUserTransactions(int userId) {

        SQLiteDatabase db = this.getReadableDatabase();
        String userID = Integer.toString(userId);
        Cursor transactionCursor = db.rawQuery("SELECT * FROM transactions WHERE userID = ?" +
                        "ORDER BY _id DESC", new String[] {userID});

        return transactionCursor;
    }

    // Return cursor for user with given userID.
    public Cursor selectUser(int userId){

        SQLiteDatabase db = this.getWritableDatabase();
        String userID = Integer.toString(userId);
        Cursor userCursor = db.rawQuery("SELECT * FROM users WHERE _id = ?",
                new String[] {userID});

        return userCursor;
    }

    // Return total costs for user with given userID.
    public float getUserCosts(int userId) {

        float costs = 0;

        SQLiteDatabase db = this.getWritableDatabase();
        String userID = Integer.toString(userId);
        Cursor userCursor = db.rawQuery("SELECT costs FROM users WHERE _id = ?", new String[] {userID});

        if (userCursor != null & userCursor.moveToFirst()) {

            costs = userCursor.getFloat(0);
            return costs;
        }
        else {
            return costs;
        }
    }

    // Return cursor for transactions table.
    public Cursor selectTransactions(){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor transactionsCursor = db.rawQuery("SELECT * FROM transactions ORDER BY _id DESC;", null);
        return transactionsCursor;
    }

    // return cursor for portfolio table
    public Cursor selectPortfolios() {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor portfolioCursor = db.rawQuery("SELECT userID, productName, productPrice, amount," +
                "total FROM portfolio ORDER BY userID ASC", null);
        return portfolioCursor;
    }

    // Return cursor for portfolio from user with given userID.
    public Cursor selectPortfolio(int userId) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor portfolioCursor = db.rawQuery("SELECT * FROM portfolio WHERE userID = ?",
                new String[] {Integer.toString(userId)});

        return portfolioCursor;
    }

    // Insert transaction into transactions table.
    public void insertTransaction(Transaction transaction) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("userID", transaction.getUserID());
        cv.put("username", transaction.getUsername());
        cv.put("productName", transaction.getProductName());
        cv.put("productPrice", transaction.getPrice());
        cv.put("amount", transaction.getAmount());
        cv.put("total", transaction.getTotal());

        db.insert("transactions", null, cv);

    }

    // Update portfolio
    public void updatePortfolio(Transaction transaction) {

        SQLiteDatabase db = this.getWritableDatabase();

        // Get userID & productname.
        String userID = Integer.toString(transaction.getUserID());
        String productName = transaction.getProductName();

        // Get portfolio cursor for given userID & product.
        Cursor portfolioCursor = db.rawQuery("SELECT * FROM portfolio WHERE userID = ?" +
                "AND productName = ?", new String[] {userID, productName});

        // Update portfolio if product already in portfolio.
        if (portfolioCursor != null & portfolioCursor.moveToFirst()) {

            // Get former amount & total for given product.
            int formerAmount = portfolioCursor.getInt(4);
            float formerTotal = portfolioCursor.getFloat(5);

            // Calculate updated amount & total.
            int updatedAmount = formerAmount + transaction.getAmount();
            float updatedTotal = formerTotal + transaction.getTotal();

            // Update portfolio including productPrice to account for price changes.
            ContentValues cv = new ContentValues();
            cv.put("amount", updatedAmount);
            cv.put("total", updatedTotal);
            cv.put("productPrice", transaction.getPrice());

            db.update("portfolio", cv, "userID = ? AND productName =?",
                    new String[] {userID, productName});

        }

        // Else insert product in users portfolio.
        else {

            ContentValues cv = new ContentValues();
            cv.put("userID", transaction.getUserID());
            cv.put("productName", transaction.getProductName());
            cv.put("productPrice", transaction.getPrice());
            cv.put("amount", transaction.getAmount());
            cv.put("total", transaction.getTotal());
            db.insert("portfolio", null, cv);
        }
    }

    // Insert e-mail address into mail table.
    public void insertMail(String address) {

        // Get db & cursor for mail table.
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor mailCursor = db.rawQuery("SELECT address FROM mail WHERE _id = ?", new String[] {"1"});

        // Add mail address to ContentValues.
        ContentValues cv = new ContentValues();
        cv.put("address", address);

        // TODO: "1" ????
        // Insert e-mail address if not yet given, else overwrite current address.
        if (mailCursor == null || !mailCursor.moveToFirst()) {
            db.insert("mail", null, cv);
        }
        else {
            db.update("mail", cv, "_id = ?", new String[] {"1"});
        }
    }

    // Get mail address from db
    public String getMail() {

        // Get db & cursor for mail table.
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mailCursor = db.rawQuery("SELECT address FROM mail WHERE _id = ?", new String[] {"1"});

        //TODO: "" ????
        // Return address if given, else return "".
        String address = "";
        if (mailCursor != null & mailCursor.moveToFirst()) {

            address = mailCursor.getString(0);
            return address;
        }
        else {
            return address;
        }
    }

    // Insert pin into pin table.
    public void insertPin(Integer pin) {

        // Get db & cursor for mail table.
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor mailCursor = db.rawQuery("SELECT pinNumber FROM pin WHERE _id = ?", new String[] {"1"});

        // Add mail address to ContentValues.
        ContentValues cv = new ContentValues();
        cv.put("pinNumber", pin);

        // TODO: "1" ????
        // Insert e-mail address if not yet given, else overwrite current address.
        if (mailCursor == null || !mailCursor.moveToFirst()) {
            db.insert("pin", null, cv);
        }
        else {
            db.update("pin", cv, "_id = ?", new String[] {"1"});
        }
    }


    //
    public Cursor getPin() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor pinCursor = db.rawQuery("SELECT pinNumber FROM pin WHERE _id = ?", new String[] {"1"});
        return pinCursor;
    }

    // Get total costs from users table.
    // https://stackoverflow.com/questions/20582320/android-get-sum-of-database-column/20582538
    public float getTotalCosts() {
        float total = 0;

        // Get db & cursor for costs from users table.
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor costsCursor = db.rawQuery("SELECT SUM(costs) FROM users", null);

        // Return total if available, else return 0 (no entry yet).
        if (costsCursor.moveToFirst()) {
            return costsCursor.getFloat(0);
        }
        else {
            return total;
        }
    }

    // Update costs in users table.
    public void streep(int userId, float total) {
        //TODO: op handigere manier

        // Convert given id to string.
        String userID = Integer.toString(userId);

        // Get db & cursor for costs from users table.
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor costsCursor = db.rawQuery("SELECT costs FROM users WHERE _id = ?", new String[] {userID});

        // Update costs.
        // https://stackoverflow.com/questions/10244222/android-database-cursorindexoutofboundsexception-index-0-requested-with-a-size
        if (costsCursor != null & costsCursor.moveToFirst()) {

            // Add given total to former costs.
            float formerCosts = costsCursor.getFloat(costsCursor.getColumnIndex("costs"));
            float updatedCosts = formerCosts + total;

            // Update costs in users table.
            ContentValues cv = new ContentValues();
            cv.put("costs", updatedCosts);
            db.update("users", cv, "_id = ?", new String[] {userID});
        }
        else {
            System.out.println("Er gaat iets fout");
        }

    }


    // Insert product into products table.
    public void insertProduct(Product product) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("name", product.getName());
        cv.put("price", product.getPrice());
        cv.put("imgPath", product.getImgPath());
        cv.put("imgName", product.getImgName());

        db.insert("products", null, cv);
    }

    // Insert user into users table.
    public void insertUser(User user) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("name", user.getName());
        cv.put("costs", 0); // New user = 0 costs
        cv.put("imgPath", user.getImgPath());
        cv.put("imgName", user.getImgName());

        db.insert("users", null, cv);
    }

    // Remove product from products table.
    public void removeProduct(int productId) {

        SQLiteDatabase db = this.getWritableDatabase();
        String productID = Integer.toString(productId);
        db.delete("products", "_id=?", new String[] {productID});
    }

    // Remove transaction.
    public void removeTransaction(int transactionId) {

        // Get db.
        SQLiteDatabase db = this.getWritableDatabase();

        // Mark transaction as removed in transaction table.
        String transactionID = Integer.toString(transactionId);
        ContentValues cv = new ContentValues();
        cv.put("removed", true);
        db.update("transactions", cv, "_id = ?", new String[] {transactionID});

        // Adjust portfolio & users table.
        Cursor cursor = db.rawQuery("SELECT * FROM transactions WHERE _id = ?", new String[] {transactionID});
        if (cursor != null & cursor.moveToFirst()) {

            // Get transaction info (userID, productName, productPrice, amount, price).
            int userId = cursor.getInt(cursor.getColumnIndex("userID"));
            String userID = Integer.toString(userId);
            String productName = cursor.getString(cursor.getColumnIndex("productName"));
            int amount = cursor.getInt(cursor.getColumnIndex("amount"));
            float transactionTotal = cursor.getFloat(cursor.getColumnIndex("total"));

            // Update portfolio.
            Cursor portfolioCursor = db.rawQuery("SELECT * FROM portfolio WHERE userID = ? " +
                            "AND productName = ?", new String[] {userID, productName});
            if (portfolioCursor != null & portfolioCursor.moveToFirst()) {

                // Get former amount & total.
                int formerAmount = portfolioCursor.getInt(portfolioCursor.getColumnIndex("amount"));
                float formerTotal = portfolioCursor.getFloat(portfolioCursor.getColumnIndex("total"));

                // Calculate updated amount & total.
                int updatedAmount = formerAmount - amount;
                float updatedTotal = formerTotal - transactionTotal;

                // Update portfolio table.
                ContentValues portfolioCV = new ContentValues();
                portfolioCV.put("amount", updatedAmount);
                portfolioCV.put("total", updatedTotal);
                db.update("portfolio", portfolioCV, "userID = ? AND productName = ?",
                        new String[] {userID, productName});

            }

            // Update users table.
            Cursor userCursor = db.rawQuery("SELECT costs FROM users WHERE _id =?", new String[] {userID});
            if (userCursor != null & userCursor.moveToFirst()) {

                // Calculate updated costs for user.
                float formerCosts = userCursor.getFloat(userCursor.getColumnIndex("costs"));
                float updatedCosts = formerCosts - transactionTotal;

                // Update users table.
                ContentValues usersCV = new ContentValues();
                usersCV.put("costs", updatedCosts);
                db.update("users", usersCV, "_id = ?", new String[] {userID});
            }
        }
    }

    // Remove user from users table.
    public void removeUser(int userId) {

        SQLiteDatabase db = this.getWritableDatabase();
        String userID = Integer.toString(userId);
        db.delete("users", "_id=?", new String[] {userID});
    }


    // Reset/empty
    public void emptyDB() {

        // Reset costs in users table.
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("UPDATE users SET costs = ?", new String[] {"0"});

        // Drop & reload transactions table.
        db.execSQL("DROP TABLE transactions");
        String createTransactions = "CREATE TABLE transactions(_id INTEGER PRIMARY KEY, " +
                "userID INTEGER, username TEXT, productName TEXT, productPrice REAL, amount INTEGER," +
                " total REAL, removed BOOLEAN DEFAULT 0, timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(createTransactions);

        // Drop & Reload portfolio table.
        db.execSQL("DROP TABLE portfolio");
        String createPortfolio = "CREATE TABLE portfolio(_id INTEGER PRIMARY KEY, userID INTEGER, productName text, " +
                "productPrice REAL, amount INTEGER, total REAL)";
        db.execSQL(createPortfolio);
    }
}
