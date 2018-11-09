package wedemo.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class AlbumItemDecoration extends RecyclerView.ItemDecoration {
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = 5;
        outRect.bottom = 5;
        if (parent.getChildLayoutPosition(view) % 4 == 0) {
            outRect.left = 0;
        }
    }
}
