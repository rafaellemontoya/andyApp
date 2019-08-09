package com.myt.andyapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.myt.andyapp.Model.CustomAdapter;
import com.myt.andyapp.Model.CustomListViewCalificarAdapter;
import com.myt.andyapp.Model.Model;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myt.andyapp.Model.NonScrollListView;
import com.myt.andyapp.Model.Pedido;
import com.myt.andyapp.Model.Tinaco;
import com.myt.andyapp.Model.ToastPersonalizado;

import java.util.ArrayList;

/**
 * Created by Rafa Pc on 30/03/2018.
 */

public class CalificarActivity extends AppCompatActivity {
    private RatingBar ratingBar;
    private TextView  tvRepartidor;
    private NonScrollListView lv;
    private Button btnCalificar;
    private float calificacionChofer;
    private ToastPersonalizado toastPersonalizado;

    ArrayList<Tinaco> tinacos;


    ProgressDialog progressDialog;
    private Pedido pedido;


    private CustomListViewCalificarAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calificar);

        /**OBTENGO EL PEDIDO***/
        Intent intentKey = getIntent();
        pedido= (Pedido) getIntent().getSerializableExtra(("pedido"));

        ratingBar = (RatingBar) findViewById(R.id.calificacion_chofer);
        //Reparitdor
        tvRepartidor = (TextView) findViewById(R.id.nombre_repartidor);
        tvRepartidor.setText(""+pedido.getRepartidor());

        toastPersonalizado = new ToastPersonalizado(CalificarActivity.this);
        //Instancio tinacos
        tinacos = new ArrayList<>();
        //Instancio lista no scroll
        lv = (NonScrollListView) findViewById(R.id.lv_tinacos_calificar);


        //Mando llamar la lista de articulos
        customAdapter = new CustomListViewCalificarAdapter(pedido.getCarritos(),getApplicationContext());
        lv.setAdapter(customAdapter);




        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

//                txtRatingValue.setText(String.valueOf(rating));
                calificacionChofer=rating;

            }
        });

//        obtenerTinacos();
        //Barra de progreso
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Guardando, por favor espere...");






        /**BOTON PARA GUARDAR CALIFICACION**/
        btnCalificar= findViewById(R.id.btn_guardar_calificar);
        btnCalificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                int error=0;
                for (int i = 0; i < customAdapter.getCount(); i++) {


                    mDatabase.child("pedidos/"+pedido.getId()+"/productos/"+customAdapter.getItem(i).getId()+"/calificacion").setValue(Math.round(customAdapter.getItem(i).getCalificacion()));

                }
                mDatabase.child("pedidos/"+pedido.getId()+"/estado_calificacion").setValue(1);
                mDatabase.child("pedidos/"+pedido.getId()+"/calificacion_repartidor").setValue(Math.round(calificacionChofer));
                progressDialog.dismiss();
                toastPersonalizado.crearToast("Pedido calificado correctamente",getApplicationContext().getResources().getDrawable(R.drawable.correcto));
                finish();
            }
        });
    }



}