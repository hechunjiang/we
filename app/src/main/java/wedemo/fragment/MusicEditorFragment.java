package wedemo.fragment;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sven.huinews.international.R;

public class MusicEditorFragment extends Fragment {
    protected String TAG = getClass().getSimpleName();
    protected View rootView;
    private PreviewFragment.OnPlayListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayout(), container, false);
        initView();
        return rootView;
    }

    protected int getLayout() {
        return R.layout.fragment_music_editer;
    }

    private void initView() {

    }

    /**
     * 设置播放监听
     *
     * @param listener
     */
    public void setPlayListener(PreviewFragment.OnPlayListener listener) {
        this.listener = listener;
    }

    protected <T extends View> T findViewById(@IdRes int id) {
        return rootView.findViewById(id);
    }

    protected void t(@NonNull String msg) {
        Toast.makeText(getContext(), "" + msg, Toast.LENGTH_LONG).show();
    }

    protected void i(@NonNull String msg) {
        Log.i(TAG, ">>>>>>>>" + msg);
    }

    protected void e(@NonNull String msg) {
        Log.e(TAG, ">>>>>>>>" + msg);
    }


}
