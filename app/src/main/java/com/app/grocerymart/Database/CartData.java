package com.app.grocerymart.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class CartData extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Cart.db";
    private static final String CART_TABLE_NAME = "cartItem";
    private static final String CART_ITEM_COLUMN_ID = "id";
    private static final String CART_ITEM_IMAGEPATH = "imagePath";
    private static final String CART_ITEM_TITLE = "title";
    private static final String CART_ITEM_DESCRIPTION = "description";
    private static final String CART_ITEM_AMOUNT = "amount"; // Count
    private static final String CART_ITEM_QUANTITY = "quantity";// Initial price for specific quantity
    private static final String CART_ITEM_PRICE = "price";
    private HashMap hp;

    public CartData(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table " + CART_TABLE_NAME +
                        "(" +
                        CART_ITEM_COLUMN_ID + " text primary key," +
                        CART_ITEM_IMAGEPATH + " text," +
                        CART_ITEM_TITLE + " text," +
                        CART_ITEM_DESCRIPTION + " text," +
                        CART_ITEM_QUANTITY + " text," +
                        CART_ITEM_PRICE + " text," +
                        CART_ITEM_AMOUNT + " text)" // count
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + CART_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertItem (String id, String imagepath, String title, String description, String quantity, String price, String amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CART_ITEM_COLUMN_ID, id);
        contentValues.put(CART_ITEM_IMAGEPATH, imagepath);
        contentValues.put(CART_ITEM_TITLE, title);
        contentValues.put(CART_ITEM_DESCRIPTION, description);
        contentValues.put(CART_ITEM_QUANTITY, quantity);
        contentValues.put(CART_ITEM_PRICE, price);
        contentValues.put(CART_ITEM_AMOUNT, amount);
        return db.insert(CART_TABLE_NAME, null, contentValues) != -1;
    }

    public Cursor getItem(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + CART_TABLE_NAME + " where id=?", new String[] { id } );
        res.moveToFirst();
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CART_TABLE_NAME);
        return numRows;
    }

    public boolean updateItem (String id, String imagepath, String title, String description, String quantity, String price, String amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CART_ITEM_IMAGEPATH, imagepath);
        contentValues.put(CART_ITEM_TITLE, title);
        contentValues.put(CART_ITEM_DESCRIPTION, description);
        contentValues.put(CART_ITEM_QUANTITY, quantity);
        contentValues.put(CART_ITEM_PRICE, price);
        contentValues.put(CART_ITEM_AMOUNT, amount);
        db.update(CART_TABLE_NAME, contentValues, "id = ? ", new String[] { id } );
        return true;
    }

    public Integer deleteItem (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(CART_TABLE_NAME,
                "id = ? ",
                new String[] { id });
    }

    public ArrayList<String> getAllItems() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + CART_TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(CART_ITEM_COLUMN_ID)));
            res.moveToNext();
        }
        return array_list;
    }

}
