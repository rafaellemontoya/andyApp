package com.myt.andyapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.myt.andyapp.Model.ToastPersonalizado;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by Rafa Pc on 30/03/2018.
 */

public class SubirComprobanteActivity extends AppCompatActivity {
    ImageView imageView;
    String keyPedido;
    Bitmap bitmap;
    ToastPersonalizado toastPersonalizado;
    private StorageReference mStorageRef;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subir_comprobante);
        toastPersonalizado = new ToastPersonalizado(this);
        /**OBTENGO EL KEY DEL PEDIDO***/
        Intent intentKey = getIntent();
        keyPedido= intentKey.getStringExtra("key");

        /**ABRO LA CÁMARA DIRECTAMENTE**/
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
//        }

        //instancio Image view
        imageView = findViewById(R.id.image_view);
        /**BOTON PARA ABRIR CAMARA**/
        Button btnCamara = findViewById(R.id.btn_camara);
        btnCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });
        /**BOTON PARA SUBIR A FB**/
        Button btnSubir = findViewById(R.id.btn_subir_comprobante);
        btnSubir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStorageRef = FirebaseStorage.getInstance().getReference();

                // Create a reference to "mountains.jpg"
                StorageReference mountainsRef = mStorageRef.child("comprobantes/"+keyPedido+".jpg");
//
                // Get the data from an ImageView as bytes
//                imageView.setDrawingCacheEnabled(true);
//                imageView.buildDrawingCache();
//                Bitmap bitmap = imageView.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = mountainsRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Log.d("Estado", downloadUrl.toString());
                        /**GUARDAR EN FIREBASE**/
// ...
                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                        mDatabase.child("pedidos/"+keyPedido+"/comprobante_pago").setValue(downloadUrl.toString());

                        toastPersonalizado.crearToast("Comprobante subido con éxito", getApplicationContext().getResources().getDrawable(R.drawable.correcto));
                        finish();

                    }
                });


            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        InputStream stream = null;
        bitmap = (Bitmap)data.getExtras().get("data");
        imageView.setImageBitmap(bitmap);

//        try {
//            stream = getContentResolver().openInputStream(data.getData());
//            bitmap = BitmapFactory.decodeStream(stream);
//
//            imageView.setImageBitmap(bitmap);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }



    }
}
