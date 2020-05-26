package com.example.b10709022_hw2.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.b10709022_hw2.data.WaitlistContract;

 public class WaitlistDbHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "waitlist.db";
        private static final int DATABASE_VERSION = 1;
        public WaitlistDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            final String SQL_CREATE_WAITLIST_TABLE = "CREATE TABLE " +
                    WaitlistContract.WaitlistEntry.TABLE_NAME + " (" +
                    WaitlistContract.WaitlistEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    WaitlistContract.WaitlistEntry.COLUMN_GUEST_NAME + " TEXT NOT NULL, " +
                    WaitlistContract.WaitlistEntry.COLUMN_PARTY_SIZE + " INTEGER NOT NULL, " +
                    WaitlistContract.WaitlistEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                    +
                    "); ";
            sqLiteDatabase.execSQL(SQL_CREATE_WAITLIST_TABLE);
        }
        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WaitlistContract.WaitlistEntry.TABLE_NAME);
            onCreate(sqLiteDatabase);
        }
     public void printTable(){
         Cursor cursor = getWritableDatabase().query(WaitlistContract.WaitlistEntry.TABLE_NAME,
                 null,
                 null,
                 null,
                 null,
                 null,
                 null,
                 null);
         System.out.println("sout : print table");

         while(cursor.moveToNext()){
             System.out.println( "sout : " +
                     cursor.getString(0)+"\t"+
                     cursor.getString(1)+"\t"+
                     cursor.getString(2));
         }
         cursor.close();
     }
 }
