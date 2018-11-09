package wedemo.activity.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sven.huinews.international.R;

import wedemo.activity.interfaces.OnTitleBarClickListener;


/**
 * Created by CaoZhiChao on 2018/5/28 15:10
 */
public class CustomTitleBar extends RelativeLayout implements View.OnClickListener {
    private ImageView back;
    private TextView next;
    private RelativeLayout btn_back;
    OnTitleBarClickListener onTitleBarClickListener;

    public CustomTitleBar(Context context) {
        super(context);
        init(context);
    }

    private void init(final Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_titlebar_meishe, this, true);

        back = findViewById(R.id.iv_back_s);
        next = findViewById(R.id.next);
        btn_back = findViewById(R.id.btn_back_s);

        next.setOnClickListener(this);
        btn_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onTitleBarClickListener != null) {
                    onTitleBarClickListener.OnBackImageClick();
                }
            }
        });
    }

    public void setRightText(String text) {
        next.setText(text);
    }

    public CustomTitleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomTitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    public void setOnTitleBarClickListener(OnTitleBarClickListener onTitleBarClickListener) {
        this.onTitleBarClickListener = onTitleBarClickListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_back) {
            if (onTitleBarClickListener != null) {
                onTitleBarClickListener.OnBackImageClick();
            }
            // AppManager.getInstance().finishActivity();

        } else if (i == R.id.next) {
            if (onTitleBarClickListener != null) {
                onTitleBarClickListener.OnNextTextClick();
            }
        }
    }
}
