package com.example.zhoukao3_moni.utils;


import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface IGetService {
    @GET
    Observable<String> select(@Url String url,@QueryMap Map<String,String> map);
    @GET
    Observable<String> updata(@Url String url,@QueryMap Map<String,String> map);

    @GET
    Observable<String> delete(@Url String url,@QueryMap Map<String,String> map);




}
