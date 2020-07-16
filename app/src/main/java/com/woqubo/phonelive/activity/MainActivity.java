package com.woqubo.phonelive.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.qq.e.ads.nativ.NativeUnifiedADData;
import com.tencent.ep.shanhuad.adpublic.ADError;
import com.tencent.ep.shanhuad.adpublic.adbuilder.ADCard;
import com.tencent.ep.shanhuad.adpublic.adbuilder.ADDownLoad;
import com.tencent.ep.shanhuad.adpublic.adbuilder.ADFullScreenVideo;
import com.tencent.ep.shanhuad.adpublic.adbuilder.AdInfoListener;
import com.tencent.ep.shanhuad.adpublic.adbuilder.RewardVideo;
import com.tencent.ep.shanhuad.adpublic.models.AdID;
import com.tencent.ep.shanhuad.adpublic.models.AdMetaInfo;
import com.tmsdk.module.coin.AdConfig;
import com.tmsdk.module.coin.AdConfigManager;
import com.tmsdk.module.coin.AdRequestData;
import com.tmsdk.module.coin.CmpAdConfig;
import com.woqubo.phonelive.dialog.CardAdDialog;
import com.woqubo.phonelive.Constant;
import com.woqubo.phonelive.R;
import com.woqubo.phonelive.dialog.DownloadAdDialog;
import com.woqubo.phonelive.utils.CommonUtil;
import com.woqubo.phonelive.utils.ToastUtil;
import com.woqubo.phonelive.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Util.requestPermission(this, Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        loadCardAd();
//        loadDownloadAd();
        initDialog();

    }
    private void loadDownloadAd(){
        newThread(new Runnable() {
            @Override
            public void run() {
                getTaskAndAd(103);
            }
        });
    }

    private void loadCardAd(){
        newThread(new Runnable() {
            @Override
            public void run() {
                getTaskAndAd(102);

            }
        });
    }

    private void initDialog() {
        if (mProgressDialog==null){
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setTitle("正在加载中");
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_1_all:
                loadCardAd();
                break;
            case R.id.btn_2_all:
                loadDownloadAd();
                break;
            case R.id.btn_3:
                newThread(new Runnable() {
                    @Override
                    public void run() {
                        getTaskAndAd(104);
                    }
                });
                break;
            case R.id.btn_4:
                newThread(new Runnable() {
                    @Override
                    public void run() {
                       getTaskAndAd(125);
                    }
                });
                break;
            case R.id.btn_5:
                newThread(new Runnable() {
                    @Override
                    public void run() {
                        getTaskAndAd(130);
                    }
                });
                break;
            case R.id.btn_6:
                newThread(new Runnable() {
                    @Override
                    public void run() {
                        getTaskAndAd(138);
                    }
                });
                break;
            case R.id.btn_7:
                newThread(new Runnable() {
                    @Override
                    public void run() {
                        getTaskAndAd(128);
                    }
                });
                break;
            case R.id.btn_8:
                mProgressDialog.show();
                newThread(new Runnable() {
                    @Override
                    public void run() {
                        getTaskAndAd(137);
                    }
                });
                break;

        }
    }
