package com.example.united.mrk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by united on 06/06/2017.
 */
public class DataHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Order.db";
    public static final String TABLE_NAME = "order_table";
    public static final String COL_1 = "Id";
    public static final String COL_2 = "Code";
    public static final String COL_3 = "Name";
    public static final String COL_4 = "Qty";
    public static final String COL_5 = "Note";
    public static final String COL_6 = "Harga";
    public static final String COL_7 = "Total";
    public static final String COL_8 = "Fkitchen_id";


    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, 7);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (Id INTEGER PRIMARY KEY AUTOINCREMENT, Code TEXT, Name TEXT, Qty INTEGER, Note TEXT, Harga TEXT, Total TEXT, Fkitchen_id TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public Cursor hitungjumlahdata(){
       // String jumlah = "0";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("  SELECT count (Name) FROM  " + TABLE_NAME + "", null);

        return res;


    }
    public boolean insertData(String codemenu, String namasubmenu, String qty, String note, String harga, String total, String fkicthen_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, codemenu);
        contentValues.put(COL_3, namasubmenu);
        contentValues.put(COL_4, qty);
        contentValues.put(COL_5, note);
        contentValues.put(COL_6, harga);
        contentValues.put(COL_7, total);
        contentValues.put(COL_8, fkicthen_id);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public boolean updateData(String codemenu, String namasubmenu, String jumlah, String harga, String total, String fkitchen_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, codemenu);
        contentValues.put(COL_3, namasubmenu);
        contentValues.put(COL_4, jumlah);
        contentValues.put(COL_6, harga);
        contentValues.put(COL_7, total);
        contentValues.put(COL_8, fkitchen_id);
        db.update(TABLE_NAME, contentValues, "code = ?", new String[]{codemenu});
        return true;
    }

    public String getTotal() {
        String qty = "0";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT SUM(Total) FROM  " + TABLE_NAME + " ", null);
        while (res.moveToNext()) {
            qty = res.getString(0);
        }

        res.close();
        return qty;

    }

    public boolean updateNote(String codemenu, String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, codemenu);
        contentValues.put(COL_5, note);
        db.update(TABLE_NAME, contentValues, "code = ?", new String[]{codemenu});
        return true;

    }

    public void HapusQty() {
        int a = 0;
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQty = "UPDATE " + TABLE_NAME + " SET Qty= " + a;
        String updateTotal = "UPDATE " + TABLE_NAME + " SET Total= " + a;
        database.execSQL(updateQty);
        database.execSQL(updateTotal);
        database.close();
    }

    public void HapusId(String id_sqlite) {
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "DELETE from " + TABLE_NAME + " where _id = " + "'" + id_sqlite + "'";
        database.execSQL(updateQuery);
        database.close();
    }

    /*int qty=0;
    String a;
    SQLiteDatabase db = this.getWritableDatabase();
    Cursor res = db.rawQuery("SELECT Name from " + TABLE_NAME + " where Total = " + qty, null);
        while (res.moveToNext()) {
        Log.e("get jumlah",a);
    }
        res.close();
        return res;*/
    public Cursor selectnol() {
        int qty=0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " where Total = " + qty, null);
        return res;
}

    public String getjumlah(String codemenu) {
        String qty = "0";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT Qty FROM " + TABLE_NAME + " WHERE Id IN (SELECT MAX(Id)  AS Id FROM " + TABLE_NAME + "  GROUP BY `code`) AND `code`=" + codemenu + " ORDER BY Id limit 1; ", null);
        while (res.moveToNext()) {
            qty = res.getString(0);
           // Log.e("get jumlah", qty);
        }
        res.close();
        return qty;
    }

    public String getnote(String codemenu) {
        String qty = "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT Note FROM " + TABLE_NAME + " WHERE Id IN (SELECT MAX(Id)  AS Id FROM " + TABLE_NAME + "  GROUP BY `code`) AND `code`=" + codemenu + " ORDER BY Id limit 1; ", null);
        while (res.moveToNext()) {
            qty = res.getString(0);
           // Log.e("getnote", qty);
        }
        res.close();
        return qty;
    }


    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE Id IN (SELECT MAX(Id) AS Id FROM " + TABLE_NAME + " GROUP BY `code`) ORDER BY Id; ", null);

        return res;
    }

    public Cursor getjumlah2() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE Id IN (SELECT MAX(Id) AS Id FROM " + TABLE_NAME + " GROUP BY `code`) ORDER BY Id; ", null);

        return res;
    }


    public Integer deleteData(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "code = ?", new String[]{String.valueOf(id)});
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME);

    }

    public void delete_tabel_database() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

}