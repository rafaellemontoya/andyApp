package com.myt.andyapp.Model;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.myt.andyapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rafa Pc on 06/04/2018.
 */

public class FirebaseManejador {

    private DatabaseReference mDatabase;
    private Context context;
    public FirebaseManejador(Context context){
        this.context= context;

    }

    public boolean guardarPedido(String nombreCliente, String direccionEntrega, String formaPago, String cuatroDigitos, String autorizacion, String idOrdenConekta){
        Boolean respuesta=false;

        /**MUESTRO LISTA CARRITO**/
        int total=0;

        BDCarritoHelper bdCarritoHelper = new BDCarritoHelper(context);

        ArrayList<Carrito>  allCarrito = bdCarritoHelper.getAllCarrito();


        /**TOTAL**/
        for (Carrito carrito: allCarrito) {
            total+=carrito.getImporte();
        }

        final Long fecha = System.currentTimeMillis();

        /**AGREGAR A FIREBASE**/
        //Instancio la base de datos de fierebase
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //hago un push para obtenr el id en firebase
        String key = mDatabase.child("pedidos").push().getKey();
        //Convierto a HASHMAP los productos que hay
        HashMap<String, Object> hmProductos = new HashMap<>();
        for (Carrito carrito: allCarrito) {

            HashMap<String, Object> hmContenido = new HashMap<>();
            if(carrito.getUnidades()==0){
                hmContenido.put("unidades","COMPLETAR CON");
            }else {

                hmContenido.put("unidades",carrito.getUnidades());
            }
            hmContenido.put("importe", carrito.getImporte());
            hmContenido.put("nota", carrito.getNotas());
            hmContenido.put("nombre", carrito.getNombre());
            hmContenido.put("calificacion", 0);
            hmProductos.put(carrito.getId(),hmContenido);
        }
        Log.d("MAp",hmProductos.toString());
        /**OBTENGO EL USUARIO**/
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String idUsuario= user.getUid();
        //convierto a Map pedido
        HashMap<String, Object> hmPedido= new HashMap<>();
        hmPedido.put("productos", hmProductos);
        hmPedido.put("cliente", idUsuario);
        hmPedido.put("nombre_cliente", nombreCliente);
        hmPedido.put("fecha", fecha);
        hmPedido.put("estado", 0);
        hmPedido.put("total", (double)Math.round(total * 100d) / 100d);
        hmPedido.put("comprobante_pago", "");
        hmPedido.put("fecha_entrega",0);
        hmPedido.put("cuatro_digitos", cuatroDigitos);
        hmPedido.put("repartidor", "");
        //Sólo en caso de pago con tarjeta de crédito
        hmPedido.put("destino", direccionEntrega);
        hmPedido.put("autorizacion", autorizacion);
        hmPedido.put("forma_pago", formaPago);
        hmPedido.put("id_orden_conekta", idOrdenConekta);
        //estado calificacion

        hmPedido.put("estado_calificacion",0);
        hmPedido.put("calificacion_repartidor",0);

        Map<String, Object> mapPedido = new HashMap<>();
        mapPedido.put("/pedidos/"+key, hmPedido);
        mDatabase.updateChildren(mapPedido);
        /**BORRAR CARRITO**/

        bdCarritoHelper.deleteCarrito();
        respuesta=true;
        return respuesta;

    }
}
