package com.sven.huinews.international.utils.itemDecoration;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;


/**
 * Created by sfy. on 2018/5/8 0008.
 */

public class SmallVideoGridItem extends RecyclerView.ItemDecoration {
    private Context context;

    public SmallVideoGridItem(Context context) {
        this.context = context;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();

        if (lp.getSpanIndex() == 0) {
            outRect.right = 0;
        } else {
            outRect.left = 5;
        }
        outRect.top = 5;

    }
}
