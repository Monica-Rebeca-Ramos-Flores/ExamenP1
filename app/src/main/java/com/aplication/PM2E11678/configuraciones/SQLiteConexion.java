/* Programa realizado por los alumnos de Programacion Movil I:
 * Jimmy Orlando Zelaya Muñoz 201910050016 y Monica Rebeca Ramos Flores 201910110078*/

package com.aplication.PM2E11678.configuraciones;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteConexion extends SQLiteOpenHelper {
    public SQLiteConexion(Context context, String dbname, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, dbname, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase BaseDatos) {
        BaseDatos.execSQL("create Table tablaContactos(id text primary key, pais text, nombre text, telefono text, nota text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(Transacciones.DROPTableContactos);
        onCreate(db);

    }
}