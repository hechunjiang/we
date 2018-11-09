package wedemo.adapter;//package com.sven.huinews.international.main.shot.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sven.huinews.international.R;
import com.sven.huinews.international.entity.response.TagResponse;

import java.util.ArrayList;
import java.util.List;

public class TagsAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<TagResponse.DataBean> mDatas = new ArrayList<>();

    public TagsAdapter(Context mContext) {
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        mDatas.add(0, new TagResponse.DataBean());
    }

    public void setDatas(List<TagResponse.DataBean> mDatas) {
        this.mDatas = mDatas;
        this.mDatas.add(0, new TagResponse.DataBean());
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.tags_list_item, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        VH vh = (VH) holder;
        TagResponse.DataBean data = mDatas.get(position);
        if (position == 0) {
            vh.tv_tag.setText(mContext.getResources().getString(R.string.select_tag));
            vh.tv_tag.setBackgroundResource(R.drawable.selected_tag_bg);
        } else {
            vh.tv_tag.setText(data.getTag_name());
            vh.tv_tag.setBackgroundResource(R.drawable.tag_selected_bg);
        }

        vh.tv_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickLisenter != null) {
                    mOnItemClickLisenter.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class VH extends RecyclerView.ViewHolder {
        TextView tv_tag;

        public VH(View itemView) {
            super(itemView);
            tv_tag = itemView.findViewById(R.id.btn_tag);
        }
    }

    public interface OnItemClickLisenter {
        void onItemClick(int position);
    }

    private OnItemClickLisenter mOnItemClickLisenter;

    public void setmOnItemClickLisenter(OnItemClickLisenter o) {
        this.mOnItemClickLisenter = o;
    }
}
