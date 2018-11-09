package com.sven.huinews.international.main.home.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dueeeke.videoplayer.player.VideoViewManager;
import com.flyco.tablayout.SlidingTabLayout;
import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseFragment;
import com.sven.huinews.international.entity.event.UpdateEvent;
import com.sven.huinews.international.entity.response.AliVideoResponse;
import com.sven.huinews.international.entity.response.HomeTab;
import com.sven.huinews.international.entity.response.PerSonWorkResponse;
import com.sven.huinews.international.entity.response.PersonLikeResponse;
import com.sven.huinews.international.entity.response.UserDatasResponse;
import com.sven.huinews.international.main.fansandfollow.activity.FansAndFollowActivity;
import com.sven.huinews.international.main.home.HomeTabConfig;
import com.sven.huinews.international.main.home.adapter.MyVideoAdapter;
import com.sven.huinews.international.main.home.adapter.MyVideoPagerAdapter;
import com.sven.huinews.international.main.home.adapter.TabPagerAdapter;
import com.sven.huinews.international.main.home.contract.PersonWorkContract;
import com.sven.huinews.international.main.home.model.PersonWorkModel;
import com.sven.huinews.international.main.home.presenter.PersonWorkPresenter;
import com.sven.huinews.international.main.me.DraftsActivity;
import com.sven.huinews.international.utils.Common;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import retrofit2.http.Url;
import wedemo.utils.LogUtil;

public class MyVideoFragment extends BaseFragment<PersonWorkPresenter, PersonWorkModel> implements PersonWorkContract.View {
    private SlidingTabLayout mTabLayout;
    private ViewPager mViewPager;
    private MyVideoAdapter mTabPagerAdapter;
    private ImageView ivUserHead;
    private TextView tvName;
    private TextView tvContent;
    private ImageView imgSex;
    private TextView tvFansSize;
    private TextView tvFfollowSize;
    private TextView tvHeartsSize;
    private TextView tvFollow;
    private int duty_type;
    private String Other_id;
    private LinearLayout ll_top;


    @Override
    protected int getLayoutResource() {
        EventBus.getDefault().register(this);
        return R.layout.my_video_fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

  /*  @Subscribe
    public void onEventLis(String str){
        if(Common.REFRESH_USERINFO.equals(str)){
            mPresenter.getUserDetailsDataInfo(true);
            LogUtil.showLog("刷新个人信息");
        }
    }*/

    @Override
    public void initObject() {
        List<HomeTab> mHomeTabs = HomeTabConfig.getMyVideoTabs(mContext);
        mTabPagerAdapter = new MyVideoAdapter(mHomeTabs, getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(mTabPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setViewPager(mViewPager);
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void initView(View v) {
        mTabLayout = v.findViewById(R.id.tablayout);
        mViewPager = v.findViewById(R.id.viewpager);
        ivUserHead = v.findViewById(R.id.iv_user_head);
        tvName = v.findViewById(R.id.tv_name);
        tvContent = v.findViewById(R.id.tv_comment);
        imgSex = v.findViewById(R.id.img_sex);
        tvFansSize = v.findViewById(R.id.tv_fans_size);
        tvFfollowSize = v.findViewById(R.id.tv_follow_size);
        tvHeartsSize = v.findViewById(R.id.tv_hearts_size);
        tvFollow = v.findViewById(R.id.tv_follow);

        ll_top = v.findViewById(R.id.ll_top);
        ll_top.setVisibility(View.GONE);

    }

    @Override
    public void initEvents() {
        tvFansSize.setOnClickListener(this);
        tvFfollowSize.setOnClickListener(this);
        tvFollow.setOnClickListener(this);
    }

    @Override
    public void OnClickEvents(View v) {
        if (v==tvFansSize){
            Bundle bundle = new Bundle();
            bundle.putString("du_type", duty_type + "");
            bundle.putString("other_id", Other_id);
            bundle.putString("load_type", "fans");
            startActivity(FansAndFollowActivity.class, bundle);
        }else if(v==tvFfollowSize){
            Bundle bundle = new Bundle();
            bundle.putString("du_type", duty_type + "");
            bundle.putString("other_id", Other_id);
            bundle.putString("load_type", "follow");
            startActivity(FansAndFollowActivity.class, bundle);
        }else if(v == tvFollow){
            startActivity(DraftsActivity.class);
        }
    }

    @Override
    public void setUserInfo(UserDatasResponse userInfo) {
        duty_type = userInfo.getData().getDu_type();
        Other_id = userInfo.getData().getUser_id();
        ivUserHead.setImageURI(Uri.parse(userInfo.getData().getUserAvatar()));
        tvName.setText(userInfo.getData().getNickname() + "");
        tvContent.setText(userInfo.getData().getSignature().equals("") ? getString(R.string.no_bio_yet) : userInfo.getData().getSignature());
//        perAge.setText(userInfo.getData().getBirthday().equals("") ? "CHINA 18" : "CHINA " + userInfo.getData().getBirthday());
        if (userInfo.getData().getSex() == 1) {
            imgSex.setImageResource(R.mipmap.girl);
        } else {
            imgSex.setImageResource(R.mipmap.man);
        }
        tvFansSize.setText(getString(R.string.fans) + " " + userInfo.getData().getFansNum() + "");
        tvFfollowSize.setText(getString(R.string.follow) + " " + userInfo.getData().getFollowNum() + "");
        tvHeartsSize.setText(getString(R.string.hearts) + " " + userInfo.getData().getGetLike());
    }

    @Override
    public void setPersonalWorksData(PerSonWorkResponse response, Boolean isRefresh, Boolean isLoadMore, int selectType) {

    }

    @Override
    public void setPersonLikesData(PersonLikeResponse response, int selectType, boolean isRefresh) {

    }

    @Override
    public void getAliNewData(AliVideoResponse response) {

    }

    @Override
    public void hideRefresh() {

    }

    @Override
    public void hideLoadMore(boolean isHide) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showErrorTip(int code, String msg) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void update(UpdateEvent event) {
        if (event.getPageType()==2){
            if (mPresenter != null) {
                mPresenter.getUserDetailsDataInfo(true);
            }
        }
    }
}
