package ar.com.futurecurrency.futurecurrency;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class TarjetasFragment extends Fragment {
    private CardViewMonedasABM abm = new CardViewMonedasABM();
    SwipeRefreshLayout swipeRefreshLayout;
    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }



    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,

                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_tarjetas_monedas, container, false);




        cargarItems(v);
        return v;

    }




    private void cargarItems(final View view){


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.agregar_tarjeta_moneda);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), NuevaTarjetaMonedaActivity.class);
              //  myIntent.putExtra("key", value); //Optional parameters
                getActivity().startActivity(myIntent);
            }
        });
        swipeRefreshLayout= (SwipeRefreshLayout)view.findViewById(R.id.swipper_card_view_monedas);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargarItems(view);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },1000);
            }
        });



        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        SharedPreferences.Editor editor = preferences.edit();

        List<CardViewMonedaObject> listaCardViews = reconsultaMonedasGuardadas();

        RecyclerView rv = (RecyclerView)view.findViewById(R.id.rv_card_view_monedas);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
        rv.setLayoutManager(llm);
        AdaptadorCardViewListaMonedas adapter = new AdaptadorCardViewListaMonedas(listaCardViews);
        rv.setAdapter(adapter);

    }



    public List<CardViewMonedaObject> reconsultaMonedasGuardadas(){

        List<CardViewMonedaObject> listaCardViews = abm.reconsultaMonedasGuardadas(this.getContext());



        return listaCardViews;
    }

}
