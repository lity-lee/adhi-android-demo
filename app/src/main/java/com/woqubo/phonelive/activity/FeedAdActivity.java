package com.woqubo.phonelive.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.tencent.ep.shanhuad.adpublic.ADError;
import com.tencent.ep.shanhuad.adpublic.adbuilder.ADFeed;
import com.tencent.ep.shanhuad.adpublic.models.AdID;
import com.woqubo.phonelive.Constant;
import com.woqubo.phonelive.R;

import java.util.ArrayList;
import java.util.List;

public class FeedAdActivity extends Activity {

    private ADFeed mADFeed;
    private ListView mListView;
    private List<ADFeed.FeedViewCreator> mFeedList;
    private FeedListAdapter mFeedListAdapter;
    int adid = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_feed);
        adid = this.getIntent().getIntExtra(Constant.AD_ID,99502949);
        mListView = findViewById(R.id.feed_list);
        mFeedList = new ArrayList<>();
        mFeedListAdapter = new FeedListAdapter();
        mListView.setAdapter(mFeedListAdapter);
        mADFeed = new ADFeed(false,true);
        mADFeed.load(new FeedListenerImpl(),new AdID(adid,968,300));

    }

    private class FeedListenerImpl implements ADFeed.FeedListener{
        @Override
        public void onAdError(ADError adError) {

        }

        @Override
        public void onLoaded(final List<ADFeed.FeedViewCreator> list) {

            FeedAdActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mFeedList.clear();
                    mFeedList.addAll(list);
                    mFeedListAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    private class FeedListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mFeedList.size();
        }

        @Override
        public Object getItem(int position) {
            return mFeedList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ADFeed.FeedViewCreator creator = (ADFeed.FeedViewCreator)getItem(position);
            creator.setInteractionListener(new ADFeed.InteractionListener(){
                @Override
                public void onAdClicked() {
                }

                @Override
                public void onAdShow() {
                }

                @Override
                public void onDislikeClicked() {
                    mFeedList.remove(creator);
                    notifyDataSetChanged();
                }
            });
            AdViewHolder adViewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(FeedAdActivity.this).inflate(R.layout.feed_list_item_ad_container,
                        parent, false);
                adViewHolder = new AdViewHolder(convertView);
                convertView.setTag(adViewHolder);
            } else {
                adViewHolder = (AdViewHolder) convertView.getTag();
            }
            View mFeedView = creator.getFeedView(FeedAdActivity.this);
            if (mFeedView != null && mFeedView.getParent() == null) {
                adViewHolder.mAdContainer.removeAllViews();
                adViewHolder.mAdContainer.addView(mFeedView);
            }
            return convertView;
        }
    }

    private static class AdViewHolder {
        FrameLayout mAdContainer;

        AdViewHolder(View convertView) {
            mAdContainer = convertView.findViewById(R.id.feed_container);
        }
    }
}
