package wedemo.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.meicam.sdk.NvsThumbnailView;
import com.sven.huinews.international.R;

import java.util.ArrayList;
import java.util.List;

import wedemo.utils.dataInfo.ClipInfo;

public class VideoEditAdapter extends RecyclerView.Adapter<VideoEditAdapter.VH> {

    private List<ClipInfo> mRecordFileList;
    private OnItemClickListener onItemClickListener;
    private int selectPos = -1;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public VideoEditAdapter(List<ClipInfo> mRecordFileList, int selectPos) {
        if (mRecordFileList != null) {
            this.mRecordFileList = mRecordFileList;
        } else {
            this.mRecordFileList = new ArrayList<>();
        }

        this.selectPos = selectPos;
    }

    @NonNull
    @Override
    public VideoEditAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_edit, parent, false);
        VH holder = new VH(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull VideoEditAdapter.VH holder, final int position) {

        if (selectPos == position) {
            holder.selectedItem.setVisibility(View.VISIBLE);
        } else {
            holder.selectedItem.setVisibility(View.GONE);
        }

        if (position == 0) {
            holder.iv_edit_top.setVisibility(View.VISIBLE);
        } else {
            holder.iv_edit_top.setVisibility(View.GONE);
        }

        String path = mRecordFileList.get(position).getFilePath();
        holder.nv_show.setMediaFilePath(path);
        holder.nv_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onVideoClick(view, position);
                    notifyItemChanged(selectPos);
                    selectPos = position;
                    notifyItemChanged(selectPos);
                }
            }
        });

        holder.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onAddClick(view, position);
                }
            }
        });

        holder.iv_edit_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onTopAddClick(view, position);
                }
            }
        });
    }

    public void callOnItemClick(int position) {
        if (onItemClickListener != null) {
            onItemClickListener.onVideoClick(null, position);
            notifyItemChanged(selectPos);
            selectPos = position;
            notifyItemChanged(selectPos);
        }
    }


    @Override
    public int getItemCount() {
        return mRecordFileList == null ? 0 : mRecordFileList.size();
    }

    public void setData(List<ClipInfo> mRecordFileList, int selectPos) {
        this.mRecordFileList = mRecordFileList;
        this.selectPos = selectPos;
        notifyDataSetChanged();
    }

    public int getSelectPos() {
        return selectPos;
    }


    class VH extends RecyclerView.ViewHolder {
        NvsThumbnailView nv_show;
        ImageView iv_edit;
        ImageView iv_edit_top;
        View selectedItem;

        public VH(View itemView) {
            super(itemView);
            nv_show = itemView.findViewById(R.id.nv_show);
            iv_edit = itemView.findViewById(R.id.iv_edit);
            iv_edit_top = itemView.findViewById(R.id.iv_edit_top);
            selectedItem = itemView.findViewById(R.id.selectedItem);
        }
    }

    public interface OnItemClickListener {
        void onVideoClick(View view, int position);

        void onAddClick(View view, int position);

        void onTopAddClick(View view, int position);
    }

}
