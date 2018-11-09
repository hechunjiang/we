package com.sven.huinews.international.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sven.huinews.international.R;


public class TitleBar extends LinearLayout {
    private Context mContext;
    private ImageButton btn_back;
    private TextView btn_close;
    private TextView tv_title;
    private TextView tv_right;

    public TitleBar(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.title_bar, this);
        btn_back = findViewById(R.id.btn_back);
        tv_title = findViewById(R.id.tv_title);
        tv_right = findViewById(R.id.tv_right);
        btn_close = findViewById(R.id.btn_close);
    }


    /**
     * 是否隐藏返回键
     *
     * @param isShow
     */
    public void hsBack(boolean isShow) {
        btn_back.setVisibility(isShow ? INVISIBLE : VISIBLE);
    }

    /**
     * 监听返回
     *
     * @param l
     */
    public void setBackOnClick(OnClickListener l) {
        btn_back.setOnClickListener(l);
    }

    /**
     * 设置标题 str
     *
     * @param s
     */
    public void setTitle(String s) {
        tv_title.setText(s);
    }

    /**
     * 设置标题 资源
     *
     * @param resId
     */
    public void setTitle(int resId) {
        tv_title.setText(resId);
    }

    /**
     * 设置右边textview 文字 str
     *
     * @param s
     */
    public void setRightTv(String s) {
        tv_right.setText(s);
        tv_right.setVisibility(View.VISIBLE);
    }

    public void setHideRightTv() {
        tv_right.setVisibility(View.GONE);
    }

    /**
     * 设置右边textview 文字 资源
     *
     * @param resId
     */
    public void setRightTv(int resId) {
        tv_right.setText(resId);
        tv_right.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏右边按钮
     */
    public void hideRightTv() {
        tv_right.setVisibility(View.INVISIBLE);
    }

    /**
     * 显示右边按钮
     */
    public void showRightTv() {
        tv_right.setVisibility(View.VISIBLE);
    }

    /**
     * 右边按钮点击
     *
     * @param l
     */
    public void setRightTvOnClick(OnClickListener l) {
        tv_right.setOnClickListener(l);
    }

    /**
     * 针对web  关闭按钮
     *
     * @param l
     */
    public void setCloseClickLisenter(OnClickListener l) {
        btn_close.setVisibility(View.VISIBLE);
        btn_close.setOnClickListener(l);
    }
}
