package com.myt.andyapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myt.andyapp.Model.AdapterHistorial;
import com.myt.andyapp.Model.Carrito;
import com.myt.andyapp.Model.Pedido;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Rafa Pc on 30/03/2018.
 */

public class FragmentHistorial extends Fragment {
    View rootView;

    RecyclerView rv;

    List<Pedido> pedidos;

    AdapterHistorial adapterRV;

    ProgressDialog progressDialog;

    //    int i =0;
    public FragmentHistorial() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_historial
                , container, false);

        progressDialog = new ProgressDialog(rootView.getContext());
        progressDialog.setMessage("Cargando, por favor espere...");
        progressDialog.show();


        rv = rootView.findViewById(R.id.recy_historial);

        rv.setLayoutManager(new LinearLayoutManager(rootView.getContext()));

        pedidos = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        adapterRV = new AdapterHistorial(pedidos, rootView.getContext());

        rv.setAdapter(adapterRV);
        /**OBTENGO EL USUARIO**/
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String idUsuario= user.getUid();
        database.getReference().child("pedidos").orderByChild("cliente").equalTo(idUsuario).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pedidos.removeAll(pedidos);
                for (DataSnapshot snapshot :
                        dataSnapshot.getChildren()
                        ) {
//                    if(Integer.valueOf(snapshot.child("estado").getValue().toString())==7) {
//
////                    Pedido pedido = snapshot.getValue(Pedido.class);
//                        Pedido pedido = new Pedido();
//
//                        pedido.setId(snapshot.getKey());
//                        Date d= new Date(Long.valueOf(snapshot.child("fecha").getValue().toString()));
//                        pedido.setFechaCreacion(d);
//                        Date fechaEntrega= new Date(Long.valueOf(snapshot.child("fecha_entrega").getValue().toString()));
//                        pedido.setFechaEntrega(fechaEntrega);
//                        Date fechaPago= new Date(Long.valueOf(snapshot.child("fecha_pago").getValue().toString()));
//                        pedido.setFechaPago(fechaPago);
//
//                        pedido.setTotal(Float.valueOf(snapshot.child("total").getValue().toString()));
//                        pedido.setAnticipo(Integer.valueOf(snapshot.child("anticipo").getValue().toString()));
//                        pedido.obtenerNombreEstado(Integer.valueOf(snapshot.child("estado").getValue().toString()));
//                        pedido.setEstado(Integer.valueOf(snapshot.child("estado").getValue().toString()));
//                        Log.d("productos", pedido.toString());
//                        /***OBTENGO LOS PRODUCTOS QUE SE SOLICITARON**/
//                        ArrayList<Carrito> allCarrito = new ArrayList<>();
//                        //recoorro con un nuevo snapshot los productos que hay
//                        for (DataSnapshot snapshotProductos : snapshot.child("productos").getChildren()) {
////                        Log.d("productos",snapshotProductos.child("nombre").getValue().toString());
//                            Carrito carrito = new Carrito();
////                            carrito.setId(snapshotProductos.getKey());
////                            carrito.setNombre(snapshotProductos.child("nombre").getValue().toString());
////                            carrito.setColor(snapshotProductos.child("color").getValue().toString());
////                            carrito.setPosicion(snapshotProductos.child("posicion").getValue().toString());
////                            carrito.setCapacidad(snapshotProductos.child("capacidad").getValue().toString());
////                            carrito.setNotas(snapshotProductos.child("nota").getValue().toString());
////                            carrito.setImporte(Float.valueOf(snapshotProductos.child("importe").getValue().toString()));
////                            if(snapshotProductos.child("unidades").getValue().toString().equals("COMPLETAR CON")){
////                                carrito.setUnidades(0);
////                            }else{
////
////                                carrito.setUnidades(Integer.valueOf(snapshotProductos.child("unidades").getValue().toString()));
////                            }
//                            //Añadimos el carrito al arrayList
//                            allCarrito.add(carrito);
//                        }
//
//                        pedido.setCarritos(allCarrito);
//                        pedidos.add(pedido);
//
//                    }//if estado =7

                }

                progressDialog.dismiss();
                if(pedidos.isEmpty()){
                    TextView vacio = rootView.findViewById(R.id.mensaje_vacio);
                    vacio.setText("Historial vacío");
                }else{

                    adapterRV.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return rootView;
    }


}


