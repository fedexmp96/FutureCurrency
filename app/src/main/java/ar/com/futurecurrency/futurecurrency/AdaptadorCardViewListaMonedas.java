package ar.com.futurecurrency.futurecurrency;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class AdaptadorCardViewListaMonedas extends RecyclerView.Adapter<AdaptadorCardViewListaMonedas.MonedasViewHolder>{



    List<CardViewMonedaObject> listaMonedas;

    AdaptadorCardViewListaMonedas(List<CardViewMonedaObject> listaMonedas){
        this.listaMonedas = listaMonedas;
    }

    @Override
    public int getItemCount() {
        return listaMonedas.size();
    }

    @NonNull
    @Override
    public MonedasViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_empty_monedas, viewGroup, false);
        MonedasViewHolder mvh = new MonedasViewHolder(v);
        return mvh;    }

    @Override
    public void onBindViewHolder(@NonNull MonedasViewHolder holder, int position) {
        MonedasViewHolder.symbolMoneda.setText(listaMonedas.get(position).symbolMoneda);
        MonedasViewHolder.symbolMonedaConvertida.setText(listaMonedas.get(position).symbolMonedaConvertida);
        MonedasViewHolder.valorMoneda.setText(listaMonedas.get(position).valorMoneda);
        MonedasViewHolder.porcentaje.setText(listaMonedas.get(position).porcentaje + "%");
        MonedasViewHolder.importePorcentaje.setText(listaMonedas.get(position).importePorcentaje);

    }


    public static class MonedasViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        static TextView symbolMoneda;
        static TextView symbolMonedaConvertida;
        static TextView valorMoneda;
        static  TextView porcentaje;
        static  TextView importePorcentaje;


        MonedasViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            symbolMoneda = (TextView)itemView.findViewById(R.id.symbol_moneda);
            symbolMonedaConvertida = (TextView)itemView.findViewById(R.id.symbol_moneda_convertida);
            valorMoneda = (TextView)itemView.findViewById(R.id.valor_moneda);
            importePorcentaje = (TextView)itemView.findViewById(R.id.importe_porcentaje);
            porcentaje = (TextView)itemView.findViewById(R.id.porcentaje);

        }
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}



