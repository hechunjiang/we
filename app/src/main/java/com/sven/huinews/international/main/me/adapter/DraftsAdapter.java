package com.sven.huinews.international.main.me.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.meicam.sdk.NvsThumbnailView;
import com.sven.huinews.international.R;

import java.util.ArrayList;
import java.util.List;

import wedemo.utils.TimeFormatUtil;
import wedemo.utils.dataInfo.ClipInfo;
import wedemo.utils.dataInfo.TimeDataCache;

public class DraftsAdapter extends RecyclerView.Adapter<DraftsAdapter.VH> {

    private List<TimeDataCache> timeDataCacheList;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drafts, parent, false);
        VH vh = new VH(inflate);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, final int position) {
        final TimeDataCache timeDataCache = timeDataCacheList.get(position);
        ArrayList<ClipInfo> m_clipInfoArray = timeDataCache.getM_clipInfoArray();

        if(m_clipInfoArray != null && m_clipInfoArray.size() > 0) {
            holder.nv_show.setMediaFilePath(m_clipInfoArray.get(0).getFilePath());
        }

        holder.tv_date.setText(timeDataCache.getDate());
        holder.tv_video_duration.setText(TimeFormatUtil.formatUsToString2(timeDataCache.getDuration()));

        holder.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onEdit(position,timeDataCache);
                }
            }
        });

        holder.iv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onDelet(position,timeDataCache);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return timeDataCacheList == null?
                0:timeDataCacheList.size();
    }

    public void setData(List<TimeDataCache> timeDataCacheList){
        this.timeDataCacheList = timeDataCacheList;
        notifyDataSetChanged();
    }

    static class VH extends RecyclerView.ViewHolder{
        NvsThumbnailView nv_show;
        TextView tv_video_duration;
        TextView tv_date;
        ImageView iv_edit;
        ImageView iv_del;

        public VH(View itemView) {
            super(itemView);
            nv_show = itemView.findViewById(R.id.nv_show);
            tv_video_duration = itemView.findViewById(R.id.tv_video_duration);
            tv_date = itemView.findViewById(R.id.tv_date);
            iv_edit = itemView.findViewById(R.id.iv_edit);
            iv_del = itemView.findViewById(R.id.iv_del);
        }
    }

    public interface OnItemClickListener{
        void onDelet(int pos,TimeDataCache timeDataCache);
        void onEdit(int pos,TimeDataCache timeDataCache);
    }
}
