package com.myrobot.org.buy.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.myrobot.org.common.AppConfig;

/**
 * @author Lixingxing
 */
public class GoodModel implements Parcelable {

   	private int id;
    private String GoodsCode; // 99902071558517934744 商品编号
	private String GoodsDescribe;  // 商品描述
    private String GoodsContent;//  容量、重量
    private String MerchantId;//eventec",
    private String Name;//可口可乐",
    private String ImgUrl;//Mid1558530357784.png商品图片 URL
    private int Price;//12312    商品价格 单位为分
    private int realPrice;
    private String Type;//S
    private String CreatedAt;// 1558530358219

    private int stock = 0; // 库存


    public int getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(int realPrice) {
        this.realPrice = realPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getGoodsCode() {
        return GoodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        GoodsCode = goodsCode;
    }

    public String getGoodsDescribe() {
        return GoodsDescribe;
    }

    public void setGoodsDescribe(String goodsDescribe) {
        GoodsDescribe = goodsDescribe;
    }

    public String getGoodsContent() {
        return GoodsContent;
    }

    public void setGoodsContent(String goodsContent) {
        GoodsContent = goodsContent;
    }

    public String getMerchantId() {
        return MerchantId;
    }

    public void setMerchantId(String merchantId) {
        MerchantId = merchantId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public String getAllImageUrl(){
        return AppConfig.URL_IMAGE  + ImgUrl;
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

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    public GoodModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.GoodsCode);
        dest.writeString(this.GoodsDescribe);
        dest.writeString(this.GoodsContent);
        dest.writeString(this.MerchantId);
        dest.writeString(this.Name);
        dest.writeString(this.ImgUrl);
        dest.writeInt(this.Price);
        dest.writeInt(this.realPrice);
        dest.writeString(this.Type);
        dest.writeString(this.CreatedAt);
        dest.writeInt(this.stock);
    }

    protected GoodModel(Parcel in) {
        this.id = in.readInt();
        this.GoodsCode = in.readString();
        this.GoodsDescribe = in.readString();
        this.GoodsContent = in.readString();
        this.MerchantId = in.readString();
        this.Name = in.readString();
        this.ImgUrl = in.readString();
        this.Price = in.readInt();
        this.realPrice = in.readInt();
        this.Type = in.readString();
        this.CreatedAt = in.readString();
        this.stock = in.readInt();
    }

    public static final Creator<GoodModel> CREATOR = new Creator<GoodModel>() {
        @Override
        public GoodModel createFromParcel(Parcel source) {
            return new GoodModel(source);
        }

        @Override
        public GoodModel[] newArray(int size) {
            return new GoodModel[size];
        }
    };
}
