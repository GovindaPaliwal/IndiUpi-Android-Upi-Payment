package com.gpfreetech.IndiUpi.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TransactionResponse {
    private String transactionId;
    private String responseCode;
    private String approvalRefNo;
    private String status;
    private String transactionRefId;

    public TransactionResponse(String transactionId, String responseCode, String approvalRefNo, String status, String transactionRefId) {
        this.transactionId = transactionId;
        this.responseCode = responseCode;
        this.approvalRefNo = approvalRefNo;
        this.status = status;
        this.transactionRefId = transactionRefId;
    }

    @Nullable
    public String getTransactionId() {
        return transactionId;
    }

    @Nullable
    public String getResponseCode() {
        return responseCode;
    }

    @Nullable
    public String getApprovalRefNo() {
        return approvalRefNo;
    }

    @Nullable
    public String getStatus() {
        return status;
    }

    @Nullable
    public String getTransactionRefId() {
        return transactionRefId;
    }

    @NonNull
    @Override
    public String toString() {
        return "transactionId:" + transactionId +
                ", responseCode:" + responseCode +
                ", transactionRefId:" + transactionRefId +
                ", approvalRefNo:" + approvalRefNo +
                ", status:" + status;

    }

    @Nullable
    public String toHTMLString() {
        return "<b>transactionId:</b>" + transactionId +
                "<br><b>responseCode:</b>" + responseCode +
                "<br><b>transactionRefId:</b>" + transactionRefId +
                "<br><b>approvalRefNo:</b>" + approvalRefNo +
                "<br><b>status:</b>" + status;

    }
}
