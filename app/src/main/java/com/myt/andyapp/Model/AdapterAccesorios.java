package com.myt.andyapp.Model;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.myt.andyapp.R;
import com.myt.andyapp.VerAccesorioActivity;

import java.util.List;

/**
 * Created by Rafa Pc on 30/03/2018.
 */

public class AdapterAccesorios  extends RecyclerView.Adapter<AdapterAccesorios.ProductosViewHolder>{

    List<Producto> productos;
    Context context;
    public AdapterAccesorios(List<Producto> productos, Context context){
        this.productos = productos;
        this.context = context;
    }

    @Override
    public AdapterAccesorios.ProductosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.elemento_lista_accesorios, parent, false);
        AdapterAccesorios.ProductosViewHolder holder = new AdapterAccesorios.ProductosViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final AdapterAccesorios.ProductosViewHolder holder, int position) {
        //aqui asigno la informacion
        final Producto producto = productos.get(position);
        holder.nombre.setText(producto.getNombre());
        holder.unidades.setText("$ "+producto.getPrecio());
        holder.descripcion.setText(producto.getDescripcion());
        Glide.with(context).load(producto.getFoto()).into(holder.imagen);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, VerAccesorioActivity.class);
                intent.putExtra("producto",  producto);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public static class  ProductosViewHolder extends RecyclerView.ViewHolder{

        TextView nombre, unidades, descripcion, capacidad, color, posicion;
        ImageView imagen;

        public ProductosViewHolder(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tvNombre);
            unidades = itemView.findViewById(R.id.unidades);
            descripcion = itemView.findViewById(R.id.tvDescripcion);
            imagen = itemView.findViewById(R.id.qStatusImage);

        }
    }
}
