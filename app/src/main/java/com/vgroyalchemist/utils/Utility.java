package com.vgroyalchemist.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Utility {

    static SharedPreferences mSharedPreferencesCurrencyBase;
    static String mStringCurrencyBase;

    public static void debugger(String str) {
        int maxLogSize = 4000;
        for (int i = 0; i <= str.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i + 1) * maxLogSize;
            end = end > str.length() ? str.length() : end;
            Log.d("VT...", str.substring(start, end));
        }
    }
    public static String toBase64(String message) {
        byte[] data;
        try {
            data = message.getBytes("UTF-8");
            String base64Sms = Base64.encodeToString(data, Base64.DEFAULT);
            return base64Sms;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static String getDecimalFormate(Context context, double value, double currencyRate, String format) {
        String val = "";
        String newDecimalValue = "";
        NumberFormat decimalFormat = new DecimalFormat(format);

            if (currencyRate != 0.00) {
                val = decimalFormat.format((double) Math.round((value * currencyRate) * 100) / 100);
            } else {
                val = decimalFormat.format((double) Math.round((value) * 100) / 100);
            }


            if (val.contains(".")) {
                String[] separated = val.split("\\.");
                if (separated[1].length() == 1) {
                    newDecimalValue = val + "0";
                } else {
                    newDecimalValue = val;
                }
            } else {
                newDecimalValue = val + ".00";
            }
            val = newDecimalValue;
            return val;


    }

}
