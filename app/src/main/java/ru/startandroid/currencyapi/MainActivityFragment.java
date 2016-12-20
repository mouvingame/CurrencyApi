package ru.startandroid.currencyapi;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivityFragment extends Fragment implements Callback<ArrayList<Rate>>{

    private RecyclerView recyclerView;
    private Button button;

    private SimpleDateFormat sdf;

    private Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl("http://www.nbrb.by/API/ExRates/")
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();

    private CurrencyApi currencyApi = retrofit.create(CurrencyApi.class);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        button = (Button)view.findViewById(R.id.button);

        sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(savedInstanceState != null){
            button.setText(sdf.format((Date)savedInstanceState.getSerializable("date")));
            Call<ArrayList<Rate>> call = currencyApi.getDateRates(button.getText().toString(), 0);
            call.enqueue(this);
        }else{
            button.setText(sdf.format(new Date(System.currentTimeMillis())));
            Call<ArrayList<Rate>> call = currencyApi.getRates();
            call.enqueue(this);
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialogFragment dialog = null;
                try {
                    dialog = DatePickerDialogFragment.newInstance(sdf.parse(button.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dialog.setTargetFragment(MainActivityFragment.this, 0);
                dialog.show(getFragmentManager(), "dialog");
            }
        });

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);

        return view;
    }

    @Override
    public void onResponse(Call<ArrayList<Rate>> call, Response<ArrayList<Rate>> response) {
        ArrayList<Rate> currencyList = response.body();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new CurrencyAdapter(currencyList));
    }

    @Override
    public void onFailure(Call<ArrayList<Rate>> call, Throwable t) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        button.setText(sdf.format(data.getSerializableExtra("date")));
        Call<ArrayList<Rate>> call = currencyApi.getDateRates(button.getText().toString(), 0);
        call.enqueue(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
            outState.putSerializable("date", sdf.parse(button.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private class CurrencyHolder extends RecyclerView.ViewHolder{

        private TextView textViewCurrency;
        private TextView textViewName;
        private TextView textViewEdinica;
        private TextView textViewRate;

        public CurrencyHolder(View itemView) {
            super(itemView);
            textViewCurrency = (TextView)itemView.findViewById(R.id.currency);
            textViewName = (TextView)itemView.findViewById(R.id.name);
            textViewEdinica = (TextView)itemView.findViewById(R.id.edinica);
            textViewRate = (TextView)itemView.findViewById(R.id.rate);
        }

        public void bindCurrency(Rate rate){
            textViewCurrency.setText(rate.getCurAbbreviation());
            textViewName.setText(rate.getCurName());
            textViewName.setSelected(true);
            textViewEdinica.setText("" + rate.getCurScale());
            textViewRate.setText("" + rate.getCurOfficialRate());
        }
    }

    private class CurrencyAdapter extends RecyclerView.Adapter<CurrencyHolder>{

        ArrayList<Rate> currencyList;

        public CurrencyAdapter(ArrayList<Rate> list){
            currencyList = list;
        }

        @Override
        public CurrencyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.recycler_view_item, parent, false);
            return new CurrencyHolder(view);
        }

        @Override
        public void onBindViewHolder(CurrencyHolder holder, int position) {
            holder.bindCurrency(currencyList.get(position));
        }

        @Override
        public int getItemCount() {
            return currencyList.size();
        }
    }
}
