package com.woqubo.phonelive.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.woqubo.phonelive.AdHiApplication;

public class GlideUtil {
    public static void glideLoadImg(ImageView imageView,String url){
        Glide.with(AdHiApplication.getAdHiApplicationContext()).asBitmap().load(url).into(imageView);
    }


}
