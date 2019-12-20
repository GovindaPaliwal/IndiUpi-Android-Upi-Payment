package com.gpfreetech.IndiUpi.util;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.EditText;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    private static int MAX_LENGTH = 10;

    private static final String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";

    public static boolean isValidUpa(EditText editText) {
        if (editText.getText().toString().trim().contains("@"))
            return true;
        else {
            editText.setError("Enter Valid UPA");
            return false;
        }
    }

    public static boolean isValidName(EditText editText) {
        if (!TextUtils.isEmpty(editText.getText()))
            return true;
        else {
            editText.setError("Enter Payee Name");
            return false;
        }
    }

    public static boolean isValidAmount(EditText editText) {
        if (!TextUtils.isEmpty(editText.getText()) && editText.getText().toString().contains("."))
            return true;
        else {
            editText.setError("Enter Amount");
            return false;
        }
    }

    public static boolean isValidDescription(EditText editText) {
        if (!TextUtils.isEmpty(editText.getText()))
            return true;
        else {
            editText.setError("Enter Description");
            return false;
        }
    }

    public static class DecimalDigitsInputFilter implements InputFilter {
        private Pattern mPattern;

        public DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero) {
            mPattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Matcher matcher = mPattern.matcher(dest);
            if (!matcher.matches())
                return "";
            return null;
        }
    }

    public static String generateRandom() {
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(MAX_LENGTH);
        for (int i = 0; i < MAX_LENGTH; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

}
