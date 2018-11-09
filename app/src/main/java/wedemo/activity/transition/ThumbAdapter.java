package wedemo.activity.transition;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sven.huinews.international.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import wedemo.activity.view.RoundImageView;
import wedemo.shot.bean.FilterItem;

public class ThumbAdapter extends RecyclerView.Adapter<ThumbAdapter.ViewHolder> {
    private Context mContext;
    private OnItemClickListener mClickListener;
    private HashMap<String, FilterItem> itemMap;
    private List<String> fileData = new ArrayList<>();
    private int mSelectedPos = 0;

    public interface OnItemClickListener {
        void onThumbItemClick(View view, int position);

        void onTransitionItemClick(View view, int position);
    }


    public int getSelectPos() {
        return mSelectedPos;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout item_assetLayout;
        private RoundImageView item_thumbView;
        private ImageView item_transitionImage;

        public ViewHolder(View view) {
            super(view);
            item_assetLayout = (LinearLayout) view.findViewById(R.id.layoutAsset);
            item_thumbView = (RoundImageView) view.findViewById(R.id.imageThumbView);
            item_transitionImage = (ImageView) view.findViewById(R.id.imageTransition);
        }
    }

    public ThumbAdapter(Context context, List<String> fileData, HashMap<String, FilterItem> itemMap) {
        if (fileData != null)
            this.fileData = fileData;
        if (itemMap != null)
            this.itemMap = itemMap;
        mContext = context;
    }

    public void setTransitionFilterItem(HashMap<String, FilterItem> itemMap) {
        if (itemMap != null)
            this.itemMap = itemMap;

        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thumb, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        String filePath = fileData.get(position);
        Glide.with(mContext).asBitmap().load(filePath).into(holder.item_thumbView);

        if (itemMap != null) {
            FilterItem mFilterItem = itemMap.get(position + "");
            if (mFilterItem == null) {
                mFilterItem = new FilterItem();
                mFilterItem.setFilterId(null);
                mFilterItem.setFilterMode(FilterItem.FILTERMODE_BUILTIN);
            }

            int filterMode = mFilterItem.getFilterMode();
            if (filterMode == FilterItem.FILTERMODE_BUILTIN) {
                int imageId = mFilterItem.getImageId();
                if (imageId != 0)
                    holder.item_transitionImage.setImageResource(imageId);
            }

            String imageUrl = mFilterItem.getImageUrl();
            if (imageUrl != null) {
                if (filterMode == FilterItem.FILTERMODE_BUNDLE) {
                    try {
                        InputStream inStream = mContext.getAssets().open(imageUrl);
                        Bitmap bitmap = BitmapFactory.decodeStream(inStream);
                        holder.item_transitionImage.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    //加载图片
                    RequestOptions options = new RequestOptions();
                    options.centerCrop();
                    options.placeholder(R.mipmap.package1);
                    Glide.with(mContext)
                            .asBitmap()
                            .load(imageUrl)
                            .apply(options)
                            .into(holder.item_transitionImage);
                }
            }
        }

        if (position == fileData.size() - 1) {
            holder.item_transitionImage.setVisibility(View.INVISIBLE);
        } else {
            holder.item_transitionImage.setVisibility(View.VISIBLE);
        }

        if (mSelectedPos == position) {
            holder.item_transitionImage.setBackground(ContextCompat.getDrawable(mContext, R.drawable.fx_item_radius_border_select2));
        } else {
            holder.item_transitionImage.setBackground(ContextCompat.getDrawable(mContext, R.drawable.fx_item_radius_shape_unselect2));
        }

        holder.item_transitionImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectedPos = position;
                notifyDataSetChanged();

                if (mClickListener != null) {
                    mClickListener.onTransitionItemClick(view, position);
                }

            }
        });

        holder.item_thumbView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mClickListener != null) {
                    mClickListener.onThumbItemClick(view, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return fileData.size();
    }
}
