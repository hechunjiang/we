package com.sven.huinews.international.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * auther: sunfuyi
 * data: 2018/5/11
 * effect:
 */
public class BaseVideoFragment  extends Fragment {
    private boolean __createdUserVisibleHint = false;
    private boolean __firstActivityCreated = true;
    private boolean __fragmentVisible = true;
    public String pageIdentify;

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (this.__firstActivityCreated) {
            this.__firstActivityCreated = false;
            if (this.__createdUserVisibleHint) {
                setUserVisibleHint(true);
            } else {
                this.__createdUserVisibleHint = true;
            }
        }
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            fragmentGone();
        } else if (this.__createdUserVisibleHint) {
            fragmentVisible(this.__fragmentVisible);
            this.__fragmentVisible = false;
        } else {
            this.__createdUserVisibleHint = true;
        }
    }

    public void onPause() {
        super.onPause();
    }

    public void onResume() {
        super.onResume();
    }

    public void onDestroy() {
        super.onDestroy();
    }



    public void fragmentVisible(boolean isFirstVisible) {
    }

    public void fragmentGone() {
    }
}
