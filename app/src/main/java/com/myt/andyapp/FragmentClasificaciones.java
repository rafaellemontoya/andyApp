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
import com.myt.andyapp.Model.Clasificacion;
import com.myt.andyapp.Model.Pedido;
import com.myt.andyapp.Model.RenglonesAdapterClasificaciones;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Rafa Pc on 30/03/2018.
 */

public class FragmentClasificaciones extends Fragment {
    View rootView;

    RecyclerView rv;

    List<Clasificacion> allClasificaciones;

    RenglonesAdapterClasificaciones adapterRV;

    ProgressDialog progressDialog;

    //    int i =0;
    public FragmentClasificaciones() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_clasificaciones
                , container, false);

        progressDialog = new ProgressDialog(rootView.getContext());
        progressDialog.setMessage("Cargando, por favor espere...");
        progressDialog.show();


        rv = rootView.findViewById(R.id.recy_clasificaciones);

        rv.setLayoutManager(new LinearLayoutManager(rootView.getContext()));

        allClasificaciones = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        adapterRV = new RenglonesAdapterClasificaciones(allClasificaciones, rootView.getContext());

        rv.setAdapter(adapterRV);
        /**OBTENGO EL USUARIO**/
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String idUsuario= user.getUid();

        database.getReference().child("clasificaciones").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allClasificaciones.removeAll(allClasificaciones);
                for (DataSnapshot snapshot :
                        dataSnapshot.getChildren()
                        ) {
                    Clasificacion clasificacion = new Clasificacion();

                    clasificacion.setId(snapshot.getKey());
                    //En caso que no haya información
                    clasificacion.setDescripcion("");
                    clasificacion.setFoto("");
                    clasificacion.setNombre("");
                    //Reviso si hay informacion

                    if(snapshot.child("descripcion").getValue()!=null){
                        clasificacion.setDescripcion(snapshot.child("descripcion").getValue().toString());
                    }
                    if(snapshot.child("imagen").getValue()!=null){
                        clasificacion.setFoto(snapshot.child("imagen").getValue().toString());
                    }
                    if(snapshot.child("nombre").getValue()!=null){
                        clasificacion.setNombre(snapshot.child("nombre").getValue().toString());
                    }
                    //Lo añado al arraylist
                    allClasificaciones.add(clasificacion);
                }

                progressDialog.dismiss();
                //Reviso que haya clasificaciones
                if(allClasificaciones.isEmpty()){
                    //En caso que no haya
                    TextView vacio = rootView.findViewById(R.id.mensaje_vacio);
                    vacio.setVisibility(View.VISIBLE);
                }else{
                    //Inicio el adaptador para los renglones
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


