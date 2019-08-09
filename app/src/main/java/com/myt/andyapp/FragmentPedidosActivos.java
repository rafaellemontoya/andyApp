package com.myt.andyapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.myt.andyapp.Model.AdapterPedidosActivos;
import com.myt.andyapp.Model.Carrito;
import com.myt.andyapp.Model.Pedido;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Rafa Pc on 30/03/2018.
 */

public class FragmentPedidosActivos extends Fragment {
    View rootView;

    RecyclerView rv;

    List<Pedido> pedidos;

    AdapterPedidosActivos adapterRV;

    ProgressDialog progressDialog;

    //    int i =0;
    public FragmentPedidosActivos() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_pedidos
                , container, false);

        progressDialog = new ProgressDialog(rootView.getContext());
        progressDialog.setMessage("Cargando, por favor espere...");
        progressDialog.show();


        rv = rootView.findViewById(R.id.recy_pedidos);

        rv.setLayoutManager(new LinearLayoutManager(rootView.getContext()));

        pedidos = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        adapterRV = new AdapterPedidosActivos(pedidos, rootView.getContext());

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
                            if(snapshotProductos.child("unidades").getValue().toString().equals("COMPLETAR CON")){
                                carrito.setUnidades(0);
                            }else{

                                carrito.setUnidades(Integer.valueOf(snapshotProductos.child("unidades").getValue().toString()));
                            }

                            //Añadimos el carrito al arrayList
                            allCarrito.add(carrito);
                        }

                        pedido.setCarritos(allCarrito);

                        if(pedido.getEstado()==0||pedido.getEstado()==1){

                            pedidos.add(pedido);
                        }



                }
                progressDialog.dismiss();
                if(pedidos.isEmpty()){
                    TextView vacio = rootView.findViewById(R.id.mensaje_vacio);
                    vacio.setText("No hay pedidos activos");
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


