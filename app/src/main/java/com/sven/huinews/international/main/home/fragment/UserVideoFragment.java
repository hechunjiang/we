package com.sven.huinews.international.main.home.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dueeeke.videoplayer.player.VideoViewManager;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.sven.huinews.international.AppConfig;
import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseFragment;
import com.sven.huinews.international.config.Constant;
import com.sven.huinews.international.entity.MyNews;
import com.sven.huinews.international.entity.event.UpdateEvent;
import com.sven.huinews.international.entity.event.VideoLikeEvent;
import com.sven.huinews.international.entity.requst.LikesRequest;
import com.sven.huinews.international.entity.requst.PersonWorkRequest;
import com.sven.huinews.international.entity.response.AliVideoResponse;
import com.sven.huinews.international.entity.response.HomeTab;
import com.sven.huinews.international.entity.response.PerSonWorkResponse;
import com.sven.huinews.international.entity.response.PersonLikeResponse;
import com.sven.huinews.international.entity.response.UserDatasResponse;
import com.sven.huinews.international.main.follow.activity.FollowVideoPlay1Activity;
import com.sven.huinews.international.main.follow.activity.FollwVideoPlayActivity;
import com.sven.huinews.international.main.home.contract.PersonWorkContract;
import com.sven.huinews.international.main.home.model.PersonWorkModel;
import com.sven.huinews.international.main.home.presenter.PersonWorkPresenter;
import com.sven.huinews.international.main.me.DraftsActivity;
import com.sven.huinews.international.main.userdetail.adapter.UserDetailsAdapter;
import com.sven.huinews.international.main.userdetail.adapter.UserLikeAdapter;
import com.sven.huinews.international.utils.Common;
import com.sven.huinews.international.view.EmptyLayout;
import com.sven.huinews.international.view.GirdItemDecoration;
import com.sven.huinews.international.view.MyRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import wedemo.db.AppDatabase;
import wedemo.db.dao.TimeLineDao;
import wedemo.db.entity.TimeLineEntity;
import wedemo.utils.dataInfo.TimeDataCache;

public class UserVideoFragment extends BaseFragment<PersonWorkPresenter, PersonWorkModel> implements PersonWorkContract.View {
    private HomeTab mHomeTab;
    private RecyclerView rv;
    private GirdItemDecoration girdItemDecoration;
    private UserLikeAdapter userLikeAdapter;
    private UserDetailsAdapter userVideoAdapter;
    private MyRefreshLayout refresh_view;
    private String user_id;
    private int duty_type = -1;
    private String Other_id = "";
    private int video_position = 0;
    private RelativeLayout rl_nodata;
    private ImageView img_nodata;
    private TextView tv_nodata;


    public static UserVideoFragment getInstance(HomeTab mHomeTab, int duty_type, String Other_id) {
        UserVideoFragment fragment = new UserVideoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Common.HOME_LAYOUT_TYPE, mHomeTab);
        bundle.putInt(Common.HOME_UD_TYPE, duty_type);
        bundle.putString(Common.HOME_OTHER_ID, Other_id);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static UserVideoFragment getInstance(HomeTab mHomeTab) {
        UserVideoFragment fragment = new UserVideoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Common.HOME_LAYOUT_TYPE, mHomeTab);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutResource() {
        mHomeTab = (HomeTab) getArguments().getSerializable(Common.HOME_LAYOUT_TYPE);
        duty_type = getArguments().getInt(Common.HOME_UD_TYPE);
        Other_id = getArguments().getString(Common.HOME_OTHER_ID);
        EventBus.getDefault().register(this);
        return R.layout.user_video_fragment;
    }


    @Override
    public void initObject() {
        setMVP();
        // getDrafts();
        initRecycler();
    }

    private int videoType;

