package com.sven.huinews.international.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sven.huinews.international.R;

public class EnterLoginDialog extends Dialog {
    private TextView btnLogin;

    public EnterLoginDialog(@NonNull Context context) {
        super(context, R.style.logindialog);
        initView();
    }

    public EnterLoginDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    protected EnterLoginDialog(@NonNull Context context, boolean cancelable, @Nullable DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    private void initView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.enter_login_dialog, null);
        setContentView(v);

        setCanceledOnTouchOutside(false);
        setCancelable(false);
        btnLogin = v.findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onLoginLisenter != null) {
                    onLoginLisenter.toLogin();
                }
            }
        });
    }

    public   interface OnLoginLisenter {
        void toLogin();
    }

    private OnLoginLisenter onLoginLisenter;

    public void setOnLoginLisenter(OnLoginLisenter l) {
        this.onLoginLisenter = l;
    }
}
