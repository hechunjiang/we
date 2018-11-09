package com.sven.huinews.international.main.userdetail.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sven.huinews.international.R;
import com.sven.huinews.international.entity.response.PerSonWorkResponse;
import com.sven.huinews.international.entity.response.PersonLikeResponse;
import com.sven.huinews.international.utils.CommonUtils;

/**
 * Created by sfy. on 2018/9/21 0021.
 */

public class UserLikeAdapter extends BaseQuickAdapter<PersonLikeResponse.DataBean, BaseViewHolder> {


    public UserLikeAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, PersonLikeResponse.DataBean item) {

        helper.setText(R.id.tv_video_item_like, CommonUtils.getLikeCount(Integer.parseInt(item.getLike_count())))
                .addOnClickListener(R.id.iv_video_item_image);
        //.addOnClickListener(R.id.tv_video_item_like)
        ((SimpleDraweeView) helper.getView(R.id.iv_video_item_image)).setImageURI(item.getVideo_cover());
    }

    public void clearDatas() {
        if (mData != null) {
            mData.clear();
            notifyDataSetChanged();
        }
    }



}
