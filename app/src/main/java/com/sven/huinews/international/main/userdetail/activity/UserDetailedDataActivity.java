package com.sven.huinews.international.main.userdetail.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.sven.huinews.international.AppConfig;
import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseActivity;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.Constant;
import com.sven.huinews.international.entity.event.UpdateEvent;
import com.sven.huinews.international.entity.requst.FollowsRequest;
import com.sven.huinews.international.entity.requst.LikesRequest;
import com.sven.huinews.international.entity.requst.LoginRequest;
import com.sven.huinews.international.entity.requst.PersonWorkRequest;
import com.sven.huinews.international.entity.requst.PlatformLogin;
import com.sven.huinews.international.entity.requst.ThirdRequest;
import com.sven.huinews.international.entity.requst.UserCommentRequest;
import com.sven.huinews.international.entity.response.AliVideoResponse;
import com.sven.huinews.international.entity.response.FacebookRegResponse;
import com.sven.huinews.international.entity.response.HomeTab;
import com.sven.huinews.international.entity.response.PerSonWorkResponse;
import com.sven.huinews.international.entity.response.PersonLikeResponse;
import com.sven.huinews.international.entity.response.TwitterRegResponse;
import com.sven.huinews.international.entity.response.UserDatasResponse;
import com.sven.huinews.international.main.fansandfollow.activity.FansAndFollowActivity;
import com.sven.huinews.international.main.home.HomeTabConfig;
import com.sven.huinews.international.main.home.adapter.MyVideoPagerAdapter;
import com.sven.huinews.international.main.userdetail.contract.UserDetailContract;
import com.sven.huinews.international.main.userdetail.module.UserDetailModulel;
import com.sven.huinews.international.main.userdetail.presenter.UserDetailPresenter;
import com.sven.huinews.international.tplatform.facebook.FacebookPlatform;
import com.sven.huinews.international.tplatform.google.GoogleLogin;
import com.sven.huinews.international.tplatform.twitter.TwitterLogin;
import com.sven.huinews.international.utils.CommonUtils;
import com.sven.huinews.international.utils.LogUtil;
import com.sven.huinews.international.utils.PhoneUtils;
import com.sven.huinews.international.utils.StatusBarUtils;
import com.sven.huinews.international.utils.ToastUtils;
import com.sven.huinews.international.utils.cache.UserSpCache;
import com.sven.huinews.international.view.dialog.CustomLoginDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;


