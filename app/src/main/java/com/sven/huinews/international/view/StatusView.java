package com.sven.huinews.international.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sven.huinews.international.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sfy. on 2018/9/10 0010.
 */

public class StatusView extends LinearLayout {
    private RecyclerView rv_line;
    private TextView tvMessage;
    private TextView btnAction;

    public StatusView(Context context) {
        this(context, null);
    }

    public StatusView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.player_status_view, this);
        rv_line = root.findViewById(R.id.rv_line);
        tvMessage = root.findViewById(R.id.message);
        btnAction = root.findViewById(R.id.status_btn);

        setClickable(true);


    }


    /**
     * 设置提示，
     *
     * @param msg
     */
    public void setMessage(String msg) {
        if (tvMessage != null) tvMessage.setText(msg);
    }

    /**
     * 设置按钮点击，
     *
     * @param text     线路 size == 1     重试，   线路 size >1   btnAction Gone
     * @param listener
     */
    public void setButtonTextAndAction(String text, OnClickListener listener) {
        if (btnAction != null) {
            btnAction.setText(text);
            btnAction.setOnClickListener(listener);
        }
    }

    /**
     * 设置线路
     *
     * @param mMovieDataAnalys
     */

}
