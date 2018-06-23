package com.example.zhoukao3_moni.utils;

import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class APIFactory {
    private static APIFactory factory = null;

    public static APIFactory getInstance(){
        if(factory==null){
            synchronized (APIFactory.class){
                if(factory==null){
                    factory = new APIFactory();

                }
            }
        }
        return factory;
    }

    //查询购物车的方法
    public void select(String url, Map<String,String> map, Observer<String> observer){
        //调用retrofit工具类
        RetroftUtils.getInstance().select(url,map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    //删除购物车的方法
    public void delete(String url, Map<String,String> map, Observer<String> observer){
        //调用retrofit工具类
        RetroftUtils.getInstance().select(url,map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


}
