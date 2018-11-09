package wedemo.view;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sven.huinews.international.R;
import com.sven.huinews.international.utils.Common;

public class ToastView implements View.OnClickListener {

    private Toast mToast;
    private TimeCount timeCount;
    private Handler mHandler = new Handler();
    private boolean canceled = true;

    private TextView facebookShare;
    private TextView twitterShare;
    private ImageButton iv_close;

    public ToastView(Context context) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //自定义布局
        View view = inflater.inflate(R.layout.toast_share, null);

        facebookShare = view.findViewById(R.id.toast_share_facebok_zoom_tv);
        twitterShare = view.findViewById(R.id.toast_share_twitter_tv);
        iv_close = view.findViewById(R.id.iv_close);

        //自定义toast文本
        if (mToast == null) {
            mToast = new Toast(context);
        }
        //设置toast居中显示
        mToast.setGravity(Gravity.TOP, 0, 0);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.setView(view);

        iv_close.setOnClickListener(this);
        facebookShare.setOnClickListener(this);
        twitterShare.setOnClickListener(this);
    }

    /**
     */
    public void show() {
        mToast.show();
    }

    /**
     * @param duration
     */
    public void show(int duration) {
        timeCount = new TimeCount(duration, 1000);
        if (canceled) {
            timeCount.start();
            canceled = false;
            showUntilCancel();
        }
    }

    /**
     * 隐藏toast
     */
    private void hide() {
        if (mToast != null) {
            mToast.cancel();
        }
        canceled = true;
    }

    private void showUntilCancel() {
        if (canceled) { //如果已经取消显示，就直接return
            return;
        }
        mToast.show();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showUntilCancel();
            }
        }, Toast.LENGTH_LONG);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toast_share_facebok_zoom_tv:
                setShare(Common.SHARE_TYPE_FACEBOOK);
                break;
            case R.id.toast_share_twitter_tv:
                setShare(Common.SHARE_TYPE_TWITTER);
                break;
            case R.id.iv_close:
                hide();
                break;
        }
    }

    private void setShare(int type) {
        hide();
        if (onUploadShareListener != null) {
            onUploadShareListener.onShare(type);
        }
    }

    public interface OnUploadShareListener {
        void onShare(int type);
    }

    private OnUploadShareListener onUploadShareListener;

    public void setOnUploadShareListener(OnUploadShareListener onUploadShareListener) {
        this.onUploadShareListener = onUploadShareListener;
    }

    /**
     *  自定义计时器
     */
    private class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval); //millisInFuture总计时长，countDownInterval时间间隔(一般为1000ms)
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            hide();
        }
    }

}
