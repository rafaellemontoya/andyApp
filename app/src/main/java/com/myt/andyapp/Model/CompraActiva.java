package com.myt.andyapp.Model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.myt.andyapp.VerCarritoActivity;

import java.util.ArrayList;

/**
 * Created by Rafa Pc on 30/03/2018.
 */

public class CompraActiva {
    private Context context;
    private View view;
    public CompraActiva(Context context, View view ){
        this.context=context;
        this.view = view;
    }
    public void checharCompraActiva(){
        BDCarritoHelper dbCarritoHelper = new BDCarritoHelper(context);
        ArrayList<Carrito> allCarrito2 = dbCarritoHelper.getAllCarrito();
        if(allCarrito2.size()!=0){
            float total = 0;
            for (Carrito carrito: allCarrito2) {
                total += carrito.getImporte();
            }
            Snackbar snackbar = Snackbar
                    .make(view, "Compra sin terminar $"+total, Snackbar.LENGTH_INDEFINITE).setAction("TERMINAR", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context,
                                    VerCarritoActivity.class);
                            context.startActivity(intent);
                        }
                    });

            snackbar.getView().setBackgroundColor(Color.parseColor("#70ca63"));
            snackbar.setActionTextColor(Color.parseColor("#424242"));
            snackbar.show();
        }


    }
}
