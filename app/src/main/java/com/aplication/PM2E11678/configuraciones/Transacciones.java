package com.aplication.PM2E11678.configuraciones;

public class Transacciones {

    //Nombre de Base de Datos

    public static final String NameDatabase = "PM1E10634";

    //tablas de base de datos de SQLite

    public  static final String tablaContactos = "contactos";

    //campos de la tabla personas de la base de datos en sqlite

    public static final String id = "id";
    public static final String pais = "pais";
    public static final String nombre = "nombre";
    public static final String telefono = "telefono";
    public static final String nota = "nota";

    //Transacciones DML Tabla Personas

    public  static  final String CreateTableContactos ="CREATE TABLE contactos (id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "pais TEXT, nombre TEXT, telefono INTEGER, nota TEXT)";

    public static final String DROPTableContactos ="DROP TABLE IF EXISTS contactos";

}
