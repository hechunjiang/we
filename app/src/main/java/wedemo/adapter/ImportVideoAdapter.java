package wedemo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sven.huinews.international.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import wedemo.shot.bean.MediaData;
import wedemo.utils.TimeUtil;

public class ImportVideoAdapter extends RecyclerView.Adapter<ImportVideoAdapter.VideoVH> {

    private Context mContext;
    public List<MediaData> mDatas = new ArrayList<>();
    private LayoutInflater mInflater;
    private List<MediaData> selectList = new ArrayList<>();

    public ImportVideoAdapter(Context mContext) {
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public void setData(List<MediaData> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VideoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.import_video_list_item, parent, false);
        return new VideoVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoVH holder, final int position) {
        final MediaData mediaData = mDatas.get(position);
        if (mediaData.getDuration() / 1000 >= 1) {
            holder.tv_duration.setText(TimeUtil.secToTime((int) (mediaData.getDuration() / 1000)));
        } else {
            holder.tv_duration.setText("");
        }
        File file = new File(mediaData.getPath());
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.placeholder(R.drawable.def_image);
        Glide.with(mContext)
                .asBitmap()
                .load(file)
                .apply(options)
                .into(holder.iv_item_image);
        holder.iv_select.setVisibility(mediaData.isState() ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaData.setState(!mediaData.isState());
                if (mediaData.isState()) {
                    selectList.add(mediaData);
                } else {
                    selectList.remove(mediaData);
                }
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class VideoVH extends RecyclerView.ViewHolder {
        ImageView iv_item_image;
        ImageView iv_select;
        TextView tv_duration;

        public VideoVH(View itemView) {
            super(itemView);
            iv_item_image = itemView.findViewById(R.id.iv_item_image);
            iv_select = itemView.findViewById(R.id.iv_select);
            tv_duration = itemView.findViewById(R.id.tv_duration);
        }
    }


    public List<MediaData> getSelectList() {
        if (selectList == null) {
            return new ArrayList<>();
        }
        return selectList;
    }
}
