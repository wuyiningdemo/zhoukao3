package com.example.zhoukao3_moni.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.zhoukao3_moni.R;
import com.example.zhoukao3_moni.bean.CarBean;
import com.example.zhoukao3_moni.bean.DeleteBean;
import com.example.zhoukao3_moni.presenter.MyPresenter;
import com.example.zhoukao3_moni.presenter.MyPresenter2;
import com.example.zhoukao3_moni.view.CustomJiajian;
import com.example.zhoukao3_moni.view.MyViewCallBack;
import com.example.zhoukao3_moni.view.MyViewCallBack2;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RecyAdapter extends RecyclerView.Adapter<RecyAdapter.MyViewHolder> implements MyViewCallBack2,MyViewCallBack {
    MyPresenter myPresenter;
    //创建大的集合
    private List<CarBean.DataBean.ListBean> list;
    //存放商家的id和商家的名称的map集合
    private Map<String,String> map = new HashMap<>();

    MyPresenter2 myPresenter2 ;
    Context context;
    public RecyAdapter(Context context) {
        this.context = context;
        myPresenter = new MyPresenter(this);
        myPresenter2 = new MyPresenter2(this);
        //初始化fresco
        Fresco.initialize(context);
    }
    public void add(CarBean cartBean) {
        if(list==null){
            list = new ArrayList<>();
        }

        if(cartBean!=null) {
            for (CarBean.DataBean shop : cartBean.getData()) {
                map.put(shop.getSellerid(), shop.getSellerName());
                //第二层遍历里面的商品
                for (int i = 0; i < shop.getList().size(); i++) {
                    //添加到list集合里
                    list.add(shop.getList().get(i));
                }
            }
            //调用方法 设置显示或隐藏 商铺名
            setFirst(list);

        }    notifyDataSetChanged();
    }

    /**
     * 设置数据源,控制是否显示商家
     * */
    private void setFirst(List<CarBean.DataBean.ListBean> list) {
        if(list.size()>0){
            list.get(0).setIsFirst(1);
            //从第二条开始遍历
            for (int i=1;i<list.size();i++){
                //如果和前一个商品是同一家商店的
                if (list.get(i).getSellerid() == list.get(i-1).getSellerid()){
                    //设置成2不显示商铺
                    list.get(i).setIsFirst(2);
                }else{//设置成1显示商铺
                    list.get(i).setIsFirst(1);
                    //如果当前条目选中,把当前的商铺也选中
                    if (list.get(i).isItem_check()==true){
                        list.get(i).setShop_check(list.get(i).isItem_check());
                    }
                }
            }
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=View.inflate(context,R.layout.recy_cart_item,null);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        /**
         * 设置商铺的 shop_checkbox和商铺的名字 显示或隐藏
         * */
        if(list.get(position).getIsFirst()==1){
            //显示商家
            holder.shop_checkbox.setVisibility(View.VISIBLE);
            holder.shop_name.setVisibility(View.VISIBLE);
            //设置shop_checkbox的选中状态
            holder.shop_checkbox.setChecked(list.get(position).isShop_check());
            holder.shop_name.setText(map.get(String.valueOf(list.get(position).getSellerid()))+" ＞");
        }else{//2
            //隐藏商家
            holder.shop_name.setVisibility(View.GONE);
            holder.shop_checkbox.setVisibility(View.GONE);
        }

        //拆分images字段
        String[] split = list.get(position).getImages().split("\\|");
        //设置商品的图片
        holder.item_face.setImageURI(Uri.parse(split[0]));
        //控制商品的item_checkbox,,根据字段改变
        holder.item_checkbox.setChecked(list.get(position).isItem_check());
        holder.item_name.setText(list.get(position).getTitle());
        holder.item_price.setText(list.get(position).getPrice()+"");
        //调用customjiajian里面的方法设置 加减号中间的数字
        holder.customJiaJian.setEditText(list.get(position).getNum());

        holder.item_bianji.setTag(1);
        //点击item编辑才显示 自定义加减框
        holder.item_bianji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag= (int) holder.item_bianji.getTag();

                if(tag==1) {
                    //加减号显示
                    holder.item_bianji.setText("完成");
                    holder.customJiaJian.setVisibility(View.VISIBLE);
                    //商品的名称隐藏
                    holder.item_name.setVisibility(View.GONE);
                    holder.item_yansechima.setVisibility(View.VISIBLE);
                    holder.item_price.setVisibility(View.GONE);
                    holder.item_delete.setVisibility(View.VISIBLE);
                    holder.item_bianji.setTag(2);
                }else{
                    //相反的 隐藏的显示,显示的隐藏
                    //加减号显示
                    holder.item_bianji.setText("编辑");
                    holder.customJiaJian.setVisibility(View.GONE);
                    //商品的名称隐藏
                    holder.item_name.setVisibility(View.VISIBLE);
                    holder.item_yansechima.setVisibility(View.GONE);
                    holder.item_price.setVisibility(View.VISIBLE);
                    holder.item_delete.setVisibility(View.GONE);
                    holder.item_bianji.setTag(1);
                }
            }
        });
        //设置点击多选框
        //商铺的shop_checkbox点击事件 ,控制商品的item_checkbox
        holder.shop_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先改变数据源中的shop_check
                list.get(position).setShop_check(holder.shop_checkbox.isChecked());

                for (int i=0;i<list.size();i++){
                    //如果是同一家商铺的 都给成相同状态
                    if(list.get(position).getSellerid()==list.get(i).getSellerid()){
                        //当前条目的选中状态 设置成 当前商铺的选中状态
                        list.get(i).setItem_check(holder.shop_checkbox.isChecked());
                    }
                }
                //刷新适配器
                notifyDataSetChanged();
                //调用求和的方法
                sum(list);
            }
        });

        //商品的item_checkbox点击事件,控制商铺的shop_checkbox
        holder.item_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先改变数据源中的item_checkbox
                list.get(position).setItem_check(holder.item_checkbox.isChecked());

                //反向控制商铺的shop_checkbox
                for (int i=0;i<list.size();i++){
                    for (int j=0;j<list.size();j++){
                        //如果两个商品是同一家店铺的 并且 这两个商品的item_checkbox选中状态不一样
                        if(list.get(i).getSellerid()==list.get(j).getSellerid() && !list.get(j).isItem_check()){
                            //就把商铺的shop_checkbox改成false
                            list.get(i).setShop_check(false);
                            break;
                        }else{
                            //同一家商铺的商品 选中状态都一样,就把商铺shop_checkbox状态改成true
                            list.get(i).setShop_check(true);
                        }
                    }
                }

                //更新适配器
                notifyDataSetChanged();
                //调用求和的方法
                sum(list);
            }
        });

        //删除条目的点击事件
        holder.item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);//移除集合中的当前数据
                //删除完当前的条目 重新判断商铺的显示隐藏
                setFirst(list);

                //调用重新求和
                sum(list);
                notifyDataSetChanged();
            }
        });

        //加减号的监听,
        holder.customJiaJian.setCustomListener(new CustomJiajian.CustomListener() {
            @Override
            public void jiajian(int count) {
                //改变数据源中的数量
                list.get(position).setNum(count);
                notifyDataSetChanged();
                sum(list);
            }

            @Override
            //输入值 求总价
            public void shuRuZhi(int count) {
                list.get(position).setNum(count);
                notifyDataSetChanged();
                sum(list);
            }
        });
    }

    //批量删除的按钮
    public void shanChu() {
        //存储删除的id
        final List<Integer> delete_listid = new ArrayList<>();
        for (int i=0;i<list.size();i++){
            if(list.get(i).isItem_check()){
                //将要删除的pid添加到这个集合里
                delete_listid.add(list.get(i).getPid());
            }
        }
        if (delete_listid.size()==0){
            //如果没有要删除的,就吐司提示
            Toast.makeText(context, "请选中至少一个商品后再删除", Toast.LENGTH_SHORT).show();
            return;
        }
        //弹框
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("操作提示");
        dialog.setMessage("你确定要删除这"+delete_listid.size()+"个商品?");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //使用接口删除
                String a  = "";
                for(int j=0;j<delete_listid.size();j++) {

                    // a+=delete_listid.get(j)+"";
                    Integer integer = delete_listid.get(j);
                    String pid = String.valueOf(integer);
                    myPresenter2.delete(pid);
                    // list.remove(j);
                }
                //  Toast.makeText(context, a,Toast.LENGTH_SHORT).show();

            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        }).create().show();
    }

    //view层调用这个方法, 点击quanxuan按钮的操作
    public void quanXuan(boolean checked) {
        for (int i=0;i<list.size();i++){
            list.get(i).setShop_check(checked);
            list.get(i).setItem_check(checked);

        }
        notifyDataSetChanged();
        sum(list);
    }

    private void sum(List<CarBean.DataBean.ListBean> list) {
        int totalNum = 0;
        float totalMoney = 0.0f;
        boolean allCheck = true;
        for (int i=0;i<list.size();i++){
            if (list.get(i).isItem_check()){
                totalNum += list.get(i).getNum();
                totalMoney += list.get(i).getNum() * list.get(i).getPrice();
            }else{
                //如果有个未选中,就标记为false
                allCheck = false;
            }
        }
        //接口回调出去 把总价 总数量 和allcheck 传给view层
        updateListener.setTotal(totalMoney+"",totalNum+"",allCheck);
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    @Override
    public void success(DeleteBean deleteBean) {
        //调用第一个presenter的方法 重新查询数据
        myPresenter.select();

    }

    @Override
    public void success(CarBean cartBean) {
        list.clear();
        add(cartBean);
    }

    @Override
    public void failure() {
        System.out.println("网不好");
        Toast.makeText(context, "adapter网有点慢", Toast.LENGTH_SHORT).show();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private final CheckBox shop_checkbox;
        private final TextView shop_name;
        private final CheckBox item_checkbox;
        private final TextView item_name;
        private final TextView item_price;
        private final CustomJiajian customJiaJian;
        //private final ImageView item_delete;
        private final TextView item_delete;
        private final SimpleDraweeView item_face;
        private final TextView item_bianji;
        private final TextView item_yansechima;


        public MyViewHolder(View itemView) {
            super(itemView);
            //拿到控件
            shop_checkbox = itemView.findViewById(R.id.shop_checkbox);
            shop_name =  itemView.findViewById(R.id.shop_name);
            item_checkbox = itemView.findViewById(R.id.item_checkbox);
            item_name = itemView.findViewById(R.id.item_name);
            item_price =  itemView.findViewById(R.id.item_price);
            customJiaJian = itemView.findViewById(R.id.custom_jiajian);
            //item_delete = (ImageView) itemView.findViewById(R.id.item_delete);
            item_delete = itemView.findViewById(R.id.item_delete);

            item_face =  itemView.findViewById(R.id.item_face);
            item_bianji = itemView.findViewById(R.id.item_bianji);
            item_yansechima = itemView.findViewById(R.id.item_yansechima);

        }
    }


    UpdateListener updateListener;
    public void setUpdateListener(UpdateListener updateListener){
        this.updateListener = updateListener;
    }
    //接口
    public interface UpdateListener{
        public void setTotal(String total, String num, boolean allCheck);
    }
}
