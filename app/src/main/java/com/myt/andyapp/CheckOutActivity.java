package com.myt.andyapp;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.myt.andyapp.Model.MySingleton;
import com.myt.andyapp.Model.ToastPersonalizado;
import com.myt.andyapp.View.CCNameFragment;
import com.myt.andyapp.View.CCNumberFragment;
import com.myt.andyapp.View.CCSecureCodeFragment;
import com.myt.andyapp.View.CCValidityFragment;
import com.myt.andyapp.View.CardBackFragment;
import com.myt.andyapp.View.CardFrontFragment;
import com.myt.andyapp.View.CreditCardUtils;
import com.myt.andyapp.View.ViewPagerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.conekta.conektasdk.Card;
import io.conekta.conektasdk.Conekta;
import io.conekta.conektasdk.Token;

/**
 * Created by Rafa Pc on 04/04/2018.
 */

public class CheckOutActivity extends FragmentActivity implements FragmentManager.OnBackStackChangedListener {

    @BindView(R.id.btnNext)
    Button btnNext;

    public CardFrontFragment cardFrontFragment;
    public CardBackFragment cardBackFragment;

    //This is our viewPager
    private ViewPager viewPager;

    CCNumberFragment numberFragment;
    CCNameFragment nameFragment;
    CCValidityFragment validityFragment;
    CCSecureCodeFragment secureCodeFragment;

    int total_item;
    boolean backTrack = false;

    private boolean mShowingBack = false;

    private String cardNumber, cardCVV, cardValidity, cardName, idCostumerConekta, nombreUser, telefonoUser, emailUser, idUser;

    private ProgressDialog progressDialog;
    private ToastPersonalizado toastPersonalizado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        /**OBTENGO PARAMETROS DE LA ACTIVITY ANTERIOR**/
        idCostumerConekta= getIntent().getExtras().getString("idCostumerConekta");
        idUser= getIntent().getExtras().getString("idUser");
        if(idCostumerConekta.isEmpty()){
            nombreUser= getIntent().getExtras().getString("nombreUser");
            telefonoUser= getIntent().getExtras().getString("telefonoUser");
            emailUser= getIntent().getExtras().getString("emailUser");
        }
        /**INSTANCIO TOAST**/
        toastPersonalizado = new ToastPersonalizado(this);

        ButterKnife.bind(this);


        cardFrontFragment = new CardFrontFragment();
        cardBackFragment = new CardBackFragment();

