package com.woqubo.phonelive.utils;

import android.widget.Toast;

public class ToastUtil {
    public static void show(String toastText){
        Toast.makeText(CommonUtil.getContext(),toastText,Toast.LENGTH_SHORT).show();
    }
}
