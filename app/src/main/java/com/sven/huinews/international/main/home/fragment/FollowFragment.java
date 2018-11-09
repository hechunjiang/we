package com.sven.huinews.international.main.home.fragment;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseFragment;
import com.sven.huinews.international.config.Constant;
import com.sven.huinews.international.entity.MyNews;
import com.sven.huinews.international.entity.event.VideoLikeEvent;
import com.sven.huinews.international.entity.event.VideoShotLikeEvent;
import com.sven.huinews.international.entity.requst.FollowRequest;
import com.sven.huinews.international.entity.response.AliVideoResponse;
import com.sven.huinews.international.entity.response.FollowVideoResponse;
import com.sven.huinews.international.main.follow.activity.FollowVideoPlay1Activity;
import com.sven.huinews.international.main.follow.activity.FollwVideoPlayActivity;
import com.sven.huinews.international.main.follow.adapter.FollowAdapter;
import com.sven.huinews.international.main.follow.contract.FollowContract;
import com.sven.huinews.international.main.follow.model.FollowModel;
import com.sven.huinews.international.main.follow.presenter.FollowPresenter;
import com.sven.huinews.international.main.login.activity.LoginActivity;
import com.sven.huinews.international.utils.Common;
import com.sven.huinews.international.utils.cache.UserSpCache;
import com.sven.huinews.international.utils.itemDecoration.SmallVideoGridItem;
import com.sven.huinews.international.view.EmptyLayout;
import com.sven.huinews.international.view.MyRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class FollowFragment extends BaseFragment<FollowPresenter, FollowModel> implements FollowContract.View {

    private MyRefreshLayout refreshLayout;
    private RecyclerView follow_video_rv;
    private SmallVideoGridItem mSmallVideoGridItem;
    private FollowAdapter followAdapter; //视频适配器
    private int video_position;
    private RelativeLayout no_follow_data_layout;

    public void initdata(){
        setMVP();
        if (emptyLayout!=null){
            emptyLayout.setErrorType(EmptyLayout.LOADING, -1);
            emptyLayout.bringToFront();
        }
        PAGE = 1;
        mPresenter.getFollowList(true);
    }

    @Override
    protected int getLayoutResource() {
        EventBus.getDefault().register(this);
        return R.layout.fragment_follow;
    }

    @Override
    public void initObject() {
        emptyLayout.setErrorType(EmptyLayout.LOADING, -1);
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void initEvents() {
        //进入界面就开始加载
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                isRefresh = true;
                PAGE = 1;
                mPresenter.getFollowList(isRefresh);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                PAGE += 1;
                mPresenter.getFollowList(false);
            }
        });

        emptyLayout.setOnEmptyRefreshLisenter(new EmptyLayout.onEmptyRefreshLisenter() {
            @Override
            public void onEmptyRefresh() {
                mPresenter.getFollowList(true);
            }
        });

    }

    @Override
    protected void initView(View v) {
        refreshLayout = v.findViewById(R.id.refresh_layout);
        follow_video_rv = v.findViewById(R.id.follow_video_rv);
        emptyLayout = v.findViewById(R.id.follow_empt);
        no_follow_data_layout = v.findViewById(R.id.no_follow_data_layout);

        setRecyclerViewManager();
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (UserSpCache.getInstance(getActivity()).getStringData(Constant.OPENID).equals("")) {
                    startActivity(LoginActivity.class);
                    getActivity().finish();
                } else {
                    mPresenter.getFollowList(true);
                    PAGE = 1;
                }

            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if (UserSpCache.getInstance(getActivity()).getStringData(UserSpCache.OPENID).equals("")) {
                    startActivity(LoginActivity.class);
                    getActivity().finish();
                } else {
                    PAGE += 1;
                    mPresenter.getFollowList(false);
                }
            }
        });

    }


    private void setRecyclerViewManager() {
        followAdapter = new FollowAdapter(R.layout.v_video_list_item);
        //分割线
        if (mSmallVideoGridItem == null) mSmallVideoGridItem = new SmallVideoGridItem(getContext());
        follow_video_rv.removeItemDecoration(mSmallVideoGridItem);
        follow_video_rv.setItemAnimator(null);
        follow_video_rv.setPadding(0, 0, 0, 0);
        follow_video_rv.addItemDecoration(mSmallVideoGridItem);
        //设置layoutManager之前必须移除之前的分割线
        final StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //   staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        follow_video_rv.setLayoutManager(staggeredGridLayoutManager);
        follow_video_rv.setAdapter(followAdapter);


        followAdapter.setOnItemClickLisenter(new FollowAdapter.OnItemClickLisenter() {
            @Override
            public void itemClick(FollowVideoResponse.DataBean data, int position) {
                FollowVideoPlay1Activity.toThis(mContext, dataConversion(), position, Common.FOLLOW_PAGE, 1, -1, "0", PAGE);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefeshHome(VideoLikeEvent event) {
        followAdapter.getItem(video_position).setLike_count(event.getmData().getLikeCount());
        followAdapter.getItem(video_position).setComment_count(event.getmData().getCommentCount());
        followAdapter.getItem(video_position).setIs_up(event.getmData().isLike());
        followAdapter.notifyDataSetChanged();
    }


    public List<MyNews> dataConversion() {
        List<MyNews> list = new ArrayList<>();
        MyNews myNews = null;
        for (int i = 0; i < mDatas.size(); i++) {
            myNews = new MyNews();
            FollowVideoResponse.DataBean dataBean = mDatas.get(i);
            myNews.setDu_type(dataBean.getDu_type() + "");
            myNews.setVideoUrl(dataBean.getVideo_url());
            myNews.setId(dataBean.getId());
            myNews.setVideo_id(dataBean.getId());
            myNews.setCoverUrl(dataBean.getVideo_cover());
            myNews.setUserIcon(dataBean.getUser_avatar());
            myNews.setTitle(dataBean.getTitle());
            myNews.setCommentCount(dataBean.getComment_count());
            myNews.setShareCount(dataBean.getShare_count());
            myNews.setLikeCount(dataBean.getLike_count());
            myNews.setUserName(dataBean.getUser_nickname());
            myNews.setLike(dataBean.isIs_up());
            myNews.setOtherId(dataBean.getUser_id()+"");
            list.add(myNews);
        }
        return list;
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
        emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR, EmptyLayout.NET_WORK_ERROR);
    }

    @Override
    public FollowRequest getFollowRequest() {
        FollowRequest request = new FollowRequest();
        request.setPage(PAGE);
        request.setPagesize(LIMIT);
        return request;
    }

    List<FollowVideoResponse.DataBean> mDatas = new ArrayList<>();

    @Override
    public void setFollowData(List<FollowVideoResponse.DataBean> datas, boolean isRefresh, boolean isLoadMore) {
        if (isRefresh) {
            followAdapter.clearDatas();
            mDatas.clear();
            mDatas.addAll(datas);
        } else {
            mDatas.addAll(datas);
        }
        emptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT, -1);
        if (refreshLayout != null) {
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadmore();
        }
        if (mDatas != null && mDatas.size() > 0) {
            followAdapter.addData(datas);
            emptyLayout.setVisibility(View.GONE);
            refreshLayout.setVisibility(View.VISIBLE);
            no_follow_data_layout.setVisibility(View.GONE);
        } else {
            if (isRefresh){
                followAdapter.clearDatas();
            }
            no_follow_data_layout.setVisibility(View.VISIBLE);
            refreshLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void hideRefresh() {
        if (refreshLayout != null) refreshLayout.finishRefresh();
    }

    @Override
    public void hideLoadMore(Boolean isHide) {
        if (refreshLayout != null) refreshLayout.finishLoadmore();
    }

    @Override
    public void setVideoPlayUrl(AliVideoResponse data, FollowVideoResponse.DataBean dataBean) {
        MyNews myNews = new MyNews();
        myNews.setCoverUrl(data.getData().getVideo_cover());
        myNews.setTitle(data.getData().getTitle());
        myNews.setVideoUrl(data.getData().getVideo_url());
        myNews.setId(data.getData().getId());
        myNews.setVideo_id(data.getData().getId());
        myNews.setDu_type("1");
        myNews.setOtherId(dataBean.getUser_id() + "");
        myNews.setUserName(dataBean.getUser_nickname());
        myNews.setUserIcon(dataBean.getUser_avatar());
        myNews.setLikeCount(Integer.parseInt(dataBean.getLike_count() + ""));
        myNews.setOtherId(dataBean.getUser_id()+"");
        FollwVideoPlayActivity.toThis(mContext, myNews, 0, 0, 2);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 更新数目
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvents(VideoShotLikeEvent event) {
        followAdapter.getItem(video_position).setLike_count(event.getData().getLike_count());
//        followAdapter.getItem(video_position).setIsthumb(event.getData().getIsthumb());
        followAdapter.notifyDataSetChanged();
    }


}
