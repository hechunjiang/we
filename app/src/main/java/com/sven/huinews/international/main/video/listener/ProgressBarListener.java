package com.sven.huinews.international.main.video.listener;

/**
 * Created by Burgess on 2018/9/20 0020.
 */
public interface ProgressBarListener {
    void onMove();

    void onStop();

    void onBuffering();

    void onPreparing();

    void onProgressCompletion();

    void onProgress(int position,int duration);
}
