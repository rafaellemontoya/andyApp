package com.myt.andyapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.myt.andyapp.Model.BDCarritoHelper;
import com.myt.andyapp.Model.Carrito;
import com.myt.andyapp.Model.CustomListViewCheckoutAdapter;
import com.myt.andyapp.Model.FirebaseManejador;
import com.myt.andyapp.Model.NonScrollListView;
import com.myt.andyapp.Model.Producto;
import com.myt.andyapp.Model.ToastPersonalizado;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Rafa Pc on 30/03/2018.
 */

public class VerCarritoActivity  extends AppCompatActivity {
    BDCarritoHelper bdCarritoHelper;
    private TextView tvTotal, tvFecha;
    private ArrayList<Carrito> allCarrito;
    private  float total;
    private Button btnRealizarPedido, btnCancelarPedido;
    private DatabaseReference mDatabase;
    private Producto producto;
    private ToastPersonalizado toastPersonalizado;
    private String nombreCliente, direccion,idCostumerConekta ;
    private RadioGroup tipoPago;
    private String formaPago, nombreUser, telefonoUser, emailUser;
    private EditText etDireccion;
    private TextInputLayout textInputLayoutDireccion;
    private int idFormaPago=0;
    private FirebaseManejador firebaseManejador;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_carrito);

        /**ADMOB**/
        MobileAds.initialize(this,
                getString(R.string.id_aplicacion_admob));

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        /**Creo el manejador**/
        firebaseManejador = new FirebaseManejador(getApplicationContext());

        /**OBTENGO EL NOMBRE DEL CLIENTE**/
        obtenerNombreDistribuidor();

        toastPersonalizado = new ToastPersonalizado(VerCarritoActivity.this);




        /**MUESTRO LISTA CARRITO**/
        total=0;

        bdCarritoHelper = new BDCarritoHelper(getApplicationContext());

        allCarrito = bdCarritoHelper.getAllCarrito();

        NonScrollListView listView = (NonScrollListView) findViewById(R.id.lv_productos);

        //declaramos el adaptador

        CustomListViewCheckoutAdapter adapter = new CustomListViewCheckoutAdapter( allCarrito, getApplicationContext());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Carrito carrito= allCarrito.get(position);

                Snackbar.make(view, carrito.getNombre()+" Id->"+carrito.getId(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
            }
        });
        /**TOTAL**/
        for (Carrito carrito: allCarrito) {
            total+=carrito.getImporte();
        }

        tvTotal = (TextView) findViewById(R.id.tv_total);
        DecimalFormat df = new DecimalFormat("#.00");
        tvTotal.setText("$ "+df.format(total));
        /**INSTANCIO FORMA DE PAGO**/
        etDireccion = (EditText) findViewById(R.id.et_direccion_entrega);
        textInputLayoutDireccion = findViewById(R.id.text_input_direccion);
        /**OBTENGO LA FORMA DE PAGO**/
        tipoPago= (RadioGroup) findViewById(R.id.forma_pago);
        tipoPago.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {

                switch (id){
                    case R.id.radio_efectivo:
                        formaPago="Efectivo";
                        idFormaPago=1;
                        break;
                    case R.id.radio_transferencia:
                        formaPago="Depósito";
                        idFormaPago=3;
                        break;
                    case R.id.radio_credito:
                        formaPago="Tarjeta de crédito";
                        idFormaPago=2;
                        break;

                }
            }
        });
        /**BOTON REALIZAR PEDIDO**/
        btnRealizarPedido = findViewById(R.id.btn_confirmar_pedido);
        btnRealizarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validar()){
                    switch (idFormaPago){
                        case 1:
                            Boolean respuesta= firebaseManejador.guardarPedido(nombreCliente, etDireccion.getText().toString(), "Efectivo", "-","-","-");
                            if(respuesta==true){
                                toastPersonalizado.crearToast("Tu pedido se ha realizado con éxito", getApplicationContext().getResources().getDrawable(R.drawable.correcto));
                                finish();
                            }
                            break;
                        case 2:
                            Intent intent = new Intent(getApplicationContext(), SeleccionarTarjetaActivity.class);
                            intent.putExtra("direccionEntrega",  etDireccion.getText().toString());
                            intent.putExtra("idCostumerConekta",  idCostumerConekta);
                            intent.putExtra("nombreUser",  nombreCliente);
                            intent.putExtra("emailUser",  emailUser);
                            intent.putExtra("telefonoUser",  telefonoUser);
                            startActivity(intent);
                            break;
                        case 3:
                              Boolean respuestaDeposito= firebaseManejador.guardarPedido(nombreCliente, etDireccion.getText().toString(), "Depósito", "-","-","-");
                            if(respuestaDeposito==true){
                                toastPersonalizado.crearToast("Tu pedido se ha realizado con éxito", getApplicationContext().getResources().getDrawable(R.drawable.correcto));
                                finish();
                            }
                            break;

                    }
                }


            }
        });
        /**BOTON CANCELAR PEDIDO**/

        btnCancelarPedido = (Button) findViewById(R.id.btn_cancelar_pedido);
        btnCancelarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bdCarritoHelper.deleteCarrito();
                finish();
                toastPersonalizado.crearToast("Tu carrito ha sido eliminado", getApplicationContext().getResources().getDrawable(R.drawable.correcto));
            }
        });




    }
    private void obtenerNombreDistribuidor(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String idUsuario= user.getUid();
        //TODO OBTENERLO DE FIREBASE
//        idCostumer="cus_2iMFbGjwpknHJiCxp";

        database.getReference().child("clientes").orderByKey().equalTo(idUsuario).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                idCostumerConekta="";
                nombreCliente="";
                emailUser="";
                telefonoUser="";
                if(dataSnapshot.child(idUsuario).child("id_conekta").getValue()!=null){

                    idCostumerConekta= dataSnapshot.child(idUsuario).child("id_conekta").getValue().toString();
                }
                if(dataSnapshot.child(idUsuario).child("nombre").getValue()!=null){

                    nombreCliente = dataSnapshot.child(idUsuario).child("nombre").getValue().toString();
                }
                if( dataSnapshot.child(idUsuario).child("email").getValue()!=null){

                    emailUser= dataSnapshot.child(idUsuario).child("email").getValue().toString();
                }
                if(dataSnapshot.child(idUsuario).child("telefono").getValue()!=null){

                    telefonoUser = dataSnapshot.child(idUsuario).child("telefono").getValue().toString();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void regresar(View v){
        finish();
    }
    private Boolean validar(){

        boolean valido = false;
        if (etDireccion.getText().toString().isEmpty()){
            textInputLayoutDireccion.setError("Se requiere una dirección");
            valido= false;
        }else{
            valido=true;

        }
        if (tipoPago.getCheckedRadioButtonId() == -1)
        {
            // no radio buttons are checked
            Toast.makeText(getApplicationContext(),"Selecciona una forma de pago", Toast.LENGTH_LONG).show();
            valido= false;
        }
        return valido;
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        finish();
    }
}