public class UserDetailedDataActivity extends BaseActivity<UserDetailPresenter, UserDetailModulel> implements UserDetailContract.View
        , GoogleApiClient.OnConnectionFailedListener, GoogleLogin.GoogleSignListener {
    private ImageButton btn_back;
    ImageView ivUserHead;
    TextView tvName;
    TextView tvContent;
    ImageView imgSex;
    TextView tvFansSize;
    TextView tvFfollowSize;
    TextView tvHeartsSize;
    TextView tvFollow;
    private int duty_type;
    private String Other_id;
    private boolean isFOLLOW;
    private SlidingTabLayout mTabLayout;
    private ViewPager mViewPager;
    private MyVideoPagerAdapter mTabPagerAdapter;
    private UserSpCache mUserSpCache = UserSpCache.getInstance(AppConfig.getAppContext());
    private CustomLoginDialog loginDialog;
    private FacebookPlatform mFacebookPlatform;
    private TwitterLogin mTwitterLogin;
    private GoogleLogin googleLogin;
    private FacebookRegResponse mFacebookRegResponse;
    private TwitterRegResponse mTwitterRegResponse;
    private GoogleSignInAccount mGoogleLogin;

    public static void toThis(Context mContext, int duty_type, String Other_id) {
        Intent intent = new Intent(mContext, UserDetailedDataActivity.class);
        intent.putExtra("du_tyep", duty_type);
        intent.putExtra("Other_id", Other_id);
        mContext.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        duty_type = getIntent().getIntExtra("du_tyep", -1);
        Other_id = getIntent().getStringExtra("Other_id");
        return R.layout.activity_user_detailed_data;
    }

    @Override
    public void initView() {
        mTabLayout = findViewById(R.id.tablayout);
        mViewPager = findViewById(R.id.viewpager);
        ivUserHead = findViewById(R.id.iv_user_head);
        tvName = findViewById(R.id.tv_name);
        tvContent = findViewById(R.id.tv_comment);
        imgSex = findViewById(R.id.img_sex);
        tvFansSize = findViewById(R.id.tv_fans_size);
        tvFfollowSize = findViewById(R.id.tv_follow_size);
        tvHeartsSize = findViewById(R.id.tv_hearts_size);
        tvFollow = findViewById(R.id.tv_follow);
        btn_back = findViewById(R.id.btn_back);
        googleLogin = new GoogleLogin(UserDetailedDataActivity.this, this);
        loginDialog = new CustomLoginDialog(UserDetailedDataActivity.this,this);
    }

    @Override
    public void initEvents() {
        tvFansSize.setOnClickListener(this);
        tvFfollowSize.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        tvFollow.setOnClickListener(this);
        googleLogin.setGoogleSignListener(this);
//        loginDialog.setThirdLogin(new CustomLoginDialog.ThirdLoginResult() {
//            @Override
//            public void onOtherfail() {
//
//            }
//
//            @Override
//            public void onCheckFail(BaseResponse response) {
//
//            }
//
//            @Override
//            public void onLoginSuccess(String json) {
//
//                mPresenter.getUserDetailsDataInfo(getUserCommentRequest(), true);
//                ToastUtils.showShort(mContext,getString(R.string.logged_in));
//            }
//
//        });
    }

    @Override
    public void onClickEvent(View v) {
        if (v == tvFansSize) {
            Bundle bundle = new Bundle();
            bundle.putString("du_type", duty_type + "");
            bundle.putString("other_id", Other_id);
            bundle.putString("load_type", "fans");
            startActivity(FansAndFollowActivity.class, bundle);
        } else if (v == tvFfollowSize) {
            Bundle bundle = new Bundle();
            bundle.putString("du_type", duty_type + "");
            bundle.putString("other_id", Other_id);
            bundle.putString("load_type", "follow");
            startActivity(FansAndFollowActivity.class, bundle);
        } else if (v == btn_back) {
            finish();
        } else if (v == tvFollow) {
            if (isLogin()) {
                if (isFOLLOW) {
                    tvFollow.setBackgroundResource(R.drawable.user_follow);
                    tvFollow.setText("+ " + mContext.getString(R.string.follow) + "");
                } else {
                    tvFollow.setBackgroundResource(R.drawable.user_follow_abolish);
                    tvFollow.setText(mContext.getString(R.string.followed) + "");
                }
                mPresenter.onFollow(new FollowsRequest(duty_type, Integer.parseInt(Other_id)));
            }
        }
    }

    @Override
    public void initObject() {
        setMVP();
        mPresenter.getUserDetailsDataInfo(getUserCommentRequest(), true);
        EventBus.getDefault().register(this);
        StatusBarUtils.setColor(UserDetailedDataActivity.this, Color.parseColor("#FFFFFF"));
        List<HomeTab> mHomeTabs = HomeTabConfig.getUserVideoTabs(mContext);
        mTabPagerAdapter = new MyVideoPagerAdapter(mHomeTabs, getSupportFragmentManager());
        mTabPagerAdapter.setDuty_type(duty_type);
        mTabPagerAdapter.setOther_id(Other_id);
        mViewPager.setAdapter(mTabPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mTabLayout.setViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public UserCommentRequest getUserCommentRequest() {
        UserCommentRequest request = new UserCommentRequest();
        request.setDu_type(duty_type + "");
        request.setOther_id(Other_id);
        return request;
    }

    @Override
    public PersonWorkRequest getPersonWorkRequest() {
        PersonWorkRequest request = new PersonWorkRequest();
        request.setDu_type(duty_type + "");
        request.setOther_id(Other_id);
        request.setPage_size(LIMIT + "");
        request.setPage(PAGE + "");
        return request;
    }

    @Override
    public LikesRequest getLikeRequest() {
        LikesRequest request = new LikesRequest();
        request.setDu_type(duty_type + "");
        request.setOther_id(Other_id);
        request.setPage_size(LIMIT + "");
        request.setPage(PAGE + "");
        return request;
    }

    @Override
    public ThirdRequest getThirdRequest(String s) {
        ThirdRequest request = new ThirdRequest();
        if (s.equals(Constant.FACEBOOK)) {
            request.setMobile_brand(PhoneUtils.getPhoneBrand());
            request.setHeadimg(mFacebookRegResponse.getHeadImg());
            request.setNickname(mFacebookRegResponse.getNickName());
            request.setFb_id(mFacebookRegResponse.getFb_id());
            request.setFb_access_token(mFacebookRegResponse.getFb_access_token());
            request.setSex(mFacebookRegResponse.getSex().equals("male") ? "1" : "2");
            request.setLogin_source(Constant.FACEBOOK);
        } else if (s.equals(Constant.TWITTER)) {
            request.setMobile_brand(PhoneUtils.getPhoneBrand());
            request.setHeadimg(mTwitterRegResponse.getHeadImg());
            request.setNickname(mTwitterRegResponse.getNickName());
            request.setTwitter_id(mTwitterRegResponse.getTwitter_id());
            request.setSex(mTwitterRegResponse.getSex().equals("male") ? "1" : "2");
            request.setLogin_source(Constant.TWITTER);
        } else if (s.contains(Constant.GOOGLELOGIN)) {
            request.setMobile_brand(PhoneUtils.getPhoneBrand());
            request.setHeadimg(mGoogleLogin.getPhotoUrl() == null ? "123" : mGoogleLogin.getPhotoUrl() + "");
            request.setNickname(mGoogleLogin.getDisplayName());
            request.setGm_id(mGoogleLogin.getId());
            request.setSex("1");
            request.setLogin_source(Constant.GOOGLELOGIN);
        }
        return request;
    }

    @Override
    public void loginSucceed() {
        hideLoading();
        mPresenter.getUserDetailsDataInfo(getUserCommentRequest(), true);
        ToastUtils.showShort(mContext,getString(R.string.logged_in));
    }

    @Override
    public void setPersonalWorksData(PerSonWorkResponse response, Boolean isRefresh, Boolean isLoadMore, int selectType) {

    }

    @Override
    public void setPersonLikesData(PersonLikeResponse response, int selectType) {

    }

    @Override
    public void setUserInfo(UserDatasResponse userInfo) {


        ivUserHead.setImageURI(Uri.parse(userInfo.getData().getUserAvatar()));
        tvName.setText(userInfo.getData().getNickname());
        tvContent.setText(userInfo.getData().getSignature().equals("") ? mContext.getString(R.string.def_signature) : userInfo.getData().getSignature());
        if (userInfo.getData().getSex() == 1) {
            imgSex.setImageResource(R.mipmap.girl);
        } else {
            imgSex.setImageResource(R.mipmap.man);
        }
        tvFansSize.setText(mContext.getString(R.string.fans) + " " + CommonUtils.getLikeCount(userInfo.getData().getFansNum()));
        tvFfollowSize.setText(mContext.getString(R.string.follow) + " " + CommonUtils.getLikeCount(userInfo.getData().getFollowNum()));
        tvHeartsSize.setText(mContext.getString(R.string.hearts) + " " + CommonUtils.getLikeCount(userInfo.getData().getGetLike()));

        if (!TextUtils.isEmpty(mUserSpCache.getStringData(Constant.C_USERID))) {
            if (userInfo.getData().getUser_id().contains(mUserSpCache.getStringData(Constant.C_USERID))) {
                tvFollow.setVisibility(View.INVISIBLE);
            }
        }
        if (userInfo.getData().isIsFollow()) {
            tvFollow.setBackgroundResource(R.drawable.user_follow_abolish);
            tvFollow.setText(mContext.getString(R.string.followed) + "");
            isFOLLOW = true;
        } else {
            tvFollow.setBackgroundResource(R.drawable.user_follow);
            tvFollow.setText("+ " + mContext.getString(R.string.follow) + "");
            isFOLLOW = false;
        }

    }

    @Override
    public void hideRefresh() {

    }

    @Override
    public void hideLoadMore(boolean isHide) {

    }

    //关注成功
    @Override
    public void follows(boolean yesOrNo, String msg) {
        mPresenter.getUserDetailsDataInfo(getUserCommentRequest(), true);
    }

    @Override
    public void setVideoPlayUrl(AliVideoResponse data, PerSonWorkResponse.DataBean dataBean) {

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
        if (event.getPageType() == 1) {
            if (mPresenter != null) {
                mPresenter.getUserDetailsDataInfo(getUserCommentRequest(), true);
            }
        }
    }

    public boolean isLogin() {
        if (!mUserSpCache.getBoolean(Constant.KEY_IS_USER_LOGIN)) {
//            startActivity(LoginActivity.class);
            loginDialog.show();
            return false;
        } else {
            return true;
        }
    }

    public void faceBookLogin() {
        if (mFacebookPlatform == null) {
            mFacebookPlatform = new FacebookPlatform(UserDetailedDataActivity.this);
        }

        mFacebookPlatform.login();

        mFacebookPlatform.setFacebookListener(new FacebookPlatform.FacebookListener() {

            @Override
            public void facebookLoginSuccess(PlatformLogin platformLogin, FacebookRegResponse response) {
                showBaseLoading("");
                mFacebookRegResponse = response;
//                mPresenter.checkLogin(platformLogin, Api.TYPE_FACEBOOK);

                mPresenter.checkLogin(Constant.FACEBOOK);
            }

            @Override
            public void facebookLoginFail() {
                ToastUtils.showShort(mContext, getString(R.string.sendCodeerror));
            }
        });
    }

    private void twitterLogin() {
        if (!CommonUtils.isApplicationAvilible(UserDetailedDataActivity.this, "com.twitter.android")) {
            ToastUtils.showShort(UserDetailedDataActivity.this, mContext.getString(R.string.you_not_installed_twitter));
            return;
        }
        if (mTwitterLogin == null) {
            mTwitterLogin = new TwitterLogin();
        }

        mTwitterLogin.loginTwitter(UserDetailedDataActivity.this, new TwitterLogin.titterLoginCallback() {
            @Override
            public void onSuccess(TwitterRegResponse data) {
                showLoading();
                mTwitterRegResponse = data;
                mPresenter.checkLogin(Constant.TWITTER);
            }

            @Override
            public void onFailure() {
                LogUtil.showLog("msg---Tiwtter:onFailure");

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        loginDialog.registerCallBack(requestCode,resultCode,data,false);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void googleLoginSuccess(GoogleSignInAccount acct) {
        mGoogleLogin = acct;
        mPresenter.checkLogin(Constant.GOOGLELOGIN);
    }

    @Override
    public void googleLoginFail() {

    }
}
