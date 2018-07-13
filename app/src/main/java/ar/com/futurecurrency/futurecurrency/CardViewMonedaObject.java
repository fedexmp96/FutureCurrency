package ar.com.futurecurrency.futurecurrency;

import java.util.ArrayList;
import java.util.List;

public class CardViewMonedaObject {
    String id;
    String name;
    String symbolMoneda;
    String valorMoneda;
    String porcentaje;
    String importePorcentaje;
    String symbolMonedaConvertida;

    public String getSymbolMonedaConvertida() {
        return symbolMonedaConvertida;
    }

    public void setSymbolMonedaConvertida(String symbolMonedaConvertida) {
        this.symbolMonedaConvertida = symbolMonedaConvertida;
    }


    public String getSymbolMoneda() {
        return symbolMoneda;
    }

    public void setSymbolMoneda(String symbolMoneda) {
        this.symbolMoneda = symbolMoneda;
    }

    public String getValorMoneda() {
        return valorMoneda;
    }

    public void setValorMoneda(String valorMoneda) {
        this.valorMoneda = valorMoneda;
    }

    public String getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(String porcentaje) {
        this.porcentaje = porcentaje;
    }

    public String getImportePorcentaje() {
        return importePorcentaje;
    }

    public void setImportePorcentaje(String importePorcentaje) {
        this.importePorcentaje = importePorcentaje;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}