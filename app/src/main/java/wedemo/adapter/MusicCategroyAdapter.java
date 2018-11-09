package wedemo.adapter;//package com.sven.huinews.international.main.shot.adapter;
//
//import android.content.Context;
//import android.net.Uri;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//
//import com.facebook.drawee.view.SimpleDraweeView;
//import com.sven.huinews.international.R;
//import com.sven.huinews.international.main.shot.bean.MusicTypeResponse;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MusicCategroyAdapter extends RecyclerView.Adapter {
//    private Context mContext;
//    private List<MusicTypeResponse.DataBean> mDatas = new ArrayList<>();
//
//    public MusicCategroyAdapter(Context mContext) {
//        this.mContext = mContext;
//    }
//
//
//    public void setDatas(List<MusicTypeResponse.DataBean> mDatas) {
//        this.mDatas = mDatas;
//        notifyDataSetChanged();
//    }
//
//    /**
//     * 默认选中第一个
//     */
//    public void setDefSelected() {
//        if (mDatas != null && mDatas.size() > 0) {
//            mDatas.get(0).setSelected(true);
//        }
//    }
//
//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(mContext).inflate(R.layout.music_cate_list_item, parent, false);
//        return new VH(v);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
//        VH vh = (VH) holder;
//        vh.iv_categroy.setImageURI(Uri.parse(mDatas.get(position).getType_cover()));
//        vh.iv_selected.setVisibility(mDatas.get(position).isSelected() ? View.VISIBLE : View.INVISIBLE);
//        vh.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mOnItemClickLisenter != null) {
//                    mOnItemClickLisenter.onItemClicl(mDatas.get(position));
//                    for (MusicTypeResponse.DataBean data:mDatas
//                         ) {
//                        data.setSelected(false);
//                    }
//                    mDatas.get(position).setSelected(!mDatas.get(position).isSelected());
//                    notifyDataSetChanged();
//                }
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return mDatas.size();
//    }
//
//    class VH extends RecyclerView.ViewHolder {
//        SimpleDraweeView iv_categroy;
//        ImageView iv_selected;
//
//        public VH(View itemView) {
//            super(itemView);
//            iv_categroy = itemView.findViewById(R.id.iv_categroy);
//            iv_selected = itemView.findViewById(R.id.iv_selected);
//        }
//    }
//
//    public interface OnItemClickLisenter {
//        void onItemClicl(MusicTypeResponse.DataBean mData);
//    }
//
//    private OnItemClickLisenter mOnItemClickLisenter;
//
//    public void setmOnItemClickLisenter(OnItemClickLisenter l) {
//        this.mOnItemClickLisenter = l;
//    }
//
//}
