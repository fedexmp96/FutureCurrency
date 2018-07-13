package ar.com.futurecurrency.futurecurrency;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class NuevaTarjetaMonedaActivity extends AppCompatActivity {

    private AutoCompleteTextView textViewAutoComplete;
    private AutoCompleteTextView textViewAutoCompleteConvert;
    private Button botonEnviar;
    private CryptoMoneda monedaSeleccionada;
    private CryptoMoneda monedaSeleccionadaConvertir;
    private CoinMarketCapApi coinMarketService = new CoinMarketCapApi();
    private CardViewMonedasABM abm = new CardViewMonedasABM();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_tarjeta_moneda);
        Toolbar toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                NuevaTarjetaMonedaActivity.super.onBackPressed();

            }
        });
        botonEnviar= findViewById(R.id.buttonEnviarNuevaTarjetaMoneda);
        botonEnviar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                 agregarTarjetaMoneda();

            } });

        cargarItems();
        //Seteo de status bar color

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void cargarItems(){

        textViewAutoComplete = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextViewCryptoMonedaTarjetas);
        textViewAutoCompleteConvert = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextViewCryptoMonedaConvertirTarjetas);

        textViewAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                monedaSeleccionada = (CryptoMoneda) parent.getItemAtPosition(position);
            }
        });

        textViewAutoCompleteConvert.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                monedaSeleccionadaConvertir = (CryptoMoneda) parent.getItemAtPosition(position);
            }
        });

//        botonEnviar= view.findViewById(R.id.buttonEnviar);

        try {
            getMonedas();

        } catch (Exception e) {

        }
    }


    public void getMonedas() throws IOException, ExecutionException, InterruptedException {

        //Some url endpoint that you may have
        String myUrl = "https://api.coinmarketcap.com/v2/listings/";
        //String to place our result in
        String result;
        //Instantiate new instance of our class
        HttpGetRequest getRequest = new HttpGetRequest();
        //Perform the doInBackground method, passing in our url
        result = getRequest.execute(myUrl).get();

        try {
            ArrayList<CryptoMoneda> cryptoMonedasList = coinMarketService.parseoGetMonedas();

            ArrayAdapter<CryptoMoneda> adapterAutoComplete = new ArrayAdapter<CryptoMoneda>(NuevaTarjetaMonedaActivity.this,
                    android.R.layout.simple_dropdown_item_1line, cryptoMonedasList);
            textViewAutoComplete.setAdapter(adapterAutoComplete);
            ArrayList<CryptoMoneda> listaConstantesConversion = coinMarketService.cargaConstantesMoneda(cryptoMonedasList);
            ArrayAdapter<CryptoMoneda> adapterAutoCompleteConvert = new ArrayAdapter<CryptoMoneda>(NuevaTarjetaMonedaActivity.this,
                    android.R.layout.simple_dropdown_item_1line, listaConstantesConversion);
            textViewAutoCompleteConvert.setAdapter(adapterAutoCompleteConvert);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void ReturnHome(View view){
        super.onBackPressed();
    }


    private void agregarTarjetaMoneda() {
        String monedaSelect =  monedaSeleccionada ==null ?"1":monedaSeleccionada.getId();
        String monedaSelectConvertir= monedaSeleccionadaConvertir==null?"USD":monedaSeleccionadaConvertir.getSymbol();
        abm.altaCardViewMoneda(monedaSelect,monedaSelectConvertir,NuevaTarjetaMonedaActivity.this);

        finish();
        Intent homepage = new Intent(NuevaTarjetaMonedaActivity.this, MainActivity.class);
        startActivity(homepage);
    }


}