        if (savedInstanceState == null) {
            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, cardFrontFragment).commit();

        } else {
            mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);
        }

        getFragmentManager().addOnBackStackChangedListener(this);

        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(4);
        setupViewPager(viewPager);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == total_item)
                    btnNext.setText("SUBMIT");
                else
                    btnNext.setText("NEXT");

                Log.d("track", "onPageSelected: " + position);

                if (position == total_item) {
                    flipCard();
                    backTrack = true;
                } else if (position == total_item - 1 && backTrack) {
                    flipCard();
                    backTrack = false;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = viewPager.getCurrentItem();
                if (pos < total_item) {
                    viewPager.setCurrentItem(pos + 1);
                } else {
                    checkEntries();
                }

            }
        });


    }

    public void checkEntries() {
        cardName = nameFragment.getName();
        cardNumber = numberFragment.getCardNumber();
        cardValidity = validityFragment.getValidity();
        cardCVV = secureCodeFragment.getValue();

        if (TextUtils.isEmpty(cardName)) {
            Toast.makeText(CheckOutActivity.this, "Ingresa un nombre válido", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(cardNumber) || !CreditCardUtils.isValid(cardNumber.replace(" ",""))) {
            Toast.makeText(CheckOutActivity.this, "Ingresa un número de tarjeta válido", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(cardValidity)||!CreditCardUtils.isValidDate(cardValidity)) {
            Toast.makeText(CheckOutActivity.this, "Ingresa una fecha válida", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(cardCVV)||cardCVV.length()<3) {
            Toast.makeText(CheckOutActivity.this, "Ingresa un código de seguridad válido (CCV)", Toast.LENGTH_SHORT).show();
        } else{

            progressDialog = new ProgressDialog(CheckOutActivity.this);
            progressDialog.setMessage("Guardando, por favor espere...");
            progressDialog.show();
            //Tokenizo la tarjeta
            Conekta.setPublicKey(getString(R.string.public_key));
            Conekta.setApiVersion("1.0.0");                       //optional
            Conekta.collectDevice(CheckOutActivity.this);
            String[] parts = cardValidity.split("/");
            String mes = parts[0]; //obtiene: 19
            String year = parts[1]; //obtiene: 19-A
            Card card = new Card(cardName, cardNumber, cardCVV, mes, year);
            Token token = new Token(CheckOutActivity.this);

            token.onCreateTokenListener( new Token.CreateToken(){
                @Override
                public void onCreateTokenReady(JSONObject data) {
                    try {
                        Log.d("TARJETA", data.getString("id"));
                        enviarId( data.getString("id"), idCostumerConekta);
                        //Send the id to the webservice.
                    } catch (Exception err) {
                        //Do something on error
                    }
                }
            });

            token.create(card);

        }



    }

    @Override
    public void onBackStackChanged() {
        mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        numberFragment = new CCNumberFragment();
        nameFragment = new CCNameFragment();
        validityFragment = new CCValidityFragment();
        secureCodeFragment = new CCSecureCodeFragment();
        adapter.addFragment(numberFragment);
        adapter.addFragment(nameFragment);
        adapter.addFragment(validityFragment);
        adapter.addFragment(secureCodeFragment);

        total_item = adapter.getCount() - 1;
        viewPager.setAdapter(adapter);

    }

    private void flipCard() {
        if (mShowingBack) {
            getFragmentManager().popBackStack();
            return;
        }
        // Flip to the back.
        //setCustomAnimations(int enter, int exit, int popEnter, int popExit)

        mShowingBack = true;

        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.card_flip_right_in,
                        R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in,
                        R.animator.card_flip_left_out)
                .replace(R.id.fragment_container, cardBackFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        int pos = viewPager.getCurrentItem();
        if (pos > 0) {
            viewPager.setCurrentItem(pos - 1);
        } else
            super.onBackPressed();
    }

    public void nextClick() {
        btnNext.performClick();
    }

    private void enviarId(String token, String idCostumer){
        HashMap<String, Object> map = new HashMap<>();// Mapeo previo
        int costumerActivo=1;

        map.put("token", token);
        map.put("id_customer", idCostumer);

        if(idCostumer.isEmpty()){
            costumerActivo=0;

            map.put("nombre", nombreUser);
            map.put("email", emailUser);
            map.put("telefono", telefonoUser);
        }
        map.put("costumerActivo", costumerActivo);
        JSONObject jsonObject = new JSONObject(map);
        String url = "https://www.themyt.com/andy/AdministrarCustomer.php";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject,
                new Response.Listener<JSONObject>() {

                    int estado=0;
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("RESPUESTA", response.toString());

                        try {

                            estado= response.getInt("estado");
                            if(estado==1) {

                                if(response.getString("id_customer").trim().length()>0){
                                    //Si esta vacio significa que es nuevo cliente
                                    actualizarUsuario(response.getString("id_customer"), idUser);
                                }
                                progressDialog.dismiss();
                                toastPersonalizado.crearToast("Tarjeta agregada correctamente", getApplicationContext().getResources().getDrawable(R.drawable.correcto));
                                finish();

                            }else {
                                progressDialog.dismiss();
                                toastPersonalizado.crearToast("Error al agregar tarjeta, intenta nuevamente", null);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                toastPersonalizado.crearToast("Error de conexión",getDrawable(R.drawable.error));
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("User-agent", "My useragent");
                return headers;
            }


        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);


    }
    private void actualizarUsuario(String idCustomer, String idUsuario){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("clientes/"+idUsuario+"/id_conekta").setValue(idCustomer);
    }
}