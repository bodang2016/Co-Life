package com.example.bodang.co_life.Database;import android.content.Context;import android.database.sqlite.SQLiteDatabase;import android.database.sqlite.SQLiteOpenHelper;/** * Created by Bodang on 17/11/2016. */public class LocalDatabaseHelper extends SQLiteOpenHelper {    final String CREATE_TABLE_SQL = "create table if not exists localDatabase_info(_id integer primary key,"            + "name varchar,"            + "longitude varchar,"            + "latitude varchar,"            + "time varchar,"            + "locationName varchar,"            + "type int)";    final String CREATE_BLACKBOARD_NOTICE_TABLE_SQL = "create table if not exists localDatabase_blackboard(_id integer primary key,"            + "username varchar,"            + "content text,"            + "groupid integer,"            + "time varchar)";    public LocalDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {        super(context, name, factory, version);    }    @Override    public void onCreate(SQLiteDatabase db) {        db.execSQL(CREATE_TABLE_SQL);        db.execSQL(CREATE_BLACKBOARD_NOTICE_TABLE_SQL);    }    @Override    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {        System.out.println("--------onUpdate has been called--------"                + oldVersion + "---->" + newVersion);    }}