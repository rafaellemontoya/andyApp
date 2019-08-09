package com.myt.andyapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import java.io.File;

/**
 * Created by Rafa Pc on 30/03/2018.
 */

public class ComprobanteActivity  extends AppCompatActivity {
    private ImageView image = null;

    private Uri mHighQualityImageUri = null;

    private final int REQUEST_CODE_LOW_QUALITY_IMAGE = 1;
    private final int REQUEST_CODE_HIGH_QUALITY_IMAGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subir_comprobante);
        image = (ImageView) findViewById(R.id.image_view);

        mHighQualityImageUri = generateTimeStampPhotoFileUri();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mHighQualityImageUri);
        startActivityForResult(intent, REQUEST_CODE_HIGH_QUALITY_IMAGE);


    }
    // Button click listener
    public void onLargeImageCapture(View v) {

        mHighQualityImageUri = generateTimeStampPhotoFileUri();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mHighQualityImageUri);
        startActivityForResult(intent, REQUEST_CODE_HIGH_QUALITY_IMAGE);

    }
    private Uri generateTimeStampPhotoFileUri() {

        Uri photoFileUri = null;
        File outputDir = getPhotoDirectory();
        if (outputDir != null) {
            Time t = new Time();
            t.setToNow();
            File photoFile = new File(outputDir, System.currentTimeMillis()
                    + ".jpg");
            photoFileUri = Uri.fromFile(photoFile);
        }
        return photoFileUri;
    }

    private File getPhotoDirectory() {
        File outputDir = null;
        String externalStorageStagte = Environment.getExternalStorageState();
        if (externalStorageStagte.equals(Environment.MEDIA_MOUNTED)) {
            File photoDir = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            outputDir = new File(photoDir, getString(R.string.app_name));
            if (!outputDir.exists())
                if (!outputDir.mkdirs()) {
                    Toast.makeText(
                            this,
                            "Failed to create directory "
                                    + outputDir.getAbsolutePath(),
                            Toast.LENGTH_SHORT).show();
                    outputDir = null;
                }
        }
        return outputDir;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case REQUEST_CODE_LOW_QUALITY_IMAGE:
                    Bitmap bmp = (Bitmap) data.getExtras().get("data");
                    image.setImageBitmap(bmp);

                    break;

                case REQUEST_CODE_HIGH_QUALITY_IMAGE:

                    image.setImageURI(mHighQualityImageUri);
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                            Uri.parse("file://"
                                    + Environment.getExternalStorageDirectory())));
                    break;
                default:
                    break;
            }

        }
    }
}