package wedemo.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sven.huinews.international.R;

import java.util.ArrayList;
import java.util.List;

import wedemo.activity.down.DownResponse;


/**
 * Created by weiwei on 2018/9/16.
 */

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.VH> {

    private List<DownResponse.DataBean> mDatas = new ArrayList<>();
    private int m_selectedPos = 0;
    private OnItemClickListener onItemClickListener;
    private Context context;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setSelectPos(int m_selectedPos) {
        this.m_selectedPos = m_selectedPos;
        notifyDataSetChanged();
    }

    public StickerAdapter(Context context) {
        this.context = context;
    }

    public void setDatas(List<DownResponse.DataBean> mDatas) {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_sticker, viewGroup, false);
        return new VH(view);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull VH vh, final int pos) {
        if (m_selectedPos == pos) {
            vh.setSelectedBackground();
        } else {
            vh.setUnselectedBackground();
        }
        //vh.iv_sticker.setImageDrawable(context.getResources().getDrawable(Daoju_RES_ARRAY[pos]));
        DownResponse.DataBean dataBean = mDatas.get(pos);

        if (dataBean.isDown()) {
            vh.iv_down.setVisibility(View.GONE);
        } else {
            vh.iv_down.setVisibility(View.VISIBLE);
        }

        if (dataBean != null && !TextUtils.isEmpty(dataBean.getCover())) {
            Uri uri = Uri.parse(dataBean.getCover());
            vh.iv_sticker.setImageURI(uri);
        } else {
            vh.iv_sticker.setImageResource(R.mipmap.no_data);
            vh.iv_down.setVisibility(View.GONE);
        }

        if (dataBean.isOnLoad()) {
            vh.pro_load.setVisibility(View.VISIBLE);
            vh.iv_sticker.setVisibility(View.GONE);
        } else {
            vh.pro_load.setVisibility(View.GONE);
            vh.iv_sticker.setVisibility(View.VISIBLE);
        }
        vh.rl_sticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, pos);
                    notifyItemChanged(m_selectedPos);
                    m_selectedPos = pos;
                    notifyItemChanged(m_selectedPos);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class VH extends RecyclerView.ViewHolder {
        FrameLayout rl_sticker;
        SimpleDraweeView iv_sticker;
        ImageView iv_down;
        ProgressBar pro_load;

        public VH(View itemView) {
            super(itemView);
            rl_sticker = itemView.findViewById(R.id.rl_sticker);
            iv_sticker = itemView.findViewById(R.id.iv_sticker);
            iv_down = itemView.findViewById(R.id.iv_down);
            pro_load = itemView.findViewById(R.id.pro_load);
        }

        public void setUnselectedBackground() {
            rl_sticker.setBackground(null);
        }

        public void setSelectedBackground() {
            rl_sticker.setBackground(ContextCompat.getDrawable(context, R.drawable.sticker_item_bg));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

//    public static final int[] Daoju_RES_ARRAY = {
//            R.mipmap.reduce, R.mipmap.test, R.mipmap.test, R.mipmap.test, R.mipmap.test,
//            R.mipmap.test, R.mipmap.test
//    };
//
//    private int m_selectedPos = 0;
//    private OnItemClickListener onItemClickListener;
//    private Context context;
//
//    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
//        this.onItemClickListener = onItemClickListener;
//    }
//
//    public void setSelectPos(int m_selectedPos){
//        this.m_selectedPos = m_selectedPos;
//    }
//
//    public StickerAdapter(Context context){
//        this.context = context;
//    }
//
//    @NonNull
//    @Override
//    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_sticker, viewGroup, false);
//        return new VH(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull VH vh, final int pos) {
//        if(m_selectedPos == pos){
//            vh.setSelectedBackground();
//        }else{
//            vh.setUnselectedBackground();
//        }
//        vh.iv_sticker.setImageDrawable(context.getResources().getDrawable(Daoju_RES_ARRAY[pos]));
//        vh.rl_sticker.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(onItemClickListener != null){
//                    onItemClickListener.onItemClick(v,pos);
//                    notifyItemChanged(m_selectedPos);
//                    m_selectedPos = pos;
//                    notifyItemChanged(m_selectedPos);
//                }
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return Daoju_RES_ARRAY.length;
//    }
//
//    class VH extends RecyclerView.ViewHolder{
//        FrameLayout rl_sticker;
//        ImageView iv_sticker;
//        public VH(View itemView) {
//            super(itemView);
//            rl_sticker = itemView.findViewById(R.id.rl_sticker);
//            iv_sticker = itemView.findViewById(R.id.iv_sticker);
//        }
//
//        public void setUnselectedBackground() {
//            rl_sticker.setBackground(null);
//        }
//
//        public void setSelectedBackground() {
//            rl_sticker.setBackground(ContextCompat.getDrawable(context, R.drawable.sticker_item_bg));
//        }
//    }
//
//    public interface OnItemClickListener {
//        void onItemClick(View view, int position);
//    }
}
