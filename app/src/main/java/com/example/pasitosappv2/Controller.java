package com.example.pasitosappv2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.time.LocalDate;
import java.util.ArrayList;

public class Controller {

    private DBManager dbmanager;
    private String NAME_TABLE = "pasos";

    public Controller(Context context){
        dbmanager = new DBManager(context);
    }

    public void eliminarPasitos(){
        ArrayList<POGOPasos> listaPasos = obtenerPasos();
        SQLiteDatabase baseDeDatos = dbmanager.getWritableDatabase();
        for(int i = 0; i<(listaPasos.size()-1); i++){
            String[] argumentos = {String.valueOf(listaPasos.get(i).getId())};
            baseDeDatos.delete(NAME_TABLE, "id = ?", argumentos);
            Log.d("Eliminando marca: ", listaPasos.get(i).getId()+"");
        }
    }

    public long nuevoPaso(POGOPasos pasos){
        SQLiteDatabase baseDeDatos = dbmanager.getWritableDatabase();
        ContentValues insert = new ContentValues();
        insert.put("fecha", String.valueOf(LocalDate.now()));
        insert.put("bateria", pasos.getBateria());
        insert.put("latitud", pasos.getLatitud());
        insert.put("longitud", pasos.getLongitud());
        return baseDeDatos.insert(NAME_TABLE, null, insert);
    }

    public ArrayList<POGOPasos> obtenerPasos(){
        ArrayList<POGOPasos> listaPasos = new ArrayList<POGOPasos>();
        SQLiteDatabase baseDeDatos = dbmanager.getReadableDatabase();
        String[] columnasAConsultar = {"fecha", "bateria", "latitud", "longitud", "id"};
        Cursor cursor = baseDeDatos.query(NAME_TABLE, columnasAConsultar,null, null, null, null, "id");
        if(cursor == null){
            return listaPasos;
        }
        if(!cursor.moveToFirst()) return listaPasos;
         do{
            String fechaBD = cursor.getString(0);
            Integer bateriaBD = cursor.getInt(1);
            Double latitudBD = cursor.getDouble(2);
            Double longitudBD = cursor.getDouble(3);
            Integer idBD = cursor.getInt(4);
            POGOPasos pasosDB = new POGOPasos(idBD, fechaBD, bateriaBD, latitudBD, longitudBD);
            listaPasos.add(pasosDB);
        }while (cursor.moveToNext());
        cursor.close();
        return listaPasos;
    }



}
