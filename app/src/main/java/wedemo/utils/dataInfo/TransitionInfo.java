package wedemo.utils.dataInfo;

import android.util.Log;

import java.util.HashMap;

import wedemo.shot.bean.FilterItem;

public class TransitionInfo {
    public static int TRANSITIONMODE_BUILTIN = 0;
    public static int TRANSITIONMODE_PACKAGE = 1;

    private HashMap<String, FilterItem> itemMap;
    private int m_transitionMode;
    private String m_transitionId;

    public TransitionInfo() {
        m_transitionId = null;
        m_transitionMode = TRANSITIONMODE_BUILTIN;
        itemMap = new HashMap<>();
    }

    public void setFilterItem(String index, FilterItem filterItem) {
        itemMap.put(index, filterItem);
    }

    public HashMap<String, FilterItem> getItemMap() {
        return itemMap;
    }

    public void setTransitionMode(int mode) {
        if (mode != TRANSITIONMODE_BUILTIN && mode != TRANSITIONMODE_PACKAGE) {
            Log.e("", "invalid mode data");
            return;
        }
        m_transitionMode = mode;
    }

    public int getTransitionMode() {
        return m_transitionMode;
    }

    public void setTransitionId(String fxId) {
        m_transitionId = fxId;
    }

    public String getTransitionId() {
        return m_transitionId;
    }
}
