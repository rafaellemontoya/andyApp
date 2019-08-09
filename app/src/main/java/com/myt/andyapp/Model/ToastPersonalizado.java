package com.myt.andyapp.Model;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.myt.andyapp.R;

/**
 * Created by Rafa Pc on 30/03/2018.
 */

public class ToastPersonalizado {
    Activity act;

    public ToastPersonalizado(Activity a) {
        act = a;
    }

    public void crearToast(String mensaje, Drawable icon) {
        LayoutInflater inflater = act.getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast, null);
        TextView text = (TextView) layout.findViewById(R.id.text_toast);
        ImageView imageToast= (ImageView)layout.findViewById(R.id.img_toast);
        imageToast.setImageDrawable(icon);

        text.setText(mensaje);

        Toast notificacion = new Toast(act.getApplicationContext());
        notificacion.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        notificacion.setDuration(Toast.LENGTH_LONG);
        notificacion.setView(layout);
        notificacion.show();
    }
}
