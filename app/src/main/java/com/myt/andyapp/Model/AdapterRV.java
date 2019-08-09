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
import com.myt.andyapp.VerArticuloActivity;

import java.util.List;

/**
 * Created by Rafa Pc on 30/03/2018.
 */

public class AdapterRV extends RecyclerView.Adapter<AdapterRV.ProductosViewHolder>{

    List<Producto> productos;
    Context context;
    public AdapterRV(List<Producto> productos, Context context){
        this.productos = productos;
        this.context = context;
    }

    @Override
    public ProductosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.elemento_lista_productos, parent, false);
        ProductosViewHolder holder = new ProductosViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ProductosViewHolder holder, int position) {
        //aqui asigno la informacion
        final Producto producto = productos.get(position);
        holder.nombre.setText(producto.getNombre());
        holder.unidades.setText("$ "+producto.getPrecio());
        holder.descripcion.setText(producto.getDescripcion());
        holder.capacidad.setText("Capacidad: "+producto.getNomreCapacidad());
        holder.color.setText("Color: "+producto.getNombreColor());
        holder.posicion.setText("Posición: "+producto.getNombrePosicion());
        Glide.with(context).load(producto.getFoto()).into(holder.imagen);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, VerArticuloActivity.class);
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
            capacidad = itemView.findViewById(R.id.tv_capacidad);
            color = itemView.findViewById(R.id.tvColor);
            posicion = itemView.findViewById(R.id.tvPosicion);
            imagen = itemView.findViewById(R.id.qStatusImage);

        }
    }
}