//    创建线程
    private void newThread(Runnable runnable){
         new Thread(runnable).start();
    }

    /**
     * 获取任务，之后通过任务获取广告   102 卡券任务  103 下载任务  104视频任务  125开屏任务  130banner任务
     * 任务ID
     * @param taskId
     *
     * @return
     */
    private AdRequestData getTask(int taskId) {
        Bundle inBundle1 = new Bundle();
        AdConfig aAdConfig1 = new AdConfig(taskId, inBundle1);
        CmpAdConfig cmpAdConfig = AdConfigManager.checkParam(aAdConfig1, 5 * 1000L);
        return  AdConfigManager.getSimplePositionAdConfig(cmpAdConfig);
    }
    /**
     * 通过business来定位使用哪个广告类进行请求
     * @param adRequestData 请求体，AdRequestData的position_id参数对应AdID.posID，
     *                      positionFormatTypes参数对应Adid的styleArray参数
     */
    private void getAdFromBusiness(AdRequestData adRequestData){
        String Business = String.valueOf(adRequestData.business);
        switch (Business) {
            case Constant.AD_DOWNLOAD:
                getdownloadAd(adRequestData.positionId);
                break;
            case Constant.AD_VIDDO:
                gotRewardVedio(adRequestData.positionId);
                break;
            case Constant.AD_CARD:
                getCardAd(adRequestData.positionId);
                break;
            case Constant.AD_BANNER:
                Intent intent = new Intent(CommonUtil.getContext(), BannerActivity.class);
                intent.putExtra(Constant.AD_ID, adRequestData.positionId);
                startActivity(intent);
                break;
            case Constant.AD_SPLASH:
                Intent intent1 = new Intent(CommonUtil.getContext(),SplashActivity.class);
                intent1.putExtra(Constant.AD_ID,adRequestData.positionId);
                startActivity(intent1);
                break;
            case Constant.AD_VIDEO_FEEDS:
                Intent intent2 = new Intent(MainActivity.this, TestContentAllianceActivity.class);
                intent2.putExtra(Constant.AD_ID,adRequestData.positionId);
                startActivity(intent2);
                break;
            case Constant.AD_FEED:
                Intent intent3 = new Intent(MainActivity.this,FeedAdActivity.class);
                intent3.putExtra(Constant.AD_ID,adRequestData.positionId);
                startActivity(intent3);
                break;
            case Constant.AD_VIDEO_FULL:
                getAdFullScreenVideo(adRequestData.positionId);
                break;
        }

    }
    private void getdownloadAd(int adId){
//        id是可以通过固定的ID 位置访问到固定广告位。已经过时动态获取广告位保留说明
        int id = 99501593;
        if(adId > 0){
            id = adId;
        }
        List<AdID> list = new ArrayList<>();
        list.add(new AdID(adId,null,968,300));
        final ADDownLoad mADDownLoad = new ADDownLoad();
        mADDownLoad.load(this.getApplicationContext(),new AdInfoListener() {
            @Override
            public void onAdLoaded(final List<AdMetaInfo> list) {
//                获取广告成功可以进行对业务自定义显示，demo只有按钮显示
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(list != null && list.size() > 0){
                            DownloadAdDialog downloadAdDialog = new DownloadAdDialog(list.get(0),mADDownLoad);
                            downloadAdDialog.show(getSupportFragmentManager(),"download");
                        }else{
                            ToastUtil.show("拉取为空");
                        }


                    }
                });
            }

            @Override
            public void onAdError(ADError adError) {
                ToastUtil.show(adError.msg);
            }

            @Override
            public void onGDTEventStatusChanged(int i) {

            }

            @Override
            public void onAdClick(NativeUnifiedADData nativeUnifiedADData, AdMetaInfo adMetaInfo) {

            }



            @Override
            public void onAdShow(AdMetaInfo adMetaInfo) {

            }

        },list);
    }
    private void getCardAd(int adId){
        //        id是可以通过固定的ID 位置访问到固定广告位。已经过时动态获取广告位保留说明
        int id = 99501657;
        if(adId > 0){
            id = adId;
        }
        List<AdID> list = new ArrayList<>();
        list.add(new AdID(adId,null,968,300));
        final ADCard mADCard = new ADCard();
        mADCard.load(this.getApplicationContext(),new AdInfoListener() {
            @Override
            public void onAdLoaded(final List<AdMetaInfo> list) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        信息展示上报
                        if (list.size()>0){
                            CardAdDialog cardAdDialog = new CardAdDialog(list.get(0),mADCard);
                            cardAdDialog.show(getSupportFragmentManager(),"card");
                        }else {
                            ToastUtil.show("没有获取到广告");
                        }
                    }
                });
            }

            @Override
            public void onAdError(ADError adError) {
                ToastUtil.show(adError.msg);
            }

            @Override
            public void onGDTEventStatusChanged(int i) {

            }

            @Override
            public void onAdClick(NativeUnifiedADData nativeUnifiedADData, AdMetaInfo adMetaInfo) {

            }



            @Override
            public void onAdShow(AdMetaInfo adMetaInfo) {


            }



        },list);
    }
    private void gotRewardVedio(int adId) {
        int id = 99002010;
        if(adId > 0){
            id = adId;
        }
        final RewardVideo mRewardVedio = new RewardVideo();
        List<AdID> list = new ArrayList<>();
        list.add(new AdID(adId,null,968,300));
        mRewardVedio.load(new RewardVideo.RVListener(){
            @Override
            public void loaded() {
                mRewardVedio.showAD(MainActivity.this);
            }

            @Override
            public void onAdError(ADError adError) {

            }

            @Override
            public void onVideoComplete() {

            }

            @Override
            public void onVideoPlay() {

            }

            @Override
            public void onClick() {

            }

            @Override
            public void onClose() {

            }


        }, CommonUtil.getContext(),new AdID(adId,null,968,300));
    }

    private void getTaskAndAd(int taskId){
        AdRequestData AdRequestData = getTask(taskId);
        getAdFromBusiness(AdRequestData);
    }
    private void getAdFullScreenVideo(int adId){
        int id = 99502948;
        if(adId > 0){
            id = adId;
        }
        final ADFullScreenVideo fullScreenVideo = new ADFullScreenVideo();
        fullScreenVideo.load(new ADFullScreenVideo.FSListener() {
            @Override
            public void onAdError(ADError adError) {
                mProgressDialog.dismiss();
            }

            @Override
            public void loaded() {
                mProgressDialog.dismiss();
                fullScreenVideo.showAD(MainActivity.this);
            }

            @Override
            public void onAdClicked() {

            }

            @Override
            public void onPageDismiss() {

            }

            @Override
            public void onVideoPlayEnd() {

            }

            @Override
            public void onVideoPlayStart() {

            }

            @Override
            public void onSkippedVideo() {

            }
        },new AdID(id,968,300));
    }
}
