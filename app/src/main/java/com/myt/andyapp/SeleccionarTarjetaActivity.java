package com.myt.andyapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
import com.myt.andyapp.Model.MySingleton;
import com.myt.andyapp.Model.NonScrollListView;
import com.myt.andyapp.Model.Producto;
import com.myt.andyapp.Model.ToastPersonalizado;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Rafa Pc on 04/04/2018.
 */

public class SeleccionarTarjetaActivity extends AppCompatActivity {
    BDCarritoHelper bdCarritoHelper;
    private TextView tvTotal, tvDireccion, tvSinTarjetas;
    private ArrayList<Carrito> allCarrito;
    private  float total;
    private Button btnRealizarPedido, btnAgregarTarjeta;
    private DatabaseReference mDatabase;
    private Producto producto;
    private ToastPersonalizado toastPersonalizado;
    private String nombreCliente, direccionEntrega ;
    private RadioGroup listaTarjetas;
    private String formaPago, idCostumerConekta, nombreUser, emailUser, telefonoUser, idUser;
    private TextInputLayout textInputLayoutDireccion;
    private int idFormaPago=0;
    private HashMap<Integer, String> hmTarjetas;
    private FirebaseManejador firebaseManejador;
    private ProgressDialog progressDialog;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_tarjeta);
        /**HARDCODE*/
        idCostumerConekta ="cus_2jbqCkaVJRyjZbQ8F";
        /**ADMOB**/
        MobileAds.initialize(this,
                getString(R.string.id_aplicacion_admob));

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        /**OBTENGO INFO DE ACTIVIDAD**/
        //Obtengo informacion cuando no hay idCostumer
        nombreUser= getIntent().getExtras().getString("nombreUser");
        telefonoUser= getIntent().getExtras().getString("telefonoUser");
        emailUser= getIntent().getExtras().getString("emailUser");
        direccionEntrega= getIntent().getExtras().getString("direccionEntrega");
        idCostumerConekta= getIntent().getExtras().getString("idCostumerConekta");

        progressDialog = new ProgressDialog(SeleccionarTarjetaActivity.this);
        progressDialog.setMessage("Cargando tarjetas, por favor espere...");
        progressDialog.show();

        firebaseManejador = new FirebaseManejador(getApplicationContext());

        toastPersonalizado = new ToastPersonalizado(SeleccionarTarjetaActivity.this);

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
        /**INSTANCIO DIRECCION ENTREGA**/

        tvDireccion =  findViewById(R.id.lugar_entrega);
        tvDireccion.setText("Dirección de entrega: "+direccionEntrega);

        textInputLayoutDireccion = findViewById(R.id.text_input_direccion);
        /**TARJETAS**/

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String idUser= user.getUid();

        hmTarjetas = new HashMap<>();
        if(idCostumerConekta.isEmpty()){
            tvSinTarjetas = findViewById(R.id.no_tarjetas);
            tvSinTarjetas.setVisibility(View.VISIBLE);
            progressDialog.dismiss();

        }else{
            listaTarjetas = findViewById(R.id.radios_tarjetas);
            obtenerTarjetas(idCostumerConekta);
        }



        /**BOTON REALIZAR PEDIDO**/
        btnRealizarPedido = findViewById(R.id.btn_confirmar_pedido);
        btnRealizarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(SeleccionarTarjetaActivity.this);
                progressDialog.setMessage("Creando pedido, por favor espere...");
                progressDialog.show();


                if(listaTarjetas.getCheckedRadioButtonId()!=0){

                    Log.d("RADIOSID",String.valueOf(listaTarjetas.getCheckedRadioButtonId()));
                    Log.d("RADIOSID2",hmTarjetas.get(listaTarjetas.getCheckedRadioButtonId()));
                    hmTarjetas.get(listaTarjetas.getCheckedRadioButtonId());
                    realizarCargo(idCostumerConekta, hmTarjetas.get(listaTarjetas.getCheckedRadioButtonId()));

                }else{
                    progressDialog.dismiss();
                    toastPersonalizado.crearToast("Hay un problema con tu tarjeta", null);
                }

            }
        });


        /**BOTON AGREGAR TARJETA**/
        btnAgregarTarjeta = findViewById(R.id.agregar_tarjeta);
        btnAgregarTarjeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CheckOutActivity.class);
                intent.putExtra("idCostumerConekta",  idCostumerConekta);
                intent.putExtra("idUser",  idUser);
                intent.putExtra("nombreUser",  nombreUser);
                intent.putExtra("telefonoUser",  telefonoUser);
                intent.putExtra("emailUser",  emailUser);
                startActivity(intent);


            }
        });





    }
    public void regresar(View v){
        finish();
    }

    private void obtenerTarjetas(String idCustomer){
        HashMap<String, Object> map = new HashMap<>();// Mapeo previo

        map.put("id_customer", idCustomer);
        JSONObject jsonObject = new JSONObject(map);
        String url = "https://www.themyt.com/andy/InformacionPago.php";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject,
                new Response.Listener<JSONObject>() {

                    int estado=0;
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Iterator<?> keys = response.keys();

                            while( keys.hasNext() ) {
                                String key = (String)keys.next();
                                if ( response.get(key) instanceof JSONObject ) {
                                    JSONObject jsonObjectData= new JSONObject (((JSONObject) response.get(key)).get("data").toString());

                                    Iterator<?> keys2 = jsonObjectData.keys();

                                    while( keys2.hasNext() ) {

                                        String key2 = (String)keys2.next();
                                        crearRadio( ((JSONObject) jsonObjectData.get(key2)).getString("last4").toString() , ((JSONObject) jsonObjectData.get(key2)).getString("default").toString()  ,((JSONObject) jsonObjectData.get(key2)).getString("brand").toString() );
                                        hmTarjetas.put(Integer.valueOf( ((JSONObject) jsonObjectData.get(key2)).getString("last4").toString() ),  ((JSONObject) jsonObjectData.get(key2)).getString("id").toString() );
                                    }
                                }
                            }
                            estado= response.getInt("estado");
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                toastPersonalizado.crearToast("Error de conexión, revisa tu conexión a internet e intenta nuevamente", null);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("User-agent", "My useragent");
                return headers;
            }


        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);


    }

    public void crearRadio(String last4, String porDefecto, String tipo){
        int icono=0;

        switch (tipo){
            case "MC":
                icono=R.drawable.ic_mastercard2;
                break;
            case "VISA":
                icono=R.drawable.ic_visa2;
                break;
            case "AMERICAN_EXPRESS":
                icono=R.drawable.ic_amex2;
                break;

        }

        RadioButton rbn = new RadioButton(this);
        rbn.setId(Integer.parseInt(last4));
        rbn.setText(" " + last4);
        rbn.setCompoundDrawablesWithIntrinsicBounds(icono,0, 0, 0);
        RadioGroup.LayoutParams params_soiled = new RadioGroup.LayoutParams(getBaseContext(), null);
        params_soiled.setMargins(0, 20, 0, 0);
        rbn.setLayoutParams(params_soiled);
//        Typeface instrucciones = Typeface.createFromAsset(getResources().getAssets(),"font/mitr_regular.ttf");
//        rbn.setTypeface(instrucciones);

        if(porDefecto.equals("true")){
            rbn.setChecked(true);
        }
        listaTarjetas.addView(rbn);


    }

    private void realizarCargo(String idCustomer , String idTarjeta){
        HashMap<String, Object> map = new HashMap<>();// Mapeo previo

        map.put("id_customer", idCustomer);
        map.put("monto", total*100);
        map.put("concepto", getString(R.string.concepto_pago));
        map.put("unidades", 1);
        map.put("tarjeta", idTarjeta);
        map.put("metodo_pago", 1);
        JSONObject jsonObject = new JSONObject(map);
        String url = "https://www.themyt.com/andy/RealizarCargo.php";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject,
                new Response.Listener<JSONObject>() {

                    int estado=0;
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.d("RESPUESTA", response.toString());

                            estado= response.getInt("estado");
                            if (estado==1 && response.getString("payment_status").equals("paid")) {
                                Boolean respuesta=firebaseManejador.guardarPedido(nombreUser,direccionEntrega, "Tarjeta", response.getString("last4"), response.getString("auth_code"),  response.getString("order_id"));
                                if (respuesta == true) {
                                    progressDialog.dismiss();
                                    toastPersonalizado.crearToast("Pedido realizado con éxtito", getDrawable(R.drawable.correcto));
                                    finish();
                                }
                            }else if(estado==2){
                                progressDialog.dismiss();
                                toastPersonalizado.crearToast("La tarjeta ingresada ha sido declinada por el banco. Por favor intenta con otra tarjeta.",  getDrawable(R.drawable.error));
                            }else{
                                progressDialog.dismiss();
                                toastPersonalizado.crearToast("Error al procesar tu pago, intenta nuevamente", null);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                toastPersonalizado.crearToast("Error de conexión, revisa tu conexión a internet e intenta nuevamente", null);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("User-agent", "My useragent");
                return headers;
            }


        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);

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
                obtenerTarjetas(idCostumerConekta);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        progressDialog = new ProgressDialog(SeleccionarTarjetaActivity.this);
        progressDialog.setMessage("Cargando tarjetas, por favor espere...");
        progressDialog.show();
        obtenerNombreDistribuidor();
        hmTarjetas.clear();
        listaTarjetas = findViewById(R.id.radios_tarjetas);
        listaTarjetas.removeAllViews();

    }
}
