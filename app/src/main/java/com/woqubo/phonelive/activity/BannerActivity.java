package com.woqubo.phonelive.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.tencent.ep.shanhuad.adpublic.ADError;
import com.tencent.ep.shanhuad.adpublic.adbuilder.ADBanner;
import com.tencent.ep.shanhuad.adpublic.adbuilder.BannerAdListener;
import com.tencent.ep.shanhuad.adpublic.models.AdID;
import com.woqubo.phonelive.Constant;
import com.woqubo.phonelive.R;
import com.woqubo.phonelive.utils.ToastUtil;

public class BannerActivity extends AppCompatActivity {
    private int adId = -1;
    private ADBanner mADBanner;
    private FrameLayout bannerContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        bannerContent = this.findViewById(R.id.banner_content);
        adId = this.getIntent().getIntExtra(Constant.AD_ID,99501657);//99002009   99502716
        fetchAD();
    }

    private void fetchAD(){
        mADBanner = new ADBanner(this,30);
        mADBanner.load(new AdID(adId,900,300),new BannerAdListener(){

            @Override
            public void onAdError(ADError adError) {
                android.util.Log.e("BannerActivity","adError : " + adError.msg);
                ToastUtil.show(adError.msg);
            }

            @Override
            public void onADLoaded(final View bannerView) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bannerContent.addView(bannerView);//广告数据加载成功把返回的view放到需要展示的地方
                        android.util.Log.d("BannerActivity","onADLoaded");
                    }
                });
            }

            @Override
            public void onADShow() {
                android.util.Log.d("BannerActivity","onADShow");
            }

            @Override
            public void onADClicked() {
                android.util.Log.d("BannerActivity","onADClicked");
            }

            @Override
            public void onClose() {
                android.util.Log.d("BannerActivity","onClose");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mADBanner!=null){
            mADBanner.destroy();
        }
    }
}
