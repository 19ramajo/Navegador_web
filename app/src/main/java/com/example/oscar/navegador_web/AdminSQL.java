package com.example.oscar.navegador_web;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;


/**
 * Created by oscar on 17/10/2016.
 */

public class AdminSQL extends SQLiteOpenHelper{

    static Historial historial;
    static String ins = "CREATE TABLE Historial (texto VARCHAR (20), url VARCHAR (200))";
    private static final int VERSION_BASEDATOS = 1;
    private static final String NOMBRE_BASEDATOS="BD_HISTORIAL.db";

    public AdminSQL(Context context) {
        super(context, NOMBRE_BASEDATOS,null,VERSION_BASEDATOS);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ins);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insertar(Historial historial) {
        long nreg_afectados=-1;
        SQLiteDatabase db = this.getWritableDatabase();
        if(db!=null){
            ContentValues valores= new ContentValues();

            valores.put("texto",historial.getTexto());
            valores.put("url", historial.getUrl());

            nreg_afectados=db.insert("Historial", null, valores);
        }
        db.close();

        return nreg_afectados;
    }

    public ArrayList<Historial> obtenerHistorial(){
        Historial historial;
        ArrayList<Historial> lista_historial = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String campos[] = {"texto","url" };
        Cursor cursor;
        if(db != null){
            cursor = db.query("Historial", campos,null,null,null,null,null);
            cursor.moveToFirst();
            do{
                historial = new Historial(cursor.getString(0), cursor.getString(1));
                lista_historial.add(historial);
            }while (cursor.moveToNext());
            db.close();
            cursor.close();
        }
        return lista_historial;
    }

    public int borrarHistorial(){
        int n = -1;
        SQLiteDatabase db = getWritableDatabase();
        if (db != null){
            n = db.delete("Historial", null, null);
        }
        db.close();
        return n;
    }
}
