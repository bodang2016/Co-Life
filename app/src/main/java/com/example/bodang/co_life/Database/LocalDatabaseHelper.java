package com.example.bodang.co_life.Database;import android.content.Context;import android.database.sqlite.SQLiteDatabase;import android.database.sqlite.SQLiteOpenHelper;/** * */public class LocalDatabaseHelper extends SQLiteOpenHelper {    //instrument for creating the table storing information about other users    final String CREATE_TABLE_SQL = "create table if not exists localDatabase_info(_id integer primary key,"            + "name varchar,"            + "longitude varchar,"            + "latitude varchar,"            + "time varchar,"            + "locationName varchar,"            + "type int)";    //instrument for creating the table storing notices on blackboard    final String CREATE_BLACKBOARD_NOTICE_TABLE_SQL = "create table if not exists localDatabase_blackboard(_id integer primary key,"            + "username varchar,"            + "content text,"            + "groupid integer,"            + "time varchar)";    //instrument for creating the table storing requests/messages    final String CREATE_REQUEST_TABLE_SQL = "create table if not exists localDatabase_request(_id integer primary key,"            + "username varchar,"            + "requester varchar,"            + "content text,"            + "time varchar)";    public LocalDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {        super(context, name, factory, version);    }    @Override    public void onCreate(SQLiteDatabase db) {        //create the table storing information about other users        db.execSQL(CREATE_TABLE_SQL);        //create the table storing notices on blackboard        db.execSQL(CREATE_BLACKBOARD_NOTICE_TABLE_SQL);        //create the table storing requests/messages        db.execSQL(CREATE_REQUEST_TABLE_SQL);    }    @Override    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {        System.out.println("--------onUpdate has been called--------"                + oldVersion + "---->" + newVersion);    }}