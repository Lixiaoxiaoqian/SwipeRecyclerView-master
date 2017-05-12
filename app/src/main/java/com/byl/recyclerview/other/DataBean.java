package com.byl.recyclerview.other;

/**
 * Created by fan on 2016/11/10.
 */

public class DataBean {
    private String id;

    private String goods_name;

    private double shop_price;

    private double market_price;

    private boolean is_coupon_allowed;

    private boolean is_allow_credit;

    private String goods_img;

    private int sales_volume;

    private String efficacy;

    private String watermarkUrl;

    private int sort;

    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
    public void setGoods_name(String goods_name){
        this.goods_name = goods_name;
    }
    public String getGoods_name(){
        return this.goods_name;
    }
    public void setShop_price(double shop_price){
        this.shop_price = shop_price;
    }
    public double getShop_price(){
        return this.shop_price;
    }
    public void setMarket_price(int market_price){
        this.market_price = market_price;
    }
    public double getMarket_price(){
        return this.market_price;
    }
    public void setIs_coupon_allowed(boolean is_coupon_allowed){
        this.is_coupon_allowed = is_coupon_allowed;
    }
    public boolean getIs_coupon_allowed(){
        return this.is_coupon_allowed;
    }
    public void setIs_allow_credit(boolean is_allow_credit){
        this.is_allow_credit = is_allow_credit;
    }
    public boolean getIs_allow_credit(){
        return this.is_allow_credit;
    }
    public void setGoods_img(String goods_img){
        this.goods_img = goods_img;
    }
    public String getGoods_img(){
        return this.goods_img;
    }
    public void setSales_volume(int sales_volume){
        this.sales_volume = sales_volume;
    }
    public int getSales_volume(){
        return this.sales_volume;
    }
    public void setEfficacy(String efficacy){
        this.efficacy = efficacy;
    }
    public String getEfficacy(){
        return this.efficacy;
    }
    public void setWatermarkUrl(String watermarkUrl){
        this.watermarkUrl = watermarkUrl;
    }
    public String getWatermarkUrl(){
        return this.watermarkUrl;
    }
    public void setSort(int sort){
        this.sort = sort;
    }
    public int getSort(){
        return this.sort;
    }
}
