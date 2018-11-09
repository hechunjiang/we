package com.sven.huinews.international.view;

import android.content.Context;
import android.util.AttributeSet;

import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.sven.huinews.international.R;

/**
 * Created by Sven on 2018/2/5.
 */

public class MyRefreshLayout extends SmartRefreshLayout {
    private MaterialHeader mMaterialHeader;
    private BallPulseFooter mBallPulseFooter;

    public MyRefreshLayout(Context context) {
        super(context);
        init(context);
    }

    public MyRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(Context context){
        setEnableRefresh(true);
        setEnableLoadmore(true);
        mMaterialHeader = new MaterialHeader(context);
        mMaterialHeader.setColorSchemeColors(context.getResources().getColor(R.color.tab_tv_color_cli));
        setRefreshHeader(mMaterialHeader);
        mBallPulseFooter = new BallPulseFooter(context);
        mBallPulseFooter.setIndicatorColor(context.getResources().getColor(R.color.tab_tv_color_cli));
        mBallPulseFooter.setAnimatingColor(context.getResources().getColor(R.color.tab_tv_color_cli));
        mBallPulseFooter.setNormalColor(context.getResources().getColor(R.color.tab_tv_color_cli));
        setRefreshFooter(mBallPulseFooter);
    }
}
