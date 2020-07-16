package com.woqubo.phonelive;

import android.app.Application;
import android.content.Context;

import com.tencent.ep.shanhuad.adpublic.ShanHuAD;
import com.tmsdk.module.coin.AbsTMSConfig;
import com.tmsdk.module.coin.TMSDKContext;

public class AdHiApplication extends Application {
    private boolean mBresult = false;
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * 为true 打开
         */
        TMSDKContext.setTMSDKLogEnable(true);
        // TMSDK初始化
        mBresult = TMSDKContext.init(this.getApplicationContext(), new AbsTMSConfig() {
            @Override
            public String getServerAddress() {
                return Constant.TCP_SERVER;
            }
        });
        /**
         * 初始化后才能调用！
         * setAutoConnectionSwitch（）影响渠道号上报这个自动联网项是否运行。请不要一直设置为false，影响激活量和活跃量统计,后台会关注并停止相关服务，请在过工信部测试允许联网后，设置回true
         */
//        boolean nFlag = true;//这里厂商应该用自己保存的用户设置
//        TMSDKContext.setAutoConnectionSwitch(this,nFlag);
        ShanHuAD.init(TMSDKContext.getApplicationContext(),new H5Browser(),TMSDKContext.getCoinProductId());
        mContext = getApplicationContext();
    }
    public static Context getAdHiApplicationContext(){
        return mContext;
    }

    public static int  getScreenW(){
       return getAdHiApplicationContext().getResources().getDisplayMetrics().widthPixels;
    }
}
