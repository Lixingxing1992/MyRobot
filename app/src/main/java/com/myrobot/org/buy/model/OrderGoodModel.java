package com.myrobot.org.buy.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Lixingxing
 */
public class OrderGoodModel implements Parcelable {

    /**
     * GoodsCode : 99888051558508953664
     * GoodsName : 商品名称
     * ImgUrl : Mid1558531627002.png
     * Price : 1(单位分)
     */

    private String GoodsCode;
    private String GoodsName;
    private String ImgUrl;
    private int Price;

    public OrderGoodModel(String goodsCode, String goodsName, String imgUrl, int price) {
        GoodsCode = goodsCode;
        GoodsName = goodsName;
        ImgUrl = imgUrl;
        Price = price;
    }

    public String getGoodsCode() {
        return GoodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        GoodsCode = goodsCode;
    }

    public String getGoodsName() {
        return GoodsName;
    }

    public void setGoodsName(String goodsName) {
        GoodsName = goodsName;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.GoodsCode);
        dest.writeString(this.GoodsName);
        dest.writeString(this.ImgUrl);
        dest.writeInt(this.Price);
    }

    public OrderGoodModel() {
    }

    protected OrderGoodModel(Parcel in) {
        this.GoodsCode = in.readString();
        this.GoodsName = in.readString();
        this.ImgUrl = in.readString();
        this.Price = in.readInt();
    }

    public static final Creator<OrderGoodModel> CREATOR = new Creator<OrderGoodModel>() {
        @Override
        public OrderGoodModel createFromParcel(Parcel source) {
            return new OrderGoodModel(source);
        }

        @Override
        public OrderGoodModel[] newArray(int size) {
            return new OrderGoodModel[size];
        }
    };
}
