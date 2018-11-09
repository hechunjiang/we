package wedemo.activity.down;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.List;

import wedemo.base.BaseResponse;
import wedemo.config.http.DataResponseCallback;
import wedemo.utils.DownloadUtils;
import wedemo.utils.PathUtils;

public class DownPresenter extends DownContract.Presenter {


    @Override
    public void onSDKCategroy(Context context) {
        mModel.onSDKCategroy(context, mView.getDownRequest(), new DataResponseCallback<List<CategoryResponse.DataBean>>() {
            @Override
            public void onSucceed(List<CategoryResponse.DataBean> s) {
                mView.setSDKCategroy(s);
            }

            @Override
            public void onFail(BaseResponse response) {
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void onDownload(Context context) {
        mModel.onDownload(context, mView.getDownRequest(), new DataResponseCallback<List<DownResponse.DataBean>>() {
            @Override
            public void onSucceed(List<DownResponse.DataBean> s) {
                mView.setSdkDown(s);
            }

            @Override
            public void onFail(BaseResponse response) {
                mView.showErrorTip(response.getCode(),response.getMsg());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * @param data
     * @param type NvAsset.ASSET_CAPTURE_SCENE 脸贴
     */
    public void downSdk(final DownResponse.DataBean data, int type) {
        final String dir = PathUtils.getAssetDownloadPath(type);
        final String checkPath = dir + File.separator + data.getF_id();
        final String path = dir + File.separator + data.getF_id() + ".zip";

        if (data.isDown()) {
            //如果存在
            mView.onDownLoadFinish(data);
            return;
        }

        boolean tmp = PathUtils.fileIsExists(checkPath);
        if (tmp) {
            //如果存在
            data.setDown(true);
            mView.onDownLoadFinish(data);
        } else {

            DownloadUtils.get().download(data.getUrl(), path, data.getName(), new DownloadUtils.OnDownloadListener() {
                @Override
                public void onDownloadSuccess() {
                    boolean ret = PathUtils.unZipFile(path, dir + File.separator);
                    Log.e("weiwei", "ret = " + ret);
                    if (ret) {
                        data.setDown(true);
                        mView.onDownLoadFinish(data);
                    }
                }

                @Override
                public void onDownloading(int progress) {
                    Log.e("weiwei", "down");
                }

                @Override
                public void onDownloadFailed() {
                    mView.showLoading();
                }
            });
        }
    }
}
