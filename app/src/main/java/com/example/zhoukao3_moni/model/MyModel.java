package com.example.zhoukao3_moni.model;


import com.example.zhoukao3_moni.bean.CarBean;
import com.example.zhoukao3_moni.utils.APIFactory;
import com.example.zhoukao3_moni.utils.AbstractObserver;

import java.util.HashMap;
import java.util.Map;

public class MyModel {
    public void select(final MyModelCallBack myModelCallBack) {

        final Map<String,String> map = new HashMap<>();
        map.put("source","android");
        map.put("uid","1650");

        //调用apifactory里面的方法
        APIFactory.getInstance().select("/product/getCarts", map, new AbstractObserver<CarBean>() {
            @Override
            public void onSuccess(CarBean cartBean) {
                myModelCallBack.success(cartBean);
            }

            @Override
            public void onFailure() {
                myModelCallBack.failure();
            }
        });
    }
}
