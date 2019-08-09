package com.myt.andyapp.Model;

/**
 * Created by Rafa Pc on 30/03/2018.
 */

public class BDCarrito {
    //Nombre del  esquema de Base de Datos
    public static final String DATABASE_NAME = "carrito";
    //Version de la Base de Datos (Este parámetro es importante  )
    public static final int DATABASE_VERSION = 1;

    //Una clase estatica en la que se definen las propiedaes que determinan la tabla Notes
// y sus 4 columnas
    public static class Carrito {
        //Nombre de la tabla
        public static final String TABLE_NAME = "carrito";
        //Nombre de las Columnas que contiene la tabla
        public static final String idCol = "id";
        public static final String nombre_producto = "nombre";
        public static final String precio = "precio";
        public static final String unidades = "unidades";
        public static final String importe = "importe";
        public static final String notas = "notas";
    }

    //Setencia SQL que permite crear la tabla Notes
    public static final String Carrito_TABLE_CREATE =
            "CREATE TABLE " + BDCarrito.Carrito.TABLE_NAME + " (" +
                    BDCarrito.Carrito.idCol + " TEXT, " +
                    BDCarrito.Carrito.nombre_producto + " TEXT, " +
                    BDCarrito.Carrito.precio + " FLOAT, " +
                    BDCarrito.Carrito.unidades + " INTEGER, " +
                    BDCarrito.Carrito.importe + " FLOAT, " +
                    BDCarrito.Carrito.notas + " TEXT);";

    //Setencia SQL que permite eliminar la tabla Notes
    public static final String Carrito_TABLE_DROP = "DROP TABLE IF EXISTS " + BDCarrito.Carrito.TABLE_NAME;
}
