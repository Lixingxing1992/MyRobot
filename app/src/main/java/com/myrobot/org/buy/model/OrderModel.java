package com.myrobot.org.buy.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单类
 * @author Lixingxing
 */
public class OrderModel implements Parcelable {
    private String DeviceSN;
    private String MerchantId;
    private List<OrderGoodModel> ShoppingList = new ArrayList<>();

    public OrderModel(String deviceSN, String merchantId, List<OrderGoodModel> shoppingList) {
        DeviceSN = deviceSN;
        MerchantId = merchantId;
        ShoppingList = shoppingList;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public String getDeviceSN() {
        return DeviceSN;
    }

    public void setDeviceSN(String deviceSN) {
        DeviceSN = deviceSN;
    }

    public String getMerchantId() {
        return MerchantId;
    }

    public void setMerchantId(String merchantId) {
        MerchantId = merchantId;
    }

    public List<OrderGoodModel> getShoppingList() {
        return ShoppingList;
    }

    public void setShoppingList(List<OrderGoodModel> shoppingList) {
        ShoppingList = shoppingList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.DeviceSN);
        dest.writeString(this.MerchantId);
        dest.writeTypedList(this.ShoppingList);
    }

    public OrderModel() {
    }

    protected OrderModel(Parcel in) {
        this.DeviceSN = in.readString();
        this.MerchantId = in.readString();
        this.ShoppingList = in.createTypedArrayList(OrderGoodModel.CREATOR);
    }

    public static final Creator<OrderModel> CREATOR = new Creator<OrderModel>() {
        @Override
        public OrderModel createFromParcel(Parcel source) {
            return new OrderModel(source);
        }

        @Override
        public OrderModel[] newArray(int size) {
            return new OrderModel[size];
        }
    };
}
