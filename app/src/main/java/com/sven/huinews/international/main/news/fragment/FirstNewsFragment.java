package com.sven.huinews.international.main.news.fragment;

import android.view.View;

import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseFragment;
import com.sven.huinews.international.entity.requst.NewsListRequst;
import com.sven.huinews.international.main.news.model.FirstNewModel;
import com.sven.huinews.international.main.news.contract.FirstNewsContruct;
import com.sven.huinews.international.main.news.presenter.FirstNewsPresenter;


public class FirstNewsFragment extends BaseFragment<FirstNewsPresenter, FirstNewModel> implements FirstNewsContruct.View {

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_first_news;
    }

    @Override
    public void initObject() {

    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void initView(View v) {

    }

    @Override
    public void initEvents() {

    }

    @Override
    public void OnClickEvents(View v) {

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


    @Override
    public NewsListRequst newsListRequest() {
        NewsListRequst newsListRequst = new NewsListRequst();
        newsListRequst.page = 1;
        newsListRequst.r_type = 1;

        return newsListRequst;
    }

    @Override
    public void setNewsList(String s) {

    }
}
