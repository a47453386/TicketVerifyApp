package com.example.ticketverifyapp;

import com.example.ticketverifyapp.APIService.APIService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    // 🚩 根據你的截圖，已修正為正確的區域網路 IP
    private static final String IP = "192.168.0.107";
    private static final String BASE_URL = "http://" + IP + ":5098/";

    public static Retrofit getInstance() {
        if (retrofit == null) {
            // 加入 Logging 攔截器，連線成功或失敗都會在 Logcat 顯示詳細資訊
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static APIService getApiService() {
        return getInstance().create(APIService.class);
    }
}