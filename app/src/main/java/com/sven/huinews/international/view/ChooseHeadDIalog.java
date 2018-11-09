package com.sven.huinews.international.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sven.huinews.international.R;


public class ChooseHeadDIalog extends Dialog {

    private TextView tv_1, tv_2;
    private String tv1, tv2;

    private int type;
    private Context mContext;

    public ChooseHeadDIalog(@NonNull Context context) {
        super(context, R.style.dialog);

        initView();
    }

    public ChooseHeadDIalog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    protected ChooseHeadDIalog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public void setDialogType(int type) {
        this.type = type;
        if (type == 1) {
            tv1 = getContext().getResources().getString(R.string.album_import);
            tv2 =getContext().getResources().getString(R.string.Sanp);
        } else {
            tv1 = getContext().getResources().getString(R.string.male);
            tv2 = getContext().getResources().getString(R.string.female);
        }
        tv_1.setText(tv1);
        tv_2.setText(tv2);
        show();
    }

    private void initView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.choose_photo_dialog, null);
        setContentView(v);
        setCanceledOnTouchOutside(true);

        tv_1 = v.findViewById(R.id.tv_1);
        tv_2 = v.findViewById(R.id.tv_2);
        tv_1.setText(getContext().getResources().getString(R.string.album_import));
        tv_2.setText(getContext().getResources().getString(R.string.Sanp));
        tv_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onCheckInLisenter != null) {
                    onCheckInLisenter.toPhoto(1);
                }
            }
        });

        tv_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onCheckInLisenter != null) {
                    onCheckInLisenter.toPhoto(2);
                }
            }
        });

    }


    public interface OnCheckInLisenter {
        void toPhoto(int type);
    }

    private OnCheckInLisenter onCheckInLisenter;

    public void setOnCheckInLisenter(OnCheckInLisenter l) {
        this.onCheckInLisenter = l;
    }


}