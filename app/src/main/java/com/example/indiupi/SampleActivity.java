package com.example.indiupi;

import android.os.Bundle;
import android.text.Html;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gpfreetech.IndiUpi.IndiUpi;
import com.gpfreetech.IndiUpi.listener.PaymentStatusListener;
import com.gpfreetech.IndiUpi.entity.TransactionResponse;
import com.gpfreetech.IndiUpi.util.Validator;

public class SampleActivity extends AppCompatActivity implements PaymentStatusListener {

    private ImageView imageView;
    private TextView statusView;
    private Button payButton;
    private EditText edUpa;
    private EditText edAmount;
    private EditText edName;
    private EditText edDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize Components
        imageView = findViewById(R.id.imageView);
        statusView = findViewById(R.id.textView_status);
        payButton = findViewById(R.id.button_pay);
        edUpa = findViewById(R.id.edUpa);
        edName = findViewById(R.id.edName);
        edAmount = findViewById(R.id.edAmount);
        edDescription = findViewById(R.id.edDescription);

        // set filter for amount in decimal format if you want
        edAmount.setFilters(new InputFilter[]{new Validator.DecimalDigitsInputFilter(5, 2)});

        //Proceed for PaymentPayload on click
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //initPayment();
                if (Validator.isValidUpa(edUpa) && Validator.isValidAmount(edAmount) && Validator.isValidName(edName) && Validator.isValidDescription(edDescription)) {
                    initPayment(edUpa.getText().toString(), edName.getText().toString(), edAmount.getText().toString(), edDescription.getText().toString());
                }
            }
        });
    }

    private void initPayment(String upa, String name, String amount, String Description) {
        //Step 1 : Create instance of IndiUpi
        final IndiUpi indiUpi = new IndiUpi.Builder()
                .with(this)
                .setPayeeVpa(upa)
                .setPayeeName(name)
                .setTransactionId(Validator.generateRandom())
                .setTransactionRefId(Validator.generateRandom())
                .setDescription(Description)
                .setAmount(amount)
                // Currently we are working on serUrl. And This is optional. Other parameter internally bind with url same as upi url
                //.setUrl("http", "www.sample.com", "test.php")
                .build();

        //Step 2 : Register Listener for Events
        indiUpi.setPaymentStatusListener(this);

        indiUpi.pay("PaymentPayload Using");
        // or
        //  indiUpi.pay(); for default title

        //Step 3 : response in callback
        // handle as per your requirement
    }

    @Override
    public void onTransactionCompleted(TransactionResponse transactionResponse) {
        // Transaction Completed
        Log.d("TransactionResponse", transactionResponse.toString());
        statusView.setText(Html.fromHtml(transactionResponse.toHTMLString()));
    }

    @Override
    public void onTransactionSuccess(TransactionResponse transactionResponse) {
        // PaymentPayload Success
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        imageView.setImageResource(R.drawable.ic_success);
    }

    @Override
    public void onTransactionSubmitted() {
        // PaymentPayload Pending
        Toast.makeText(this, "Pending | Submitted", Toast.LENGTH_SHORT).show();
        imageView.setImageResource(R.drawable.ic_success);
    }

    @Override
    public void onTransactionFailed() {
        // PaymentPayload Failed
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
        imageView.setImageResource(R.drawable.ic_failed);
    }

    @Override
    public void onTransactionCancelled() {
        // PaymentPayload Cancelled by User
        Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
        imageView.setImageResource(R.drawable.ic_failed);
    }
}