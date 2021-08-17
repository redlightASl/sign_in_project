package cn.edu.dlut.mail.wuchen2020.signinapp.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyHelper extends SQLiteOpenHelper {
    public MyHelper(Context context) {
        super(context, "facedata.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String stu_table = "create table name_id (id text primary key, name text)"; // name-id
        String stu_table1 = "create table time_id(id text, time text)"; // time-id
        db.execSQL(stu_table);
        db.execSQL(stu_table1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public void Insert(SQLiteDatabase db, String table, String name, String id) { // db, table, name, id
        ContentValues cValue = new ContentValues();
        cValue.put("id", id);
        cValue.put("name", name);
        db.insert(table, null, cValue);
    }

    public void InsertTwo(SQLiteDatabase db, String table, String time, String id) { //db, table, time, id
        ContentValues cValue = new ContentValues();
        cValue.put("id", id);
        cValue.put("time", time);
        db.insert(table, null, cValue);
    }

    public void Delete(SQLiteDatabase db, String table, String message) {
        String sql = "delete from " + table + " where " + message;
        db.execSQL(sql);
    }
}
