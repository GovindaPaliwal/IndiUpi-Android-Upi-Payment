package com.gpfreetech.IndiUpi.listener;

import com.gpfreetech.IndiUpi.entity.TransactionResponse;

public interface PaymentStatusListener {
    void onTransactionCompleted(TransactionResponse transactionDetails);
    void onTransactionSuccess(TransactionResponse transactionDetails);
    void onTransactionSubmitted();
    void onTransactionFailed();
    void onTransactionCancelled();
}
