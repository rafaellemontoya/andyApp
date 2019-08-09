package com.myt.andyapp;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myt.andyapp.Model.Articulo;
import com.myt.andyapp.Model.Clasificacion;
import com.myt.andyapp.Model.CompraActiva;
import com.myt.andyapp.Model.RenglonesAdapterArticulos;

import java.util.ArrayList;

/**
 * Created by Rafa Pc on 02/04/2018.
 * Mostrar los productos de acuerdo a la clasificacion seleccionada
 */

public class ListaProductosActivity extends AppCompatActivity {
    private ArrayList<Articulo> allArticulo;
    private TextView nombreClasificacion;
    private ImageView imagenClasificacion;
    private DatabaseReference mDatabase;
    private Clasificacion clasificacion;
    private ProgressDialog progressDialog;
    private RenglonesAdapterArticulos adapterArticulos;
    private LinearLayout layoutColor;
    private RecyclerView rv;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_productos);

        //ADMOB
        MobileAds.initialize(this,
                getString(R.string.id_aplicacion_admob));

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //Obtengo la clasificacion seleccionada
        clasificacion=(Clasificacion) getIntent().getSerializableExtra(("clasificacion"));

        //Pongo el color del layout de la clasificacion
//        layoutColor = findViewById(R.id.layout_color);
//        layoutColor.setBackgroundColor(Color.parseColor(clasificacion.getColor()));

        //Muestro el nombre de la clasificacion
        nombreClasificacion =  findViewById(R.id.nombre_clasificacion);
        nombreClasificacion.setText(""+clasificacion.getNombre());

        //Muestro la imagen de la clasificacion
        imagenClasificacion = findViewById(R.id.imagen_clasificacion);
        Glide.with(getApplicationContext()).load(clasificacion.getFoto()).into(imagenClasificacion);

        //Muestro el progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando, por favor espere...");
        progressDialog.show();

        //Instancio el recicler view(la lista)
        rv = findViewById(R.id.recy);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        //Instancio ArrayList de Articulo
        allArticulo = new ArrayList<>();

        //Instancio mi adaptador
        adapterArticulos = new RenglonesAdapterArticulos(allArticulo, this);
        rv.setAdapter(adapterArticulos);

        //Obtengo los productos
        obtenerArticulos(clasificacion.getId());
        /**REVISO SI HAY CARRITO**/
        CoordinatorLayout coordinatorLayout =(CoordinatorLayout) findViewById(R.id.main_content);
        CompraActiva compraActiva = new CompraActiva(ListaProductosActivity.this,coordinatorLayout );
        compraActiva.checharCompraActiva();





    }
    private void obtenerArticulos(final String idClasificacion){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference().child("articulos").orderByChild("clasificacion").equalTo(idClasificacion).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allArticulo.removeAll(allArticulo);
                for (DataSnapshot snapshot :
                        dataSnapshot.getChildren()
                        ) {
                    Articulo articulo = new Articulo();

                    articulo.setId(snapshot.getKey());
                    articulo.setIdClasificacion(idClasificacion);
                    //En caso que no haya información
                    articulo.setNombre("");
                    articulo.setDescripcion("");
                    articulo.setImagen("");
                    articulo.setPrecio(0);
                    articulo.setCosto(0);
                    articulo.setUnidades(0);
                    articulo.setEstadoActivado(0);
                    //Reviso si hay informacion
                    if(snapshot.child("nombre").getValue()!=null){
                        articulo.setNombre(snapshot.child("nombre").getValue().toString());
                    }
                    if(snapshot.child("descripcion").getValue()!=null){
                        articulo.setDescripcion(snapshot.child("descripcion").getValue().toString());
                    }
                    if(snapshot.child("imagen").getValue()!=null){
                        articulo.setImagen(snapshot.child("imagen").getValue().toString());
                    }
                    if(snapshot.child("precio").getValue()!=null){
                        articulo.setPrecio(Float.valueOf(snapshot.child("precio").getValue().toString()));
                    }
                    if(snapshot.child("costo").getValue()!=null){
                        articulo.setCosto(Float.valueOf(snapshot.child("costo").getValue().toString()));
                    }
                    if(snapshot.child("unidades").getValue()!=null){
                        articulo.setUnidades(Integer.valueOf(snapshot.child("unidades").getValue().toString()));
                    }
                    if(snapshot.child("estado_activo").getValue()!=null){
                        articulo.setEstadoActivado(Integer.valueOf( snapshot.child("estado_activo").getValue().toString()) );
                    }
                    //Lo añado al arraylist
                    allArticulo.add(articulo);
                }

                progressDialog.dismiss();
                //Reviso que haya clasificaciones
                if(allArticulo.isEmpty()){
                    //En caso que no haya
                    TextView vacio = findViewById(R.id.mensaje_vacio);
                    vacio.setText("No hay artículos activos");
                }else{
                    //Inicio el adaptador para los renglones
                    adapterArticulos.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        /**REVISO SI HAY CARRITO**/
        CoordinatorLayout coordinatorLayout =(CoordinatorLayout) findViewById(R.id.main_content);
        CompraActiva compraActiva = new CompraActiva(ListaProductosActivity.this,coordinatorLayout );
        compraActiva.checharCompraActiva();
    }
}
