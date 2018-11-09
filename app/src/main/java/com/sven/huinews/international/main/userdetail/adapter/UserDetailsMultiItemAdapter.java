package com.sven.huinews.international.main.userdetail.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dueeeke.videoplayer.player.IjkVideoView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sven.huinews.international.R;
import com.sven.huinews.international.entity.MyNews;
import com.sven.huinews.international.entity.NewsInfo;
import com.sven.huinews.international.entity.UserDetails;
import com.sven.huinews.international.entity.requst.FollowsRequest;
import com.sven.huinews.international.entity.response.PerSonWorkResponse;
import com.sven.huinews.international.entity.response.PersonLikeResponse;
import com.sven.huinews.international.entity.response.UserDatasResponse;
import com.sven.huinews.international.main.follow.activity.FollwVideoPlayActivity;
import com.sven.huinews.international.main.home.adapter.HomeVVideoAdapter;
import com.sven.huinews.international.utils.CommonUtils;
import com.sven.huinews.international.utils.LogUtil;
import com.sven.huinews.international.view.GirdItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Burgess on 2018/9/28 0028.
 */
public class UserDetailsMultiItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private UserDetailsAdapter mUserDetailsAdapter;
    private UserLikeAdapter mLikeAdapter;
    private List<Map<String, Object>> list;
    public static final int USER_DATA = 0;
    public static final int USER_VIDEO = 1;
    public static final int USER_LIKE = 2;
    public static final int NO_DATA = 3;
    public static final int ALL_NO_DATA = 4;
    public static final int USER_DATAS = 5;
    private GirdItemDecoration girdItemDecoration;


    public UserDetailsMultiItemAdapter(Context mContext, List<Map<String, Object>> list) {
        this.list = list;
        this.mContext = mContext;
    }

    public List<Map<String, Object>> getList() {
        return list;
    }

    public void setList(List<Map<String, Object>> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void dataClear() {
        this.list = new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {//USER_DATAS
        if (Integer.parseInt(list.get(position).get("type").toString()) == ALL_NO_DATA) {
            return ALL_NO_DATA;
        } else if (Integer.parseInt(list.get(position).get("type").toString()) == USER_DATA) {
            return USER_DATA;
        }else if (Integer.parseInt(list.get(position).get("type").toString()) == USER_DATAS) {
            return USER_DATAS;
        } else if (Integer.parseInt(list.get(position).get("type").toString()) == USER_VIDEO) {
            return USER_VIDEO;
        } else if (Integer.parseInt(list.get(position).get("type").toString()) == USER_LIKE) {
            return USER_LIKE;
        } else if (Integer.parseInt(list.get(position).get("type").toString()) == NO_DATA) {
            return NO_DATA;
        } else {
            return super.getItemViewType(position);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == ALL_NO_DATA) {
            return null;
        } else if (viewType == USER_DATA) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_data, parent, false);
            return new UserDataViewHolder(view);
        }else if (viewType == USER_DATAS) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_data, parent, false);
            return new UserDataViewHolderS(view);
        } else if (viewType == USER_VIDEO) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_videos_videos, parent, false);
            return new UserVideoViewHolder(view);
        } else if (viewType == USER_LIKE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_videos_videos, parent, false);
            return new UserLikeViewHolder(view);
        } else if (viewType == NO_DATA) {
            return null;
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof UserDataViewHolder) {
            final UserDataViewHolder viewHolder = (UserDataViewHolder) holder;
            final UserDatasResponse userInfo = (UserDatasResponse) list.get(position).get("Data");
            viewHolder.ivUserHead.setImageURI(Uri.parse(userInfo.getData().getUserAvatar()));
            viewHolder.tvName.setText(userInfo.getData().getNickname());
            viewHolder.tvContent.setText(userInfo.getData().getSignature().equals("") ? mContext.getString(R.string.def_signature) : userInfo.getData().getSignature());
            if (userInfo.getData().getSex() == 1) {
                viewHolder.imgSex.setImageResource(R.mipmap.girl);
            } else {
                viewHolder.imgSex.setImageResource(R.mipmap.man);
            }
            viewHolder.tvFansSize.setText(mContext.getString(R.string.fans) + " " + CommonUtils.getLikeCount(userInfo.getData().getFansNum()));
            viewHolder.tvFfollowSize.setText(mContext.getString(R.string.follow) + " " + CommonUtils.getLikeCount(userInfo.getData().getFollowNum()));
            viewHolder.tvHeartsSize.setText(mContext.getString(R.string.hearts) + " " + CommonUtils.getLikeCount(userInfo.getData().getGetLike()));
            final boolean isFOLLOW;
            if (userInfo.getData().isIsFollow()) {
                viewHolder.tvFollow.setBackgroundResource(R.drawable.user_follow_abolish);
                viewHolder.tvFollow.setText(mContext.getString(R.string.followed) + "");
                isFOLLOW = true;
            } else {
                viewHolder.tvFollow.setBackgroundResource(R.drawable.user_follow);
                viewHolder.tvFollow.setText("+ " + mContext.getString(R.string.follow) + "");
                isFOLLOW = false;
            }

            viewHolder.tvFansSize.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnUserDataLisenter.fansSize();
                }
            });

            viewHolder.tvFfollowSize.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnUserDataLisenter.followSize();
                }
            });

            ((LinearLayout) viewHolder.ll_videos).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnUserDataLisenter.clickVideo();
                    reduction(viewHolder);
                    clickVideoAndLike(viewHolder,1);
                    userInfo.getData().setLoadType(1);
                    Map<String, Object> map = new HashMap<>();
                    map.put("type", UserDetailsMultiItemAdapter.USER_DATA);
                    map.put("Data", userInfo);
                        if (null != list.get(0)) {
                            list.remove(0);
                        }
                    list.add(0, map);
                }
            });

            ((LinearLayout) viewHolder.ll_likes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnUserDataLisenter.clickLike();
                    reduction(viewHolder);
                    clickVideoAndLike(viewHolder,2);
                    userInfo.getData().setLoadType(2);
                    Map<String, Object> map = new HashMap<>();
                    map.put("type", UserDetailsMultiItemAdapter.USER_DATA);
                    map.put("Data", userInfo);
                    if (null != list.get(0)) {
                        list.remove(0);
                    }
                    list.add(0, map);
                }
            });

            viewHolder.tvFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isFOLLOW) {
                        viewHolder.tvFollow.setBackgroundResource(R.drawable.user_follow);
                        viewHolder.tvFollow.setText("+ " + mContext.getString(R.string.follow) + "");
                    } else {
                        viewHolder.tvFollow.setBackgroundResource(R.drawable.user_follow_abolish);
                        viewHolder.tvFollow.setText(mContext.getString(R.string.followed) + "");
                    }
                    mOnUserDataLisenter.follows();

                }
            });
            //头部高度
            mOnUserDataLisenter.userHandle(viewHolder.ll_user.getHeight()+viewHolder.ll_user_fans.getHeight()+viewHolder.vv_line.getHeight());
            reduction(viewHolder);
            if (userInfo.getData().getLoadType()==1){
                viewHolder.img_videos.setImageResource(R.mipmap.videos_img);
                viewHolder.tv_videos.setTextColor(Color.parseColor("#6FB5C5"));
            }else{
                viewHolder.img_likes.setImageResource(R.mipmap.like);
                viewHolder.tv_likes.setTextColor(Color.parseColor("#6FB5C5"));
            }
        } else if (holder instanceof UserVideoViewHolder) {
            UserVideoViewHolder viewHolder = (UserVideoViewHolder) holder;

            viewHolder.rv_video_like_l.setVisibility(View.GONE);
            viewHolder.rv_video_like_v.setVisibility(View.VISIBLE);

            PerSonWorkResponse response = (PerSonWorkResponse) list.get(position).get("Data");
            mUserDetailsAdapter = new UserDetailsAdapter(R.layout.item_videos_videos);
            mUserDetailsAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    PerSonWorkResponse.DataBean item = (PerSonWorkResponse.DataBean) adapter.getItem(position);
                    if (item.getDu_type() != 1) {
                        MyNews myNews = new MyNews();
                        myNews.setTitle(item.getTitle());
                        myNews.setLikeCount(Integer.parseInt(item.getLike_count() + ""));
                        myNews.setCoverUrl(item.getVideo_cover());
                        myNews.setVideoUrl(item.getVideo_url());
                        myNews.setUserName(item.getUser_nickname());
                        myNews.setVideo_id(item.getId());
                        myNews.setDu_type(item.getDu_type() + "");
                        myNews.setOtherId(item.getUser_id()+"");
                        myNews.setUserIcon(item.getUser_avatar());
                        myNews.setShareCount(Integer.parseInt(item.getShare_count()+""));
                        myNews.setCommentCount(Integer.parseInt(item.getComment_count()+""));
                        FollwVideoPlayActivity.toThis(mContext, myNews, 0, 0, 2);
                    }else{
                        mOnUserDataLisenter.aliyunvideo(item.getId(), item);
                    }
                }
            });

            viewHolder.rv_video_like_v.setItemAnimator(null);
            viewHolder.rv_video_like_v.setPadding(0, 0, 0, 0);

            if (girdItemDecoration == null) {
                girdItemDecoration = new GirdItemDecoration(mContext);
            }
            viewHolder.rv_video_like_v.removeItemDecoration(girdItemDecoration);
            viewHolder.rv_video_like_v.addItemDecoration(girdItemDecoration);
            //设置layoutManager之前必须移除之前的分割线
            final GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            viewHolder.rv_video_like_v.setLayoutManager(gridLayoutManager);
            viewHolder.rv_video_like_v.setAdapter(mUserDetailsAdapter);
            mUserDetailsAdapter.clearDatas();
            if (null != response.getData()) {
                mUserDetailsAdapter.addData(response.getData());
            } else {
                mUserDetailsAdapter.clearDatas();
            }
        } else if (holder instanceof UserLikeViewHolder) {
            UserLikeViewHolder viewHolder = (UserLikeViewHolder) holder;
            viewHolder.rv_video_like_l.setVisibility(View.VISIBLE);
            viewHolder.rv_video_like_v.setVisibility(View.GONE);
            PersonLikeResponse response = (PersonLikeResponse) list.get(position).get("Data");
            mLikeAdapter = new UserLikeAdapter(R.layout.item_videos_videos);
            mLikeAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    PersonLikeResponse.DataBean item = (PersonLikeResponse.DataBean) adapter.getItem(position);
                    LogUtil.showLog("msg-----item:" + item.getVideo_cover());
                    MyNews myNews = new MyNews();
                    myNews.setTitle(item.getTitle());
                    myNews.setLikeCount(Integer.parseInt(item.getLike_count() + ""));
                    myNews.setCoverUrl(item.getVideo_cover());
                    myNews.setVideoUrl(item.getVideo_url());
                    myNews.setUserName(item.getUser_nickname());
                    myNews.setVideo_id(item.getId());
                    myNews.setDu_type(item.getDu_type() + "");
                    myNews.setOtherId(item.getUser_id());
                    myNews.setUserIcon(item.getUser_avatar());
                    myNews.setShareCount(Integer.parseInt(item.getShare_count()+""));
                    myNews.setCommentCount(Integer.parseInt(item.getComment_count()+""));
                    FollwVideoPlayActivity.toThis(mContext, myNews, 0, 0, 2);
                }
            });

            viewHolder.rv_video_like_l.setItemAnimator(null);
            viewHolder.rv_video_like_l.setPadding(0, 0, 0, 0);

            if (girdItemDecoration == null) {
                girdItemDecoration = new GirdItemDecoration(mContext);
            }
            viewHolder.rv_video_like_l.removeItemDecoration(girdItemDecoration);
            viewHolder.rv_video_like_l.addItemDecoration(girdItemDecoration);
            //设置layoutManager之前必须移除之前的分割线
            final GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            viewHolder.rv_video_like_l.setLayoutManager(gridLayoutManager);
            viewHolder.rv_video_like_l.setAdapter(mLikeAdapter);
            mLikeAdapter.clearDatas();
            if (null != response.getData()) {
                mLikeAdapter.addData(response.getData());
            } else {
                mLikeAdapter.clearDatas();
            }
        }else if(holder instanceof UserDataViewHolderS){
            final UserDataViewHolderS viewHolder = (UserDataViewHolderS) holder;
            final UserDatasResponse userInfo = (UserDatasResponse) list.get(position).get("Data");
            viewHolder.ivUserHead.setImageURI(Uri.parse(userInfo.getData().getUserAvatar()));
            viewHolder.tvName.setText(userInfo.getData().getNickname());
            viewHolder.tvContent.setText(userInfo.getData().getSignature().equals("") ? mContext.getString(R.string.def_signature) : userInfo.getData().getSignature());

            if (userInfo.getData().getSex() == 1) {
                viewHolder.imgSex.setImageResource(R.mipmap.girl);
            } else {
                viewHolder.imgSex.setImageResource(R.mipmap.man);
            }
            viewHolder.tvFansSize.setText(mContext.getString(R.string.fans) + " " + CommonUtils.getLikeCount(userInfo.getData().getFansNum()));
            viewHolder.tvFfollowSize.setText(mContext.getString(R.string.follow) + " " + CommonUtils.getLikeCount(userInfo.getData().getFollowNum()));
            viewHolder.tvHeartsSize.setText(mContext.getString(R.string.hearts) + " " + CommonUtils.getLikeCount(userInfo.getData().getGetLike()));
            final boolean isFOLLOW;
            if (userInfo.getData().isIsFollow()) {
                viewHolder.tvFollow.setBackgroundResource(R.drawable.user_follow_abolish);
                viewHolder.tvFollow.setText(mContext.getString(R.string.followed) + "");
                isFOLLOW = true;
            } else {
                viewHolder.tvFollow.setBackgroundResource(R.drawable.user_follow);
                viewHolder.tvFollow.setText("+ " + mContext.getString(R.string.follow) + "");
                isFOLLOW = false;
            }

            viewHolder.tvFansSize.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnUserDataLisenter.fansSize();
                }
            });

            viewHolder.tvFfollowSize.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnUserDataLisenter.followSize();
                }
            });

            ((LinearLayout) viewHolder.ll_videos).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnUserDataLisenter.clickVideo();
                    reduction(viewHolder);
                    viewHolder.img_videos.setImageResource(R.mipmap.videos_img);
                    viewHolder.tv_videos.setTextColor(Color.parseColor("#6FB5C5"));
                    userInfo.getData().setLoadType(1);
                    Map<String, Object> map = new HashMap<>();
                    map.put("type", UserDetailsMultiItemAdapter.USER_DATAS);
                    map.put("Data", userInfo);
                    if (null != list.get(0)) {
                        list.remove(0);
                    }
                    list.add(0, map);

                }
            });

            ((LinearLayout) viewHolder.ll_likes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnUserDataLisenter.clickLike();
                    reduction(viewHolder);
                    viewHolder.img_likes.setImageResource(R.mipmap.like);
                    viewHolder.tv_likes.setTextColor(Color.parseColor("#6FB5C5"));
                    userInfo.getData().setLoadType(2);
                    Map<String, Object> map = new HashMap<>();
                    map.put("type", UserDetailsMultiItemAdapter.USER_DATAS);
                    map.put("Data", userInfo);
                    if (null != list.get(0)) {
                        list.remove(0);
                    }
                    list.add(0, map);
                }
            });

            viewHolder.tvFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isFOLLOW) {
                        viewHolder.tvFollow.setBackgroundResource(R.drawable.user_follow);
                        viewHolder.tvFollow.setText("+ " + mContext.getString(R.string.follow) + "");
                    } else {
                        viewHolder.tvFollow.setBackgroundResource(R.drawable.user_follow_abolish);
                        viewHolder.tvFollow.setText(mContext.getString(R.string.followed) + "");
                    }
                    mOnUserDataLisenter.follows();

                }
            });

            //头部高度
            mOnUserDataLisenter.userHandle(viewHolder.ll_user.getHeight()+viewHolder.ll_user_fans.getHeight()+viewHolder.vv_line.getHeight());
            reduction(viewHolder);
            if (userInfo.getData().getLoadType()==1){
                viewHolder.img_videos.setImageResource(R.mipmap.videos_img);
                viewHolder.tv_videos.setTextColor(Color.parseColor("#6FB5C5"));
            }else{
                viewHolder.img_likes.setImageResource(R.mipmap.like);
                viewHolder.tv_likes.setTextColor(Color.parseColor("#6FB5C5"));
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //    还原
    public void reduction(UserDataViewHolder viewHolder) {
        viewHolder.img_videos.setImageResource(R.mipmap.videos_img_un);
        viewHolder.img_likes.setImageResource(R.mipmap.like_un);
        viewHolder.tv_videos.setTextColor(Color.parseColor("#333333"));
        viewHolder.tv_likes.setTextColor(Color.parseColor("#333333"));
    }

    //    还原
    public void reduction(UserDataViewHolderS viewHolder) {
        viewHolder.img_videos.setImageResource(R.mipmap.videos_img_un);
        viewHolder.img_likes.setImageResource(R.mipmap.like_un);
        viewHolder.tv_videos.setTextColor(Color.parseColor("#333333"));
        viewHolder.tv_likes.setTextColor(Color.parseColor("#333333"));
    }

    public void clickVideoAndLike(UserDataViewHolder viewHolder,int type){
        if (type==1){
            viewHolder.img_videos.setImageResource(R.mipmap.videos_img);
            viewHolder.tv_videos.setTextColor(Color.parseColor("#6FB5C5"));
        }else if(type==2){
            viewHolder.img_likes.setImageResource(R.mipmap.like);
            viewHolder.tv_likes.setTextColor(Color.parseColor("#6FB5C5"));
        }
    }

    class UserDataViewHolder extends RecyclerView.ViewHolder {
        ImageView ivUserHead;
        TextView tvName;
        TextView tvContent;
        ImageView imgSex;
        TextView tvFansSize;
        TextView tvFfollowSize;
        TextView tvHeartsSize;
        TextView tvFollow;
        LinearLayout ll_videos;
        LinearLayout ll_likes;
        ImageView img_videos;
        TextView tv_videos;
        ImageView img_likes;
        TextView tv_likes;
        LinearLayout ll_user;
        LinearLayout ll_user_fans;
        TextView vv_line;

        public UserDataViewHolder(View itemView) {
            super(itemView);
            ivUserHead = itemView.findViewById(R.id.iv_user_head);
            tvName = itemView.findViewById(R.id.tv_name);
            tvContent = itemView.findViewById(R.id.tv_comment);
            imgSex = itemView.findViewById(R.id.img_sex);
            tvFansSize = itemView.findViewById(R.id.tv_fans_size);
            tvFfollowSize = itemView.findViewById(R.id.tv_follow_size);
            tvHeartsSize = itemView.findViewById(R.id.tv_hearts_size);
            tvFollow = itemView.findViewById(R.id.tv_follow);
            ll_videos = itemView.findViewById(R.id.ll_videos);
            ll_likes = itemView.findViewById(R.id.ll_likes);
            img_videos = itemView.findViewById(R.id.img_videos);
            tv_videos = itemView.findViewById(R.id.tv_videos);
            img_likes = itemView.findViewById(R.id.img_likes);
            tv_likes = itemView.findViewById(R.id.tv_likes);
            ll_user = itemView.findViewById(R.id.ll_user);
            ll_user_fans = itemView.findViewById(R.id.ll_user_fans);
            vv_line = itemView.findViewById(R.id.vv_line);

        }
    }



    class UserDataViewHolderS extends RecyclerView.ViewHolder {
        ImageView ivUserHead;
        TextView tvName;
        TextView tvContent;
        ImageView imgSex;
        TextView tvFansSize;
        TextView tvFfollowSize;
        TextView tvHeartsSize;
        TextView tvFollow;
        LinearLayout ll_videos;
        LinearLayout ll_likes;
        ImageView img_videos;
        TextView tv_videos;
        ImageView img_likes;
        TextView tv_likes;
        LinearLayout ll_user;
        LinearLayout ll_user_fans;
        TextView vv_line;

        public UserDataViewHolderS(View itemView) {
            super(itemView);
            ivUserHead = itemView.findViewById(R.id.iv_user_head);
            tvName = itemView.findViewById(R.id.tv_name);
            tvContent = itemView.findViewById(R.id.tv_comment);
            imgSex = itemView.findViewById(R.id.img_sex);
            tvFansSize = itemView.findViewById(R.id.tv_fans_size);
            tvFfollowSize = itemView.findViewById(R.id.tv_follow_size);
            tvHeartsSize = itemView.findViewById(R.id.tv_hearts_size);
            tvFollow = itemView.findViewById(R.id.tv_follow);
            ll_videos = itemView.findViewById(R.id.ll_videos);
            ll_likes = itemView.findViewById(R.id.ll_likes);
            img_videos = itemView.findViewById(R.id.img_videos);
            tv_videos = itemView.findViewById(R.id.tv_videos);
            img_likes = itemView.findViewById(R.id.img_likes);
            tv_likes = itemView.findViewById(R.id.tv_likes);
            ll_user = itemView.findViewById(R.id.ll_user);
            ll_user_fans = itemView.findViewById(R.id.ll_user_fans);
            vv_line = itemView.findViewById(R.id.vv_line);
        }
    }

    class UserVideoViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rv_video_like_v;
        RecyclerView rv_video_like_l;

        public UserVideoViewHolder(View itemView) {
            super(itemView);
            rv_video_like_v = itemView.findViewById(R.id.rv_video_like_v);
            rv_video_like_l = itemView.findViewById(R.id.rv_video_like_l);
        }
    }

    class UserLikeViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rv_video_like_v;
        RecyclerView rv_video_like_l;

        public UserLikeViewHolder(View itemView) {
            super(itemView);
            rv_video_like_v = itemView.findViewById(R.id.rv_video_like_v);
            rv_video_like_l = itemView.findViewById(R.id.rv_video_like_l);
        }
    }


    class NoData extends RecyclerView.ViewHolder{

        public NoData(View itemView) {
            super(itemView);

        }
    }


    private onUserDataLisenter mOnUserDataLisenter;

    public void setmOnUserDataLisenter(onUserDataLisenter mOnUserDataLisenter) {
        this.mOnUserDataLisenter = mOnUserDataLisenter;
    }

    public interface onUserDataLisenter {
        void clickVideo();

        void clickLike();

        void fansSize();

        void followSize();

        void follows();

        void aliyunvideo(String videoId,PerSonWorkResponse.DataBean bean);

        void userHandle(int height);
    }

}
