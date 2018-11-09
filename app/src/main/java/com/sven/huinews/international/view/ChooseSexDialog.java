package com.sven.huinews.international.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sven.huinews.international.R;

/**
 * Created by sfy. on 2018/9/13 0013.
 */

public class ChooseSexDialog extends Dialog {
    private TextView tv_1, tv_2;
    private String tv1, tv2;

    private int type;

    public ChooseSexDialog(@NonNull Context context) {
        super(context, R.style.dialog);
        initView();
    }

    public ChooseSexDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    protected ChooseSexDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }


    private void initView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.choose_photo_dialog, null);
        setContentView(v);
        setCanceledOnTouchOutside(true);

        tv_1 = v.findViewById(R.id.tv_1);
        tv_2 = v.findViewById(R.id.tv_2);
        tv_1.setText(getContext().getString(R.string.male));
        tv_2.setText(getContext().getString(R.string.female));
        tv_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onCheckInLisenter != null) {
                    onCheckInLisenter.toChooseSex(2);
                }
            }
        });

        tv_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onCheckInLisenter != null) {
                    onCheckInLisenter.toChooseSex(1);
                }
            }
        });

    }


    public interface OnCheckInLisenter {
        void toChooseSex(int type);
    }

    private OnCheckInLisenter onCheckInLisenter;

    public void setOnCheckInLisenter(OnCheckInLisenter l) {
        this.onCheckInLisenter = l;
    }

}
