package wedemo.activity.down;

import android.content.Context;

import java.util.List;

import wedemo.base.BaseModel;
import wedemo.base.BasePresenter;
import wedemo.base.BaseView;
import wedemo.config.http.DataResponseCallback;

public interface DownContract {
    abstract class Model extends BaseModel {
        //获取素材分类
        public abstract void onSDKCategroy(Context context, DownRequest request, DataResponseCallback<List<CategoryResponse.DataBean>> callback);

        //获取对应素材列表
        public abstract void onDownload(Context context, DownRequest request, DataResponseCallback<List<DownResponse.DataBean>> callback);
    }

    interface View extends BaseView {
        void setSDKCategroy(List<CategoryResponse.DataBean> mDatas);

        void setSdkDown(List<DownResponse.DataBean> mDatas);

        DownRequest getDownRequest();

        void onDownLoadFinish(DownResponse.DataBean dataBean);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void onSDKCategroy(Context context);

        public abstract void onDownload(Context context);
    }
}
