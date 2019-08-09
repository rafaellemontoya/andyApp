package com.myt.andyapp.Model;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.myt.andyapp.CalificarActivity;
import com.myt.andyapp.ListaProductosActivity;
import com.myt.andyapp.R;
import com.myt.andyapp.SubirComprobanteActivity;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Rafa Pc on 30/03/2018.
 */

public class AdapterHistorial extends RecyclerView.Adapter<AdapterHistorial.PedidosViewHolder>{

    List<Pedido> pedidos;
    Context context;
    public AdapterHistorial(List<Pedido> pedidos, Context context){
        this.pedidos = pedidos;
        this.context = context;
    }

    @Override
    public AdapterHistorial.PedidosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.elemento_lista_historial, parent, false);
        AdapterHistorial.PedidosViewHolder holder = new AdapterHistorial.PedidosViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final AdapterHistorial.PedidosViewHolder holder, int position) {
        //aqui asigno la informacion

        final Pedido pedido = pedidos.get(position);
        /***REVISO QUE EL ESTADO NO SEA TERMINADO**/


            holder.idPedido.setText("" + pedido.getId());
            holder.estado.setText("" + pedido.getEstado());
            //Fechas
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");
            holder.fechaRealización.setText("Fecha realización: " +  dateFormat.format(pedido.getFechaCreacion()));
            holder.fechaEntrega.setText("Fecha entrega: " +  dateFormat.format(pedido.getFechaEntrega()));
            holder.total.setText(" $ " + pedido.getTotal());
            holder.estado.setText("" + pedido.getNombreEstado());
            CustomListViewProductosCalificados adapter = new CustomListViewProductosCalificados(pedido.getCarritos(), context, pedido.getEstadoCalificado());
            holder.listViewProductos.setAdapter(adapter);
            holder.tvRepartidor.setText(""+pedido.getRepartidor());

            /**FORMA PAGO**/
            holder.formaPago.setText(pedido.getFormaPago());
            holder.imgFormaPago.setBackgroundResource(R.drawable.ic_billete);
        if (pedido.getFormaPago().equals("Tarjeta")) {
            //Pago con tarjeta
            holder.imgFormaPago.setBackgroundResource(R.drawable.iconspartedelanteradetarjetabancaria);
            holder.tvAutorizacion.setVisibility(View.VISIBLE);
            holder.tvAutorizacion.setText("Código de autorización: "+pedido.getAutorizacion());
            holder.tvCuatroDigitos.setVisibility(View.VISIBLE);
            holder.tvCuatroDigitos.setText("Tarjeta terminación: "+pedido.getCuatroDigitos());
        } else if(pedido.getFormaPago().equals("Depósito")) {
            holder.imgFormaPago.setBackgroundResource(R.drawable.deposito);
        }


        /**CALIFICACION**/
        if(pedido.getEstadoCalificado()==1){
            //Esta calificado
            holder.tvMensajeSinCalificacion.setVisibility(View.GONE);
            holder.ratingBarRepartidor.setVisibility(View.VISIBLE);
            holder.ratingBarRepartidor.setRating(pedido.getCalificacionRepartidor());
            holder.btnCalificar.setVisibility(View.GONE);
        }else{
            holder.tvMensajeSinCalificacion.setVisibility(View.VISIBLE);
            holder.ratingBarRepartidor.setVisibility(View.GONE);
            holder.btnCalificar.setVisibility(View.VISIBLE);
            holder.btnCalificar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CalificarActivity.class);
                    intent.putExtra("pedido",  pedido);
                    context.startActivity(intent);
                }
            });
        }



            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                Intent intent = new Intent(context, VerProductoActivity.class);
//                intent.putExtra("producto",  pedido);
//                context.startActivity(intent);
                }
            });

    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    public static class  PedidosViewHolder extends RecyclerView.ViewHolder{

        TextView idPedido, estado, fechaRealización, fechaEntrega, formaPago, total, tvAutorizacion,tvCuatroDigitos, tvRepartidor, tvMensajeSinCalificacion;
        NonScrollListView listViewProductos;
        Button btnComprobante, btnCalificar;
        ImageView imgEstado, imgFormaPago;
        RatingBar ratingBarRepartidor;


        public PedidosViewHolder(View itemView) {
            super(itemView);
            idPedido = itemView.findViewById(R.id.id_pedido);
            estado = itemView.findViewById(R.id.tv_estado);
            fechaRealización = itemView.findViewById(R.id.tv_fecha_realizacion);
            fechaEntrega = itemView.findViewById(R.id.tv_fecha_entrega);
            formaPago = itemView.findViewById(R.id.tv_forma_pago);
            total = itemView.findViewById(R.id.tv_total);
            listViewProductos = (NonScrollListView)itemView.findViewById(R.id.lv_productos_calificados2);
            imgFormaPago = itemView.findViewById(R.id.img_forma_pago);
            tvRepartidor = itemView.findViewById(R.id.tv_repartidor);
            //Pago con tarjeta
            tvAutorizacion = itemView.findViewById(R.id.autorizacion);
            tvCuatroDigitos = itemView.findViewById(R.id.cuatro_digitos);
            btnCalificar = itemView.findViewById(R.id.btn_calificar);

            //Calificacion
            ratingBarRepartidor= itemView.findViewById(R.id.ratting_repartidor);
            tvMensajeSinCalificacion= itemView.findViewById(R.id.mensaje_sin_calificacion);



        }
    }
}
