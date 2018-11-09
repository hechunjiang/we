package wedemo.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.sven.huinews.international.R;

public class ShotBlockDialog extends Dialog {
    private ImageButton btn_jian, btn_jia;
    private TextView tv_count;
    private RadioButton rb_video_10, rv_video_15, rv_video_30;
    private int count = 4;
    private RadioGroup rg_video;
    private long time = 10;
    private LinearLayout ll_dialog;
    private ImageButton btn_close;

    public ShotBlockDialog(@NonNull Context context) {
        super(context, R.style.dialog);
        setContentView(R.layout.dialog_shot_block);
        // setCancelable(true);
        initView();
    }

    private void initView() {
        btn_jia = findViewById(R.id.btn_jia);
        btn_jian = findViewById(R.id.btn_jian);
        tv_count = findViewById(R.id.tv_count);
        rb_video_10 = findViewById(R.id.rb_video_10);
        rv_video_15 = findViewById(R.id.rb_video_15);
        rv_video_30 = findViewById(R.id.rb_video_30);
        rg_video = findViewById(R.id.rg_video);
        btn_close = findViewById(R.id.btn_close);
        rb_video_10.setChecked(true);
        btn_jia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (count == 10) {
                    return;
                }
                count++;
                tv_count.setText(count + "");
                setData();
            }
        });

        btn_jian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (count == 1) {
                    return;
                }
                count--;
                tv_count.setText(count + "");
                setData();
            }
        });

        rg_video.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rb_video_10) {
                    time = 10;
                    setData();

                } else if (i == R.id.rb_video_15) {
                    time = 15;
                    setData();

                } else if (i == R.id.rb_video_30) {
                    time = 30;
                    setData();

                }
            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


    }


    private void setData() {
        if (mBlockCountLisenter != null) {
            mBlockCountLisenter.setVideoCount(time, count);
        }
    }

    public interface setBlockCountLisenter {
        void setVideoCount(long time, int count);
    }

    private setBlockCountLisenter mBlockCountLisenter;

    public void setmBlockCountLisenter(setBlockCountLisenter mBlockCountLisenter) {
        this.mBlockCountLisenter = mBlockCountLisenter;
    }
}
