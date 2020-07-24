package com.woqubo.phonelive.utils;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.woqubo.phonelive.AdHiApplication;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CommonUtil {
    /**
     * 日志标签
     */
    private static final String TAG = "CommonUtil";

    /**
     * 获取字符串的MD5编码
     *
     * @param source
     * @return
     */
    public static String getMD5(String source) {
        //定义一个字节数组
        byte[] secretBytes = null;
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            //对字符串进行加密
            md.update(source.getBytes());
            //获得加密后的数据
            secretBytes = md.digest();
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "getMD5 (NoSuchAlgorithmException)", e);
        }
        //将加密后的数据转换为16进制数字
        StringBuilder md5code = new StringBuilder(new BigInteger(1, secretBytes).toString(16));// 16进制数字
        // 如果生成数字未满32位，需要前面补0
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code.insert(0, "0");
        }
        return md5code.toString();
    }


    /**
     * 应用是否已安装
     *
     * @param pkg 包名
     * @return
     */
    public static boolean isPkgInstalled(Context mContext, String pkg) {
        PackageInfo info = null;
        try {
            info = mContext.getPackageManager().getPackageInfo(pkg, 0);//flag 0 不会返回多余的数据
        } catch (Throwable e) {
            Log.e(TAG, "IsPkgInstalled (Throwable)", e);
        }
        return (info != null);
    }

    /**
     * 外部应用安装器安装apk（原生接口）
     *
     * @param path apk的路径
     * @return
     */
    @SuppressLint("WrongConstant")
    public static boolean installApkByPath(Context mContext, String path) {
        try {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri url = null;
        if (Build.VERSION.SDK_INT>Build.VERSION_CODES.N) {
            url = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".fileprovider", new File(path));
        }else {
            url =Uri.fromFile(new File(path));
        }
            intent.setDataAndType(url,"application/vnd.android.package-archive");
            for (ResolveInfo resolveInfo : mContext.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)) {
                mContext.grantUriPermission(resolveInfo.activityInfo.packageName, url, 3);
            }
        mContext.startActivity(intent);
            return true;
        } catch (Throwable e) {
            Log.e(TAG, "installApkByPath (Throwable)", e);
        }
        return false;
    }
    public static Context getContext(){
        return AdHiApplication.getAdHiApplicationContext();
    }
}
