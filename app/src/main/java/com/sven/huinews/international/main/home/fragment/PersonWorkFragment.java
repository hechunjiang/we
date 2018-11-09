package com.sven.huinews.international.main.home.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facebook.drawee.view.SimpleDraweeView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseFragment;
import com.sven.huinews.international.config.Constant;
import com.sven.huinews.international.entity.MyNews;
import com.sven.huinews.international.entity.requst.LikesRequest;
import com.sven.huinews.international.entity.requst.PersonWorkRequest;
import com.sven.huinews.international.entity.response.AliVideoResponse;
import com.sven.huinews.international.entity.response.PerSonWorkResponse;
import com.sven.huinews.international.entity.response.PersonLikeResponse;
import com.sven.huinews.international.entity.response.UserDatasResponse;
import com.sven.huinews.international.main.fansandfollow.activity.FansAndFollowActivity;
import com.sven.huinews.international.main.follow.activity.FollwVideoPlayActivity;
import com.sven.huinews.international.main.home.contract.PersonWorkContract;
import com.sven.huinews.international.main.home.model.PersonWorkModel;
import com.sven.huinews.international.main.home.presenter.PersonWorkPresenter;
import com.sven.huinews.international.main.userdetail.adapter.UserDetailsAdapter;
import com.sven.huinews.international.main.userdetail.adapter.UserLikeAdapter;
import com.sven.huinews.international.utils.Common;
import com.sven.huinews.international.utils.LogUtil;
import com.sven.huinews.international.utils.CommonUtils;
import com.sven.huinews.international.utils.LogUtil;
import com.sven.huinews.international.utils.ToastUtils;
import com.sven.huinews.international.view.GirdItemDecoration;
import com.sven.huinews.international.view.MyRefreshLayout;
import com.sven.huinews.international.view.SlidingSuspensionScrollView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class PersonWorkFragment extends BaseFragment<PersonWorkPresenter, PersonWorkModel> implements PersonWorkContract.View, SlidingSuspensionScrollView.OnScrollListener {

    private SimpleDraweeView ivUserHead;
    private TextView tvFansSize, tvFfollowSize, tvHeartsSize, tvName, tvContent, tv_videos, tv_likes, tv_nodata, tv_likess, tv_videoss, perAge;
    private LinearLayout llVideos, llLikes, Location, ll_toup_suspension, ll_top_output, ll_user, ll_videoss, ll_likess;
    private RecyclerView personalWorksVideorv;
    private RelativeLayout rlNodata;
    private ImageButton btnBack;
    private MyRefreshLayout videosListRefresh;
    private SlidingSuspensionScrollView slVideosLikes;

    private ImageView imgSex, img_videos, img_likes, img_videoss, img_likess, img_nodata;
    private RelativeLayout rl_view;

    private int duty_type;
    private String Other_id;
    private int loadType = 0;//0 打开页面时  1加载videos  2加载likes
    private int page = 1;//当前页数
    private GirdItemDecoration girdItemDecoration;
    BaseQuickAdapter mAdapter;
    private UserLikeAdapter userLikeAdapter;
    private String user_id;

    @Override
    protected int getLayoutResource() {
        EventBus.getDefault().register(this);
        return R.layout.fragment_person_work;
    }


    @Override
    protected void initView(View v) {
        ivUserHead = v.findViewById(R.id.iv_user_head);
        tvFansSize = v.findViewById(R.id.tv_fans_size);
        tvFfollowSize = v.findViewById(R.id.tv_follow_size);
        tvHeartsSize = v.findViewById(R.id.tv_hearts_size);
        tvName = v.findViewById(R.id.nickName);
        perAge = v.findViewById(R.id.perAge);
        tvContent = v.findViewById(R.id.perSign);
        tv_videos = v.findViewById(R.id.tv_videos);
        tv_likes = v.findViewById(R.id.tv_likes);
        tv_likess = v.findViewById(R.id.tv_likess);
        tv_nodata = v.findViewById(R.id.tv_nodata);
        tv_videoss = v.findViewById(R.id.tv_videoss);
        ll_likess = v.findViewById(R.id.ll_likess);

        llLikes = v.findViewById(R.id.ll_likes);
        llVideos = v.findViewById(R.id.ll_videos);
        ll_videoss = v.findViewById(R.id.ll_videoss);
        ll_toup_suspension = v.findViewById(R.id.ll_toup_suspension);
        ll_top_output = v.findViewById(R.id.ll_top_output);
        ll_user = v.findViewById(R.id.ll_user);

        imgSex = v.findViewById(R.id.img_sex);
        img_videos = v.findViewById(R.id.img_videos);
        img_likes = v.findViewById(R.id.img_likes);
        img_videoss = v.findViewById(R.id.img_videoss);
        img_likess = v.findViewById(R.id.img_likess);
        img_nodata = v.findViewById(R.id.img_nodata);

        personalWorksVideorv = v.findViewById(R.id.personal_works_video_rv);
        rl_view = v.findViewById(R.id.rl_view);
        rlNodata = v.findViewById(R.id.rl_nodata);

        btnBack = v.findViewById(R.id.btn_back);
        videosListRefresh = v.findViewById(R.id.videos_list_refresh);
        slVideosLikes = v.findViewById(R.id.sl_videos_likes);
        slVideosLikes.setOnScrollListener(this);
        setRecyclerViewManager();
        videosListRefresh.setEnableLoadmore(false);

        videosListRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 1;
                switch (loadType) {
                    case 0://加载all
                        //打开页面即加载用户信息和列表
                        mPresenter.getUserDetailsDataInfo(true);
//                        mPresenter.getUserDetailsInfo(true, Constant.SELECT_USER_VIDEOS);
                        break;
                    case 1://加载Videos
//                        mPresenter.getUserDetailsInfo(true, Constant.SELECT_USER_VIDEOS);
                        break;
                    case 2://加载likes
//                        mPresenter.getUserDetailsLikesInfo(true, Constant.SELECT_USER_LIKE_VIDEOS);
                        break;
                }
            }
        });

        videosListRefresh.setEnableLoadmoreWhenContentNotFull(false);
        videosListRefresh.setEnableNestedScroll(true);
        videosListRefresh.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page++;
                switch (loadType) {
                    case 0://加载all
                        //打开页面即加载用户信息和列表
                        mPresenter.getUserDetailsDataInfo(true);
//                        mPresenter.getUserDetailsInfo(true, Constant.SELECT_USER_VIDEOS);
                        break;
                    case 1://加载Videos
//                        mPresenter.getUserDetailsInfo(true, Constant.SELECT_USER_VIDEOS);
                        break;
                    case 2://加载likes
//                        mPresenter.getUserDetailsLikesInfo(true, Constant.SELECT_USER_LIKE_VIDEOS);
                        break;
                }
            }
        });


        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                PerSonWorkResponse.DataBean item = (PerSonWorkResponse.DataBean) adapter.getItem(position);
                user_id = item.getUser_id() + "";
                mPresenter.getAliPlayUrl(item.getId());
            }
        });

    }

    private void setRecyclerViewManager() {
        mAdapter = new UserDetailsAdapter(R.layout.item_videos_videos);
        userLikeAdapter = new UserLikeAdapter(R.layout.item_videos_videos);
        personalWorksVideorv.setItemAnimator(null);
        personalWorksVideorv.setPadding(0, 0, 0, 0);

        if (girdItemDecoration == null) {
            girdItemDecoration = new GirdItemDecoration(mContext);
        }
        personalWorksVideorv.removeItemDecoration(girdItemDecoration);
        personalWorksVideorv.addItemDecoration(girdItemDecoration);
        //设置layoutManager之前必须移除之前的分割线
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        personalWorksVideorv.setLayoutManager(gridLayoutManager);
        personalWorksVideorv.setAdapter(mAdapter);
    }

    @Override
    public void initObject() {
        setMVP();
        videosListRefresh.autoRefresh();

    }

    @Override
    protected void loadData() {

    }


    @Override
    public void initEvents() {
        llVideos.setOnClickListener(this);
        llLikes.setOnClickListener(this);
        ll_videoss.setOnClickListener(this);
        ll_likess.setOnClickListener(this);


        tvFansSize.setOnClickListener(this);
        tvFfollowSize.setOnClickListener(this);
        tvHeartsSize.setOnClickListener(this);

    }

    @Override
    public void OnClickEvents(View v) {
        if (v == tvFansSize) {
            Bundle bundle = new Bundle();
            bundle.putString("du_type", duty_type + "");
            bundle.putString("other_id", Other_id);
            bundle.putString("load_type", "fans");
            startActivity(FansAndFollowActivity.class, bundle);
        } else if (v == tvFfollowSize) {
            Bundle bundle = new Bundle();
            bundle.putString("du_type", duty_type + "");
            bundle.putString("other_id", Other_id);
            bundle.putString("load_type", "follow");
            startActivity(FansAndFollowActivity.class, bundle);
        } else if (v == llVideos) {
            if (!videosListRefresh.getState().opening) {
                personalWorksVideorv.setAdapter(null);
                personalWorksVideorv.setAdapter(mAdapter);
                reduction();
                img_videos.setImageResource(R.mipmap.videos_img);
                tv_videos.setTextColor(Color.parseColor("#6FB5C5"));
                img_videoss.setImageResource(R.mipmap.videos_img);
                tv_videoss.setTextColor(Color.parseColor("#6FB5C5"));
                if (mPresenter != null) {
                    if (videosListRefresh != null) videosListRefresh.autoRefresh();
                }
                loadType = 1;
                ll_toup_suspension.setVisibility(View.GONE);
            }
        } else if (v == ll_videoss) {
            if (!videosListRefresh.getState().opening) {
                reduction();
                img_videos.setImageResource(R.mipmap.videos_img);
                tv_videos.setTextColor(Color.parseColor("#6FB5C5"));
                img_videoss.setImageResource(R.mipmap.videos_img);
                tv_videoss.setTextColor(Color.parseColor("#6FB5C5"));
                if (mPresenter != null) {
                    if (videosListRefresh != null) videosListRefresh.autoRefresh();
                }
                loadType = 1;
                ll_toup_suspension.setVisibility(View.GONE);
            }
        } else if (v == llLikes) {
            if (!videosListRefresh.getState().opening) {
                personalWorksVideorv.setAdapter(null);
                personalWorksVideorv.setAdapter(userLikeAdapter);
                reduction();
                img_likes.setImageResource(R.mipmap.like);
                tv_likes.setTextColor(Color.parseColor("#6FB5C5"));
                img_likess.setImageResource(R.mipmap.like);
                tv_likess.setTextColor(Color.parseColor("#6FB5C5"));
                if (mPresenter != null) {
                    if (videosListRefresh != null) videosListRefresh.autoRefresh();
                }
                loadType = 2;
                ll_toup_suspension.setVisibility(View.GONE);
            }
        } else if (v == ll_likess) {
            if (!videosListRefresh.getState().opening) {
                reduction();
                img_videos.setImageResource(R.mipmap.videos_img);
                tv_videos.setTextColor(Color.parseColor("#6FB5C5"));
                img_videoss.setImageResource(R.mipmap.videos_img);
                tv_videoss.setTextColor(Color.parseColor("#6FB5C5"));
                if (mPresenter != null) {
                    if (videosListRefresh != null) videosListRefresh.autoRefresh();
                }
                loadType = 2;
                ll_toup_suspension.setVisibility(View.GONE);
            }
        }

    }

    public void reduction() {
        img_videos.setImageResource(R.mipmap.videos_img_un);
        img_likes.setImageResource(R.mipmap.like_un);
        tv_videos.setTextColor(Color.parseColor("#333333"));
        tv_likes.setTextColor(Color.parseColor("#333333"));
        img_videoss.setImageResource(R.mipmap.videos_img_un);
        img_likess.setImageResource(R.mipmap.like_un);
        tv_videoss.setTextColor(Color.parseColor("#333333"));
        tv_likess.setTextColor(Color.parseColor("#333333"));
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
    public void setPersonalWorksData(PerSonWorkResponse response, Boolean isRefresh, Boolean isLoadMore, int selectType) {
        int height = rl_view.getHeight() - ll_top_output.getHeight() - ll_user.getHeight();
        if (videosListRefresh != null) {
            videosListRefresh.finishRefresh();
            videosListRefresh.finishLoadmore();
        }
        if (response.getData().size() > 0) {
            int itemsizeheight = CommonUtils.dip2px(getActivity(), (int) (response.getData().size() % 3!=0?response.getData().size() / 3+1:response.getData().size() / 3) * 153);
            if (itemsizeheight >= height) {
                personalWorksVideorv.getLayoutParams().height = itemsizeheight+30;
            } else {
                personalWorksVideorv.getLayoutParams().height = height+30;
            }
            if (isRefresh) {
                ((UserDetailsAdapter) mAdapter).clearDatas();
            }
            mAdapter.addData(response.getData());
            personalWorksVideorv.setVisibility(View.VISIBLE);
            rlNodata.setVisibility(View.GONE);

        } else {
            if (isRefresh) {
                personalWorksVideorv.setVisibility(View.GONE);
                rlNodata.setVisibility(View.VISIBLE);
                if (selectType == Constant.SELECT_USER_VIDEOS) {
                    img_nodata.setImageResource(R.mipmap.usernovideo);
                    tv_nodata.setText(R.string.no_follow_video_data_list);
                } else if (selectType == Constant.SELECT_USER_LIKE_VIDEOS) {
                    img_nodata.setImageResource(R.drawable.nodatastate);
                    tv_nodata.setText(R.string.no_follow_data_list);
                }
            } else {
                ToastUtils.showShort(mContext, R.string.no_more_datas);
            }
        }
    }




    @Override
    public void setPersonLikesData(PersonLikeResponse response, int selectType,boolean isRefresh) {
        int height = rl_view.getHeight() - ll_top_output.getHeight() - ll_user.getHeight();

        if (videosListRefresh != null) {
            videosListRefresh.finishRefresh();
            videosListRefresh.finishLoadmore();
        }

        if (response.getData().size() > 0) {
            int itemsizeheight = CommonUtils.dip2px(getActivity(), (int) (response.getData().size() % 3 != 0 ? response.getData().size() / 3 + 1 : response.getData().size() / 3) * 153);
            if (itemsizeheight >= height) {
                personalWorksVideorv.getLayoutParams().height = itemsizeheight + 30;
            } else {
                personalWorksVideorv.getLayoutParams().height = height + 30;
            }
            if (isRefresh) {
                userLikeAdapter.clearDatas();
            }
            userLikeAdapter.addData(response.getData());
            personalWorksVideorv.setVisibility(View.VISIBLE);
            rlNodata.setVisibility(View.GONE);
        } else {
            if (isRefresh) {
                personalWorksVideorv.setVisibility(View.GONE);
                rlNodata.setVisibility(View.VISIBLE);
                if (selectType == Constant.SELECT_USER_VIDEOS) {
                    img_nodata.setImageResource(R.mipmap.usernovideo);
                    tv_nodata.setText(R.string.no_follow_video_data_list);
                } else if (selectType == Constant.SELECT_USER_LIKE_VIDEOS) {
                    img_nodata.setImageResource(R.drawable.nodatastate);
                    tv_nodata.setText(R.string.no_follow_data_list);
                }
            } else {
                ToastUtils.showShort(mContext, R.string.no_more_datas);
            }
        }




        userLikeAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                PersonLikeResponse.DataBean item = (PersonLikeResponse.DataBean) adapter.getItem(position);
                //判断是否为虚拟用户 1真实用户 2为虚拟用户  真实用户则请求阿里云数据
                if (item.getDu_type().equals("2")) {
                    MyNews myNews = new MyNews();
                    myNews.setTitle(item.getTitle());
                    myNews.setLikeCount(Integer.parseInt(item.getLike_count() + ""));
                    myNews.setCoverUrl(item.getVideo_cover());
                    myNews.setVideoUrl(item.getVideo_url());
                    myNews.setUserName(item.getUser_nickname());
                    myNews.setVideo_id(item.getId());
                    FollwVideoPlayActivity.toThis(mContext, myNews, 0, 0, 2);
                } else {
                    mPresenter.getAliPlayUrl(item.getAliyun_id());
                }
            }
        });

    }

    @Override
    public void setUserInfo(UserDatasResponse userInfo) {
        duty_type = userInfo.getData().getDu_type();
        Other_id = userInfo.getData().getUser_id();
        ivUserHead.setImageURI(userInfo.getData().getUserAvatar());
        tvName.setText(userInfo.getData().getNickname() + "");
        tvContent.setText(userInfo.getData().getSignature().equals("") ? getString(R.string.no_bio_yet) : userInfo.getData().getSignature());
//        perAge.setText(userInfo.getData().getBirthday().equals("") ? "CHINA 18" : "CHINA " + userInfo.getData().getBirthday());
        if (userInfo.getData().getSex() == 1) {
            imgSex.setImageResource(R.mipmap.girl);
        } else {
            imgSex.setImageResource(R.mipmap.man);
        }
        tvFansSize.setText(getString(R.string.fans) + " " + userInfo.getData().getFansNum() + "");
        tvFfollowSize.setText(getString(R.string.follow) + " " + userInfo.getData().getFollowNum() + "");
        tvHeartsSize.setText(getString(R.string.hearts) + " " + userInfo.getData().getLikeCount());
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
        if (videosListRefresh != null) videosListRefresh.finishRefresh();
    }

    @Override
    public void hideLoadMore(boolean isHide) {
        if (videosListRefresh != null) videosListRefresh.finishRefresh();
    }

    @Override
    public void onScroll(int scrollY) {
        int topheight = (ll_top_output.getHeight() + ll_user.getHeight());

        if (scrollY >= topheight) {
            ll_toup_suspension.setVisibility(View.VISIBLE);
        } else {
            ll_toup_suspension.setVisibility(View.GONE);
        }
    }

    @Subscribe
    public void onEvents(String str) {
        if (str.equals(Common.REFRESH_VIDEO)) {
            if (loadType == 0 || loadType == 1) {
                LogUtil.showLog("msg-----str：" + Common.REFRESH_VIDEO);
//                mPresenter.getUserDetailsInfo(true, Constant.SELECT_USER_VIDEOS);
            }
        }
    }


    public PersonWorkRequest getPersonWorkRequest() {
        PersonWorkRequest request = new PersonWorkRequest();
        request.setPage_size(LIMIT + "");
        request.setPage(page + "");
        return request;
    }

    public LikesRequest getLikeRequest() {
        LikesRequest request = new LikesRequest();
        request.setPage_size(LIMIT + "");
        request.setPage(page + "");
        return request;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
