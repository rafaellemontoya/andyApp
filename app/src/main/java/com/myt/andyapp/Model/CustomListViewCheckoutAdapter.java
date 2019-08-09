package com.myt.andyapp.Model;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.myt.andyapp.R;

import java.util.ArrayList;

/**
 * Created by Rafa Pc on 30/03/2018.
 */

public class CustomListViewCheckoutAdapter  extends ArrayAdapter<Carrito> implements View.OnClickListener {
    ArrayList<Carrito> carrito;
    Context mContext;
    // View lookup cache
    private static class ViewHolder {
        TextView tvNombre;
        TextView tvUnidades;
        TextView tvImporte, tvNota;
    }
    public CustomListViewCheckoutAdapter(ArrayList<Carrito> datos, Context context){
        super(context, R.layout.elemento_lista_carrito, datos);
        this.carrito = datos;
        this.mContext = context;

    }


    @Override
    public void onClick(View v) {

        int posicion = (Integer) v.getTag();
        Object object = getItem(posicion);
        Carrito carrito=(Carrito)object;
        Snackbar.make(v, "Release date " +carrito.getNombre(), Snackbar.LENGTH_LONG)
                .setAction("No action", null).show();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Carrito carrito = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        CustomListViewCheckoutAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new CustomListViewCheckoutAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.elemento_lista_carrito, parent, false);
            viewHolder.tvUnidades= (TextView) convertView.findViewById(R.id.tv_unidades);
            viewHolder.tvNombre = (TextView) convertView.findViewById(R.id.tv_nombre);
            viewHolder.tvImporte = (TextView) convertView.findViewById(R.id.tv_importe);
            viewHolder.tvNota = (TextView) convertView.findViewById(R.id.tv_nota);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CustomListViewCheckoutAdapter.ViewHolder) convertView.getTag();
            result=convertView;
        }


        if(carrito.getUnidades()==0){
            viewHolder.tvUnidades.setText("COMPLETAR");
            viewHolder.tvImporte.setText("-");
        }else{

            viewHolder.tvUnidades.setText(""+carrito.getUnidades());
            viewHolder.tvImporte.setText("$"+ carrito.getImporte());
        }
        viewHolder.tvNombre.setText(carrito.getNombre());
        viewHolder.tvNota.setText(""+ carrito.getNotas());
        // Return the completed view to render on screen
        return convertView;
    }
}
