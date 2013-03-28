package com.nearme.gamecenter.open.api;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductInfo implements Parcelable {

	private double amount;
	private String callBackUrl;
	private String partnerOrder;
	private String productDesc;
	private String productName;
	private int type;
	private int count;

	public ProductInfo(double amount, String callBackUrl, String partnerOrder,
			String productDesc, String productName, int type, int count) {
		super();
		this.amount = amount;
		this.callBackUrl = callBackUrl;
		this.partnerOrder = partnerOrder;
		this.productDesc = productDesc;
		this.productName = productName;
		this.type = type;
		this.count = count;
	}

	public double getAmount() {
		return amount;
	}

	public String getCallBackUrl() {
		return callBackUrl;
	}

	public String getPartnerOrder() {
		return partnerOrder;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public String getProductName() {
		return productName;
	}

	public int getType() {
		return type;
	}

	public void setCallBackUrl(String callBackUrl) {
		this.callBackUrl = callBackUrl;
	}

	public int getCount() {
		return count;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeDouble(amount);
		dest.writeString(callBackUrl);
		dest.writeString(partnerOrder);
		dest.writeString(productDesc);
		dest.writeString(productName);
		dest.writeInt(type);
		dest.writeInt(count);
	}

	public static final Parcelable.Creator<ProductInfo> CREATOR = new Creator<ProductInfo>() {

		@Override
		public ProductInfo createFromParcel(Parcel source) {
			return new ProductInfo(source.readDouble(), source.readString(),
					source.readString(), source.readString(),
					source.readString(), source.readInt(), source.readInt());
		}

		@Override
		public ProductInfo[] newArray(int size) {
			return new ProductInfo[size];
		}

	};
}
