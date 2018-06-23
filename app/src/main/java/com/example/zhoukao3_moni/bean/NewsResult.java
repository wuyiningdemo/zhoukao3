package com.example.zhoukao3_moni.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class NewsResult {
    public String name;
    public int id;
    public String price;
    @Generated(hash = 412167241)
    public NewsResult(String name, int id, String price) {
        this.name = name;
        this.id = id;
        this.price = price;
    }
    @Generated(hash = 788391713)
    public NewsResult() {
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getPrice() {
        return this.price;
    }
    public void setPrice(String price) {
        this.price = price;
    }

}
