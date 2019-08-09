package com.myt.andyapp.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Rafa Pc on 30/03/2018.
 */

public class BDCarritoHelper extends SQLiteOpenHelper {
    //Definimos in Contructor para Instanciar el Database Helper
    public BDCarritoHelper(Context context) {
        super(context, BDCarrito.DATABASE_NAME, null, BDCarrito.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(BDCarrito.Carrito_TABLE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //El método onUpgrade se ejecuta cada vez que recompilamos e instalamos la app con un
        //nuevo numero de version de base de datos (DATABASE_VERSION), de tal mamera que en este
        // método lo que hacemos es:
        // 1. Con esta sentencia borramos la tabla "notes"
        sqLiteDatabase.execSQL(BDCarrito.Carrito_TABLE_DROP);

        // 2. Con esta sentencia llamamos de nuevo al método onCreate para que se cree de nuevo
        //la tabla "notes" la cual seguramente al cambair de versión ha tenido modifciaciones
        // en cuanto a su estructura de columnas
        this.onCreate(sqLiteDatabase);
    }

    /*
    * OPERACIONES CRUD (Create, Read, Update, Delete)
    * A partir de aquí se definen los métodos para insertar, leer, actualizar y borrar registros de
    * la base de datos (BD)
    * */

    public void insertCarrito(Carrito carrito){
        //Con este método insertamos las notas nuevas que el usuario vaya creando

        // 1. Obtenemos una reference de la BD con permisos de escritura
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. Creamos un obejto de tipo ContentValues para agregar los pares
        // de Claves de Columna y Valor
        ContentValues values = new ContentValues();
        values.put(BDCarrito.Carrito.idCol, carrito.getId()); // nombre
        values.put(BDCarrito.Carrito.nombre_producto, carrito.getNombre()); // codigo barras
        values.put(BDCarrito.Carrito.precio, carrito.getPrecio()); // precio
        values.put(BDCarrito.Carrito.unidades, carrito.getUnidades()); // costo
        values.put(BDCarrito.Carrito.importe, carrito.getImporte()); // notas
        values.put(BDCarrito.Carrito.notas, carrito.getNotas()); // notas

        // 3. Insertamos los datos en la tabla "carritos"
        db.insert(BDCarrito.Carrito.TABLE_NAME, null, values);

        // 4. Cerramos la conexión comn la BD
        db.close();
    }

    //Obtener uan Nota dado un ID
    public Carrito getCarritoById(int id){
        // Declaramos un objeto Note para instanciarlo con el resultado del query
        Carrito carrito= null;

        // 1. Obtenemos una reference de la BD con permisos de lectura
        SQLiteDatabase db = this.getReadableDatabase();

        //Definimos un array con los nombres de las columnas que deseamos sacar
        String[] COLUMNS = {BDCarrito.Carrito.idCol, BDCarrito.Carrito.nombre_producto, BDCarrito.Carrito.precio, BDCarrito.Carrito.unidades, BDCarrito.Carrito.importe, BDCarrito.Carrito.notas};


        // 2. Contruimos el query
        Cursor cursor =
                db.query(BDCarrito.Carrito.TABLE_NAME,  //Nomre de la tabla
                        COLUMNS, // b. Nombre de las Columnas
                        " id = ?", // c. Columnas de la clausula WHERE
                        new String[] { String.valueOf(id) }, // d. valores de las columnas de la clausula WHERE
                        null, // e. Clausula Group by
                        null, // f. Clausula having
                        null, // g. Clausula order by
                        null); // h. Limte de regsitros

        // 3. Si hemos obtenido algun resultado entonces sacamos el primero de ellos ya que se supone
        //que ha de existir un solo registro para un id
        if (cursor != null) {
            cursor.moveToFirst();
            // 4. Contruimos el objeto Note
            carrito = new Carrito();
            carrito.setId(cursor.getString(0));
            carrito.setNombre(cursor.getString(1));
            carrito.setPrecio(Float.valueOf(cursor.getString(2)));
            carrito.setUnidades(Integer.valueOf(cursor.getString(3)));
            carrito.setImporte(Float.parseFloat(cursor.getString(4)));
            carrito.setNotas(cursor.getString(5));

        }

        // 5. Devolvemos le objeto carrito
        return carrito;
    }
    //Obtener un Carrito dado un parametro
    public Carrito getCarritoByParam(String param, String valor){
        // Declaramos un objeto Note para instanciarlo con el resultado del query
        Carrito carrito= null;

        // 1. Obtenemos una reference de la BD con permisos de lectura
        SQLiteDatabase db = this.getReadableDatabase();

        //Definimos un array con los nombres de las columnas que deseamos sacar
        String[] COLUMNS = {BDCarrito.Carrito.idCol, BDCarrito.Carrito.nombre_producto, BDCarrito.Carrito.precio, BDCarrito.Carrito.unidades, BDCarrito.Carrito.importe, BDCarrito.Carrito.notas};


        // 2. Contruimos el query
        Cursor cursor =
                db.query(BDCarrito.Carrito.TABLE_NAME,  //Nomre de la tabla
                        COLUMNS, // b. Nombre de las Columnas
                        " "+param+" = ?", // c. Columnas de la clausula WHERE
                        new String[] { valor }, // d. valores de las columnas de la clausula WHERE
                        null, // e. Clausula Group by
                        null, // f. Clausula having
                        null, // g. Clausula order by
                        null); // h. Limte de regsitros

        // 3. Si hemos obtenido algun resultado entonces sacamos el primero de ellos ya que se supone
        //que ha de existir un solo registro para un id
        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){
            //cursor is empty
            carrito = new Carrito();
            carrito.setId("-1");
        }else{

            cursor.moveToFirst();
            // 4. Contruimos el objeto Note
            carrito = new Carrito();
            carrito.setId(cursor.getString(0));
            carrito.setNombre(cursor.getString(1));
            carrito.setPrecio(Float.valueOf(cursor.getString(2)));
            carrito.setUnidades(Integer.valueOf(cursor.getString(3)));
            carrito.setImporte(Float.parseFloat(cursor.getString(4)));
            carrito.setNotas(cursor.getString(5));
        }

        // 5. Devolvemos le objeto carrito
        return carrito;
    }


    // Obtener todas las notas
    public ArrayList<Carrito> getAllCarrito() {
        //Instanciamos un Array para llenarlo con todos los objetos Notes que saquemos de la BD
        ArrayList<Carrito> aCarrito = new ArrayList();

        // 1. Aramos un String con el query a ejecutar
        String query = "SELECT  * FROM " + BDCarrito.Carrito.TABLE_NAME;

        // 2. Obtenemos una reference de la BD con permisos de escritura y ejecutamos el query
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. Iteramos entre cada uno de olos registros y agregarlos al array de Notas
        Carrito carrito = null;
        if (cursor.moveToFirst()) {
            do {
                carrito = new Carrito();
                carrito.setId(cursor.getString(0));
                carrito.setNombre(cursor.getString(1));
                carrito.setPrecio(Float.valueOf(cursor.getString(2)));
                carrito.setUnidades(Integer.valueOf(cursor.getString(3)));
                carrito.setImporte(Float.parseFloat(cursor.getString(4)));
                carrito.setNotas(cursor.getString(5));
                // Add book to books
                aCarrito.add(carrito);
            } while (cursor.moveToNext());
        }

        //Cerramos el cursor
        cursor.close();

        // Devolvemos las notas encontradas o un array vacio en caso de que no se encuentre nada
        return aCarrito;
    }


    //Actualizar los datos en una nota
    public int updateNote(Carrito carrito) {

        // 1. Obtenemos una reference de la BD con permisos de escritura
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. Creamos el objeto ContentValues con las claves "columna"/valor
        // que se desean actualizar
        ContentValues values = new ContentValues();
        values.put(BDCarrito.Carrito.nombre_producto, carrito.getNombre());
        values.put(BDCarrito.Carrito.precio, carrito.getPrecio());
        values.put(BDCarrito.Carrito.unidades, carrito.getUnidades());
        values.put(BDCarrito.Carrito.importe, carrito.getImporte());
        values.put(BDCarrito.Carrito.notas, carrito.getNotas());

        // 3. Actualizamos el registro con el método update el cual devuelve la cantidad
        // de registros actualizados
        int i = db.update(BDCarrito.Carrito.TABLE_NAME, //table
                values, // column/value
                BDCarrito.Carrito.idCol+" = ?", // selections
                new String[] { String.valueOf(carrito.getId()) }); //selection args

        // 4. Cerramos la conexión a la BD
        db.close();

        // Devolvemos la cantidad de registros actualizados
        return i;
    }

    // Borrar una Nota
    public void deleteNote(Carrito carrito) {

        // 1. Obtenemos una reference de la BD con permisos de escritura
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. Borramos el registro
        db.delete(BDCarrito.Carrito.TABLE_NAME,
                BDCarrito.Carrito.idCol+" = ?",
                new String[] { String.valueOf(carrito.getId()) });

        // 3. Cerramos la conexión a la Bd
        db.close();
    }  // Borrar carrito
    public void deleteCarrito() {

        // 1. Obtenemos una reference de la BD con permisos de escritura
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. Borramos el registro
//        db.delete(BDCarrito.Carrito.TABLE_NAME, null, null);
        db.execSQL("delete from " + BDCarrito.Carrito.TABLE_NAME);

        // 3. Cerramos la conexión a la Bd
        db.close();
    }
    public Cursor getCursorBuscador(String textSearch){
        textSearch = textSearch.replace("'", "''");
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT id AS _id, nombre AS item" +
                " FROM "+BDCarrito.Carrito.TABLE_NAME +
                " WHERE nombre LIKE '%" +textSearch+ "%' ", null);
    }

}

