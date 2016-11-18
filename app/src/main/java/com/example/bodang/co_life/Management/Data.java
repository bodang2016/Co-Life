package com.example.bodang.co_life.Management;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Bodang on 18/11/2016.
 */

public class Data {
    public static Client client;


    public static void insertData(SQLiteDatabase db, ContentValues v) {
        db.beginTransaction();
        try {
            db.insert("localDatabase_info", "_id", v);
            db.setTransactionSuccessful();
            System.out.println("insert successful");
        } finally {
            db.endTransaction();
        }

    }

    public static void insertReview(SQLiteDatabase db, ContentValues v) {
        db.beginTransaction();
        try {
            db.insert("reviewDatabase_info", "_id", v);
            db.setTransactionSuccessful();
            System.out.println("insert successful");
        } finally {
            db.endTransaction();
        }

    }

    public static void deleteData(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            db.delete("localDatabase_info", null, null);
            db.setTransactionSuccessful();
            System.out.println("delete successful");
        } finally {
            db.endTransaction();
        }
    }

    public static void deleteReview(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            db.delete("reviewDatabase_info", null, null);
            db.setTransactionSuccessful();
            System.out.println("delete successful");
        } finally {
            db.endTransaction();
        }
    }

    public static String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
