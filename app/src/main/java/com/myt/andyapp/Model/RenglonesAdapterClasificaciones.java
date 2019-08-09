package com.myt.andyapp.Model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.myt.andyapp.ListaProductosActivity;
import com.myt.andyapp.R;

import java.util.List;

/**
 * Created by Rafa Pc on 30/03/2018.
 */

public class RenglonesAdapterClasificaciones extends RecyclerView.Adapter<RenglonesAdapterClasificaciones.ClasificacionesViewHolder>{

    List<Clasificacion> clasificaciones;
    Context context;
    public RenglonesAdapterClasificaciones(List<Clasificacion> clasificaciones, Context context){
        this.clasificaciones = clasificaciones;
        this.context = context;
    }

    @Override
    public RenglonesAdapterClasificaciones.ClasificacionesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.elemento_lista_clasificacion, parent, false);
        RenglonesAdapterClasificaciones.ClasificacionesViewHolder holder = new RenglonesAdapterClasificaciones.ClasificacionesViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ClasificacionesViewHolder holder, int position) {
        //aqui asigno la informacion
        final Clasificacion clasificacion = clasificaciones.get(position);
        holder.nombre.setText(clasificacion.getNombre());
        holder.descripcion.setText(clasificacion.getDescripcion());
        holder.color.setBackgroundColor(Color.parseColor("#ffffff"));
        Log.d("IMAGEN", clasificacion.getFoto());
        Glide.with(context).load(clasificacion.getFoto()).into(holder.imagen);

        holder.color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("CLICK", "CLICK");

                Intent intent = new Intent(context, ListaProductosActivity.class);
                intent.putExtra("clasificacion",  clasificacion);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return clasificaciones.size();
    }

    public static class  ClasificacionesViewHolder extends RecyclerView.ViewHolder{

        TextView nombre,  descripcion;
        LinearLayout color;
        ImageView imagen;

        public ClasificacionesViewHolder(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombre_clasificacion);
            descripcion = itemView.findViewById(R.id.descripcion_clasificacion);
            imagen = itemView.findViewById(R.id.imagen_clasificacion);
            color = itemView.findViewById(R.id.contenedor_clasificacion);

        }
    }
}
