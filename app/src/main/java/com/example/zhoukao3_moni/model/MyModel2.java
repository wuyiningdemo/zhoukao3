package com.example.zhoukao3_moni.model;


import com.example.zhoukao3_moni.bean.DeleteBean;
import com.example.zhoukao3_moni.utils.APIFactory;
import com.example.zhoukao3_moni.utils.AbstractObserver;

import java.util.HashMap;
import java.util.Map;

public class MyModel2 {
    public void delete(String pid,final MyModelCallBack2 myModelCallBack2) {

        final Map<String,String> map = new HashMap<>();
        map.put("source","android");
        map.put("pid",pid);
        map.put("uid","1650");

        //调用apifactory里面的方法
        APIFactory.getInstance().delete("/product/deleteCart", map, new AbstractObserver<DeleteBean>() {
            @Override
            public void onSuccess(DeleteBean deleteBean) {
                myModelCallBack2.success(deleteBean);
            }

            @Override
            public void onFailure() {
                myModelCallBack2.failure();
            }
        });
    }
}
