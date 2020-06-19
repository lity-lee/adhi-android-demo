package com.woqubo.phonelive;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.tencent.ep.shanhuad.adpublic.H5BrowserListener;
import com.tencent.ep.shanhuad.adpublic.models.AdMetaInfo;
import com.tencent.qqpim.discovery.AdDisplayModel;
import com.tmsdk.module.coin.TMSDKContext;
import com.woqubo.phonelive.utils.CommonUtil;

import java.io.File;


/**
 * sdk 广告点击回调实现
 *3
 */
public class H5Browser implements H5BrowserListener {
    private BroadcastReceiver downloadBroadcastReceiver, installBroadcastReceiver;
    public static final String TAG = "TMSDK_H5Browser";



    @Override
    public void openH5(String url) {
        Log.d("H5Impl","openH5");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse(url));
        TMSDKContext.getApplicationContext().startActivity(intent);
    }
    /**
     * app安装回到，因为广告源返回情况比较复杂，
     * 1 先判断 model.appDownloadUrl 尝试下载
     * 2 如果没有appDownloadUrl：则用浏览器开启jumpUrl地址通过浏览器下载，
     * 下载和安装后要调用对应接口上报，便于结算和对账
     */

    @Override
    public void openAppDetailPage(AdMetaInfo adMetaInfo, AdDisplayModel adDisplayModel) {
        Log.d("H5Impl","openAppDetailPage");
        String url = adDisplayModel.jumpUrl;
        if(TextUtils.isEmpty(url) && adDisplayModel.appDownloadUrl!=null){
            url =adDisplayModel.appDownloadUrl;
//            ShanHuAD.reportStartDownload(model);//开始下载
//            ShanHuAD.reportDownloadFinish(model);//下载完成
//            ShanHuAD.reporttinstalled(model);//安装成功上报
//            ShanHuAD.reportrtActive(model);//激活成功上报
            downloadApk(adMetaInfo,adDisplayModel,"下载应用", "广告应用");
        }
        if(TextUtils.isEmpty(url)){
            return;
        }
    }


    /**
     * 下载Apk, 并设置Apk地址,
     * 默认位置: /storage/sdcard0/Download
     *
     * @param infoName    通知名称
     * @param description 通知描述
     */
    public void downloadApk(final AdMetaInfo mStyleAdEntity,AdDisplayModel adDisplayModel, String description, String infoName) {
        showToast("应用广告开始下载上报");
        DownloadReportProxy.reportStartDownload(mStyleAdEntity,adDisplayModel);//开始下载

        DownloadManager.Request request;
        try {
            request = new DownloadManager.Request(Uri.parse(adDisplayModel.appDownloadUrl));
        } catch (Throwable e) {
            Log.e(TAG, "DownloadManager.Request (Throwable)", e);
            return;
        }

        request.setTitle(infoName);
        request.setDescription(description);

        //在通知栏显示下载进度
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }

        //设置保存下载apk保存路径
        String apkName =
                System.currentTimeMillis() + "_" + CommonUtil.getMD5(adDisplayModel.appDownloadUrl) + ".apk";
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, apkName);
        Context appContext = TMSDKContext.getApplicationContext();
        DownloadManager manager = (DownloadManager) appContext.getSystemService(Context.DOWNLOAD_SERVICE);
        //进入下载队列
        long id = manager.enqueue(request);
        listenerDownLoad(id, mStyleAdEntity,adDisplayModel, apkName);
    }

    private void showToast(String str) {
        Toast.makeText(TMSDKContext.getApplicationContext(),str,Toast.LENGTH_SHORT).show();
    }

    private void listenerDownLoad(final long Id, final AdMetaInfo mStyleAdEntity, final AdDisplayModel adDisplayModel, final String apkName) {
        // 注册广播监听系统的下载完成事件。
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        downloadBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                TMSDKContext.getApplicationContext().unregisterReceiver(downloadBroadcastReceiver);
                Bundle mBundle = intent.getExtras();
                long ID = mBundle.getLong(DownloadManager.EXTRA_DOWNLOAD_ID);
                if (ID == Id) {
                    final String apkFilePath = new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath())
                            .append(File.separator).append(Environment.DIRECTORY_DOWNLOADS).append(File.separator)
                            .append(apkName).toString();

                    showToast("应用广告下载成功上报");
                    DownloadReportProxy.reportDownloadFinish(mStyleAdEntity,adDisplayModel, apkFilePath);
                    listenerInstall(mStyleAdEntity,adDisplayModel);
                    CommonUtil.installApkByPath(TMSDKContext.getApplicationContext(), apkFilePath);
                }
            }
        };

        TMSDKContext.getApplicationContext().registerReceiver(downloadBroadcastReceiver, intentFilter);
    }
    private void listenerInstall(final AdMetaInfo mStyleAdEntity, final AdDisplayModel adDisplayModel) {
        // 注册广播监听系统的下载完成事件。
        IntentFilter installFilter = new IntentFilter();
        installFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        installFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        installFilter.addDataScheme("package");

        installBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                TMSDKContext.getApplicationContext().unregisterReceiver(installBroadcastReceiver);
                showToast("应用广告安装成功上报");
                DownloadReportProxy.reporttinstalled(mStyleAdEntity,adDisplayModel);
                startAdApp(mStyleAdEntity,adDisplayModel);
            }
        };
        TMSDKContext.getApplicationContext().registerReceiver(installBroadcastReceiver, installFilter);
    }
    private void startAdApp(final AdMetaInfo mStyleAdEntity, AdDisplayModel adDisplayModel) {
        try {
            showToast("应用广告打开成功上报");
            DownloadReportProxy.reportrtActive(mStyleAdEntity,adDisplayModel);
            PackageManager packageManager = TMSDKContext.getApplicationContext().getPackageManager();
            Intent intent = packageManager.getLaunchIntentForPackage(adDisplayModel.packageName);
            TMSDKContext.getApplicationContext().startActivity(intent);
        } catch (Throwable t) {
            //IGNORE
        }
    }
}
