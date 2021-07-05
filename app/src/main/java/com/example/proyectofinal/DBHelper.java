package com.example.proyectofinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.SimpleCursorTreeAdapter;

import java.util.Vector;

public class DBHelper extends SQLiteOpenHelper{

    private static  final String DB_FILE = "Proyect.db";

    private static  final String TABLE_USERS = "User";
    private static  final String FIELD_UNAME = "userName";
    private static  final String FIELD_NOMBRE = "nombre";
    private static  final String FIELD_CITY = "city";
    private static  final String FIELD_PSW = "password";

    private static  final String TABLE_LOCALES = "locales";
    private static  final String FIELD_ID_LOCALES = "id";
    private static  final String FIELD_NOMBRE_LOCALES = "nombreLocal";
    private static  final String FIELD_CITY_LOCALES = "city";
    private static  final String FIELD_TIPO = "tipo";
    private static  final String FIELD_DISCA = "discapacitado";

    public DBHelper (Context context){
        super(context, DB_FILE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se llama al crear la base de datos
        String query="CREATE TABLE " + TABLE_USERS + "(" +
                FIELD_UNAME + " TEXT PRIMARY KEY, " +
                FIELD_NOMBRE + " TEXT, " +
                FIELD_CITY + " TEXT, " +
                FIELD_PSW + " TEXT)";
        db.execSQL(query);

        String query2="CREATE TABLE " + TABLE_LOCALES + "(" +
                FIELD_ID_LOCALES + " INTEGER PRIMARY KEY, " +
                FIELD_NOMBRE_LOCALES + " TEXT, " +
                FIELD_CITY_LOCALES + " TEXT, " +
                FIELD_TIPO+ " TEXT, " +
                FIELD_DISCA + " TEXT)";
        db.execSQL(query2);

        ContentValues valores = new ContentValues();
        valores.put(FIELD_UNAME, "admin");
        valores.put(FIELD_NOMBRE, "Juan Sotelo");
        valores.put(FIELD_CITY, "CDMX");
        valores.put(FIELD_PSW, "123");
        db.insert(TABLE_USERS, null, valores);

        ContentValues valoresLocal = new ContentValues();
        valoresLocal.put(FIELD_ID_LOCALES, "1");
        valoresLocal.put(FIELD_NOMBRE_LOCALES, "Tacos el Wero");
        valoresLocal.put(FIELD_CITY_LOCALES, "CDMX");
        valoresLocal.put(FIELD_TIPO, "Comida");
        valoresLocal.put(FIELD_DISCA, "si");
        db.insert(TABLE_LOCALES, null, valoresLocal);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Se llama al actualizar la version de la db

        //Prepared  statemens!!
        String query = "DROP TABLE IF EXISTS ?";
        String[] params = {TABLE_USERS};

        db.execSQL(query, params);

        onCreate(db);
    }

    public String buscarUser(String nombre){
        //Obtener referencia a la bd
        SQLiteDatabase db = getReadableDatabase();
        String clause = FIELD_UNAME + " = ?";
        String[] params = {nombre};
        Cursor c = db.query(TABLE_USERS, null, clause, params, null, null, null);
        String resultado = "";
        String city="", psw="", nombredb="";
        if (c.moveToFirst()){
            resultado = c.getString(0);
            nombredb = c.getString(1);
            city = c.getString(2);
            psw = c.getString(3);
        }
        else{
            return "VACIO";
        }
        resultado = resultado+"$"+nombredb+"$"+city+"$"+psw;
        return  resultado;
    }

    public Vector<String> buscarLocales(String ciudad){
        SQLiteDatabase db = getReadableDatabase();
        String clause = FIELD_CITY_LOCALES + " = ?";
        String[] params = {ciudad};
        Cursor c = db.query(TABLE_LOCALES, null, clause, params, null, null, null);
        Vector<String> resultado = new Vector<String>();
        if (c.moveToFirst()){
            resultado.add("Nombre: "+c.getString(1)+" Ciudad: "+c.getString(2)+" Tipo: "+c.getString(3)+" Apto para discapacitados: "+c.getString(4));
            while(c.moveToNext()){
                resultado.add("Nombre: "+c.getString(1)+" Ciudad: "+c.getString(2)+" Tipo: "+c.getString(3)+" Apto para discapacitados: "+c.getString(4));
            }
        }
        else{
            resultado.add("VACIO");
        }
        return  resultado;
    }

    public  void guardarLocales(String nombre, String ciudad, String tipo, String disca){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues val = new ContentValues();
        val.put(FIELD_NOMBRE_LOCALES, nombre);
        val.put(FIELD_CITY_LOCALES, ciudad);
        val.put(FIELD_TIPO, tipo);
        val.put(FIELD_DISCA, disca);
        db.insert(TABLE_LOCALES, null, val);
    }
}
