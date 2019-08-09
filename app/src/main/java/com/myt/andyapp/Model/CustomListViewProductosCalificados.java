package com.myt.andyapp.Model;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.myt.andyapp.R;

import java.util.ArrayList;

/**
 * Created by Rafa Pc on 30/03/2018.
 */

public class CustomListViewProductosCalificados extends ArrayAdapter<Carrito> implements View.OnClickListener {
    ArrayList<Carrito> carrito;
    Context mContext;
    int estadoCalificacion;
    // View lookup cache
    private static class ViewHolder {
        TextView tvNombre, tvMensajeSinCalificacion;
        TextView tvUnidades;
        TextView tvImporte, tvNota;
        RatingBar ratingBar;

    }
    public CustomListViewProductosCalificados(ArrayList<Carrito> datos, Context context, int estadoCalificacion){
        super(context, R.layout.elemento_lista_productos_calificados, datos);
        this.carrito = datos;
        this.mContext = context;
        this.estadoCalificacion= estadoCalificacion;

    }


    @Override
    public void onClick(View v) {

        int posicion = (Integer) v.getTag();
        Object object = getItem(posicion);
        Carrito carrito=(Carrito)object;
//        Snackbar.make(v, "Release date " +carrito.getNombre(), Snackbar.LENGTH_LONG)
//                .setAction("No action", null).show();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Carrito carrito = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        CustomListViewProductosCalificados.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new CustomListViewProductosCalificados.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.elemento_lista_productos_calificados, parent, false);
            viewHolder.tvUnidades= (TextView) convertView.findViewById(R.id.tv_unidades);
            viewHolder.tvNombre = (TextView) convertView.findViewById(R.id.tv_nombre);
            viewHolder.tvImporte = (TextView) convertView.findViewById(R.id.tv_importe);
            viewHolder.tvNota = (TextView) convertView.findViewById(R.id.tv_nota);
            viewHolder.tvMensajeSinCalificacion = (TextView) convertView.findViewById(R.id.mensaje_sin_calificacion);
            viewHolder.ratingBar = convertView.findViewById(R.id.ratting_articulo);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CustomListViewProductosCalificados.ViewHolder) convertView.getTag();
            result=convertView;
        }



        viewHolder.tvUnidades.setText(""+carrito.getUnidades());
        viewHolder.tvNombre.setText(carrito.getNombre());
        viewHolder.tvImporte.setText("$"+ carrito.getImporte());
        viewHolder.tvNota.setText(""+ carrito.getNotas());

        if(estadoCalificacion==1){
            //ya se calificaron
            viewHolder.tvMensajeSinCalificacion.setVisibility(View.GONE);
            viewHolder.ratingBar.setVisibility(View.VISIBLE);
            viewHolder.ratingBar.setRating(carrito.getCalificacion());
        }else{
            //no se han calificado
            viewHolder.ratingBar.setVisibility(View.GONE);
            viewHolder.tvMensajeSinCalificacion.setVisibility(View.VISIBLE);
        }
        // Return the completed view to render on screen
        return convertView;
    }
}
