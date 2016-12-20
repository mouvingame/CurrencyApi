package ru.startandroid.currencyapi;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CurrencyApi {

    @GET("http://www.nbrb.by/API/ExRates/Rates?Periodicity=0")
    Call<ArrayList<Rate>> getRates();

    @GET("http://www.nbrb.by/API/ExRates/Rates")
    Call<ArrayList<Rate>> getDateRates(@Query("onDate") String date, @Query("Periodicity") int periodicity);

}
