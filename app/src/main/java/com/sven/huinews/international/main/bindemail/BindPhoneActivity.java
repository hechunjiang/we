package com.sven.huinews.international.main.bindemail;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseActivity;
import com.sven.huinews.international.config.api.Api;
import com.sven.huinews.international.entity.event.LoginSucceedEvent;
import com.sven.huinews.international.entity.requst.FacebookRegRequest;
import com.sven.huinews.international.entity.requst.GetCodeRequest;
import com.sven.huinews.international.entity.requst.LinkedInRegRequest;
import com.sven.huinews.international.entity.requst.SmsCodeRequest;
import com.sven.huinews.international.entity.requst.TwitterRegRequest;
import com.sven.huinews.international.entity.response.FacebookRegResponse;
import com.sven.huinews.international.entity.response.LinkedInResponse;
import com.sven.huinews.international.entity.response.TwitterRegResponse;
import com.sven.huinews.international.main.bindemail.contract.BindPhoneContract;
import com.sven.huinews.international.main.bindemail.model.BindPhoneModel;
import com.sven.huinews.international.main.bindemail.persenter.BindPhonePresenter;
import com.sven.huinews.international.main.home.activity.MainActivity;
import com.sven.huinews.international.utils.Common;
import com.sven.huinews.international.view.LoginCodeCountDown;

