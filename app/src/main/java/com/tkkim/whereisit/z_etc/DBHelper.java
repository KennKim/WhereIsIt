package com.tkkim.whereisit.z_etc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.tkkim.whereisit.add_location.data.MyLocation;
import com.tkkim.whereisit.add_stuff.data.Stuff;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by conscious on 2016-12-02.
 */

public class DBHelper extends SQLiteOpenHelper {

    private Context context;

    public static final String DB_NAME = "WhereIsIt";

    public static final String TABLE_mylocation = "mylocation";
    public static final String COL_loc_no = "loc_no";
    public static final String COL_loc_name = "loc_name";
    public static final String COL_loc_comment = "loc_comment";
    public static final String COL_loc_imgpath = "loc_imgpath";
    public static final String COL_loc_thumbpath = "loc_thumbpath";
    public static final String COL_loc_date = "loc_date";
    private static final String SQL_CREATE_TABLE_1 = "CREATE TABLE " + TABLE_mylocation + " (" + COL_loc_no + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_loc_name + " VARCHAR(22)," + COL_loc_comment + " VARCHAR(50)," + COL_loc_imgpath + " VARCHAR(50)," + COL_loc_thumbpath + " VARCHAR(50)," + COL_loc_date + " VARCHAR(22))";

    public static final String TABLE_stuff = "stuff";
    public static final String COL_stu_no = "stu_no";
    public static final String COL_stu_name = "stu_name";
    public static final String COL_stu_comment = "stu_comment";
    public static final String COL_stu_imgpath = "stu_imgpath";
    public static final String COL_stu_thumbpath = "stu_thumbpath";
    public static final String COL_stu_date = "stu_date";
    private static final String SQL_CREATE_TABLE_2 = "CREATE TABLE " + TABLE_stuff + " (" + COL_stu_no + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_loc_no + " INTEGER," + COL_stu_name + " VARCHAR(22)," + COL_stu_comment + " VARCHAR(50)," + COL_stu_imgpath + " VARCHAR(50)," + COL_stu_thumbpath + " VARCHAR(50)," + COL_stu_date + " VARCHAR(22))";

    public static final String TABLE_tag = "tag";
    public static final String COL_tag_no = "tag_no";
    public static final String COL_tag_name = "tag_name";
    private static final String SQL_CREATE_TABLE_3 = "CREATE TABLE " + TABLE_tag + " (" + COL_tag_no + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_stu_no + " INTEGER," + COL_tag_name + " VARCHAR(22))";

