package com.myt.andyapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.myt.andyapp.Model.Articulo;
import com.myt.andyapp.Model.BDCarritoHelper;
import com.myt.andyapp.Model.Carrito;
import com.myt.andyapp.Model.Producto;
import com.myt.andyapp.Model.ToastPersonalizado;

import com.bumptech.glide.Glide;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Rafa Pc on 30/03/2018.
 */

public class VerArticuloActivity extends AppCompatActivity {
    private Articulo articulo;

    TextView nombreP, descripcion, capacidad, color, posicion;
    ImageView imagen;
    private EditText etNota;
    Button btnAgregar, btnAtras;
    private DatabaseReference mDatabase;
    private int unidadesFinales;
    private float importeFinal;
    NumberPicker np;
    ToastPersonalizado toastPersonalizado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_articulo);

        toastPersonalizado= new ToastPersonalizado(this);

        //Instancio la base de datos de fierebase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Mostrar información
        articulo=(Articulo) getIntent().getSerializableExtra(("articulo"));

        imagen = findViewById(R.id.qStatusImage);
        Glide.with(getApplicationContext()).load(articulo.getImagen()).into(imagen);
        nombreP = findViewById(R.id.nombre_producto);
        nombreP.setText(""+articulo.getNombre());

        descripcion= findViewById(R.id.descripcion_producto);
        descripcion.setText(""+articulo.getDescripcion());

        btnAgregar = findViewById(R.id.btn_agregar);
        btnAgregar.setText("AÑADIR POR MXN "+articulo.getPrecio());

        unidadesFinales =1;
        importeFinal = articulo.getPrecio();

        np = findViewById(R.id.unidades_producto);
        np.setMinValue(1);
        np.setMaxValue(200);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                //Display the newly selected number from picker
                float importe=articulo.getPrecio()* newVal;
                btnAgregar.setText("AÑADIR POR MXN "+importe);
                unidadesFinales = newVal;
                importeFinal= importe;
            }
        });
        /**CHECKBOX**/

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
                carrito.setId(articulo.getId());
                carrito.setNombre(articulo.getNombre());
                carrito.setUnidades(unidadesFinales);
                DecimalFormat df = new DecimalFormat("#.00");
                carrito.setImporte(Float.valueOf(df.format(importeFinal)));
                carrito.setPrecio(articulo.getPrecio());
                carrito.setNotas(nota);
                Log.d("CARRITO", carrito.getId());
                Log.d("CARRITO", carrito.getNombre());
                Log.d("CARRITO", carrito.getId());
                Log.d("CARRITO", carrito.getId());
                //Instancio a bd de carrito y guardo
                BDCarritoHelper bdCarritoHelper = new BDCarritoHelper(getApplicationContext());
                bdCarritoHelper.insertCarrito(carrito);
                toastPersonalizado.crearToast("Producto añadido al carrito con éxito", getApplicationContext().getResources().getDrawable(R.drawable.correcto));
                finish();



            }
        });

    }
    public void atras(View v){
        finish();
    }
}