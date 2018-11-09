package wedemo.shot.bean;

import android.util.Log;

public class FilterItem {
    public static int FILTERMODE_BUILTIN = 0;
    public static int FILTERMODE_BUNDLE = 1;
    public static int FILTERMODE_PACKAGE = 2;

    private String m_filterName;
    private int m_filterMode;
    private String m_filterId;
    private int m_imageId;
    private String m_imageUrl;
    private String m_packageId;
    private String englishName;

    public FilterItem() {
        m_filterId = null;
        m_filterName = null;
        m_filterMode = FILTERMODE_BUILTIN;
        m_imageId = 0;
        m_imageUrl = null;
        m_packageId = null;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public void setFilterName(String name) {
        m_filterName = name;
    }

    public String getFilterName() {
        return m_filterName;
    }

    public void setFilterMode(int mode) {
        if (mode != FILTERMODE_BUILTIN && mode != FILTERMODE_BUNDLE && mode != FILTERMODE_PACKAGE) {
            Log.e("", "invalid mode data");
            return;
        }
        m_filterMode = mode;
    }

    public int getFilterMode() {
        return m_filterMode;
    }

    public void setFilterId(String fxId) {
        m_filterId = fxId;
    }

    public String getFilterId() {
        return m_filterId;
    }

    public void setImageId(int imageId) {
        m_imageId = imageId;
    }

    public int getImageId() {
        return m_imageId;
    }

    public void setImageUrl(String imageUrl) {
        m_imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return m_imageUrl;
    }

    public void setPackageId(String packageId) {
        m_packageId = packageId;
    }

    public String getPackageId() {
        return m_packageId;
    }
}
