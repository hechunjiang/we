package wedemo.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.meicam.sdk.NvsThumbnailView;
import com.sven.huinews.international.R;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import wedemo.shot.bean.FilterItem;

public class TransitionFileAdapter extends RecyclerView.Adapter<TransitionFileAdapter.Holder> {

    private List<String> pathList;
    private HashMap<String, FilterItem> itemMap;
    private OnItemClickListener listener;

    public TransitionFileAdapter(@NonNull List<String> pathList, HashMap<String, FilterItem> itemMap) {
        this.pathList = pathList;
        this.itemMap = itemMap;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transition_file, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.setPosition(position);
    }

    @Override
    public int getItemCount() {
        return pathList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        public NvsThumbnailView item_thumbnail_view;
        public ImageView iv_transition_link;
        private FilterItem item;
        private int position;

        public Holder(View itemView) {
            super(itemView);

            item_thumbnail_view = itemView.findViewById(R.id.item_thumbnail_view);
            iv_transition_link = itemView.findViewById(R.id.iv_transition_link);

            item_thumbnail_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) listener.onClickThumbnail(position);
                }
            });

            iv_transition_link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) listener.onClickLink(position);
                }
            });
        }

        public void setPosition(int position) {
            this.position = position;
            item_thumbnail_view.setMediaFilePath(pathList.get(position));

            if (pathList.size() - 1 == position) {
                iv_transition_link.setVisibility(View.GONE);
            } else {
                iv_transition_link.setVisibility(View.VISIBLE);

                item = itemMap.get(pathList.get(position));

                if (item != null) {
                    int filterMode = item.getFilterMode();
                    if (filterMode == FilterItem.FILTERMODE_BUILTIN) {
                        int imageId = item.getImageId();
                        if (imageId != 0)
                            iv_transition_link.setImageResource(imageId);
                    }
                    String imageUrl = item.getImageUrl();
                    if (imageUrl != null) {
                        if (filterMode == FilterItem.FILTERMODE_BUNDLE) {
                            try {
                                InputStream inStream = itemView.getContext().getAssets().open(imageUrl);
                                Bitmap bitmap = BitmapFactory.decodeStream(inStream);
                                iv_transition_link.setImageBitmap(bitmap);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Glide.with(itemView.getContext())
                                    .load(imageUrl)
                                    .into(iv_transition_link);
                        }
                    }
                } else {
                    iv_transition_link.setImageResource(R.mipmap.icon_transition_no);
                }

            }
        }
    }

    public interface OnItemClickListener {
        void onClickThumbnail(int position);

        void onClickLink(int position);
    }
}
