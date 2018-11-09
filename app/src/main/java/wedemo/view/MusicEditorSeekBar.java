package wedemo.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sven.huinews.international.R;

public class MusicEditorSeekBar extends LinearLayout {
    private TextView seekBar_current_time, seekBar_max_time;
    private SeekBar seekBar_cursor;

    public MusicEditorSeekBar(Context context) {
        this(context, null);
    }

    public MusicEditorSeekBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MusicEditorSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_music_editor, this);
        seekBar_current_time = view.findViewById(R.id.seekBar_current_time);
        seekBar_max_time = view.findViewById(R.id.seekBar_max_time);
        seekBar_cursor = view.findViewById(R.id.seekBar_cursor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
