package com.myt.andyapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.myt.andyapp.Model.ToastPersonalizado;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{
        private static final String TAG = "Hola";
        Button btnAcceder, btnRegistrar;
        private EditText etEmail, etPassword;
        private FirebaseAuth mAuth;
        private String mCustomToken;
        private FirebaseAuth.AuthStateListener mAuthListener;
        ToastPersonalizado toastPersonalizado;
        private DatabaseReference mDatabase;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);


            mAuth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance().getReference();

            toastPersonalizado = new ToastPersonalizado(this);

            etEmail = findViewById(R.id.email);
            etPassword = findViewById(R.id.password);

            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                    if(firebaseAuth.getCurrentUser()!=null){

                        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        Log.d(TAG, "Nombre:" + user.getUid());
                        startActivity(new Intent(LoginActivity.this,PrincipalActivity.class));
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("/distribuidores/" + user.getUid()+"/token", refreshedToken);
                        Log.d("TOKEN",  refreshedToken);

                        mDatabase.updateChildren(childUpdates);

                    }
                    else {

                    }
                }
            };

            btnAcceder = findViewById(R.id.btn_acceder);
            btnAcceder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loginUsuario();

                }
            });
            if (getIntent().getExtras() != null) {
                Log.d("ALERTA", "DATOS RECIBIDOS (INTENT)");
                Log.d("ALERTA", "Usuario: " + getIntent().getExtras().getString("usuario"));
                Log.d("ALERTA", "Estado: " + getIntent().getExtras().getString("estado"));
            }
            btnRegistrar = findViewById(R.id.btn_registrar);
            btnRegistrar.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(LoginActivity.this,RegistroActivity.class));
                }
            });




        }

        @Override
        protected void onStart() {
            super.onStart();
            mAuth.addAuthStateListener(mAuthListener);
        }

        private void loginUsuario(){
            if(etEmail.getText().equals("")||etPassword.getText().equals("")){
                toastPersonalizado.crearToast("Escribe un usuario y una contraseña", null);
            }else {

                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();


                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (task.isSuccessful()) {
                                    toastPersonalizado.crearToast("Acceso correcto", getApplicationContext().getResources().getDrawable(R.drawable.correcto));
                                }

                                if (!task.isSuccessful()) {
                                    Log.w(TAG, "signInWithEmail", task.getException());
                                    toastPersonalizado.crearToast("Usuario o contraseña incorrectas", null);
                                }

                                // ...
                            }
                        });
            }
        }



}
