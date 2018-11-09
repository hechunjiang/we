package wedemo.utils;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;

import java.io.IOException;

public class MediaPlayerUtil implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {
    public MediaPlayer mediaPlayer = null;

    //by xyp
    public String url;

    //by xyp
    public int time;//音乐时长

    public static int sStartTime = 0;
    public static float sMusicSpeed = 1.0f;

    //by xyp 是否重复播放
    public boolean isRePlay;

    public MediaPlayerUtil() {
        initPlayer();
    }

    @SuppressWarnings("unchecked")
    private void initPlayer() {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnPreparedListener(this);
        } catch (Exception e) {
            Log.e("mediaPlayer", "error", e);
        }
    }

    public void setRePlay(boolean rePlay) {
        this.isRePlay = rePlay;
        if (mediaPlayer != null) {
            mediaPlayer.setLooping(rePlay);
        }
    }

    public int getDuration() {
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration();
        }
        return 0;
    }

    /**
     * 重置播放器
     * xuyuanpeng
     * 2017-12-12
     */
    public void resumePlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(0);
        }
    }

    //*****************************************************

    public void setUrl(String url) {
        this.url = url;
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            try {
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepare();
                time = mediaPlayer.getDuration();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void play() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }

    }

    public void playUrl(String videoUrl) {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(videoUrl);
                mediaPlayer.prepare();//prepare之后自动播放
                //mediaPlayer.start();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }


    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onPrepared(MediaPlayer arg0) {
        time = arg0.getDuration();
    }

    @Override
    public void onCompletion(MediaPlayer arg0) {
        if (isRePlay) {
            if (mediaPlayer != null) {
                mediaPlayer.start();
            }
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer arg0, int arg1) {
    }

    public void seekTo(int position) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(position);
        }
    }

    /**
     * 改变视频播放速度
     **/
    public void setSpeed(float speed) {
        if (mediaPlayer != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(speed));
            }
        }
    }

    /**
     * 视频是否播放中
     **/
    public boolean isPlaying() {
        if (mediaPlayer != null) {
            return mediaPlayer.isPlaying();
        }
        return false;
    }

    public void setVolume(float leftVolume, float rightVolume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(leftVolume, rightVolume);
        }
    }

}
