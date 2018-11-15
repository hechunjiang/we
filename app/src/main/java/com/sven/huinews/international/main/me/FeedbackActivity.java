package com.sven.huinews.international.main.me;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseActivity;
import com.sven.huinews.international.entity.requst.FeedBackRequest;
import com.sven.huinews.international.entity.response.FeedBackClassifyResponse;
import com.sven.huinews.international.entity.response.FeedBackResponse;
import com.sven.huinews.international.main.me.Model.FeedBackModel;
import com.sven.huinews.international.main.me.contract.FeedBackContract;
import com.sven.huinews.international.main.me.presenter.FeedBackPresenter;
import com.sven.huinews.international.utils.Common;
import com.sven.huinews.international.utils.StatusBarUtils;
import com.sven.huinews.international.utils.ToastUtils;
import com.sven.huinews.international.view.EmptyLayout;
import com.sven.huinews.international.view.dialog.UploadProgressDialog;
import com.umeng.analytics.MobclickAgent;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * 后续版本开放此功能
 */

public class FeedbackActivity extends BaseActivity<FeedBackPresenter, FeedBackModel> implements FeedBackContract.View {
    private TagFlowLayout taglayout;
    private TextView btnSend;
    private EditText et_input;
    private ImageView iv_back;

    private List<FeedBackClassifyResponse.DataBean> tags;
    private List<FeedBackClassifyResponse.DataBean> selectedTags = new ArrayList<>();
    private UploadProgressDialog mDialog;

    private FrameLayout fAdView;
    private ImageButton close_ad;
    private AdView mAdView;

    //广告
    private TranslateAnimation mShowAction;//显示动画
    private TranslateAnimation mHiddenAction;//隐藏动画

    @Override
    public int getLayoutId() {
        return R.layout.activity_feedback;
    }

    @Override
    public void initView() {
        iv_back = findViewById(R.id.action_bar_back_iv);
        taglayout = findViewById(R.id.taglayout);
        btnSend = findViewById(R.id.btnSend);
        et_input = findViewById(R.id.et_input);
        mEmptyLayout = findViewById(R.id.mEmptyLayout);

        mEmptyLayout.setErrorType(EmptyLayout.LOADING, -1);

        mDialog = UploadProgressDialog.initGrayDialog(this);
        mDialog.setCancelable(false);
        mDialog.setMessage("");

        fAdView = findViewById(R.id.f_ad_view);
        mAdView = findViewById(R.id.video_banner_adView);
        close_ad = findViewById(R.id.close_ad);
        //初始化动画效果
        initAnimation();
        //初始化google广告
        initGoogleAd();
    }

    private void initGoogleAd() {
        if (mAdView != null) {
            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    if (fAdView != null && mAdView != null) {
                        if (fAdView.getVisibility() == View.GONE) {
                            fAdView.startAnimation(mShowAction);
                            fAdView.setVisibility(View.VISIBLE);
                            MobclickAgent.onEvent(FeedbackActivity.this, Common.AD_TYPE_GOOGLE_ME_LOOK);
                            MobclickAgent.onEvent(FeedbackActivity.this, Common.AD_TYPE_GOOGLE_ME_LOOK, "google_me_look");
                        }
                    }
                }

                @Override
                public void onAdOpened() {
                    MobclickAgent.onEvent(FeedbackActivity.this, Common.AD_TYPE_GOOGLE_ME_CLICK);
                    MobclickAgent.onEvent(FeedbackActivity.this, Common.AD_TYPE_GOOGLE_ME_CLICK, "google_me");
                    super.onAdOpened();
                }
            });
        }

        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);
        mAdView.setVisibility(View.VISIBLE);
    }

    public void initAnimation() {
        mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(500);

        mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f);
        mHiddenAction.setDuration(500);
    }

    @Override
    public void initEvents() {
        btnSend.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        mEmptyLayout.setOnEmptyRefreshLisenter(new EmptyLayout.onEmptyRefreshLisenter() {
            @Override
            public void onEmptyRefresh() {
                mPresenter.getFeedClassifyList();
            }
        });
    }

    @Override
    public void onClickEvent(View v) {
        if(v == btnSend){
            if(getSelectedTags().size() == 0){
                ToastUtils.showShort(this,R.string.feedback_select_type);
                return;
            }
            if(TextUtils.isEmpty(et_input.getText())){
                ToastUtils.showShort(this,R.string.feedback_inpit_detail);
                return;
            }
            mDialog.show();
            FeedBackRequest request = new FeedBackRequest();
            request.setType(selectedTags.get(0).getId()+"");
            request.setContent(et_input.getText().toString());
            mPresenter.sendFeed(request);
        }else if(v == iv_back){

        }finish();
    }

    @Override
    public void initObject() {
        StatusBarUtils.setColor(this, Color.parseColor("#FFFFFF"));
        setMVP();

        mPresenter.getFeedClassifyList();
    }


    private void initTags(final List<FeedBackClassifyResponse.DataBean> tags) {
        this.tags = tags;

        taglayout.setAdapter(new TagAdapter(tags) {
            @Override
            public View getView(FlowLayout parent, int position, Object o) {
                TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.tag_feedback_item, taglayout, false);
                tv.setText(tags.get(position).getName());
                return tv;
            }
        });
    }

    @Override
    public void getFeedClassify(FeedBackClassifyResponse response) {
        mEmptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT, -1);
        initTags(response.getData());
    }

    @Override
    public void sendFeed(FeedBackResponse response) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showErrorTip(int code, String msg) {
        mDialog.cancel();
        ToastUtils.showShort(this,msg);
    }

    public List<FeedBackClassifyResponse.DataBean> getSelectedTags() {
        selectedTags.clear();
        Set<Integer> set = taglayout.getSelectedList();
        for (Integer i : set) {
            selectedTags.add(tags.get(i));
        }
        return selectedTags;
    }
}
