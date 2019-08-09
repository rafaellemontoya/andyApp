package com.myt.andyapp.Model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.myt.andyapp.R;
import com.myt.andyapp.VerArticuloActivity;

import java.util.List;

/**
 * Created by Rafa Pc on 02/04/2018.
 */

public class RenglonesAdapterArticulos extends RecyclerView.Adapter<RenglonesAdapterArticulos.ArticulosViewHolder>{

    List<Articulo> articulos;
    Context context;
    public RenglonesAdapterArticulos(List<Articulo> articulos, Context context){
        this.articulos = articulos;
        this.context = context;
    }

    @Override
    public RenglonesAdapterArticulos.ArticulosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.elemento_lista_articulos, parent, false);
        RenglonesAdapterArticulos.ArticulosViewHolder holder = new RenglonesAdapterArticulos.ArticulosViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RenglonesAdapterArticulos.ArticulosViewHolder holder, int position) {
        //aqui asigno la informacion
        final Articulo articulo = articulos.get(position);
        holder.nombre.setText(articulo.getNombre());
        holder.precio.setText("$ "+articulo.getPrecio());
        holder.precio.setPaintFlags(holder.descripcion.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        holder.descripcion.setText(articulo.getDescripcion());
        Glide.with(context).load(articulo.getImagen()).into(holder.imagen);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(articulo.getEstadoActivado()==0){


                }else{

                    Intent intent = new Intent(context, VerArticuloActivity.class);
                    intent.putExtra("articulo",  articulo);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return articulos.size();
    }

    public static class  ArticulosViewHolder extends RecyclerView.ViewHolder{

        TextView nombre, descripcion, precio;
        ImageView imagen;

        public ArticulosViewHolder(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tvNombre);
            descripcion = itemView.findViewById(R.id.tv_descripcion);
            precio = itemView.findViewById(R.id.tv_precio);
            imagen = itemView.findViewById(R.id.qStatusImage);

        }
    }
}
