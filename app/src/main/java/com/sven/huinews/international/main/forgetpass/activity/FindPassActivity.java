package com.sven.huinews.international.main.forgetpass.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseActivity;
import com.sven.huinews.international.entity.requst.GetCodeRequest;
import com.sven.huinews.international.entity.requst.ResetPassRequst;
import com.sven.huinews.international.main.forgetpass.contract.FindPassContract;
import com.sven.huinews.international.main.forgetpass.model.FindPassModel;
import com.sven.huinews.international.main.forgetpass.presenter.FindPassPresenter;
import com.sven.huinews.international.main.login.activity.LoginActivity;
import com.sven.huinews.international.utils.CommonUtils;
import com.sven.huinews.international.utils.ToastUtils;
import com.sven.huinews.international.view.LoginCodeCountDown;

public class FindPassActivity extends BaseActivity<FindPassPresenter, FindPassModel> implements FindPassContract.View {

    private EditText etEmail, etGetCode, etPassWord;
    private TextView tvGetCode, tvResetNow;
    private String sEmail, sCode, sPass;
    private LoginCodeCountDown mLoginCodeCountDown;
    private ImageView ivCloseImg;


    @Override
    public int getLayoutId() {
        return R.layout.activity_find_pass;
    }

    @Override
    public void initView() {

        etEmail = findViewById(R.id.find_email_et);
        etGetCode = findViewById(R.id.find_code_et);
        etPassWord = findViewById(R.id.find_pass_et);

        tvGetCode = findViewById(R.id.find_pass_get_code_tv);
        tvResetNow = findViewById(R.id.find_pass_commit_btn);

        ivCloseImg = findViewById(R.id.find_close_iv);


    }

    @Override
    public void initEvents() {
        tvGetCode.setOnClickListener(this);
        tvResetNow.setOnClickListener(this);
        ivCloseImg.setOnClickListener(this);
        etPassWord.addTextChangedListener(textWatcher);
        etGetCode.addTextChangedListener(textWatcher);
        etEmail.addTextChangedListener(textWatcher);
        mLoginCodeCountDown = new LoginCodeCountDown(tvGetCode);
    }

    @Override
    public void onClickEvent(View v) {
        if (v == tvGetCode) {//获取验证码
            checkGetCode();
        } else if (v == tvResetNow) { //设置新的密码
            checkCommit();
        } else if (v == ivCloseImg) {
            FindPassActivity.this.finish();
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
        tvResetNow.setEnabled(true);
        mLoginCodeCountDown.onFinish();
        if (mLoginCodeCountDown!=null){
            mLoginCodeCountDown.cancel();
            mLoginCodeCountDown.onFinish();
        }
    }


    public void checkGetCode() {
        sEmail = etEmail.getText().toString().trim();
        if (!CommonUtils.checkIsEmail(sEmail)) {
            ToastUtils.showShort(mContext, getResources().getString(R.string.enter_e_mail_address));
            return;
        }
        mLoginCodeCountDown.setNotClick();
        mLoginCodeCountDown.start();
        mPresenter.getFindPassCode();
    }

    public void checkCommit() {
        sCode = etGetCode.getText().toString().trim();
        sPass = etPassWord.getText().toString().trim();

        if (!CommonUtils.checkIsEmail(sEmail)) {
            ToastUtils.showShort(mContext, getResources().getString(R.string.enter_e_mail_address));
            return;
        }
        if (!CommonUtils.checkPhoneCode(sCode)) {
            ToastUtils.showShort(mContext, getResources().getString(R.string.enter_the_code));
            return;
        }
        if (!CommonUtils.checkPassWord(sPass)) {
            ToastUtils.showShort(mContext, getResources().getString(R.string.password_must_be_6_to_22_numbers));
            return;
        }
        mPresenter.resetNotPass();
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
            String inputPass = etPassWord.getText().toString();
            String inputCode = etGetCode.getText().toString();
            String inputPhone = etEmail.getText().toString();
            if (TextUtils.isEmpty(inputPhone) || TextUtils.isEmpty(inputCode)) {
                return;
            }

            if (inputPhone.length() >= 11 && inputCode.length() >= 4 && inputPass.length() >= 6) {
                tvResetNow.setBackground(getResources().getDrawable(R.drawable.btn_login));
                tvResetNow.setEnabled(true);
            } else {
                tvResetNow.setBackground(getResources().getDrawable(R.drawable.btn_login_nor));
                tvResetNow.setEnabled(false);
            }
        }
    };

    @Override
    public GetCodeRequest getFindPassCodeRequest() {
        GetCodeRequest request = new GetCodeRequest();
        request.setType("findpwd");
        request.setMail(sEmail);
        return request;
    }

    @Override
    public ResetPassRequst getResetPassRequset() {
        ResetPassRequst requst = new ResetPassRequst();
        requst.setCode(sCode);
        requst.setPass(sPass);
        requst.setPhone(sEmail);

        return requst;
    }

    @Override
    public void resetPassSuccess(String s) {
        //startActivity(LoginActivity.class);
        FindPassActivity.this.finish();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            FindPassActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
