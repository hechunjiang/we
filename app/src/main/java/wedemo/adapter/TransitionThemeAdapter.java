package wedemo.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sven.huinews.international.R;

import java.io.InputStream;
import java.util.List;

import wedemo.shot.bean.FilterItem;

public class TransitionThemeAdapter extends RecyclerView.Adapter<TransitionThemeAdapter.Holder> {

    private List<FilterItem> themeModels;
    private OnItemClickListener onItemClickListener;
    private int selectedPosition;

    public TransitionThemeAdapter(@NonNull List<FilterItem> themeModels) {
        this.themeModels = themeModels;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setSelectedPosition(int position) {
        this.selectedPosition = position;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transition_theme, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.setItemPosition(position);
    }

    @Override
    public int getItemCount() {
        return themeModels.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        private int itemPosition;
        ImageView iv_transition_theme_thumbnail;
        TextView tv_transition_theme_name;

        public Holder(View itemView) {
            super(itemView);
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
            itemView.setLayoutParams(layoutParams);
            iv_transition_theme_thumbnail = itemView.findViewById(R.id.iv_transition_theme_thumbnail);
            tv_transition_theme_name = itemView.findViewById(R.id.tv_transition_theme_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedPosition = itemPosition;
                    if (onItemClickListener != null) onItemClickListener.onItemClick(itemPosition);
                }
            });
        }

        public void setItemPosition(int position) {
            this.itemPosition = position;
            if (selectedPosition == position) {

            }
            FilterItem item = themeModels.get(position);
            if (item == null) return;
            String name = item.getFilterName();
            if (name != null) {
                tv_transition_theme_name.setText(name);
            }

            int filterMode = item.getFilterMode();
            if (filterMode == FilterItem.FILTERMODE_BUILTIN) {
                int imageId = item.getImageId();
                if (imageId != 0)
                    iv_transition_theme_thumbnail.setImageResource(imageId);
            }

            String imageUrl = item.getImageUrl();
            if (imageUrl != null) {
                if (filterMode == FilterItem.FILTERMODE_BUNDLE) {
                    try {
                        InputStream inStream = itemView.getContext().getAssets().open(imageUrl);
                        Bitmap bitmap = BitmapFactory.decodeStream(inStream);
                        iv_transition_theme_thumbnail.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    //加载图片
                    Glide.with(itemView.getContext())
                            .load(imageUrl)
                            .into(iv_transition_theme_thumbnail);
                }
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int itemPosition);
    }
}
