package wedemo.activity.down;

import wedemo.base.BaseRequest;

public class DownRequest extends BaseRequest {
    /**
     * 1—-theme
     * 2—-videofx
     * 3—-captionstyle
     * 4—-animatedsticker
     * 5—-videotransition
     * 6—-font(涉及版权暂时不用)
     * 7—-music(涉及版权暂时不用)
     * 8—-capture scene
     * 9—-particle
     **/
    private int type;  //使用场景
//    private int c_id;
//    private int page;
//    private int page_size;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

//    public int getCategory() {
//        return category;
//    }
//
//    public void setCategory(int category) {
//        this.category = category;
//    }
//
//    public int getPage() {
//        return page;
//    }
//
//    public void setPage(int page) {
//        this.page = page;
//    }
//
//    public int getPage_size() {
//        return page_size;
//    }
//
//    public void setPage_size(int page_size) {
//        this.page_size = page_size;
//    }

}
