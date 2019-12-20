package com.gpfreetech.IndiUpi.ui;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gpfreetech.IndiUpi.R;
import com.gpfreetech.IndiUpi.Singleton;
import com.gpfreetech.IndiUpi.entity.PaymentPayload;
import com.gpfreetech.IndiUpi.entity.TransactionResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PaymentActivity extends AppCompatActivity {
    private static final String TAG = "PaymentActivity";
    public static final int PAYMENT_REQUEST = 1001;
    private Singleton singleton;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        // Get instance of Singleton class
        singleton = Singleton.getInstance();

        //Get PaymentPayload Information
        Intent intent = getIntent();
        PaymentPayload payment = (PaymentPayload) intent.getParcelableExtra("payment");
        if (!TextUtils.isEmpty(intent.getStringExtra("title"))) {
            title = intent.getStringExtra("title");
        } else {
            title = getString(R.string.default_text_pay_using);
        }
        // Set Parameters for UPI
        Uri.Builder payUri = new Uri.Builder();

        payUri.scheme("upi").authority("pay");
        payUri.appendQueryParameter("pa", payment.getVpa());
        payUri.appendQueryParameter("pn", payment.getName());
        payUri.appendQueryParameter("tid", payment.getTxnId());

        if (payment.getPayeeMerchantCode() != null) {
            payUri.appendQueryParameter("mc", payment.getPayeeMerchantCode());
        }

        payUri.appendQueryParameter("tr", payment.getTxnRefId());
        payUri.appendQueryParameter("tn", payment.getDescription());
        payUri.appendQueryParameter("am", payment.getAmount());
        payUri.appendQueryParameter("cu", payment.getCurrency());

        try {
            if (payment.getUrl() != null) {
                Uri.Builder callbackUrl = payment.getUrl().buildUpon();
                callbackUrl.appendQueryParameter("pa", payment.getVpa());
                callbackUrl.appendQueryParameter("pn", payment.getName());
                callbackUrl.appendQueryParameter("tid", payment.getTxnId());

                if (payment.getPayeeMerchantCode() != null) {
                    callbackUrl.appendQueryParameter("mc", payment.getPayeeMerchantCode());
                }

                callbackUrl.appendQueryParameter("tr", payment.getTxnRefId());
                callbackUrl.appendQueryParameter("tn", payment.getDescription());
                callbackUrl.appendQueryParameter("am", payment.getAmount());
                callbackUrl.appendQueryParameter("cu", payment.getCurrency());
                payUri.appendQueryParameter("url", callbackUrl.build().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Build URI
        Uri uri = payUri.build();

        // Set Data Intent
        Intent paymentIntent = new Intent(Intent.ACTION_VIEW);
        paymentIntent.setData(uri);

        // Check if app is installed or not
        if (paymentIntent.resolveActivity(getPackageManager()) != null) {
            List<ResolveInfo> intentList = getPackageManager().queryIntentActivities(paymentIntent, 0);
            showApps(intentList, paymentIntent);
        } else {
            Toast.makeText(this, "No UPI Supported app found in device! Please Install to Proceed!", Toast.LENGTH_LONG).show();
        }
    }

    private void showApps(List<ResolveInfo> appsList, final Intent intent) {
        //Listener to know about cancellation of payment
        View.OnClickListener onCancelListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackTransactionCancelled();
                finish();
            }
        };

        AppsBottomSheet appsBottomSheet = new AppsBottomSheet(appsList, intent, onCancelListener);
        appsBottomSheet.show(getSupportFragmentManager(), title);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYMENT_REQUEST) {
            if (data != null) {
                //Get Response from activity intent
                String response = data.getStringExtra("response");

                if (response == null) {
                    callbackTransactionCancelled();
                    Log.d(TAG, "Response is null");

                } else {

                    TransactionResponse transactionDetails = getTransactionDetails(response);

                    //Update Listener onTransactionCompleted()
                    callbackTransactionComplete(transactionDetails);

                    //Check if success, submitted or failed
                    try {
                        if (transactionDetails.getStatus().toLowerCase().equals("success")) {
                            callbackTransactionSuccess(transactionDetails);
                        } else if (transactionDetails.getStatus().toLowerCase().equals("submitted")) {
                            callbackTransactionSubmitted();
                        } else {
                            callbackTransactionFailed();
                        }
                    } catch (Exception e) {
                        callbackTransactionCancelled();
                        callbackTransactionFailed();
                    }
                }
            } else {
                Log.e(TAG, "Intent Data is null. User cancelled");
                callbackTransactionCancelled();
            }
            finish();
        }
    }

    private Map<String, String> getQueryString(String url) {
        String[] params = url.split("&");
        Map<String, String> map = new HashMap<String, String>();
        for (String param : params) {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }

    //Make TransactionResponse object from response string
    private TransactionResponse getTransactionDetails(String response) {
        Map<String, String> map = getQueryString(response);

        String transactionId = map.get("txnId");
        String responseCode = map.get("responseCode");
        String approvalRefNo = map.get("ApprovalRefNo");
        String status = map.get("Status");
        String transactionRefId = map.get("txnRef");

        return new TransactionResponse(transactionId, responseCode, approvalRefNo, status, transactionRefId);
    }

    private boolean isListenerRegistered() {
        return (Singleton.getInstance().isListenerRegistered());
    }

    private void callbackTransactionSuccess(TransactionResponse transactionDetails) {
        if (isListenerRegistered()) {
            singleton.getListener().onTransactionSuccess(transactionDetails);
        }
    }

    private void callbackTransactionSubmitted() {
        if (isListenerRegistered()) {
            singleton.getListener().onTransactionSubmitted();
        }
    }

    private void callbackTransactionFailed() {
        if (isListenerRegistered()) {
            singleton.getListener().onTransactionFailed();
        }
    }

    private void callbackTransactionCancelled() {
        if (isListenerRegistered()) {
            singleton.getListener().onTransactionCancelled();
        }
    }

    private void callbackTransactionComplete(TransactionResponse transactionDetails) {
        if (isListenerRegistered()) {
            singleton.getListener().onTransactionCompleted(transactionDetails);
        }
    }
}
