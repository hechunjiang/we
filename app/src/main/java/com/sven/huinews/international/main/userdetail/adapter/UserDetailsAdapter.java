package com.sven.huinews.international.main.userdetail.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.meicam.sdk.NvsThumbnailView;
import com.sven.huinews.international.R;
import com.sven.huinews.international.entity.MyNews;
import com.sven.huinews.international.entity.response.PerSonWorkResponse;
import com.sven.huinews.international.utils.CommonUtils;

import java.util.List;

/**
 * Created by sfy. on 2018/9/20 0020.
 */

public class UserDetailsAdapter extends BaseQuickAdapter<PerSonWorkResponse.DataBean, BaseViewHolder> {


    public UserDetailsAdapter(int layoutResId) {
        super(layoutResId);
    }


    @Override
    protected void convert(BaseViewHolder helper, PerSonWorkResponse.DataBean item) {
        NvsThumbnailView nv_show = helper.getView(R.id.nv_show);
        if("-111".equals(item.getId())){
            helper.setText(R.id.tv_video_item_like, mContext.getString(R.string.draft));
            helper.addOnClickListener(R.id.nv_show);
           // nv_show.setMediaFilePath(item.getVideo_cover());
            nv_show.setVisibility(View.VISIBLE);
            helper.setVisible(R.id.iv_video_item_image,false);
            helper.setVisible(R.id.tv_video_item_like,false);
            helper.setVisible(R.id.tv_video_draft,true);
        }else {
            helper.setVisible(R.id.tv_video_item_like,true);
            helper.setVisible(R.id.tv_video_draft,false);
            nv_show.setVisibility(View.GONE);
            helper.setVisible(R.id.iv_video_item_image,true);
            helper.setText(R.id.tv_video_item_like, CommonUtils.getLikeCount(item.getLike_count())).addOnClickListener(R.id.iv_video_item_image);
            //.addOnClickListener(R.id.tv_video_item_like)
            ((SimpleDraweeView) helper.getView(R.id.iv_video_item_image)).setImageURI(item.getVideo_cover());
        }
    }

    public void clearDatas() {
        if (mData != null) {
            mData.clear();
            notifyDataSetChanged();
        }
    }


}