    public static final String TABLE_loc = "loc";
    private static final String SQL_CREATE_TABLE_4 = "CREATE TABLE " + TABLE_loc + " (" + COL_loc_no + " INTEGER, " + COL_stu_no + " INTEGER )";


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_1);
        db.execSQL(SQL_CREATE_TABLE_2);
        db.execSQL(SQL_CREATE_TABLE_3);
        db.execSQL(SQL_CREATE_TABLE_4);
        Toast.makeText(context, "Table 생성완료", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("TAG", "Upgrading db from version" + oldVersion + " to" +
                newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS tablename");
        onCreate(db);
        Toast.makeText(context, "버전 " + newVersion + "로 업그레이드.", Toast.LENGTH_SHORT).show();
    }

    public void addLoc(MyLocation data) {

        SQLiteDatabase db = getWritableDatabase();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_loc_name, data.getLoc_name());
        contentValues.put(COL_loc_comment, data.getLoc_comment());
        contentValues.put(COL_loc_imgpath, data.getLoc_imgpath());
        contentValues.put(COL_loc_date, dateFormat.format(date));
        db.insert(TABLE_mylocation, null, contentValues);
        db.close();
        Toast.makeText(context, "Insert 완료", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<MyLocation> getLocList(int start) {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM " + TABLE_mylocation + " ORDER BY " + COL_loc_no + " DESC LIMIT " + start + ", 10";

        Cursor c = db.rawQuery(sql, null);
        ArrayList<MyLocation> items = new ArrayList<MyLocation>();
        try {
            if (c.moveToFirst()) {
                do {
                    MyLocation myLocation = new MyLocation();
                    myLocation.setLoc_no(c.getInt(c.getColumnIndex(COL_loc_no)));
                    myLocation.setLoc_name(c.getString(c.getColumnIndex(COL_loc_name)));
                    myLocation.setLoc_comment(c.getString(c.getColumnIndex(COL_loc_comment)));
                    myLocation.setLoc_imgpath(c.getString(c.getColumnIndex(COL_loc_imgpath)));
                    myLocation.setLoc_thumbpath(c.getString(c.getColumnIndex(COL_loc_thumbpath)));
                    myLocation.setLoc_date(c.getString(c.getColumnIndex(COL_loc_date)));
                    items.add(myLocation);
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            c.close();
        }
        db.close();
        return items;
    }

    public ArrayList<MyLocation> getLocItem(String locNo) {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM " + TABLE_mylocation + " WHERE " + COL_loc_no + " = " + locNo;
//        String sql = "SELECT * FROM " + TABLE_mylocation + " WHERE " + COL_loc_no + " = " + locNo;
        Cursor c = db.rawQuery(sql, null);

        ArrayList<MyLocation> items = new ArrayList<MyLocation>();
        try {
            if (c.moveToFirst()) {
                do {
                    MyLocation data = new MyLocation();
                    data.setLoc_no(c.getInt(c.getColumnIndex(COL_loc_no)));
                    data.setLoc_name(c.getString(c.getColumnIndex(COL_loc_name)));
                    data.setLoc_comment(c.getString(c.getColumnIndex(COL_loc_comment)));
                    data.setLoc_imgpath(c.getString(c.getColumnIndex(COL_loc_imgpath)));
                    data.setLoc_thumbpath(c.getString(c.getColumnIndex(COL_loc_thumbpath)));
                    data.setLoc_date(c.getString(c.getColumnIndex(COL_loc_date)));
                    items.add(data);
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            c.close();
        }
        db.close();
        return items;
    }

    public void addStuff(Stuff data) {

        SQLiteDatabase db = getWritableDatabase();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_loc_no, data.getLoc_no());
        contentValues.put(COL_stu_name, data.getStu_name());
        contentValues.put(COL_stu_comment, data.getStu_comment());
        contentValues.put(COL_stu_imgpath, data.getStu_imgpath());
        contentValues.put(COL_stu_date, dateFormat.format(date));
        db.insert(TABLE_stuff, null, contentValues);
        db.close();
        Toast.makeText(context, "Stuff Insert 완료", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<Stuff> getStuList(String locNo) {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM " + TABLE_stuff + " WHERE " + COL_loc_no + " = " + locNo;
//        String sql = "SELECT * FROM " + TABLE_stuff;
//        String sql = "SELECT * FROM " + TABLE_mylocation + " WHERE " + COL_loc_no + " = " + locNo;
        Cursor c = db.rawQuery(sql, null);
        ArrayList<Stuff> items = new ArrayList<Stuff>();

        try {

            if (c.moveToFirst()) {
                do {
                    Stuff data = new Stuff();
                    data.setLoc_no(c.getString(c.getColumnIndex(COL_loc_no)));
                    data.setStu_name(c.getString(c.getColumnIndex(COL_stu_name)));
                    data.setStu_comment(c.getString(c.getColumnIndex(COL_stu_comment)));
                    data.setStu_imgpath(c.getString(c.getColumnIndex(COL_stu_imgpath)));
                    data.setStu_thumbpath(c.getString(c.getColumnIndex(COL_stu_thumbpath)));
                    data.setStu_date(c.getString(c.getColumnIndex(COL_stu_date)));
                    items.add(data);
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            c.close();
        }


        db.close();
        return items;
    }


    public void updateLoc(MyLocation data) {
        SQLiteDatabase db = getWritableDatabase();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_loc_no, data.getLoc_no());
        contentValues.put(COL_loc_no, data.getLoc_no());
        contentValues.put(COL_loc_name, data.getLoc_name());
        contentValues.put(COL_loc_comment, data.getLoc_comment());
        contentValues.put(COL_loc_imgpath, data.getLoc_imgpath());
        contentValues.put(COL_loc_date, dateFormat.format(date));
        db.update(TABLE_mylocation, contentValues, COL_loc_no + " = " + data.getLoc_no(), null);
        db.close();
    }

    public void deleteLoc(String locNo) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from " + TABLE_mylocation + " WHERE " + COL_loc_no + " = " + locNo);
        db.execSQL("delete from " + TABLE_stuff + " WHERE " + COL_loc_no + " = " + locNo);
        db.close();
        Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<MyLocation> getSearch(String word, int start) {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM " + TABLE_mylocation + " WHERE " + COL_loc_name + " LIKE '%" + word + "%'" + " DESC LIMIT " + start + ", 10";

        Cursor c = db.rawQuery(sql, null);
        ArrayList<MyLocation> items = new ArrayList<MyLocation>();
        try {
            if (c.moveToFirst()) {
                do {
                    MyLocation myLocation = new MyLocation();
                    myLocation.setLoc_no(c.getInt(c.getColumnIndex(COL_loc_no)));
                    myLocation.setLoc_name(c.getString(c.getColumnIndex(COL_loc_name)));
                    myLocation.setLoc_comment(c.getString(c.getColumnIndex(COL_loc_comment)));
                    myLocation.setLoc_imgpath(c.getString(c.getColumnIndex(COL_loc_imgpath)));
                    myLocation.setLoc_thumbpath(c.getString(c.getColumnIndex(COL_loc_thumbpath)));
                    myLocation.setLoc_date(c.getString(c.getColumnIndex(COL_loc_date)));
                    items.add(myLocation);
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            c.close();
        }
        db.close();
        return items;
    }
}
