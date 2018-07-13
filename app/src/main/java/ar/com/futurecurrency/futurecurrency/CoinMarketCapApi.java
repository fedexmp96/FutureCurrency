package ar.com.futurecurrency.futurecurrency;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class CoinMarketCapApi {

    //Metodo para cargar combos
    public ArrayList<CryptoMoneda> parseoGetMonedas() throws JSONException, ExecutionException, InterruptedException {

        //Api url para consulta general de monedas (Cargar combos, y cargar autocompletes)
        String myUrl = "https://api.coinmarketcap.com/v2/listings/";

        String result;
        //Coneccion al api (method get)
        HttpGetRequest getRequest = new HttpGetRequest();

        result = getRequest.execute(myUrl).get();

        try {
            ArrayList<CryptoMoneda> cryptoMonedasList = new ArrayList<CryptoMoneda>();
            JSONObject jsonObj = new JSONObject(result);
            JSONArray array;
            array = (JSONArray) jsonObj.get("data");

            for (int i = 0; i < array.length(); i++) {
                CryptoMoneda moneda = new CryptoMoneda();
                JSONObject object = array.getJSONObject(i);
                moneda.setId(object.getString("id"));
                moneda.setName(object.getString("name"));
                moneda.setSymbol(object.getString("symbol"));
                moneda.setWebsiteSlug(object.getString("website_slug"));

                cryptoMonedasList.add(moneda);
            }
            return cryptoMonedasList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Realiza la convercion de la moneda consultando al api y realiza el calculo por la cantidad ingresada de la misma.
    //Ruta de prueba: https://api.coinmarketcap.com/v2/ticker/1/?convert=EUR

    //idMonedaAConvertir: el id de la moneda que se quiere convertir
    //symbolMonedaConversion: el simbolo de la moneda a la que se va a convertir
    //cantidadMoneda: es la cantidad que se quiere convertir


    public String calcularConversionMoneda(BigDecimal cantidadMoneda, String idMonedaAConvertir, String symbolMonedaConversion) throws IOException, ExecutionException, InterruptedException {

        String resultadoMostrar = "";

        if (idMonedaAConvertir == null || idMonedaAConvertir.equalsIgnoreCase("")) {
            idMonedaAConvertir = "1";
        }
        if (symbolMonedaConversion == null || symbolMonedaConversion.equalsIgnoreCase("")) {
            symbolMonedaConversion = "USD";
        }

        //Api url para conversion de monedas
        String myUrl = "https://api.coinmarketcap.com/v2/ticker/" + idMonedaAConvertir + "/?convert=" + symbolMonedaConversion;


        String result;

        HttpGetRequest getRequest = new HttpGetRequest();

        result = getRequest.execute(myUrl).get();
        try {
            JSONObject jsonObjConversion = new JSONObject(result);
            JSONObject detail = jsonObjConversion.getJSONObject("data");
            JSONObject quotes = detail.getJSONObject("quotes");
            JSONObject jsonMoneda = quotes.getJSONObject(symbolMonedaConversion);
            BigDecimal valorMoneda = new BigDecimal(jsonMoneda.getString("price"));

            String valorMonedaMultiply = (valorMoneda.multiply(cantidadMoneda)).toString();
            resultadoMostrar = valorMonedaMultiply;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resultadoMostrar;
    }
    //Consulta todos los datos que corresponden a la moneda con la respuesta en formato json
    //Ruta para ver formato: https://api.coinmarketcap.com/v2/ticker/1/

    public CryptoMoneda consultaMoneda(String idMoneda, String symbolMoneda, String flagPorcentaje) throws IOException, ExecutionException, InterruptedException {


        CryptoMoneda moneda = new CryptoMoneda();

        String myUrl = "https://api.coinmarketcap.com/v2/ticker/" + idMoneda + "/?convert="+symbolMoneda;

        String result;

        HttpGetRequest getRequest = new HttpGetRequest();

        result = getRequest.execute(myUrl).get();
        try {
            JSONObject jsonObjConversion = new JSONObject(result);
            JSONObject detail = jsonObjConversion.getJSONObject("data");

            moneda.setId(detail.getString("id"));
            moneda.setName(detail.getString("name"));
            moneda.setSymbol(detail.getString("symbol"));
            moneda.setWebsiteSlug(detail.getString("website_slug"));
            moneda.setRank(detail.getString("rank"));
            moneda.setCirculatingSupply(detail.getString("circulating_supply"));
            moneda.setMaxSupply(detail.getString("max_supply"));
            moneda.setQuotes(detail.getJSONObject("quotes"));
            moneda.setPrice(moneda.getQuotes().getJSONObject(symbolMoneda).getString("price"));
            moneda.setVolume_24h(moneda.getQuotes().getJSONObject(symbolMoneda).getString("volume_24h"));
            moneda.setMarket_cap(moneda.getQuotes().getJSONObject(symbolMoneda).getString("market_cap"));
            moneda.setPercent_change_1h(moneda.getQuotes().getJSONObject(symbolMoneda).getString("percent_change_1h"));
            moneda.setPercent_change_24h(moneda.getQuotes().getJSONObject(symbolMoneda).getString("percent_change_24h"));
            moneda.setPercent_change_7d(moneda.getQuotes().getJSONObject(symbolMoneda).getString("percent_change_7d"));
            moneda.setLast_updated(detail.getString("last_updated"));
            Double porcentaje = Double.parseDouble(moneda.getPercent_change_24h());
            if(flagPorcentaje==null || flagPorcentaje.equalsIgnoreCase(""))
            {
                porcentaje = Double.parseDouble(moneda.getPercent_change_24h());
            }
            if (flagPorcentaje.equalsIgnoreCase("1")) {
                 porcentaje = Double.parseDouble(moneda.getPercent_change_1h());
            }
            if( flagPorcentaje.equalsIgnoreCase("2"))
            {
                 porcentaje = Double.parseDouble(moneda.getPercent_change_24h());
            }
            if(flagPorcentaje.equalsIgnoreCase("3")){
                porcentaje =  Double.parseDouble(moneda.getPercent_change_7d());
            }
            Double precioMoneda= Double.parseDouble(moneda.getPrice());
            DecimalFormat dosDecimales = new DecimalFormat("#.##");

            moneda.setImporte_change(String.valueOf(dosDecimales.format(porcentaje*precioMoneda/100)));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return moneda;
    }
    public ArrayList<CryptoMoneda> cargaConstantesMoneda(ArrayList<CryptoMoneda> listaMonedasConvert){

        String[] arrayNombres= {"Peso Argentino","Euro","Dollar","Australian Dollar"};
        String[] arraySymbols = {"ARS","EUR","USD","AUD"};
        ArrayList<CryptoMoneda> listaMonedasConversion = new ArrayList<CryptoMoneda>(listaMonedasConvert) ;
        for (int i = 0; i < arrayNombres.length; i++) {
            CryptoMoneda moneda = new CryptoMoneda();
            moneda.setId("nn");
            moneda.setName(arrayNombres[i].toString());
            moneda.setSymbol(arraySymbols[i].toString());

            listaMonedasConversion.add(moneda);
        }
        return listaMonedasConversion;
    }


}
