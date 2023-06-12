package com.example.cowmap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.CloudMediaProvider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class BaseDatos extends SQLiteOpenHelper {

    private static final String LOCALIZACIONES = "localizaciones";

    public BaseDatos(Context context) {
        super(context, "Localizacion", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + LOCALIZACIONES + "(nombre VARCHAR, latitud VARCHAR, longitud VARCHAR)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + LOCALIZACIONES);
        onCreate(db);
    }

    public void insertar(String nombre, String latitud, String longitud) {
        SQLiteDatabase bd = this.getWritableDatabase();
        ContentValues registro = new ContentValues();

        registro.put("nombre", nombre);
        registro.put("latitud", latitud);
        registro.put("longitud", longitud);

        bd.insert(LOCALIZACIONES, null, registro);
        bd.close();
    }

    public ArrayList<Vaca> getVacas() {
        Cursor fila = null;
        ArrayList<Vaca> vacas = new ArrayList<>();
        try {
            SQLiteDatabase bd = this.getReadableDatabase();

            fila = bd.rawQuery("SELECT  nombre, latitud, longitud FROM " + LOCALIZACIONES, null);

            if (fila.moveToFirst()) {
                while (!fila.isAfterLast()) {
                    vacas.add(new Vaca(fila.getString(0), fila.getString(1), fila.getString(2)));
                    fila.moveToNext();
                }
            }
            bd.close();

        } finally {
            if (fila != null) fila.close();
        }
        return vacas;
    }


}
