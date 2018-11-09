package com.sven.huinews.international.main.home.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dueeeke.videoplayer.player.VideoViewManager;
import com.flyco.tablayout.SlidingTabLayout;
import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseFragment;
import com.sven.huinews.international.config.api.Api;
import com.sven.huinews.international.dialog.GoldComeDialog;
import com.sven.huinews.international.dialog.OpenTheTreasureBoxDialog;
import com.sven.huinews.international.entity.event.OpenNewPageEvent;
import com.sven.huinews.international.entity.requst.TaskRequest;
import com.sven.huinews.international.entity.response.HomeTab;
import com.sven.huinews.international.main.home.HomeTabConfig;
import com.sven.huinews.international.main.home.adapter.TabPagerAdapter;
import com.sven.huinews.international.publicclass.AddGoldPresenter;
import com.sven.huinews.international.publicclass.AddGoldView;
import com.sven.huinews.international.utils.Common;
import com.sven.huinews.international.utils.GoogleNativeAdsUtils;
import com.sven.huinews.international.utils.TimeUtils;
import com.sven.huinews.international.view.MovingView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import wedemo.MessageEvent;

public class VideoAndNewsFragment extends BaseFragment implements View.OnClickListener, AddGoldView {

    private ViewPager viewPager;
    private TabPagerAdapter mTabPagerAdapter;
    private SlidingTabLayout mTabLayout;
    private ImageView iv_box;
    private OpenTheTreasureBoxDialog mOpenTheTreasureBoxDialog;
    private GoldComeDialog mGoldComeDialog;
    private AddGoldPresenter mAddGoldPresenter;
    private MovingView movingView;
    private List<HomeTab> mHomeTabs;
    private TextView tv_boxtime;
    private Timer timer = new Timer();
    private int lasttime;

    @Override
    protected int getLayoutResource() {
        EventBus.getDefault().register(this);
        return R.layout.fragment_video_and_news;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onSelectEvent(MessageEvent event){
        if(event.getMessage().equals(Common.SELECT_TASK)){
            viewPager.setCurrentItem((int)event.getData());
        }
    }

    @Override
    protected void initView(View v) {
        viewPager = v.findViewById(R.id.vp_video_news);
        mTabLayout = v.findViewById(R.id.tablayout);
        iv_box = v.findViewById(R.id.iv_box);
        movingView = v.findViewById(R.id.moview_view);
        tv_boxtime = v.findViewById(R.id.tv_boxtime);
    }

    @Override
    public void initEvents() {
        iv_box.setOnClickListener(this);
        movingView.setOnClickListener(this);
    }

    @Override
    public void initObject() {
        initTab();
        mAddGoldPresenter = new AddGoldPresenter(this, getActivity());
//        获取金币时间
        mAddGoldPresenter.getGoldTime();
        //签到
        mAddGoldPresenter.readAnyNewsGetGold(TaskRequest.TASK_ID_SIGN_IN, 0);
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void OnClickEvents(View v) {
        switch (v.getId()) {
            case R.id.iv_box:
                mAddGoldPresenter.readAnyNewsGetGold(TaskRequest.TASK_ID_OPEN_BOX, 0);
                break;
            case R.id.moview_view:
                EventBus.getDefault().post(new OpenNewPageEvent(Api.WEB_HOW_TO_EARN));
                break;
        }
    }

    private void initTab() {
        mHomeTabs = HomeTabConfig.getHomeTabs(mContext);
        mTabPagerAdapter = new TabPagerAdapter(mHomeTabs, getFragmentManager());
        viewPager.setAdapter(mTabPagerAdapter);
        mTabLayout.setViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                String textView = mHomeTabs.get(1).getTabName();
            }

            @Override
            public void onPageSelected(int position) {
                VideoViewManager.instance().releaseVideoPlayer();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void showGoldCome(int count, int type, String masgess) {
        if (mOpenTheTreasureBoxDialog == null) {
            mOpenTheTreasureBoxDialog = new OpenTheTreasureBoxDialog(getActivity());
        }
        if (!mOpenTheTreasureBoxDialog.isShowing()) {
            mOpenTheTreasureBoxDialog.setCanceledOnTouchOutside(false);//点击空白处不消失
            mOpenTheTreasureBoxDialog.showDialog(count);
            mAddGoldPresenter.getGoldTime();
        }
    }

    @Override
    public void showGoldTime(final int time) {
        lasttime = time;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timeHandler.sendEmptyMessage(0x12);
            }
        }, 0, 1000);
    }

    @Override
    public void showGoldSignInCome(int count, int type, String masgess) {
        if (mGoldComeDialog == null){
            mGoldComeDialog = new GoldComeDialog(getActivity());
        }
        if (!mGoldComeDialog.isShowing()){
            mGoldComeDialog.setCanceledOnTouchOutside(false);//点击空白处不消失
            mGoldComeDialog.showDialog(count);
        }
    }

    private Handler timeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x12) {
                if (lasttime > 0) {
                    iv_box.setImageResource(R.mipmap.icon_tab_unbox);
                    iv_box.setClickable(false);
                } else {
                    iv_box.setImageResource(R.mipmap.icon_tab_box);
                    tv_boxtime.setText("04:00:00");
                    iv_box.setClickable(true);
                    timer.cancel();
                }
                lasttime = lasttime - 1;
                tv_boxtime.setText(TimeUtils.secToTime(lasttime));
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}