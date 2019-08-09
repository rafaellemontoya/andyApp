package com.myt.andyapp.Model;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.myt.andyapp.R;

import java.util.ArrayList;

/**
 * Created by Rafa Pc on 30/03/2018.
 */

public class CustomListViewCalificarAdapter extends ArrayAdapter<Carrito> implements View.OnClickListener {
    ArrayList<Carrito> carrito;
    Context mContext;
    // View lookup cache
    private static class ViewHolder {
        TextView tvNombre, tvNotas;
        RatingBar ratingBar;
    }
    public CustomListViewCalificarAdapter(ArrayList<Carrito> datos, Context context){
        super(context, R.layout.elemento_lista_calificacion, datos);
        this.carrito = datos;
        this.mContext = context;

    }


    @Override
    public void onClick(View v) {

        int posicion = (Integer) v.getTag();
        Object object = getItem(posicion);
        Carrito tinaco=(Carrito)object;
//        Snackbar.make(v, "Release date " +tinaco.getIdCarrito(), Snackbar.LENGTH_LONG)
//                .setAction("No action", null).show();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Carrito carrito = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final CustomListViewCalificarAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new CustomListViewCalificarAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.elemento_lista_calificacion, parent, false);
            viewHolder.tvNombre= (TextView) convertView.findViewById(R.id.tvNombre);
            viewHolder.tvNotas = (TextView) convertView.findViewById(R.id.tv_nota);
            viewHolder.ratingBar = convertView.findViewById(R.id.calificacion_articulo);


            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CustomListViewCalificarAdapter.ViewHolder) convertView.getTag();
            result=convertView;
        }



        viewHolder.tvNombre.setText(""+carrito.getNombre());
        viewHolder.tvNotas.setText(""+carrito.getNotas());

        viewHolder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                carrito.setCalificacion(Math.round(viewHolder.ratingBar.getRating()));
            }
        });


        // Return the completed view to render on screen
        return convertView;
    }
}
