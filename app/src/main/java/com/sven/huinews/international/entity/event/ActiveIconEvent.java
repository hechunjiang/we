package com.sven.huinews.international.entity.event;

import com.sven.huinews.international.entity.ActiveIcon;

/**
 * Created by sfy. on 2018/4/19 0019.
 */

public class ActiveIconEvent {
    private ActiveIcon mActiveIcon;

    public ActiveIconEvent(ActiveIcon mActiveIcon) {
        this.mActiveIcon = mActiveIcon;
    }

    public ActiveIcon getmActiveIcon() {
        return mActiveIcon;
    }

    public void setmActiveIcon(ActiveIcon mActiveIcon) {
        this.mActiveIcon = mActiveIcon;
    }
}
