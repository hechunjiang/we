package wedemo.music;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sven.huinews.international.R;
import com.sven.huinews.international.utils.Common;

import java.util.ArrayList;
import java.util.List;

import wedemo.config.Constant;
import wedemo.view.MusicStartingView;

public class MusicListAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<MusicListResponse.DataBean> mDatas = new ArrayList<>();
    private int m_selectedPos = -1;

    public MusicListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setDatas(List<MusicListResponse.DataBean> mDatas, boolean isRefresh) {
        if (mDatas == null && mDatas.size() == 0) {
            return;
        }
        if (isRefresh) {
            this.mDatas.clear();
        }
        this.mDatas.addAll(mDatas);
        notifyDataSetChanged();
    }


    public void insertData(int position, MusicListResponse.DataBean mData) {
        mDatas.set(position, mData);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.music_list_item, parent, false);
        return new VH(v);
    }

    private String format(long i) {
        return i < 10 ? "0" + i : String.valueOf(i);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final VH vh = (VH) holder;
        final MusicListResponse.DataBean mMusic = mDatas.get(position);

        if(mMusic.getId() == Constant.MUSIC_STORE){
            vh.iv_music.setImageResource(R.mipmap.default_music);
        }else {
            vh.iv_music.setImageURI(Uri.parse(mMusic.getMusic_cover()));
        }
        vh.tv_music_name.setText(mMusic.getTitle());
        vh.tv_music_auther.setText(mMusic.getMusic_singer());
        vh.tv_music_time.setText(format((mMusic.getMusic_duration()) / 60 % 60) + ":" + format((mMusic.getMusic_duration()) % 60));

        vh.tv_use.setVisibility(mMusic.isPlay() ? View.VISIBLE : View.GONE);
        vh.music_view.setVisibility(mMusic.isPlay() ? View.VISIBLE : View.INVISIBLE);
        vh.tv_paly_status.setImageResource(mMusic.isPlay() ? R.mipmap.icon_music_stop : R.mipmap.icon_music_start);

        if (mMusic.isPlay()) {
            vh.music_view.startAim();
        } else {
            vh.music_view.stopAim();
        }
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (m_selectedPos == -1) {
                    m_selectedPos = position;
                    mDatas.get(position).setPlay(!mDatas.get(position).isPlay());
                    notifyItemChanged(position);
                } else {
                    if (m_selectedPos == position) {
                        mMusic.setPlay(!mMusic.isPlay());
                        notifyItemChanged(m_selectedPos);
                    } else {
                        m_selectedPos = position;
                        for (MusicListResponse.DataBean mData : mDatas) {
                            mData.setPlay(false);
                        }
                        mDatas.get(position).setPlay(!mDatas.get(position).isPlay());
                        notifyDataSetChanged();
                    }
                }

                if (mPlayMusicLisenter != null) {
                    mPlayMusicLisenter.onPlayMusic(position, mMusic);
                }

            }
        });

        vh.tv_use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayMusicLisenter != null) {
                    mPlayMusicLisenter.onSelectMusic(mMusic);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class VH extends RecyclerView.ViewHolder {
        SimpleDraweeView iv_music;
        TextView tv_music_name;
        TextView tv_music_auther;
        TextView tv_music_time;
        TextView tv_use;
        MusicStartingView music_view;
        ImageView tv_paly_status;

        public VH(View itemView) {
            super(itemView);
            iv_music = itemView.findViewById(R.id.iv_music);
            tv_music_name = itemView.findViewById(R.id.tv_music_name);
            tv_music_auther = itemView.findViewById(R.id.tv_music_auther);
            tv_music_time = itemView.findViewById(R.id.tv_music_time);
            tv_use = itemView.findViewById(R.id.tv_use);
            music_view = itemView.findViewById(R.id.music_view);
            tv_paly_status = itemView.findViewById(R.id.tv_paly_status);
        }
    }

    public interface onPlayMusicLisenter {
        void onPlayMusic(int position, MusicListResponse.DataBean mData);

        void onSelectMusic(MusicListResponse.DataBean mData);
    }

    private onPlayMusicLisenter mPlayMusicLisenter;

    public void onPlayMusicLisenter(onPlayMusicLisenter mPlayMusicLisenter) {
        this.mPlayMusicLisenter = mPlayMusicLisenter;
    }
}
