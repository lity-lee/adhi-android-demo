package com.woqubo.phonelive.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qq.e.ads.nativ.widget.NativeAdContainer;
import com.tencent.ep.shanhuad.adpublic.adbuilder.ADCard;
import com.tencent.ep.shanhuad.adpublic.models.AdMetaInfo;
import com.woqubo.phonelive.AdHiApplication;
import com.woqubo.phonelive.Base.AbsDialogFragment;
import com.woqubo.phonelive.R;
import com.woqubo.phonelive.utils.GlideUtil;

@SuppressLint("ValidFragment")
public class CardAdDialog extends AbsDialogFragment {

    private  ADCard mAdCard;
    private  AdMetaInfo mAdMetaInfo;
    private ImageView mIcon;
    private NativeAdContainer mNtc;
    private TextView mDesc;
    private TextView mTitle;
    private LinearLayout mContent;

    public CardAdDialog(AdMetaInfo adMetaInfo, ADCard adCard){
        mAdMetaInfo = adMetaInfo;
        mAdCard = adCard;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_card_ad;
    }

    @Override
    protected int getDialogStyle() {
        return R.style.dialog;
    }

    @Override
    protected boolean canCancel() {
        return true;
    }

    @Override
    protected void setWindowAttributes(Window window) {
        window.setWindowAnimations(R.style.bottomToTopAnim);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = AdHiApplication.getScreenW()-200;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mIcon = (ImageView) findViewById(R.id.icon);
        mDesc = (TextView) findViewById(R.id.desc);
        mTitle = (TextView) findViewById(R.id.title);
        mNtc = (NativeAdContainer) findViewById(R.id.adContainer);
        mContent = (LinearLayout) findViewById(R.id.dialog_content);
        setData();
        mAdCard.registerViewForInteraction(mAdMetaInfo,mNtc,mContent);
    }

    private void setData(){
        GlideUtil.glideLoadImg(mIcon,mAdMetaInfo.icon);
        mTitle.setText(mAdMetaInfo.title);
        mDesc.setText(mAdMetaInfo.desc);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdCard =null;
        mAdMetaInfo =null;
    }
}
