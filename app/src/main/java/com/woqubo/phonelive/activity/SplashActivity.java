package com.woqubo.phonelive.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tencent.ep.shanhuad.adpublic.ADError;
import com.tencent.ep.shanhuad.adpublic.adbuilder.ADSplashImage;
import com.tencent.ep.shanhuad.adpublic.adbuilder.SplashAdListener;
import com.tencent.ep.shanhuad.adpublic.models.AdID;
import com.woqubo.phonelive.Constant;
import com.woqubo.phonelive.R;
import com.woqubo.phonelive.utils.ToastUtil;


public class SplashActivity extends AppCompatActivity {
    private int adId = -1;
    private com.tencent.ep.shanhuad.adpublic.adbuilder.ADSplashImage ADSplashImage;
    private FrameLayout adContainer;
    private TextView skipView;
    private SplashAdListener mSplashAdListenerImpl = new SplashAdListener(){

        @Override
        public void onAdError(ADError adError) {
            ToastUtil.show(adError.msg);
        }

        @Override
        public void onADDismissed() {
            android.util.Log.d("SplashActivity" , "onADDismissed");
        }

        @Override
        public void onADPresent() {
            skipView.setVisibility(View.VISIBLE);
            android.util.Log.d("SplashActivity" , "onADPresent");
        }

        @Override
        public void onADClicked() {
            android.util.Log.d("SplashActivity" , "onADClicked");
        }

        @Override
        public void onADExposure() {
            android.util.Log.d("SplashActivity" , "onADExposure");
        }

        @Override
        public void onADTick(long millisUntilFinished) {
            skipView.setText("跳过 ：" + millisUntilFinished/1000);
            android.util.Log.d("SplashActivity" , "onADTick : " +millisUntilFinished );
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_ad_activity_main);
        adContainer = this.findViewById(R.id.splash_container);
        skipView = this.findViewById(R.id.skip_view);
        adId = this.getIntent().getIntExtra(Constant.AD_ID,99501593);//   99502717  99002004
        fetchAD();

        skipView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //goto next page
            }
        });
    }
    private void fetchAD(){
        ADSplashImage = new ADSplashImage(SplashActivity.this);
        ADSplashImage.load(mSplashAdListenerImpl,new AdID(adId,720,1080),adContainer,skipView);
    }
}