    private void initRecycler() {
        if (mHomeTab.getTabType() == Common.VIDEO) {
            setGridLayoutManager();
            videoType = Common.VIDEO;
            userVideoAdapter = new UserDetailsAdapter(R.layout.item_videos_videos);
            rv.setAdapter(userVideoAdapter);
            userVideoAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    video_position = position;
                    PerSonWorkResponse.DataBean item = (PerSonWorkResponse.DataBean) adapter.getItem(position);

                    if ("-111".equals(item.getId())) {
                        startActivity(DraftsActivity.class);
                    } else {
                        //判断是否为虚拟用户 1真实用户 2为虚拟用户  真实用户则请求阿里云数据
                        user_id = item.getUser_id() + "";
                        FollowVideoPlay1Activity.toThis(mContext, dataConversion(listWorkResponse), position, Common.VIDEO_VIDEOS_PAGE, 1, duty_type, Other_id, PAGE);
                    }
                }
            });


        } else if (mHomeTab.getTabType() == Common.LIKE) {
            setGridLayoutManager();
            videoType = Common.LIKE;
            userLikeAdapter = new UserLikeAdapter(R.layout.item_videos_videos);
            rv.setAdapter(userLikeAdapter);
            userLikeAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    video_position = position;
                    PersonLikeResponse.DataBean item = (PersonLikeResponse.DataBean) adapter.getItem(position);
                    //判断是否为虚拟用户 1真实用户 2为虚拟用户  真实用户则请求阿里云数据
                    FollowVideoPlay1Activity.toThis(mContext, dataConversions(listLikeResponse), position, Common.VIDEO_LIKES_PAGE, 1, duty_type, Other_id, PAGE);
                }
            });
        }
    }

    private void setGridLayoutManager() {
        rv.setItemAnimator(null);
        rv.setPadding(0, 0, 0, 0);
        if (girdItemDecoration == null) {
            girdItemDecoration = new GirdItemDecoration(mContext);
        }
        rv.removeItemDecoration(girdItemDecoration);
        rv.addItemDecoration(girdItemDecoration);
        //设置layoutManager之前必须移除之前的分割线
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
        rv.setLayoutManager(gridLayoutManager);
    }


    @Override
    protected void loadData() {
//        emptyLayout.setErrorType(EmptyLayout.LOADING, -1);
//        PAGE = 1;
//        getVideoList(isRefresh);
        refresh_view.autoRefresh();
    }


    @Override
    protected void initView(View v) {
        rv = v.findViewById(R.id.video_rv);
        refresh_view = v.findViewById(R.id.refresh_view);
//        refresh_view.setEnableRefresh(false);
        rl_nodata = v.findViewById(R.id.rl_nodata);
        img_nodata = v.findViewById(R.id.img_nodata);
        tv_nodata = v.findViewById(R.id.tv_nodata);
        emptyLayout = v.findViewById(R.id.mEmptyLayout);
    }

    @Override
    public void initEvents() {
        rl_nodata.setOnClickListener(this);
        refresh_view.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                VideoViewManager.instance().releaseVideoPlayer();
                isRefresh = true;
                PAGE = 1;
                getVideoList(isRefresh);
                hasAdd = false;
                if (duty_type != -1 && duty_type != 0) {
                    EventBus.getDefault().post(new UpdateEvent(1));//avtivity
                } else {
                    EventBus.getDefault().post(new UpdateEvent(2));//fragment
                }

            }
        });
        refresh_view.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                VideoViewManager.instance().releaseVideoPlayer();
                isRefresh = false;
                PAGE += 1;
                getVideoList(isRefresh);
            }
        });
        img_nodata.setOnClickListener(this);
    }

    public List<MyNews> dataConversions(List<PersonLikeResponse.DataBean> listLikeResponse) {
        List<MyNews> list = new ArrayList<>();
        MyNews myNews = null;
        for (int i = 0; i < listLikeResponse.size(); i++) {
            myNews = new MyNews();
            PersonLikeResponse.DataBean dataBean = listLikeResponse.get(i);
            myNews.setDu_type(dataBean.getDu_type() + "");
            myNews.setVideoUrl(dataBean.getVideo_url());
            myNews.setId(dataBean.getId());
            myNews.setVideo_id(dataBean.getId());
            myNews.setCoverUrl(dataBean.getVideo_cover());
            myNews.setUserIcon(dataBean.getUser_avatar());
            myNews.setTitle(dataBean.getTitle());
            myNews.setCommentCount(Integer.parseInt(dataBean.getComment_count()));
            myNews.setShareCount(Integer.parseInt(dataBean.getShare_count()));
            myNews.setLikeCount(Integer.parseInt(dataBean.getLike_count()));
            myNews.setUserName(dataBean.getUser_nickname());
            myNews.setLike(dataBean.isIs_up());
            myNews.setOtherId(dataBean.getUser_id() + "");
            list.add(myNews);
        }
        return list;
    }

    public List<MyNews> dataConversion(List<PerSonWorkResponse.DataBean> listWorkResponse) {
        List<MyNews> list = new ArrayList<>();
        MyNews myNews = null;
        for (int i = 0; i < listWorkResponse.size(); i++) {
            myNews = new MyNews();
            PerSonWorkResponse.DataBean dataBean = listWorkResponse.get(i);
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
            myNews.setOtherId(dataBean.getUser_id() + "");
            list.add(myNews);
        }
        return list;
    }

    private void getVideoList(boolean isRefresh) {
        emptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT, -1);
        if (mHomeTab.getTabType() == Common.VIDEO) {
            mPresenter.getUserDetailsInfo(getPersonWorkRequest(), isRefresh, Constant.SELECT_USER_VIDEOS);
        } else {
            mPresenter.getUserDetailsLikesInfo(getLikeRequest(), isRefresh, Constant.SELECT_USER_LIKE_VIDEOS);
        }
    }

    public PersonWorkRequest getPersonWorkRequest() {
        PersonWorkRequest request = new PersonWorkRequest();
        if (duty_type != -1) {
            request.setDu_type(duty_type + "");
            request.setOther_id(Other_id);
        }
        request.setPage_size(LIMIT + "");
        request.setPage(PAGE + "");
        return request;
    }

    public LikesRequest getLikeRequest() {
        LikesRequest request = new LikesRequest();
        if (duty_type != -1) {
            request.setDu_type(duty_type + "");
            request.setOther_id(Other_id);
        }
        request.setPage_size(LIMIT + "");
        request.setPage(PAGE + "");
        return request;
    }

    @Override
    public void OnClickEvents(View v) {
        if (v == img_nodata) {
            loadData();
        } else if (v == rl_nodata) {
            rl_nodata.setVisibility(View.GONE);
            refresh_view.autoRefresh();
        }
    }

    @Override
    public void setUserInfo(UserDatasResponse userInfo) {

    }


    @Override
    public void onResume() {
        super.onResume();
        //  getDrafts();
    }

    /**
     * 查询
     */
    private void getDrafts() {
        Observable.create(new Observable.OnSubscribe<List<TimeLineEntity>>() {
            @Override
            public void call(Subscriber<? super List<TimeLineEntity>> subscriber) {
                AppDatabase database = AppDatabase.getDatabase(AppConfig.getAppContext());
                TimeLineDao dao = database.timeLineDao();
                List<TimeLineEntity> allTimeLine = dao.getAllTimeLine();
                subscriber.onNext(allTimeLine);
            }
        }).map(new Func1<List<TimeLineEntity>, List<TimeDataCache>>() {
            @Override
            public List<TimeDataCache> call(List<TimeLineEntity> timeLineEntities) {
                List<TimeDataCache> timeDataCacheList = new ArrayList<>();

                for (TimeLineEntity timeLineEntity : timeLineEntities) {
                    Gson gson = new Gson();
                    TimeDataCache timeDataCache = gson.fromJson(timeLineEntity.getJson(), TimeDataCache.class);
                    timeDataCache.setId(timeLineEntity.getId());
                    timeDataCacheList.add(timeDataCache);
                }
                return timeDataCacheList;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<TimeDataCache>>() {
                    @Override
                    public void call(List<TimeDataCache> timeDataCacheList) {
                        timeList = timeDataCacheList;
                        if (timeDataCacheList != null) {
                            draftCount = timeDataCacheList.size();
                        } else {
                            draftCount = 0;
                        }
                    }
                });
    }

    private int draftCount = 0;
    private List<TimeDataCache> timeList;
    List<PerSonWorkResponse.DataBean> listWorkResponse = new ArrayList<>();

    private boolean hasAdd = false;

    @Override
    public void setPersonalWorksData(PerSonWorkResponse response, Boolean isRefresh, Boolean isLoadMore, int selectType) {
        emptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT, -1);
        if (isRefresh) {
            listWorkResponse.clear();
            listWorkResponse.addAll(response.getData());
        } else {
            listWorkResponse.addAll(response.getData());
        }

//        if(!hasAdd) {
//            if (Constants.DRAFT_VIDEO) {
//                if (draftCount > 0) {
//                    PerSonWorkResponse.DataBean dataBean = new PerSonWorkResponse.DataBean();
//                    dataBean.setId("-111");
//
//                    TimeDataCache timeDataCache = timeList.get(timeList.size() - 1);
//                    dataBean.setVideo_cover(timeDataCache.getM_clipInfoArray().get(0).getFilePath());
//                    response.getData().add(0, dataBean);
//                }
//            } else {
//                PerSonWorkResponse.DataBean dataBean = new PerSonWorkResponse.DataBean();
//                dataBean.setId("-111");
//
//                if (timeList != null && timeList.size() > 0) {
//                    TimeDataCache timeDataCache = timeList.get(timeList.size() - 1);
//                    dataBean.setVideo_cover(timeDataCache.getM_clipInfoArray().get(0).getFilePath());
//                }
//                response.getData().add(0, dataBean);
//            }
//
//            hasAdd = true;
//        }


        if (response.getData().size() > 0) {
            rl_nodata.setVisibility(View.GONE);
            refresh_view.setVisibility(View.VISIBLE);
            if (isRefresh) {
                userVideoAdapter.clearDatas();
            }
            userVideoAdapter.addData(response.getData());
        } else {
            if (isRefresh) {
                rl_nodata.setVisibility(View.VISIBLE);
                refresh_view.setVisibility(View.GONE);
                if (duty_type != -1 && duty_type != 0) {
                    img_nodata.setImageResource(R.mipmap.usernovideo);
                    tv_nodata.setText(R.string.no_follow_video_data_list);
                } else {
//                    EventBus.getDefault().post(new OpenMainShort(1));
                    img_nodata.setImageResource(R.mipmap.usernovideo);
                    tv_nodata.setText(R.string.no_follow_video_data_list);
                }
            }
        }
    }

    List<PersonLikeResponse.DataBean> listLikeResponse = new ArrayList<>();

    @Override
    public void setPersonLikesData(PersonLikeResponse response, int selectType, boolean isRefresh) {
        if (isRefresh) {
            listLikeResponse.clear();
            listLikeResponse.addAll(response.getData());
        } else {
            listLikeResponse.addAll(response.getData());
        }
        if (response.getData().size() > 0) {
            rl_nodata.setVisibility(View.GONE);
            refresh_view.setVisibility(View.VISIBLE);
            if (isRefresh) {
                userLikeAdapter.clearDatas();
            }
            userLikeAdapter.addData(response.getData());
        } else {
            if (isRefresh) {
                rl_nodata.setVisibility(View.VISIBLE);
                refresh_view.setVisibility(View.GONE);
                if (duty_type != -1 && duty_type != 0) {
                    img_nodata.setImageResource(R.drawable.nodatastate);
                    tv_nodata.setText(R.string.no_follow_data_list);
                } else {
                    img_nodata.setImageResource(R.drawable.nodatastate);
                    tv_nodata.setText(R.string.no_follow_data_list);
                }
            }
        }
    }

    @Override
    public void getAliNewData(AliVideoResponse aliVideoResponse) {
        MyNews myNews = new MyNews();
        myNews.setTitle(aliVideoResponse.getData().getTitle());
        myNews.setLikeCount(Integer.parseInt(aliVideoResponse.getData().getLike_count()));
        myNews.setLike(aliVideoResponse.getData().isIs_liked());
        myNews.setCoverUrl(aliVideoResponse.getData().getVideo_cover());
        myNews.setVideoUrl(aliVideoResponse.getData().getVideo_url());
        myNews.setDu_type("1");
        myNews.setOtherId(user_id);
        myNews.setUserName(aliVideoResponse.getData().getUser_nickname());
        myNews.setVideo_id(aliVideoResponse.getData().getId());
        myNews.setUserName(aliVideoResponse.getData().getUser_nickname());
        myNews.setUserIcon(aliVideoResponse.getData().getUser_avatar());
        myNews.setCommentCount(Integer.parseInt(aliVideoResponse.getData().getComment_count()));
        FollwVideoPlayActivity.toThis(mContext, myNews, 0, 0, 2);
    }

    @Override
    public void hideRefresh() {
        if (refresh_view != null) refresh_view.finishRefresh();
    }

    @Override
    public void hideLoadMore(boolean isHide) {
        if (refresh_view != null) refresh_view.finishLoadmore();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefeshHome(VideoLikeEvent event) {
        if (mHomeTab.getTabType() == Common.VIDEO) {
            userVideoAdapter.getItem(video_position).setLike_count(event.getmData().getLikeCount());
            userVideoAdapter.getItem(video_position).setIs_up(event.getmData().isLike());
            userVideoAdapter.getItem(video_position).setComment_count(event.getmData().getCommentCount());
            userVideoAdapter.notifyDataSetChanged();
        } else if (mHomeTab.getTabType() == Common.LIKE) {
            userLikeAdapter.getItem(video_position).setLike_count(event.getmData().getLikeCount() + "");
            userLikeAdapter.getItem(video_position).setIs_up(event.getmData().isLike());
            userLikeAdapter.getItem(video_position).setComment_count(event.getmData().getCommentCount() + "");
            userLikeAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe
    public void refresh(String str) {

        if (Common.REFRESH_VIDEO.equals(str)) {
            VideoViewManager.instance().releaseVideoPlayer();
            isRefresh = true;
            PAGE = 1;
            hasAdd = false;
            getVideoList(true);
            //  getDrafts();

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
        refresh_view.finishRefresh();
    }


}
