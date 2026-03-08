package com.example.ticketverifyapp;


import com.example.ticketverifyapp.APIService.APIService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;
    // 🚩 指向你本機 API 的網址
    private static final String BASE_URL = "http://10.0.2.2:5098/";

    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public static APIService getApiService() {
        return getInstance().create(APIService.class);
    }
}
