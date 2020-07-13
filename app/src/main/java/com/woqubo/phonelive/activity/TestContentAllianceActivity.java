package com.woqubo.phonelive.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.kwad.sdk.export.model.ContentItem;
import com.tencent.ep.shanhuad.adpublic.ADError;
import com.tencent.ep.shanhuad.adpublic.adbuilder.ADContentAlliance;
import com.tencent.ep.shanhuad.adpublic.models.AdID;
import com.woqubo.phonelive.Constant;
import com.woqubo.phonelive.R;


public class TestContentAllianceActivity extends FragmentActivity {

    private ADContentAlliance mADContentAlliance;
    int adid = -1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emptycontainer);
        adid = this.getIntent().getIntExtra(Constant.AD_ID,99502950);
        mADContentAlliance = new ADContentAlliance();
        mADContentAlliance.load(new ADContentAllianceListenerImpl(),new AdID(adid,968,300));
    }




    private class ADContentAllianceListenerImpl implements ADContentAlliance.ADContentAllianceListener{

        @Override
        public void onAdError(ADError adError) {
        }

        @Override
        public void onLoaded() {
            TestContentAllianceActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, mADContentAlliance.getFragment())
                            .commitAllowingStateLoss();
                }
            });
        }

        @Override
        public void onPageEnter(ContentItem item) {
        }

        @Override
        public void onPageResume(ContentItem item) {
        }

        @Override
        public void onPagePause(ContentItem item) {
        }

        @Override
        public void onPageLeave(ContentItem item) {
        }

        @Override
        public void onVideoPlayStart(ContentItem item) {
        }

        @Override
        public void onVideoPlayPaused(ContentItem item) {
        }

        @Override
        public void onVideoPlayResume(ContentItem item) {
        }

        @Override
        public void onVideoPlayCompleted(ContentItem item) {
        }
    }
}
