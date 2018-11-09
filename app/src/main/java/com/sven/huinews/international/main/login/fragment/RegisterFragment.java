package com.sven.huinews.international.main.login.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseFragment;
import com.sven.huinews.international.config.Constant;
import com.sven.huinews.international.entity.Users;
import com.sven.huinews.international.entity.requst.GetCodeRequest;
import com.sven.huinews.international.entity.requst.RegistRequst;
import com.sven.huinews.international.main.home.activity.MainActivity;
import com.sven.huinews.international.main.login.activity.PrivacyActivity;
import com.sven.huinews.international.main.login.contract.RegistFragmentContract;
import com.sven.huinews.international.main.login.model.RegistModel;
import com.sven.huinews.international.main.login.presenter.RegistPrensenter;
import com.sven.huinews.international.utils.CommonUtils;
import com.sven.huinews.international.utils.PhoneUtils;
import com.sven.huinews.international.utils.ToastUtils;
import com.sven.huinews.international.view.LoginCodeCountDown;

import org.greenrobot.eventbus.EventBus;

import wedemo.MessageEvent;


public class RegisterFragment extends BaseFragment<RegistPrensenter, RegistModel> implements RegistFragmentContract.View {

    private EditText etEmail, etCode, etPass, etInvitationcode;
    private TextView tvGetCode, tvRegist, tv_privacy;
    private String sEmail, sCode, sPass, sInvitation;
    private LoginCodeCountDown mLoginCodeCountDown;

    private int otherLogin = 0;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_login_register;
    }

    @Override
    public void initObject() {
        setMVP();
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void initView(View v) {
        Bundle arguments = getArguments();
        if(arguments != null){
            otherLogin = arguments.getInt(Constant.OTHER_LOGIN,0);
        }

        etEmail = v.findViewById(R.id.et_regist_mail);
        etCode = v.findViewById(R.id.register_sms_code_et);
        tvGetCode = v.findViewById(R.id.register_get_sms_tv);
        etPass = v.findViewById(R.id.register_pass_et);
        etInvitationcode = v.findViewById(R.id.register_yq_et);
        tvRegist = v.findViewById(R.id.register_btn);
        tv_privacy = v.findViewById(R.id.tv_privacy);

    }

    @Override
    public void initEvents() {
        tvGetCode.setOnClickListener(this);
        tvRegist.setOnClickListener(this);
        etEmail.addTextChangedListener(textWatcher);
        etCode.addTextChangedListener(textWatcher);
        etPass.addTextChangedListener(textWatcher);
        mLoginCodeCountDown = new LoginCodeCountDown(tvGetCode);
        agreementEvent();
    }

    private void agreementEvent() {
        final String agreement = " Privacy Policy";
        tv_privacy.setText("By signing up,you agree to the");
        String endTv = "，Including Cookie Use.";
        SpannableString string = new SpannableString(agreement);

        string.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                openActivity(PrivacyActivity.class);
            }
        }, 0, agreement.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        string.setSpan(new ForegroundColorSpan(Color.parseColor("#6fb5c5")), 0,
                agreement.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_privacy.setHighlightColor(Color.TRANSPARENT);
        tv_privacy.append(string);
        tv_privacy.append(endTv);

        tv_privacy.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件

    }

    @Override
    public void OnClickEvents(View v) {
        if (v == tvGetCode) {//发送验证码
            checkCanGetCode();
        } else if (v == tvRegist) {//注册
            checkCanRegister();
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showErrorTip(int code, String msg) {
        ToastUtils.showShort(mContext, msg);
        tvRegist.setEnabled(true);

        if (mLoginCodeCountDown!=null){
            mLoginCodeCountDown.cancel();
            mLoginCodeCountDown.onFinish();
        }
    }

    @Override
    public GetCodeRequest getCodeRequest() {
        GetCodeRequest request = new GetCodeRequest();
        request.setType("reg");
        request.setMail(sEmail);
        return request;
    }

    @Override
    public RegistRequst getRegistRequst() {
        RegistRequst registRequst = new RegistRequst();
        registRequst.setMail(sEmail);
        registRequst.setVerify(sCode);
        registRequst.setPass(sPass);
        registRequst.setMobile_brand(PhoneUtils.getPhoneBrand());
        if (!TextUtils.isEmpty(sInvitation)) {
            registRequst.setInvitCode(sInvitation);
        }
        return registRequst;
    }


    private void checkCanGetCode() {
        sEmail = etEmail.getText().toString().trim();
        if (!CommonUtils.checkIsEmail(sEmail)) {
            ToastUtils.showShort(mContext, getResources().getString(R.string.enter_e_mail_address));
            return;
        }
        mLoginCodeCountDown.setNotClick();
        mLoginCodeCountDown.start();
        mPresenter.getRegistCode();
    }

    private void checkCanRegister() {
        sEmail = etEmail.getText().toString().trim();
        sPass = etPass.getText().toString().trim();
        sCode = etCode.getText().toString().trim();
        sInvitation = etInvitationcode.getText().toString().trim();
        if (!CommonUtils.checkIsEmail(sEmail)) {
            ToastUtils.showShort(mContext, getResources().getString(R.string.enter_e_mail_address));
            return;
        }
        if (!CommonUtils.checkPhoneCode(sCode)) {
            ToastUtils.showShort(mContext, getResources().getString(R.string.enter_the_code));
        }
        if (!CommonUtils.checkPassWord(sPass)) {
            ToastUtils.showShort(mContext, getResources().getString(R.string.password_must_be_6_to_22_numbers));
            return;
        }
        tvRegist.setEnabled(false);
        mPresenter.regist();
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
            String inputPhone = etEmail.getText().toString().trim();
            String verfiyCode = etCode.getText().toString().trim();
            String password = etPass.getText().toString().trim();
            if (TextUtils.isEmpty(inputPhone) || TextUtils.isEmpty(verfiyCode)) {
                tvRegist.setEnabled(false);
                return;
            }

            if (verfiyCode.length() >= 4 && password.length() >= 6) {
                tvRegist.setBackground(getActivity().getResources().getDrawable(R.drawable.btn_login));
                tvRegist.setEnabled(true);
            } else {
                tvRegist.setBackground(getActivity().getResources().getDrawable(R.drawable.btn_login_nor));
                tvRegist.setEnabled(false);
            }
        }
    };

    /**
     * 注册成功
     */
    @Override
    public void RegistSuccess(Users user) {
//        showMsg(user.getName() + "Done");
        Toast.makeText(getContext(), user.getName() + mContext.getString(R.string.wancheng), Toast.LENGTH_LONG).show();
        if(otherLogin == 1) {
            EventBus.getDefault().post(new MessageEvent(Constant.LOGIN_SUCCESS));
            getActivity().finish();
        }else {
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }

    /**
     * 获取验证码成功
     */
    @Override
    public void getCodeSuccess() {
        ToastUtils.showShort(mContext, getResources().getString(R.string.the_verifacation_code_has_been_send_to_your_email));
    }
}
