package com.gpfreetech.IndiUpi.entity;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class PaymentPayload implements Parcelable {
    private String vpa;
    private String name;
    private String payeeMerchantCode;
    private String txnId;
    private String txnRefId;
    private String description;
    private String amount;
    private Uri url;
    private String currency;

    public PaymentPayload() {
        currency = "INR";
    }

    protected PaymentPayload(Parcel in) {
        vpa = in.readString();
        name = in.readString();
        payeeMerchantCode = in.readString();
        txnId = in.readString();
        txnRefId = in.readString();
        description = in.readString();
        amount = in.readString();
        currency = in.readString();
        url = in.readParcelable(Uri.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(vpa);
        dest.writeString(name);
        dest.writeString(payeeMerchantCode);
        dest.writeString(txnId);
        dest.writeString(txnRefId);
        dest.writeString(description);
        dest.writeString(amount);
        dest.writeString(currency);
        dest.writeParcelable(url, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PaymentPayload> CREATOR = new Creator<PaymentPayload>() {
        @Override
        public PaymentPayload createFromParcel(Parcel in) {
            return new PaymentPayload(in);
        }

        @Override
        public PaymentPayload[] newArray(int size) {
            return new PaymentPayload[size];
        }
    };

    public String getVpa() {
        return vpa;
    }

    public void setVpa(String vpa) {
        this.vpa = vpa;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPayeeMerchantCode() {
        return payeeMerchantCode;
    }

    public void setPayeeMerchantCode(String payeeMerchantCode) {
        this.payeeMerchantCode = payeeMerchantCode;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public String getTxnRefId() {
        return txnRefId;
    }

    public void setTxnRefId(String txnRefId) {
        this.txnRefId = txnRefId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public Uri getUrl() {
        return url;
    }

    public void setUrl(String scheme, String authority, String appendPath) {
        Uri.Builder callbackUrl = new Uri.Builder();
        try {
            callbackUrl.scheme(scheme).authority(authority);
            callbackUrl.appendPath(appendPath);
            this.url = callbackUrl.build();
        } catch (Exception e) {
            e.printStackTrace();
            this.url = callbackUrl.build();
        }
    }
}
