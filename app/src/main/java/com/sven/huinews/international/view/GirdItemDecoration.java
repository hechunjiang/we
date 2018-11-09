package com.sven.huinews.international.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by sfy. on 2018/9/20 0020.
 */

public class GirdItemDecoration extends RecyclerView.ItemDecoration {

    private Context context;

    public GirdItemDecoration(Context context) {
        this.context = context;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) view.getLayoutParams();

        if (lp.getSpanIndex() % 3 == 0) {
            outRect.right = 0;
        } else {
            outRect.left = 5;
        }

        if (position < 3) {
            outRect.top = 0;
        } else {
            outRect.top = 5;
        }
    }
}
