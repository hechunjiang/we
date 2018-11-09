package wedemo.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meicam.sdk.NvsThumbnailView;
import com.sven.huinews.international.R;

import java.util.List;

public class VideoShowAdapter extends RecyclerView.Adapter<VideoShowAdapter.VH> {

    private List<String> mRecordFileList;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public VideoShowAdapter(List<String> mRecordFileList) {
        this.mRecordFileList = mRecordFileList;
    }

    @NonNull
    @Override
    public VideoShowAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_show, parent, false);
        VH holder = new VH(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull VideoShowAdapter.VH holder, final int position) {
        holder.nv_show.setMediaFilePath(mRecordFileList.get(position));
        holder.nv_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(view, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRecordFileList.size();
    }

    public void setData(List<String> mRecordFileList) {
        this.mRecordFileList = mRecordFileList;
        notifyDataSetChanged();
    }

    class VH extends RecyclerView.ViewHolder {
        NvsThumbnailView nv_show;

        public VH(View itemView) {
            super(itemView);
            nv_show = itemView.findViewById(R.id.nv_show);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

}
