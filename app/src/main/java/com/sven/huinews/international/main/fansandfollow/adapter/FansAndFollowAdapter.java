package com.sven.huinews.international.main.fansandfollow.adapter;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sven.huinews.international.BuildConfig;
import com.sven.huinews.international.R;
import com.sven.huinews.international.entity.FansAndFollow;

/**
 * 作者：burgess by Burgess on 2018/8/30 00:41
 * 作用：wechatEarn
 */
public class FansAndFollowAdapter extends BaseQuickAdapter<FansAndFollow.DataBean, BaseViewHolder> {

    public FansAndFollowAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, FansAndFollow.DataBean item) {
        helper.setText(R.id.tv_user_name, item.getNickname()).addOnClickListener(R.id.ll_item);
        ((SimpleDraweeView) helper.getView(R.id.iv_user_head)).setImageURI(item.getUser_avatar());
        if (item.getSex() == 1) {
            ((ImageView) helper.getView(R.id.img_user_sex)).setImageResource(R.mipmap.girl);
        } else {
            ((ImageView) helper.getView(R.id.img_user_sex)).setImageResource(R.mipmap.man);
        }
        helper.setText(R.id.tv_explain, item.getSignature().equals("") ? mContext.getResources().getString(R.string.def_signature) : item.getSignature());
        TextView tv_age = helper.getView(R.id.tv_age);
        if (null !=item.getBirthday()){
            if (item.getBirthday().equals("")) {
                tv_age.setVisibility(View.GONE);
            } else {
                tv_age.setText(mContext.getResources().getString(R.string.age) + " " + item.getBirthday());
                tv_age.setVisibility(View.VISIBLE);
            }
        }else{
            tv_age.setVisibility(View.GONE);

        }


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

}
