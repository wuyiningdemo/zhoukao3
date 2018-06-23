package com.example.zhoukao3_moni.presenter;


import com.example.zhoukao3_moni.bean.CarBean;
import com.example.zhoukao3_moni.model.MyModel;
import com.example.zhoukao3_moni.model.MyModelCallBack;
import com.example.zhoukao3_moni.view.MyViewCallBack;

public class MyPresenter {
    MyModel myModel;
    MyViewCallBack myViewCallBack;
    public MyPresenter(MyViewCallBack myViewCallBack) {
        this.myViewCallBack = myViewCallBack;
        this.myModel = new MyModel();
    }


    public void select() {
        myModel.select(new MyModelCallBack() {
            @Override
            public void success(CarBean cartBean) {
                myViewCallBack.success(cartBean);
            }

            @Override
            public void failure() {
                myViewCallBack.failure();
            }
        });
    }

    public void detach() {
        this.myViewCallBack = null;
    }
}

