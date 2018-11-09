package com.sven.huinews.international.main.novicevideo;


import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.dueeeke.videoplayer.listener.VideoListener;
import com.dueeeke.videoplayer.player.IjkVideoView;
import com.dueeeke.videoplayer.player.PlayerConfig;
import com.sven.huinews.international.BuildConfig;
import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.http.DataCallBack;
import com.sven.huinews.international.main.follow.video.VerticalVideoController;
import com.sven.huinews.international.publicclass.AddGoldModel;
import com.sven.huinews.international.utils.ToastUtils;


public class VideoPlayerActivity extends AppCompatActivity {
    private FrameLayout videoPlayerContainer;
    private TextView actionBarTitleTv;
    private ImageView action_bar_back_iv;
    private IjkVideoView video_player;
    private PlayerConfig mPlayerConfig;
    private VerticalVideoController mController;

    private int time = 30;
    private boolean timeIsContinue = true;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    new AddGoldModel(VideoPlayerActivity.this).addIntroduceVideoCoid(new DataCallBack() {
                        @Override
                        public void onSucceed(String json) {
                            ToastUtils.showLong(VideoPlayerActivity.this, "+ 100" + getString(R.string.me_coins));
                        }

                        @Override
                        public void onFail(BaseResponse response) {
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        init();
        actionBarTitleTv.setText(getString(R.string.introduce_video_title));
        initData();
    }

    public void init() {
        videoPlayerContainer = findViewById(R.id.video_player_container);
        actionBarTitleTv = findViewById(R.id.action_bar_title_tv);
        action_bar_back_iv = findViewById(R.id.action_bar_back_iv);
        action_bar_back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        video_player = findViewById(R.id.video_player);
        mController = new VerticalVideoController(this);
        mPlayerConfig = new PlayerConfig.Builder().enableCache()./*setLooping().*/addToPlayerManager().build();
        video_player.setVideoListener(new VideoListener() {
            @Override
            public void onVideoStarted() {
                timeIsContinue = true;
                startTime1();
            }

            @Override
            public void onVideoPaused() {
                timeIsContinue = false;
            }

            @Override
            public void onComplete() {
                timeIsContinue = false;
            }

            @Override
            public void onPrepared() {
                timeIsContinue = true;
            }

            @Override
            public void onError() {
                timeIsContinue = false;
            }

            @Override
            public void onInfo(int what, int extra) {
                timeIsContinue = true;
            }
        });
    }

    private void initData() {
        video_player.setPlayerConfig(mPlayerConfig);
        video_player.setUrl(BuildConfig.BASE_WEB_URL + "MP4/news.mp4");
        video_player.setVideoController(mController);
        video_player.start();
    }

    public void startTime1() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (timeIsContinue){
                    time--;
                    if (time <= 0) {
                        mHandler.sendEmptyMessage(0);
                    } else {
                        mHandler.postDelayed(this, 1000);
                    }
                }
            }
        };
        new Thread(runnable).start();
    }


    @Override
    protected void onPause() {
        super.onPause();
        video_player.pause();
    }


    @Override
    protected void onResume() {
        super.onResume();
        video_player.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        video_player.release();
    }

    @Override
    public void onBackPressed() {
        if (!video_player.onBackPressed()) {
            super.onBackPressed();
        }
    }


}
