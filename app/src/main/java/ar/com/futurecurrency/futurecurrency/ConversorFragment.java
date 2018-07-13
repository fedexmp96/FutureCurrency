package ar.com.futurecurrency.futurecurrency;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ConversorFragment extends Fragment {
    private AutoCompleteTextView textViewAutoComplete;
    private AutoCompleteTextView textViewAutoCompleteConvert;
    private TextView tvCotizacion;
    private EditText cantidadMoneda;
    private Button botonEnviar;
    private CryptoMoneda monedaSeleccionada;
    private CryptoMoneda monedaSeleccionadaConvertir;
    private CoinMarketCapApi coinMarketService = new CoinMarketCapApi();

    public ConversorFragment() {
        // Required empty public constructor



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

            View v = inflater.inflate(R.layout.fragment_conversor, container, false);
            cargarItemsConversor(v);
        return v;


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

            ArrayAdapter<CryptoMoneda> adapterAutoComplete = new ArrayAdapter<CryptoMoneda>(this.getActivity(),
                    android.R.layout.simple_dropdown_item_1line, cryptoMonedasList);
            textViewAutoComplete.setAdapter(adapterAutoComplete);
            ArrayList<CryptoMoneda> listaConstantesConversion = coinMarketService.cargaConstantesMoneda(cryptoMonedasList);
            ArrayAdapter<CryptoMoneda> adapterAutoCompleteConvert= new ArrayAdapter<CryptoMoneda>(this.getActivity(),
                    android.R.layout.simple_dropdown_item_1line, listaConstantesConversion);
            textViewAutoCompleteConvert.setAdapter(adapterAutoCompleteConvert);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }







    public void cargarItemsConversor(final View view){

        textViewAutoComplete = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextViewCryptoMoneda);
        textViewAutoCompleteConvert = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextViewCryptoMonedaConvertir);

        tvCotizacion = view.findViewById(R.id.tvCotizacion);
        cantidadMoneda= view.findViewById(R.id.cantidadConvertir);
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

        botonEnviar= view.findViewById(R.id.buttonEnviar);
        botonEnviar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                try {
                    onClickConvert();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } });

        try {
            getMonedas();

        } catch (Exception e) {

        }
    }


    public void onClickConvert() throws IOException, ExecutionException, InterruptedException {
        String cantidadCurrency = cantidadMoneda.getText().toString().trim();
        String monedaSelect =  monedaSeleccionada ==null ?"1":monedaSeleccionada.getId();
        String monedaSelectConvertir= monedaSeleccionadaConvertir==null?"USD":monedaSeleccionadaConvertir.getSymbol();
        BigDecimal cantidadConversion = new BigDecimal(cantidadCurrency.equalsIgnoreCase("")? "1":cantidadCurrency);

        tvCotizacion.setText(coinMarketService.calcularConversionMoneda(cantidadConversion,monedaSelect,monedaSelectConvertir));
        coinMarketService.consultaMoneda(monedaSelect,monedaSelectConvertir,"3");


    }
}