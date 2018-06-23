package com.example.zhoukao3_moni;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhoukao3_moni.adapter.RecyAdapter;
import com.example.zhoukao3_moni.bean.CarBean;
import com.example.zhoukao3_moni.presenter.MyPresenter;
import com.example.zhoukao3_moni.view.MyViewCallBack;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Main2Activity extends AppCompatActivity implements MyViewCallBack {
    @BindView(R.id.bianji)
    TextView bianji;
    @BindView(R.id.recycler_View)
    RecyclerView recyclerView;
    @BindView(R.id.quanxuan)
    CheckBox quanxuan;
    @BindView(R.id.total_price)
    TextView totalPrice;
    @BindView(R.id.total_num)
    TextView totalNum;
    @BindView(R.id.quzhifu)
    TextView quzhifu;
    @BindView(R.id.shanchu)
    TextView shanchu;
    @BindView(R.id.linear_shanchu)
    LinearLayout linearShanchu;

    private MyPresenter myPresenter;
    private RecyAdapter recyAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);

        bianji.setTag(1);//编辑设置tag
        quanxuan.setTag(1);//1为不选中
        LinearLayoutManager manager = new LinearLayoutManager(Main2Activity.this, LinearLayoutManager.VERTICAL, false);

        myPresenter = new MyPresenter(this);
        recyAdapter = new RecyAdapter(this);
        //进入页面查询购物车的数据
        myPresenter.select();

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(recyAdapter);

        //调用recyAdapter里面的接口,设置 全选按钮 总价 总数量
        recyAdapter.setUpdateListener(new RecyAdapter.UpdateListener() {
            @Override
            public void setTotal(String total, String num, boolean allCheck) {
                //设置ui的改变
                totalNum.setText("共" + num + "件商品");//总数量
                totalPrice.setText("总价 :¥" + total + "元");//总价
                if (allCheck) {
                    quanxuan.setTag(2);
                    quanxuan.setBackgroundResource(R.drawable.shopcart_unselected);
                } else {
                    quanxuan.setTag(1);
                    quanxuan.setBackgroundResource(R.drawable.shopcart_unselected);
                }
                quanxuan.setChecked(allCheck);
            }
        });

        //这里只做ui更改, 点击全选按钮,,调到adapter里面操作
        quanxuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用adapter里面的方法 ,,把当前quanxuan状态传递过去

                int tag = (int) quanxuan.getTag();
                if (tag == 1) {
                    quanxuan.setTag(2);
                    quanxuan.setBackgroundResource(R.drawable.shopcart_unselected);
                } else {
                    quanxuan.setTag(1);
                    quanxuan.setBackgroundResource(R.drawable.shopcart_unselected);
                }

                recyAdapter.quanXuan(quanxuan.isChecked());
            }
        });

        //点击批量删除的按钮
        shanchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyAdapter.shanChu();
            }
        });

        //点击编辑按钮,
        bianji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag = (int) bianji.getTag();
                if (tag == 1) {
                    bianji.setText("完成");
                    bianji.setTag(2);
                    quzhifu.setVisibility(View.GONE);
                    linearShanchu.setVisibility(View.VISIBLE);
                } else {
                    bianji.setText("编辑");
                    bianji.setTag(1);
                    quzhifu.setVisibility(View.VISIBLE);
                    linearShanchu.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public void success(CarBean cartBean) {
        if (cartBean != null) {
            //System.out.println(cartBean.getData().get(0).getList().get(0).getTitle());
            //将返回的数据 添加到适配器里
            recyAdapter.add(cartBean);
        }
    }

    @Override
    public void failure() {
        System.out.println("网不好");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Main2Activity.this, "网有点慢", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //调用p层的解除绑定
        myPresenter.detach();
    }
}