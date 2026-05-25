package com.example.androidya;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "administracion";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_ARTICULOS = "articulos";
    public static final String COLUMN_CODIGO = "codigo";
    public static final String COLUMN_DESCRIPCION = "descripcion";
    public static final String COLUMN_PRECIO = "precio";

    public AdminSQLiteOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_ARTICULOS + " (" +
                COLUMN_CODIGO + " INTEGER PRIMARY KEY, " +
                COLUMN_DESCRIPCION + " TEXT, " +
                COLUMN_PRECIO + " REAL)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Implement upgrade logic if needed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTICULOS);
        onCreate(db);
    }
}
