package wedemo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.util.AttributeSet;
import android.view.View;

import com.sven.huinews.international.R;

public class MusicEditorProgressBar extends View {

    private Paint paint = new Paint();

    private long maxTime, videoTime, progress;
    private int startP, videoWidth;
    private float progressWidth;
    private Bitmap bitmap, bitmap2;

    public MusicEditorProgressBar(Context context) {
        this(context, null);
    }

    public MusicEditorProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MusicEditorProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_music_g);
        bitmap2 = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_seek_bar);
    }

    public void mBackground(@RawRes int id) {
        bitmap = BitmapFactory.decodeResource(getResources(), id);
        invalidate();
    }

    public void mFloat(@RawRes int id) {
        bitmap2 = BitmapFactory.decodeResource(getResources(), id);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, canvas.getClipBounds(), new Rect(0, 0, getRight(), getBottom()), paint);
        canvas.drawBitmap(bitmap2, canvas.getClipBounds(), new Rect(startP, 0, startP + videoWidth, getHeight()), paint);
    }

    public void setMaxTime(long maxTime) {
        this.maxTime = maxTime;
    }

    public void setVideoTime(long videoTime) {
        this.videoTime = videoTime;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (progressWidth == 0) progressWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        if (videoWidth == 0) videoWidth = (int) (videoTime * progressWidth / maxTime);
    }

    public void setProgress(long progress) {
        this.progress = progress;
        if (maxTime == 0 || videoTime == 0) return;
        if (videoTime > maxTime) return;
        startP = (int) (progressWidth * progress / maxTime);
        invalidate();
    }
}
