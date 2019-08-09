package com.myt.andyapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myt.andyapp.Model.AdapterHistorial;
import com.myt.andyapp.Model.Articulo;
import com.myt.andyapp.Model.Carrito;
import com.myt.andyapp.Model.Clasificacion;
import com.myt.andyapp.Model.Pedido;
import com.myt.andyapp.Model.RenglonesAdapterArticulos;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Rafa Pc on 06/04/2018.
 */

public class HistorialActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private AdapterHistorial adapterHistorial;
    private RecyclerView rv;
    private AdView mAdView;
    private ArrayList<Pedido> pedidos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        //ADMOB
//        MobileAds.initialize(this,
//                getString(R.string.id_aplicacion));
//
//        mAdView = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);



        //Muestro el progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando, por favor espere...");
        progressDialog.show();

        //Instancio el recicler view(la lista)
        rv = findViewById(R.id.recy_historial);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        //Instancio ArrayList de Pedido
        pedidos = new ArrayList<>();

        //Instancio mi adaptador
        adapterHistorial = new AdapterHistorial(pedidos, this);
        rv.setAdapter(adapterHistorial);

        //Obtengo los productos
        obtenerPedidos();





    }
    private void obtenerPedidos(){

        /**OBTENGO EL USUARIO**/
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String idUsuario= user.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        database.getReference().child("pedidos").orderByChild("cliente").equalTo(idUsuario).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pedidos.removeAll(pedidos);
                for (DataSnapshot snapshot :
                        dataSnapshot.getChildren()
                        ) {

                        Pedido pedido = new Pedido();
                        pedido.setId(snapshot.getKey());

                        pedido.setAutorizacion( snapshot.child("autorizacion").getValue().toString() );
                        pedido.setComprobantePago( snapshot.child("comprobante_pago").getValue().toString() );
                        pedido.setCuatroDigitos(  snapshot.child("cuatro_digitos").getValue().toString() );
                        pedido.setDestino(  snapshot.child("destino").getValue().toString() );
                        pedido.setEstado( Integer.valueOf( snapshot.child("estado").getValue().toString() ) );
                        pedido.setFormaPago(  snapshot.child("forma_pago").getValue().toString() );
                        pedido.setRepartidor(  snapshot.child("repartidor").getValue().toString() );
                        pedido.setTotal(Float.valueOf(snapshot.child("total").getValue().toString()));
                        pedido.setEstadoCalificado(0);
                        if(snapshot.child("estado_calificacion").getValue()!=null){

                            pedido.setEstadoCalificado(Integer.valueOf(snapshot.child("estado_calificacion").getValue().toString()));
                        }
                        //Calificacion repartidor
                        pedido.setCalificacionRepartidor(0);
                        if(snapshot.child("calificacion_repartidor").getValue()!=null){

                            pedido.setCalificacionRepartidor( Integer.valueOf( snapshot.child("calificacion_repartidor").getValue().toString() ) );
                        }

                        Date d= new Date(Long.valueOf(snapshot.child("fecha").getValue().toString()));
                        pedido.setFechaCreacion(d);
                        Date fechaEntrega= new Date(Long.valueOf(snapshot.child("fecha_entrega").getValue().toString()));
                        pedido.setFechaEntrega(fechaEntrega);

                        pedido.obtenerNombreEstado(Integer.valueOf(snapshot.child("estado").getValue().toString()));
                        /***OBTENGO LOS PRODUCTOS QUE SE SOLICITARON**/
                        ArrayList<Carrito> allCarrito = new ArrayList<>();
                        //recoorro con un nuevo snapshot los productos que hay
                        for (DataSnapshot snapshotProductos : snapshot.child("productos").getChildren()) {
                            Carrito carrito = new Carrito();
                            carrito.setId(snapshotProductos.getKey());
                            carrito.setNombre(snapshotProductos.child("nombre").getValue().toString());
                            carrito.setNotas(snapshotProductos.child("nota").getValue().toString());
                            carrito.setImporte(Float.valueOf(snapshotProductos.child("importe").getValue().toString()));
                            carrito.setUnidades(Integer.valueOf(snapshotProductos.child("unidades").getValue().toString()));
                            carrito.setCalificacion(0);
                            if(snapshotProductos.child("calificacion").getValue()!=null){

                                carrito.setCalificacion(Integer.valueOf(snapshotProductos.child("calificacion").getValue().toString()));
                            }

                            //Añadimos el carrito al arrayList
                            allCarrito.add(carrito);
                        }

                        pedido.setCarritos(allCarrito);

                        if(pedido.getEstado()==2){

                            pedidos.add(pedido);
                        }


                }
                progressDialog.dismiss();
                if(pedidos.isEmpty()){
                    TextView vacio = findViewById(R.id.mensaje_vacio);
                    vacio.setText("Aún no tienes pedidos finalizados!");
                }else{

                    adapterHistorial.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
