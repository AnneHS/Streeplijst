package com.example.anneh.streeplijst;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.widget.Toast;

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
        String createUsers = "CREATE TABLE users(_id INTEGER PRIMARY KEY, name TEXT, costs FLOAT NOT NULL)";
        db.execSQL(createUsers);

        // Create transactions table
        String createTransactions = "CREATE TABLE transactions(_id INTEGER PRIMARY KEY, " +
                "userID INTEGER, username TEXT, productName TEXT, productPrice REAL, amount INTEGER," +
                " total REAL, removed BOOLEAN DEFAULT 0, timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(createTransactions);

        // Create portfolio table
        String createPortfolio = "CREATE TABLE portfolio(_id INTEGER PRIMARY KEY, userID INTEGER, productName text, " +
                "productPrice REAL, amount INTEGER, total REAL)";
        db.execSQL(createPortfolio);

        // Create e-mail table
        String createMail = "CREATE TABLE mail(_id INTEGER PRIMARY KEY, address TEXT)";
        db.execSQL(createMail);

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

        db.execSQL("DROP TABLE transactions");
        onCreate(db);

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

    // Get cursor for transactions for given user ID
    public Cursor selectUserTransactions(int userId) {

        SQLiteDatabase db = this.getReadableDatabase();
        String userID = Integer.toString(userId);
        Cursor transactionCursor = db.rawQuery("SELECT * FROM transactions WHERE userID = ?" +
                        "ORDER BY _id DESC", new String[] {userID});

        return transactionCursor;
    }

    // Get cursor for given userId
    public Cursor selectUser(int userId){

        SQLiteDatabase db = this.getWritableDatabase();
        String userID = Integer.toString(userId);
        Cursor userCursor = db.rawQuery("SELECT * FROM users WHERE _id = ?", new String[] {userID});

        return userCursor;
    }

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

    // Get cursor for all transactions
    public Cursor selectTransactions(){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor transactionsCursor = db.rawQuery("SELECT * FROM transactions ORDER BY _id DESC;", null);
        return transactionsCursor;
    }

    // Get cursor for portfolio from given user ID.
    public Cursor selectPortfolio(int userId) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor portfolioCursor = db.rawQuery("SELECT * FROM portfolio WHERE userID = ?",
                new String[] {Integer.toString(userId)});

        return portfolioCursor;
    }

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

        db.insert("transactions", null, cv);

    }

    // Update portfolio
    public void updatePortfolio(Transaction transaction) {

        SQLiteDatabase db = this.getWritableDatabase();

        // Get userID & productname.
        String userID = Integer.toString(transaction.getUserID());
        String productName = transaction.getProductName();

        // Get cursor for given userID & product.
        Cursor portfolioCursor = db.rawQuery("SELECT * FROM portfolio WHERE userID = ?" +
                "AND productName = ?", new String[] {userID, productName});

        // Update if product in portfolio, else insert.
        if (portfolioCursor != null & portfolioCursor.moveToFirst()) {

            // Get former amount & total for given product.
            int formerAmount = portfolioCursor.getInt(3);
            float formerTotal = portfolioCursor.getFloat(4);

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
        else {

            // Insert product into portfolio
            ContentValues cv = new ContentValues();
            cv.put("userID", transaction.getUserID());
            cv.put("productName", transaction.getProductName());
            cv.put("productPrice", transaction.getPrice());
            cv.put("amount", transaction.getAmount());
            cv.put("total", transaction.getTotal());

            db.insert("portfolio", null, cv);
        }
    }

    // Insert e-mailaddress into mail table.
    public void insertMail(String address) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("address", address);

        Cursor mailCursor = db.rawQuery("SELECT address FROM mail WHERE _id = ?", new String[] {"1"});

        // TODO: "1" ????
        // Insert e-mail address if not yet given, else overwrite
        if (mailCursor == null || !mailCursor.moveToFirst()) {
            db.insert("mail", null, cv);
        }
        else {
            db.update("mail", cv, "_id = ?", new String[] {"1"});
        }
    }

    // Get mail address from db
    public String getMail() {


        SQLiteDatabase db = this.getReadableDatabase();

        Cursor mailCursor = db.rawQuery("SELECT address FROM mail WHERE _id = ?", new String[] {"1"});

        //TODO: "" ????
        String address = "";
        if (mailCursor != null & mailCursor.moveToFirst()) {

            address = mailCursor.getString(0);
            return address;
        }
        else {
            return address;
        }
    }


    // Get total costs from users table
    // https://stackoverflow.com/questions/20582320/android-get-sum-of-database-column/20582538
    public float getTotalCosts() {
        float total = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor costsCursor = db.rawQuery("SELECT SUM(costs) FROM users", null);

        if (costsCursor.moveToFirst()) {
            return costsCursor.getFloat(0);
        }
        else {
            return total;
        }
    }

    // Update costs in users table
    public void streep(int userId, float total) {
        //TODO: op handigere manier

        SQLiteDatabase db = this.getWritableDatabase();

        String userID = Integer.toString(userId);

        Cursor costsCursor = db.rawQuery("SELECT costs FROM users WHERE _id = ?", new String[] {userID});

        // TODO: wat doet dit?
        // https://stackoverflow.com/questions/10244222/android-database-cursorindexoutofboundsexception-index-0-requested-with-a-size
        if (costsCursor != null & costsCursor.moveToFirst()) {
            float formerCosts = costsCursor.getFloat(costsCursor.getColumnIndex("costs"));
            float updatedCosts = formerCosts + total;

            // Update users table.
            ContentValues cv = new ContentValues();
            cv.put("costs", updatedCosts);
            db.update("users", cv, "_id = ?", new String[] {userID});
        }
        else {
            System.out.println("Er gaat iets fout");
        }

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



    // Remove product from products table
    public void removeProduct(int productId) {

        SQLiteDatabase db = this.getWritableDatabase();
        String productID = Integer.toString(productId);
        db.delete("products", "_id=?", new String[] {productID});
    }

    public void removeTransaction(int transactionId) {

        SQLiteDatabase db = this.getWritableDatabase();
        String transactionID = Integer.toString(transactionId);

        // UPDATE TRANSACTIONS TABLE (removed = true)
        ContentValues cv = new ContentValues();
        cv.put("removed", true);
        db.update("transactions", cv, "_id = ?", new String[] {transactionID});


        // ADJUST PORTFOLIO & USERS TABLE
        Cursor cursor = db.rawQuery("SELECT * FROM transactions WHERE _id = ?", new String[] {transactionID});
        if (cursor != null & cursor.moveToFirst()) {

            // Get transaction info (userID, productName, productPrice, amount, price).
            int userId = cursor.getInt(cursor.getColumnIndex("userID"));
            String userID = Integer.toString(userId);
            String productName = cursor.getString(cursor.getColumnIndex("productName"));
            int amount = cursor.getInt(cursor.getColumnIndex("amount"));
            float transactionTotal = cursor.getFloat(cursor.getColumnIndex("total"));

            // UPDATE PORTFOLIO
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

            // UPDATE USERS TABLE
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

    // Remove user from users table
    public void removeUser(int userId) {

        SQLiteDatabase db = this.getWritableDatabase();
        String userID = Integer.toString(userId);
        db.delete("users", "_id=?", new String[] {userID});

    }


}
