package ar.com.futurecurrency.futurecurrency;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CardViewMonedasABM {
    private AutoCompleteTextView textViewAutoComplete;
    private AutoCompleteTextView textViewAutoCompleteConvert;
    private Button botonEnviar;
    private CryptoMoneda monedaSeleccionada;
    private CryptoMoneda monedaSeleccionadaConvertir;
    private CoinMarketCapApi coinMarketService = new CoinMarketCapApi();

    //Metodo de alta de cada CardView visualizado en el menu Tarjetas
    //Context: Activity en la que se encuentra
    public void altaCardViewMoneda(String idMonedaSeleccionada, String symbolMonedaConvertir,Context context){


        String monedaSelect = idMonedaSeleccionada;
        String monedaSelectConvertir= symbolMonedaConvertir;
        try {
            CryptoMoneda monedaConsultada = coinMarketService.consultaMoneda(monedaSelect,monedaSelectConvertir,"2");

            CardViewMonedaObject cardViewMoneda = new CardViewMonedaObject();
            cardViewMoneda.setId(monedaConsultada.getId());
            cardViewMoneda.setName(monedaConsultada.getName());
            cardViewMoneda.setSymbolMoneda(monedaConsultada.getSymbol());
            cardViewMoneda.setValorMoneda(monedaConsultada.getPrice());
            cardViewMoneda.setPorcentaje(monedaConsultada.getPercent_change_24h());
            cardViewMoneda.setImportePorcentaje(monedaConsultada.getImporte_change());
            cardViewMoneda.setSymbolMonedaConvertida(monedaSelectConvertir);
            List<CardViewMonedaObject> listaCardView = new ArrayList<CardViewMonedaObject>();
            listaCardView.add(cardViewMoneda);


            Gson gson = new Gson();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();

            String savedList = preferences.getString("ListaCardViews","");
            Type type = new TypeToken<List<CardViewMonedaObject>>(){}.getType();
            List<CardViewMonedaObject> objects = gson.fromJson(savedList, type);
            for (int i = 0; i < objects.size(); ++i) {
                listaCardView.add(objects.get(i));
            }
            String jsonList = gson.toJson(listaCardView);

            editor.putString("ListaCardViews", jsonList);
            editor.commit();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Metodo para realizar baja de cada CardView visualizado en el menu Tarjetas
    //Parametros de borrado total: "","",context,"-1";
    public void bajaCardViewMoneda(String idMonedaBaja, String symbolMonedaConvertirBaja,Context context, String flagBorrarTodo) {
        List<CardViewMonedaObject> listaCardView = new ArrayList<CardViewMonedaObject>();

        Gson gson = new Gson();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        String savedList = preferences.getString("ListaCardViews","");
        Type type = new TypeToken<List<CardViewMonedaObject>>(){}.getType();
        List<CardViewMonedaObject> objects = gson.fromJson(savedList, type);
        listaCardView = objects;
        for (int i = 0; i < objects.size(); ++i) {

            if(objects.get(i).getId().equalsIgnoreCase(idMonedaBaja) && objects.get(i).getSymbolMonedaConvertida().equalsIgnoreCase(symbolMonedaConvertirBaja)) {
                listaCardView.remove(i);
            }

            }
            if(idMonedaBaja.equalsIgnoreCase("") && symbolMonedaConvertirBaja.equalsIgnoreCase("") && flagBorrarTodo.equalsIgnoreCase("-1")){
                listaCardView = new ArrayList<CardViewMonedaObject>();
            }
        String jsonList = gson.toJson(listaCardView);

        editor.putString("ListaCardViews", jsonList);
        editor.commit();

    }

    public List<CardViewMonedaObject> reconsultaMonedasGuardadas (Context context){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        Gson gson = new Gson();
        String savedList = preferences.getString("ListaCardViews","");
        Type type = new TypeToken<List<CardViewMonedaObject>>(){}.getType();
        List<CardViewMonedaObject> listaCardViews = gson.fromJson(savedList, type);
        bajaCardViewMoneda("","",context,"-1");
        List<CardViewMonedaObject> listaCardViewActualizada = new ArrayList<CardViewMonedaObject>();

        for (int i = 0; i < listaCardViews.size(); ++i){

            try {
                CryptoMoneda monedaConsultada = coinMarketService.consultaMoneda(listaCardViews.get(i).id,listaCardViews.get(i).symbolMonedaConvertida,"2");

                CardViewMonedaObject cardViewMoneda = new CardViewMonedaObject();
                cardViewMoneda.setId(monedaConsultada.getId());
                cardViewMoneda.setName(monedaConsultada.getName());
                cardViewMoneda.setSymbolMoneda(monedaConsultada.getSymbol());
                cardViewMoneda.setValorMoneda(monedaConsultada.getPrice());
                cardViewMoneda.setPorcentaje(monedaConsultada.getPercent_change_24h());
                cardViewMoneda.setImportePorcentaje(monedaConsultada.getImporte_change());
                cardViewMoneda.setSymbolMonedaConvertida(listaCardViews.get(i).symbolMonedaConvertida);
                listaCardViewActualizada.add(cardViewMoneda);






            } catch (IOException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String jsonList = gson.toJson(listaCardViewActualizada);

        editor.putString("ListaCardViews", jsonList);
        editor.commit();
        List<CardViewMonedaObject> nuevaListaConsulta = gson.fromJson(savedList, type);







        return nuevaListaConsulta;
    }
}
