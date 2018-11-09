package com.sven.huinews.international.main.follow.adapter;

import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sven.huinews.international.BuildConfig;
import com.sven.huinews.international.R;
import com.sven.huinews.international.entity.response.FollowVideoResponse;
import com.sven.huinews.international.utils.CommonUtils;

import java.util.Random;

/**
 * Created by sfy. on 2018/9/10 0010.
 */

public class FollowAdapter extends BaseQuickAdapter<FollowVideoResponse.DataBean, BaseViewHolder> {


    public FollowAdapter(int layoutResId) {
        super(layoutResId);
    }

    /**
     * 设置瀑布流 父布局的高度
     * by w.mago
     */
    private void setImageParams(ImageView iv) {
        Random r = new Random();
        int heightDp = r.nextInt(50);
        heightDp = heightDp + 300;
        int mHeight = convertDpToPixel(heightDp);
        ViewGroup.LayoutParams para = iv.getLayoutParams();
        para.height = mHeight;
        iv.setLayoutParams(para);
    }

    private int convertDpToPixel(int dp) {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        return (int) (dp * displayMetrics.density);
    }




    /**
     * 清除所有数据
     */
    public void clearDatas() {
        if (mData != null) {
            mData.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    protected void convert(final BaseViewHolder helper, final FollowVideoResponse.DataBean item) {
        setImageParams((ImageView) helper.getView(R.id.video_img));
        helper.setText(R.id.tv_user_name, item.getUser_nickname())
                .setText(R.id.tv_video_dis, item.getTitle())
                .setText(R.id.tv_like, CommonUtils.getLikeCount(item.getLike_count()));

        ((SimpleDraweeView) helper.getView(R.id.user_head)).setImageURI(item.getUser_avatar());
        ((SimpleDraweeView) helper.getView(R.id.video_img)).setImageURI(item.getVideo_cover());
        ImageView iv_bg = helper.getView(R.id.video_img1);
        RequestOptions options = new RequestOptions();
        options.centerCrop()
                .placeholder(R.drawable.def_image);
        Glide.with(mContext).load(item.getVideo_cover()).apply(options).into(iv_bg);
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickLisenter != null) {
                    mOnItemClickLisenter.itemClick(item, helper.getPosition());
                }
            }
        });
    }

    public interface OnItemClickLisenter {
        void itemClick(FollowVideoResponse.DataBean data, int position);
    }

    private OnItemClickLisenter mOnItemClickLisenter;

    public void setOnItemClickLisenter(OnItemClickLisenter l) {
        this.mOnItemClickLisenter = l;
    }
}
