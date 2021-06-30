package com.teit.weather;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {
    //name of our database
    private static final String DataBase_Name="citydatabase";
    //database version
    private static final int DataBase_Version=1;
    //table name
    private static final String Table_Name = "citynametable";

    //city name column:
    private static final String CityName_Column = "citynamecolumn";

    public DataBaseHelper(Context context){
        super(context,DataBase_Name,null,DataBase_Version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE "+Table_Name+" ("+CityName_Column + " TEXT)";
        db.execSQL(query);
    }
    public void addCity(String cityname){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CityName_Column,cityname);
        db.insert(Table_Name,null,values);
        //show records:
        db.close();
    }
    public void deleteCity(String cityname){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + Table_Name+ " WHERE "+CityName_Column+"='"+cityname+"'");
        //show records:
        db.close();
    }
    public void getAllRecords(ArrayList<String> list){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(Table_Name, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int nameCity = c.getColumnIndex(CityName_Column);
            do {
                //add all records to list
                list.add(c.getString(nameCity));
            } while (c.moveToNext());
        }
        else{

        }
        db.close();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
