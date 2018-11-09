package com.sven.huinews.international.view;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sven.huinews.international.R;
import com.sven.huinews.international.utils.ScreensUitls;

public class ShotDialog extends DialogFragment {
    private RelativeLayout dialog_layout;
    private LinearLayout bottom_layout;
    private TextView shot_free, shot_block;
    private Handler mHandler;
    private ImageButton btn_close;
    private boolean isShow = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.publishdialog_style);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.activity_select_shot, container);
        return inflate;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialog_layout = view.findViewById(R.id.dialog_layout);
        bottom_layout = view.findViewById(R.id.bottom_layout);
        shot_block = view.findViewById(R.id.shot_block);
        shot_free = view.findViewById(R.id.shot_free);
        btn_close = view.findViewById(R.id.btn_close);

        shot_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onPublishDilaogClickLisenter != null) {
                    onPublishDilaogClickLisenter.dialogCancleClick(2);
                }

                dismiss();
            }
        });

        shot_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onPublishDilaogClickLisenter != null) {
                    onPublishDilaogClickLisenter.dialogCancleClick(1);
                }

                dismiss();
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outDialog();
            }
        });

        mHandler = new Handler();

        if (isShow) {
            inDialog();
        }

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    outDialog();
                    return true;
                }
                return false;
            }
        });
    }

    private void inDialog() {
        dialog_layout.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.publish_dialog_in));
        bottom_layout.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.publish_dialog_bottom_in));
    }

    private void outDialog() {
        dialog_layout.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.publish_dialog_out));
        bottom_layout.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.publish_dialog_bottom_out));
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                dismiss();
            }
        }, 400);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        isShow = false;
    }

    public void dialogShow(FragmentManager manager, String tag) {
        if(isShow){
            return;
        }
        show(manager, tag);
        isShow = true;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    @Override
    public void onResume() {
        super.onResume();


        int screenHeight = ScreensUitls.getScreenHeight(getActivity());
        int statusBarHeght = ScreensUitls.getStatusBarHeight(getActivity());

        Window mWindow = getDialog().getWindow();
        WindowManager.LayoutParams mLayoutParams = mWindow.getAttributes();
        mLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        mLayoutParams.height = screenHeight - statusBarHeght;
        mWindow.setAttributes(mLayoutParams);
    }


    private onPublishDilaogClickLisenter onPublishDilaogClickLisenter;

    public void onPublishDilaogClickLisenter(onPublishDilaogClickLisenter onPublishDilaogClickLisenter) {
        this.onPublishDilaogClickLisenter = onPublishDilaogClickLisenter;
    }

    public interface onPublishDilaogClickLisenter {
        void dialogCancleClick(int index);
    }


}
