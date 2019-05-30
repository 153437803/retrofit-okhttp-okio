package com.kalu.retrofit;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * description: api
 * create by kalu on 2019/02/26
 */
public interface HttpApi {

    @POST("api/main/index")
    Observable<Object> homelist(@Body RequestBody body);
}