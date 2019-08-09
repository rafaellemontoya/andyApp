package com.myt.andyapp;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.database.DatabaseReference;
import com.myt.andyapp.Model.BDCarritoHelper;
import com.myt.andyapp.Model.Carrito;
import com.myt.andyapp.Model.MySingleton;
import com.myt.andyapp.Model.Producto;
import com.myt.andyapp.Model.ToastPersonalizado;
import io.conekta.conektasdk.Conekta;
import io.conekta.conektasdk.Card;
import io.conekta.conektasdk.Token;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.security.AccessController.getContext;

/**
 * Created by Rafa Pc on 02/04/2018.
 */

public class RegistrarTarjetaActivity extends AppCompatActivity {
    BDCarritoHelper bdCarritoHelper;
    private TextView tvMensaje, tvFecha;
    private ArrayList<Carrito> allCarrito;
    private float total;
    private Button btnRealizarPedido, btnMetodoPago,btnCancelarPedido;
    private DatabaseReference mDatabase;
    private Producto producto;
    private ToastPersonalizado toastPersonalizado;
    private String nombreCliente, direccion;
    private RadioGroup tipoPago;
    private String formaPago;
    private EditText etNumeroTarjeta, etVencimientoMes,etVencimientoYear, etCCV;
    private TextInputLayout textInputLayoutDireccion;
    private int idFormaPago = 0;
    private Card cardToSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_tarjeta);

        tvMensaje = findViewById(R.id.mensaje);





        btnRealizarPedido = findViewById(R.id.btn);
        btnRealizarPedido.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                etNumeroTarjeta = findViewById(R.id.numero_tarjeta);
                etVencimientoMes = findViewById(R.id.vencimiento_mes);
                etVencimientoYear = findViewById(R.id.vencimiento_year);
                etCCV = findViewById(R.id.ccv);

                Conekta.setPublicKey("key_LuH3qP8hZxz6frsyzxX6HrQ");
                Conekta.setApiVersion("1.0.0");                       //optional
                Conekta.collectDevice(RegistrarTarjetaActivity.this);

                Card card = new Card("Fulanito Pérez", etNumeroTarjeta.getText().toString(), etCCV.getText().toString(), etVencimientoMes.getText().toString(), etVencimientoYear.getText().toString());
                Token token = new Token(RegistrarTarjetaActivity.this);

                token.onCreateTokenListener( new Token.CreateToken(){
                    @Override
                    public void onCreateTokenReady(JSONObject data) {
                        try {
                            Log.d("TARJETA", data.getString("id"));
                            enviarId( data.getString("id"));
                            //Send the id to the webservice.
                        } catch (Exception err) {
                            //Do something on error
                        }
                    }
                });

                token.create(card);
            }
        });

        btnMetodoPago = findViewById(R.id.btn_metodo_pago);
        btnMetodoPago.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                metodoPago("cus_2iMFbGjwpknHJiCxp");
            }
        });


    }

    private void enviarId(String token){
        HashMap<String, Object> map = new HashMap<>();// Mapeo previo

        map.put("token", token);
        map.put("id_customer", "cus_2iMFbGjwpknHJiCxp");
        map.put("nombre", "Rafael Montoya");
        map.put("email", "r@d.com");
        map.put("telefono", "555555555");
        map.put("monto", 10000);
        map.put("concepto", "Compra en M y T");
        map.put("unidades", 1);
        map.put("tarjeta", "src_2iMFbGjwpknHJiCxq");
        JSONObject jsonObject = new JSONObject(map);
        String url = "http://www.themyt.com/conekta_web/ConexionAndroid.php";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject,
                new Response.Listener<JSONObject>() {

                    int estado=0;
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("RESPUESTA", response.toString());
                            estado= response.getInt("estado");
                            if(estado==1) {
                                Toast.makeText(getApplicationContext(), "PAgo Bien",Toast.LENGTH_LONG);

                            }else {
//                                String mensaje= (String) response.get("mensaje");
//                                toastPersonalizado.crearToast(mensaje,null);
                                Toast.makeText(getApplicationContext(), "Error:",Toast.LENGTH_LONG);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                toastPersonalizado.crearToast("Error de conexión",null);
                Toast.makeText(getApplicationContext(), "Errorde conexion:",Toast.LENGTH_LONG);
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
    private void metodoPago(String idCliente){
        HashMap<String, Object> map = new HashMap<>();// Mapeo previo

        map.put("id_customer", idCliente);
        JSONObject jsonObject = new JSONObject(map);
        String url = "http://www.themyt.com/conekta_web/InformacionPago.php";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject,
                new Response.Listener<JSONObject>() {

                    int estado=0;
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("RESPUESTA", response.toString());
                            estado= response.getInt("estado");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                toastPersonalizado.crearToast("Error de conexión",null);
                Toast.makeText(getApplicationContext(), "Errorde conexion:",Toast.LENGTH_LONG);
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
}