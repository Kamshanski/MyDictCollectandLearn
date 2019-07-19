package com.dawandeapp.mydictcollectandlearn.zHelpClasses;

import android.content.Context;
import android.os.Environment;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.widget.Toast;

public abstract class M {
    public static void d(String str) {
        Log.d("MyDebug", str);
    }
    public static void wSh(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }
    public static void wL(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
    }
    public static String appPath() {
        return Environment.getExternalStorageDirectory().getPath().concat("/MyDict");
    }
    public static InputFilter jsonFilter() {
        return new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                //Набор символов, которые нельзя писать в названии
                if (source != null && "\"\'\\".contains(("" + source))) {
                    return "";
                }
                return null;
            }
        };
    }

}