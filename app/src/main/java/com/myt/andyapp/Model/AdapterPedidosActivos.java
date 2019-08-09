package com.myt.andyapp.Model;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.myt.andyapp.CalificarActivity;
import com.myt.andyapp.R;
import com.myt.andyapp.SubirComprobanteActivity;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Rafa Pc on 30/03/2018.
 */

public class AdapterPedidosActivos  extends RecyclerView.Adapter<AdapterPedidosActivos.PedidosViewHolder>{

    List<Pedido> pedidos;
    Context context;
    public AdapterPedidosActivos(List<Pedido> pedidos, Context context){
        this.pedidos = pedidos;
        this.context = context;
    }

    @Override
    public AdapterPedidosActivos.PedidosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.elemento_lista_pedidos, parent, false);
        AdapterPedidosActivos.PedidosViewHolder holder = new AdapterPedidosActivos.PedidosViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final AdapterPedidosActivos.PedidosViewHolder holder, int position) {
        //aqui asigno la informacion

        final Pedido pedido = pedidos.get(position);

        /**CALIFICAR**/
//            if(Integer.valueOf(pedido.getEstado())==6){
//                holder.btnCalificar.setEnabled(true);
//                holder.btnCalificar.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(context, CalificarActivity.class);
//                        intent.putExtra("key", pedido.getId());
//                        context.startActivity(intent);
//                    }
//
//                });
//            }
            if(Integer.valueOf(pedido.getEstado())==1){
                holder.imgEstado.setBackgroundResource(R.drawable.estado1);
            }else if(Integer.valueOf(pedido.getEstado())==2){
                holder.imgEstado.setBackgroundResource(R.drawable.estado2);
            }else if(Integer.valueOf(pedido.getEstado())==0){
                holder.imgEstado.setBackgroundResource(R.drawable.estado0);
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");
            holder.idPedido.setText("" + pedido.getId());
            holder.estado.setText("" + pedido.getEstado());


            holder.fechaRealización.setText("Fecha realización: " +dateFormat.format(pedido.getFechaCreacion()));
            holder.total.setText(" $ " + pedido.getTotal());
            holder.estado.setText("" + pedido.getNombreEstado());
            CustomListViewCheckoutAdapter adapter = new CustomListViewCheckoutAdapter(pedido.getCarritos(), context);
            holder.listViewProductos.setAdapter(adapter);
            holder.formaPago.setText(""+pedido.getFormaPago());
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
                    float total = pedido.getTotal();

//                Log.d("total", Float.toString(total));
//                Log.d("anticipo", Float.toString(anticipo));
                    /**BOTON COMPROBANTE**/
                    holder.btnComprobante.setVisibility(View.VISIBLE);
                    holder.btnComprobante.setEnabled(true);
                    holder.btnComprobante.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, SubirComprobanteActivity.class);
                            intent.putExtra("key", pedido.getId());
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

        TextView idPedido, estado, fechaRealización,  formaPago, total,  tvCuatroDigitos, tvAutorizacion;
        NonScrollListView listViewProductos;
        Button btnComprobante;
        ImageView imgEstado, imgFormaPago;


        public PedidosViewHolder(View itemView) {
            super(itemView);
            idPedido = itemView.findViewById(R.id.id_pedido);
            estado = itemView.findViewById(R.id.tv_estado);
            imgEstado = itemView.findViewById(R.id.img_estado);
            fechaRealización = itemView.findViewById(R.id.tv_fecha_realizacion);
            formaPago = itemView.findViewById(R.id.tv_forma_pago);
            total = itemView.findViewById(R.id.tv_total);
            listViewProductos = (NonScrollListView)itemView.findViewById(R.id.lv_productos);
            btnComprobante = itemView.findViewById(R.id.btn_comprobante);

            imgFormaPago = itemView.findViewById(R.id.img_forma_pago);
            //Pago con tarjeta
            tvAutorizacion = itemView.findViewById(R.id.autorizacion);
            tvCuatroDigitos = itemView.findViewById(R.id.cuatro_digitos);


        }
    }
}
