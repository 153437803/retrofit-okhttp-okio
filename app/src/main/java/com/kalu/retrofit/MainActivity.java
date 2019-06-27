package com.kalu.retrofit;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .build();

        Retrofit build = new Retrofit.Builder()
                .baseUrl("https://t.thortk.com/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        HttpApi api = build.create(HttpApi.class);

        final FormBody.Builder builder = new FormBody.Builder();
        builder.add("osType", "1");
        builder.add("version", BuildConfig.VERSION_NAME);
        builder.add("uuid", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        builder.add("lang", "cn");

        Observable<Object> observable = api.homelist(builder.build());
        observable.delaySubscription(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    Log.e("klau", o.toString());
                });
    }
}
