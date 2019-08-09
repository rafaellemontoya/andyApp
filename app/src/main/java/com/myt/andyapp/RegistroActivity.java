package com.myt.andyapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.myt.andyapp.Model.ToastPersonalizado;

/**
 * Created by Rafa Pc on 12/04/2018.
 */

public class RegistroActivity extends AppCompatActivity {
    private EditText mNameField, mEmailField, mPasswordField,mTelField;
    private static final String TAG = "Hola";
    Button registrar;
    private EditText etEmail, etPassword;
    private FirebaseAuth mAuth;
    private String mCustomToken;
    private FirebaseAuth.AuthStateListener mAuthListener;
    ToastPersonalizado toastPersonalizado;
    private DatabaseReference mDatabase;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        mAuth =FirebaseAuth.getInstance();

        mNameField = (EditText) findViewById(R.id.nombre);
        mTelField = (EditText) findViewById(R.id.telefono);
        mEmailField = (EditText) findViewById(R.id.email);
        mPasswordField = (EditText) findViewById(R.id.password);
        toastPersonalizado= new ToastPersonalizado(this);

        registrar = (Button) findViewById(R.id.btn_registrar);

        mProgress = new ProgressDialog(this);

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRegister();
            }
        });




    }
    private void startRegister(){

        final String name = mNameField.getText().toString().trim();
        final String telefono = mTelField.getText().toString().trim();
        final String email = mEmailField.getText().toString().trim();
        final String pass = mPasswordField.getText().toString().trim();

        if(!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(pass)&&!TextUtils.isEmpty(telefono)){
            mProgress.setMessage("Registrando, por favor espere...");
            mProgress.show();
            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    mProgress.dismiss();
                    if(task.isSuccessful()){
                        FirebaseUser user = mAuth.getCurrentUser();

//                        updateUI(user);
                        registrarInfo(user,name, telefono, email);
                        toastPersonalizado.crearToast("Acceso correcto", getApplicationContext().getResources().getDrawable(R.drawable.correcto));
                        Intent intent3 = new Intent(getApplicationContext(),
                                PrincipalActivity.class);
                        startActivity(intent3);
                    }else{
                        toastPersonalizado.crearToast("Error al guardar", getApplicationContext().getResources().getDrawable(R.drawable.correcto));
                    }
                }
            });
        }

    }

    private void registrarInfo(FirebaseUser user, String nombre, String telefono, String email){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        mDatabase.child("clientes/"+user.getUid()+"/nombre").setValue(nombre);
        mDatabase.child("clientes/"+user.getUid()+"/id_conekta").setValue("");
        mDatabase.child("clientes/"+user.getUid()+"/telefono").setValue(telefono);
        mDatabase.child("clientes/"+user.getUid()+"/email").setValue(email);
        mDatabase.child("clientes/"+user.getUid()+"/token").setValue(refreshedToken);
    }
}
