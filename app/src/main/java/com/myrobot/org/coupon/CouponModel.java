package com.myrobot.org.coupon;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Lixingxing
 */
public class CouponModel implements Parcelable {
    private String couponName;
    private String couponPic;
    private String couponDesc;

    private int couponRes;

    public CouponModel(String couponName, String couponDesc,int couponRes) {
        this.couponName = couponName;
        this.couponRes = couponRes;
        this.couponDesc = couponDesc;
    }

    public String getCouponDesc() {
        return couponDesc;
    }

    public void setCouponDesc(String couponDesc) {
        this.couponDesc = couponDesc;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getCouponPic() {
        return couponPic;
    }

    public void setCouponPic(String couponPic) {
        this.couponPic = couponPic;
    }

    public int getCouponRes() {
        return couponRes;
    }

    public void setCouponRes(int couponRes) {
        this.couponRes = couponRes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.couponName);
        dest.writeString(this.couponPic);
        dest.writeString(this.couponDesc);
        dest.writeInt(this.couponRes);
    }

    protected CouponModel(Parcel in) {
        this.couponName = in.readString();
        this.couponPic = in.readString();
        this.couponDesc = in.readString();
        this.couponRes = in.readInt();
    }

    public static final Creator<CouponModel> CREATOR = new Creator<CouponModel>() {
        @Override
        public CouponModel createFromParcel(Parcel source) {
            return new CouponModel(source);
        }

        @Override
        public CouponModel[] newArray(int size) {
            return new CouponModel[size];
        }
    };
}
