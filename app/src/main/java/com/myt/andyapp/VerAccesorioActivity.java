package com.myt.andyapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.myt.andyapp.Model.BDCarritoHelper;
import com.myt.andyapp.Model.Carrito;
import com.myt.andyapp.Model.Producto;
import com.myt.andyapp.Model.ToastPersonalizado;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Rafa Pc on 30/03/2018.
 */

public class VerAccesorioActivity extends AppCompatActivity {
    Producto producto;

    TextView nombreP, descripcion;
    ImageView imagen;
    private EditText etNota;
    Button btnAgregar;
    private DatabaseReference mDatabase;
    private int unidadesFinales;
    private float importeFinal;
    ToastPersonalizado toastPersonalizado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_accesorios);

        toastPersonalizado= new ToastPersonalizado(this);

        //Instancio la base de datos de fierebase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Mostrar información
        producto=(Producto) getIntent().getSerializableExtra(("producto"));

        imagen = findViewById(R.id.qStatusImage);
        Glide.with(getApplicationContext()).load(producto.getFoto()).into(imagen);
        nombreP = findViewById(R.id.nombre_producto);
        nombreP.setText(""+producto.getNombre());
        descripcion= findViewById(R.id.descripcion_producto);
        descripcion.setText(""+producto.getDescripcion());

        btnAgregar = findViewById(R.id.btn_agregar);
        btnAgregar.setText("AÑADIR POR MXN "+producto.getPrecio());

        unidadesFinales =1;
        importeFinal = producto.getPrecio();
        NumberPicker np = findViewById(R.id.unidades_producto);
        np.setMinValue(1);
        np.setMaxValue(10);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                //Display the newly selected number from picker
                float importe=producto.getPrecio()* newVal;
                btnAgregar.setText("AÑADIR POR MXN "+importe);
                unidadesFinales = newVal;
                importeFinal= importe;
            }
        });

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**MUESTRO FECHA**/
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm", Locale.getDefault());
                Date date = new Date();

                final String fecha = dateFormat.format(date);

                /**OBTENGO NOTA**/

                etNota = findViewById(R.id.et_nota);
                String nota = etNota.getText().toString();

                Carrito carrito = new Carrito();
                carrito.setId(producto.getId());
                carrito.setNombre(producto.getNombre());
                carrito.setUnidades(unidadesFinales);
                carrito.setImporte(importeFinal);
                carrito.setPrecio(producto.getPrecio());
                carrito.setNotas(nota);
//                carrito.setPosicion(producto.getPosicion());
//                carrito.setCapacidad(producto.getCapacidad());
//                carrito.setColor(producto.getColor());
                //Instancio a bd de carrito y guardo
                BDCarritoHelper bdCarritoHelper = new BDCarritoHelper(getApplicationContext());
                bdCarritoHelper.insertCarrito(carrito);
                /**AGREGAR A FIREBASE**/
//                //hago un push para obtenr el id en firebase
//                String key = mDatabase.child("pedidos").push().getKey();
//                //convierto a Map pedido
//                HashMap<String, Object> hmPedido= new HashMap<>();
//                hmPedido.put("modelo", producto.getId());
//                hmPedido.put("cliente", 1);
//                hmPedido.put ("unidades", unidadesFinales);
//                hmPedido.put("fecha", fecha);
//                hmPedido.put("total", importeFinal);
//                hmPedido.put("nota", nota);
//
//                Map<String, Object> mapPedido = new HashMap<>();
//                mapPedido.put("/pedidos/"+key, hmPedido);
//                mDatabase.updateChildren(mapPedido);
                toastPersonalizado.crearToast("Producto añadido al carrito con éxito", getApplicationContext().getResources().getDrawable(R.drawable.correcto));
                finish();



            }
        });

    }
}