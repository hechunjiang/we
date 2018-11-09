package com.sven.huinews.international.main.me.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sven.huinews.international.R;
import com.sven.huinews.international.entity.response.MessageResponse;
import com.sven.huinews.international.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<MessageResponse.DataBean.ListBean> messageList = new ArrayList<>();
    private LayoutInflater mInflater;

    public MessageAdapter(Context mContext) {
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public void refreshItem(List<MessageResponse.DataBean.ListBean> mData, boolean isRefresh) {
        if (mData == null && mData.size() <= 0) {
            return;
        }
        if (isRefresh) {
            this.messageList.clear();
        }

        this.messageList.addAll(mData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.message_list_item, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH vh = (VH) holder;
        MessageResponse.DataBean.ListBean message = messageList.get(position);
        vh.msg_title.setText(message.getTitle());
        vh.msg_content.setText(CommonUtils.ToDBC(message.getContent()));
        vh.msg_time.setText(message.getCreate_time());
        vh.msg_type.setText(message.getType());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    class VH extends RecyclerView.ViewHolder {
        private TextView msg_title;
        private TextView msg_content;
        private TextView msg_time;
        private TextView msg_type;

        public VH(View itemView) {
            super(itemView);
            msg_title = itemView.findViewById(R.id.msg_title);
            msg_content = itemView.findViewById(R.id.msg_content);
            msg_time = itemView.findViewById(R.id.msg_time);
            msg_type = itemView.findViewById(R.id.msg_type);
        }
    }
}