import org.greenrobot.eventbus.EventBus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BindPhoneActivity extends BaseActivity<BindPhonePresenter, BindPhoneModel> implements BindPhoneContract.View, View.OnClickListener {

    ImageView mActionBarBackIv;
    TextView mActionBarTitleTv;
    EditText mBindPhoneEt;
    EditText mBindPhoneCodeEt;
    TextView mBindGetCodeBtn;
    EditText mBindPassEt;
    EditText mBindInvitationEt;
    Button mBindCommitBtn;
    private TwitterRegResponse mTwitterUser;
    private FacebookRegResponse mFacebookUser;
    private LinkedInResponse mLinkedInUser;
    private LoginCodeCountDown mLoginCodeCountDown;
    private String platformType;

    @Override
    public int getLayoutId() {
        return R.layout.activity_bind_phone;
    }

    @Override
    public void initView() {
        mActionBarBackIv = findViewById(R.id.action_bar_back_iv);
        mActionBarTitleTv = findViewById(R.id.action_bar_title_tv);
        mBindPhoneEt = findViewById(R.id.bind_phone_et);
        mBindPhoneCodeEt = findViewById(R.id.bind_phone_code_et);
        mBindGetCodeBtn = findViewById(R.id.bind_get_code_btn);
        mBindPassEt = findViewById(R.id.bind_pass_et);
        mBindInvitationEt = findViewById(R.id.bind_invitation_et);
        mBindCommitBtn = findViewById(R.id.bind_commit_btn);


        mActionBarTitleTv.setText(getString(R.string.link_email));
        mActionBarTitleTv.setTextColor(getResources().getColor(R.color.colorBlack));
        mLoginCodeCountDown = new LoginCodeCountDown(mBindGetCodeBtn);
        mBindPhoneEt.addTextChangedListener(textWatcher);
        mBindPhoneCodeEt.addTextChangedListener(textWatcher);
        mBindPassEt.addTextChangedListener(textWatcher);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            platformType = bundle.getString(Common.BUNDLE_TO_BIND_PHONE_TYPE);
            if (platformType.equals(Api.TYPE_FACEBOOK)) {
                mFacebookUser = (FacebookRegResponse) bundle.getSerializable(Common.BUNDLE_TO_BIND_PHONE);
            } else if (platformType.equals(Api.TYPE_TIWTTER)) {
                mTwitterUser = (TwitterRegResponse) bundle.getSerializable(Common.BUNDLE_TO_BIND_PHONE);
            } else if (platformType.equals(Api.TYPE_LINKEDIN)) {
                mLinkedInUser = (LinkedInResponse) bundle.getSerializable(Common.BUNDLE_TO_BIND_PHONE);
            }
        }

    }

    @Override
    public void initEvents() {
        mActionBarBackIv.setOnClickListener(this);
        mBindGetCodeBtn.setOnClickListener(this);
        mBindCommitBtn.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        if (v == mActionBarBackIv) {
            finish();
        } else if (v == mBindGetCodeBtn) {
            //发送验证码
            mPresenter.getSmsCode(getSmsRequest());
        } else if (v == mBindCommitBtn) {
            platformReg();
        }
    }


    private void platformReg() {
        if (platformType.equals(Api.TYPE_TIWTTER)) {
            mPresenter.twitterRegister();
        } else if (platformType.equals(Api.TYPE_FACEBOOK)) {
            mPresenter.facebookRegister();
        } else if (platformType.equals(Api.TYPE_LINKEDIN)) {
            mPresenter.linkedInRegister();
        }
    }

    @Override
    public void initObject() {
        setMVP();
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

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String inputEmail = mBindPhoneEt.getText().toString();
            String verfiyCode = mBindPhoneCodeEt.getText().toString();
            String password = mBindPassEt.getText().toString();
            if (TextUtils.isEmpty(inputEmail) || TextUtils.isEmpty(verfiyCode)) {
                mBindCommitBtn.setEnabled(false);
                return;
            }

            if (isEmail(inputEmail) && verfiyCode.length() >= 4 && password.length() >= 6) {
                mBindCommitBtn.setBackground(getResources().getDrawable(R.drawable.btn_login));
                mBindCommitBtn.setEnabled(true);
            } else {
                mBindCommitBtn.setBackground(getResources().getDrawable(R.drawable.btn_login_nor));
                mBindCommitBtn.setEnabled(false);
            }
        }
    };


    //    邮箱辨识
    private static boolean isEmail(String email) {

        Pattern emailPattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

        Matcher matcher = emailPattern.matcher(email);

        if (matcher.find()) {
            return true;
        }
        return false;

    }

    @Override
    public GetCodeRequest getSmsRequest() {
        GetCodeRequest request = new GetCodeRequest();
        request.setType("reg");
        request.setMail(mBindPhoneEt.getText().toString());
        return request;
    }

    @Override
    public TwitterRegRequest getTwitterRequest() {
        TwitterRegRequest request = new TwitterRegRequest();
        request.setRegisterCode(mBindPhoneCodeEt.getText().toString().trim());
        request.setPass(mBindPassEt.getText().toString().trim());
        request.setMail(mBindPhoneEt.getText().toString().trim());
        request.setHeadIcon(mTwitterUser.getHeadImg());
        request.setTwitterId(mTwitterUser.getTwitter_id());
        request.setSex("1");
        request.setNickName(mTwitterUser.getNickName());
        request.setInvitCode(mBindInvitationEt.getText().toString().trim());
        return request;
    }

    @Override
    public FacebookRegRequest getFaceBookRequest() {
        FacebookRegRequest request = new FacebookRegRequest();
        request.setRegisterCode(mBindPhoneCodeEt.getText().toString().trim());
        request.setPass(mBindPassEt.getText().toString().trim());
        request.setMail(mBindPhoneEt.getText().toString().trim());
        request.setHeadIcon(mFacebookUser.getHeadImg());
        request.setFbId(mFacebookUser.getFb_id());
        if (mFacebookUser.getSex().equals("female")) {
            request.setSex("2");
        } else {
            request.setSex("1");
        }
        request.setFb_access_token(mFacebookUser.getFb_access_token());
        request.setNickName(mFacebookUser.getNickName());
        request.setInvitCode(mBindInvitationEt.getText().toString().trim());
        return request;
    }

    @Override
    public LinkedInRegRequest getLinkedInRequest() {
        LinkedInRegRequest request = new LinkedInRegRequest();
        request.setRegisterCode(mBindPhoneCodeEt.getText().toString().trim());
        request.setPass(mBindPassEt.getText().toString().trim());
        request.setMail(mBindPhoneEt.getText().toString().trim());
        if (TextUtils.isEmpty(mLinkedInUser.getPictureUrl())) {
            request.setHeadIcon(Api.DEFAULT_IMG);
        } else {
            request.setHeadIcon(mLinkedInUser.getPictureUrl());
        }
        request.setLk_id(mLinkedInUser.getId());
        request.setSex("1");
        request.setNickName(mLinkedInUser.getFirstName() + mLinkedInUser.getLastName());
        request.setInvitCode(mBindInvitationEt.getText().toString().trim());
        return request;
    }

    @Override
    public void registSuccess() {
        EventBus.getDefault().post(new LoginSucceedEvent());
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void getMsgSuccessful() {
        mLoginCodeCountDown.start();
        mLoginCodeCountDown.setNotClick();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoginCodeCountDown.cancel();
    }
}
