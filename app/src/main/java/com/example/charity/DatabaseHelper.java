package com.example.charity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

class DbBitmapUtility {
    // convert from bitmap to byte array
    static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String databaseName = "register.db";
    private static final String tableName1 = "registeruser";
    private static final String tableName2 = "userinfo";
    private static final String col1 = "ID";
    public static final String col2 = "username";
    private static final String col3 = "email";
    public static final String col4 = "mobile";
    private static final String col5 = "password";

    public DatabaseHelper(Context context) {
        super(context, databaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE registeruser (ID INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, email TEXT, mobile INTEGER, password TEXT)");
        db.execSQL("CREATE TABLE userinfo (ID INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, image BLOB, category TEXT, thingname TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName1);
        db.execSQL("DROP TABLE IF EXISTS " + tableName2);
        onCreate(db);
    }

    long addUser(String username, String email, long mobile, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username",username);
        contentValues.put("email",email);
        contentValues.put("mobile",mobile);
        contentValues.put("password",password);
        long res = db.insert(tableName1,null, contentValues);
        db.close();
        return res;
    }

    long editUser(String username, long mobile, String password, String email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("mobile", mobile);
        long res;
        if(!password.equals("")) {
            contentValues.put("password", password);
            res = db.update(tableName2, contentValues, "email="+"'"+email+"'", null);
        }
        else {
            res = db.update(tableName1, contentValues, "email="+"'"+email+"'", null);
        }
        db.close();
        return res;
    }

    long addUserInfo(String email, Bitmap image, String category, String thingname){
        byte[] img = DbBitmapUtility.getBytes(image);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("image", img);
        contentValues.put("category", category);
        contentValues.put("thingname", thingname);
        long res = db.insert(tableName2,null, contentValues);
        db.close();
        return res;
    }

    ArrayList getUserData(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList s = new ArrayList();
        Cursor c = db.rawQuery("SELECT username, email FROM registeruser WHERE email = ?", new String[] {email});
        if(c.moveToFirst()) {
            do {
                s.add(c.getString(0));
                s.add(c.getString(1));
            }while (c.moveToNext());
        }
        c.close();
        db.close();
        return s;
    }

    ArrayList getUserInfo(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList s = new ArrayList();
        Cursor c = db.rawQuery("SELECT image, category, thingname FROM userinfo WHERE email = ?", new String[] {email});
        if(c.moveToFirst()) {
            do {
                Bitmap img = DbBitmapUtility.getImage(c.getBlob(0));
                s.add(img);
                s.add(c.getString(1));
                s.add(c.getString(2));
            }while (c.moveToNext());
        }
        c.close();
        db.close();
        return s;
    }

    ArrayList getAllInfo(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList s = new ArrayList();
        Cursor c = db.rawQuery("SELECT username, r.email, image, category, thingname FROM registeruser r, userinfo u", new String[] {});
        if(c.moveToFirst()) {
            do {
                s.add(c.getString(0));
                s.add(c.getString(1));
                Bitmap img = DbBitmapUtility.getImage(c.getBlob(2));
                s.add(img);
                s.add(c.getString(3));
                s.add(c.getString(4));
            }while (c.moveToNext());
        }
        c.close();
        db.close();
        return s;
    }

    boolean checkUser(String email, String password) {
        String[] columns = {col1};
        SQLiteDatabase db = getReadableDatabase();
        String selection = col3 + "=?" + " and " + col5 + "=?";
        String[] selectionArgs = {email, password};
        Cursor cursor = db.query(tableName1, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }
}
