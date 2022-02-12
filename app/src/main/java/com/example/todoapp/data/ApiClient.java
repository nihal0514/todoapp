package com.example.todoapp.data;


import com.example.todoapp.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static String BASE_URL="http://192.168.1.40:3000/api/todo/";
    private static Retrofit retrofit;
    public static Retrofit getRetrofit() {
        HttpLoggingInterceptor httpLoggingInterceptor=new HttpLoggingInterceptor();
        if(BuildConfig.DEBUG){
            httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        }else{
            httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);

        }
        OkHttpClient client=new OkHttpClient.Builder()
                .writeTimeout(10, TimeUnit.SECONDS)             //iska matlab data upload kar rhe hai
                .readTimeout(10,TimeUnit.SECONDS)               //read timeout data load karne ke liye
                .addInterceptor(httpLoggingInterceptor).build();
        if(retrofit==null){
            retrofit=new Retrofit.Builder().baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofit;
    }
    public static ApiService getApiService(){
        ApiService apiService= getRetrofit().create(ApiService.class);
        return  apiService;
    }
}
