package wedemo.activity;//package com.sven.huinews.international.main.shot.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.meicam.sdk.NvsAssetPackageManager;
import com.meicam.sdk.NvsLiveWindow;
import com.meicam.sdk.NvsRational;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.sven.huinews.international.AppConfig;
import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseActivity;
import com.sven.huinews.international.base.BaseResponse;
import com.sven.huinews.international.config.Constant;
import com.sven.huinews.international.entity.response.TagResponse;
import com.sven.huinews.international.main.login.activity.LoginActivity;
import com.sven.huinews.international.service.UploadService;
import com.sven.huinews.international.utils.ActivityManager;
import com.sven.huinews.international.utils.CommonUtils;
import com.sven.huinews.international.utils.ImageUtils;
import com.sven.huinews.international.utils.LogUtil;
import com.sven.huinews.international.utils.ToastUtils;
import com.sven.huinews.international.utils.cache.UserSpCache;
import com.sven.huinews.international.utils.shot.utils.PathUtils;
import com.sven.huinews.international.utils.statusbar.Eyes;
import com.sven.huinews.international.view.EdittextUtlis;
import com.sven.huinews.international.view.dialog.CustomLoginDialog;
import com.sven.huinews.international.view.dialog.UploadProgressDialog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import wedemo.MessageEvent;
import wedemo.activity.data.PublishInfo;
import wedemo.activity.data.WcsBean;
import wedemo.activity.request.WcsRequest;
import wedemo.adapter.TagsAdapter;
import wedemo.contract.PublishContract;
import wedemo.model.PublishModel;
import wedemo.presenter.PublishPresenter;
import wedemo.utils.Constants;
import wedemo.utils.CustomTimelineUtil;
import wedemo.utils.TimelineManager;
import wedemo.utils.UpdatePhotoUtils;
import wedemo.utils.dataInfo.CustomTimeLine;
import wedemo.utils.dataInfo.TimelineData;

public class PublishActivity extends BaseActivity<PublishPresenter, PublishModel> implements PublishContract.View {
    private RecyclerView tags_rv;
    private ImageButton btn_close;
    private TagsAdapter mTagsAdapter;
    private NvsLiveWindow liveWindow;
    private NvsStreamingContext mStreamingContext;
    private NvsTimeline mTimeline;
    private TextView btn_publish;
    private TextView et_input;
    private TextView btn_tag;
    private List<TagResponse.DataBean> mTagsDatas; //选中的tag
    private String content; //标题
    private CustomTimeLine masterTimeline;
    private String imagePath;
    private String path;
    private ImageButton ib_push;
    private TextView tv_draft;

    private boolean isComplie = false;  //是否已经合成视频
    private UploadProgressDialog mDialog;
    private CustomTimeLine waterTimeLine;

    private CustomLoginDialog loginDialog;

    private boolean hasSave = false;

    @Override
    public int getLayoutId() {
        Eyes.translucentStatusBar(this, true);
        ActivityManager.getInstance().pushOneActivity(this);
        EventBus.getDefault().register(this);
        return R.layout.activity_publish;
    }

    @Override
    public void initView() {
        liveWindow = findViewById(R.id.liveWindow);
        btn_publish = findViewById(R.id.btn_publish);
        //  btn_tag = findViewById(R.id.btn_tag);
        tags_rv = findViewById(R.id.tags_rv);
        et_input = findViewById(R.id.et_input);
        btn_close = findViewById(R.id.btn_close);
        tv_draft = findViewById(R.id.tv_draft);

        ib_push = findViewById(R.id.ib_push);


        mDialog = UploadProgressDialog.initGrayDialog(this);
        mDialog.setCancelable(false);

        loginDialog = new CustomLoginDialog(this,this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDialog.dismiss();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initEvents() {
        btn_publish.setOnClickListener(this);
        btn_close.setOnClickListener(this);
        et_input.setOnClickListener(this);
        ib_push.setOnClickListener(this);
        tv_draft.setOnClickListener(this);

        loginDialog.setThirdLogin(new CustomLoginDialog.ThirdLoginResult() {
            @Override
            public void onOtherfail() {
                if(mDialog != null){
                    mDialog.dismiss();
                }
            }

            @Override
            public void onCheckFail(BaseResponse response) {
                if(mDialog != null){
                    mDialog.dismiss();
                }
            }

            @Override
            public void onLoginSuccess(String json, boolean isBind) {
                if (isLogin()) {
                    mDialog.show();
                    mDialog.setInfo(getString(R.string.image_upload));
                    mPresenter.onWcsToken(wcsRequest, request);
                }
            }

            @Override
            public void onStartCheck() {

            }

            @Override
            public void onGetGoldSuccess(boolean isBind) {
//                if (isLogin()) {
//                    mDialog.show();
//                    mDialog.setInfo(getString(R.string.image_upload));
//                    mPresenter.onWcsToken(wcsRequest, request);
//                }
            }

            @Override
            public void onGetGoldFail() {
//                if (isLogin()) {
//                    mDialog.show();
//                    mDialog.setInfo(getString(R.string.image_upload));
//                    mPresenter.onWcsToken(wcsRequest, request);
//                }
            }

            @Override
            public void onLogin() {
//                if (isLogin()) {
//                    mDialog.show();
//                    mDialog.setInfo(getString(R.string.image_upload));
//                    mPresenter.onWcsToken(wcsRequest, request);
//                }
            }
        });
//        loginDialog.setThirdLogin(new CustomLoginDialog.ThirdLoginResult() {
//            @Override
//            public void onOtherfail() {
//                loginDialog.dismiss();
//            }
//
//            @Override
//            public void onCheckFail(BaseResponse response) {
//                loginDialog.dismiss();
//            }
//
//            @Override
//            public void onLoginSuccess(String json) {
//                loginDialog.dismiss();
//                if (isLogin()) {
//                    mDialog.show();
//                    mDialog.setInfo(getString(R.string.image_upload));
//                    mPresenter.onWcsToken(wcsRequest, request);
//                }
//            }
//        });
        //   btn_tag.setOnClickListener(this);
    }

    private void initWaterMark() {
        waterTimeLine = CustomTimelineUtil.createcCopyMasterTimeline();

        String waterLic = "assets:/watermark/FBBA9FBD-18BB-ACC2-EFF5-01B9D0F4F28D.lic";
        String waterAnim = "assets:/watermark/FBBA9FBD-18BB-ACC2-EFF5-01B9D0F4F28D.animatedsticker";
        String themeId = installSingleCaptureScene(waterAnim, waterLic);

        if (themeId != null) {
            CustomTimelineUtil.setWaterSticker(waterTimeLine, themeId, 720 / 2f, 1080 / 2f);
        }
    }

    private String installSingleCaptureScene(String path, String licpath) {
        StringBuilder themeId = new StringBuilder();
        int error = mStreamingContext.getAssetPackageManager().installAssetPackage(path, licpath,
                NvsAssetPackageManager.ASSET_PACKAGE_TYPE_ANIMATEDSTICKER, true, themeId);
        if (error != NvsAssetPackageManager.ASSET_PACKAGE_MANAGER_ERROR_NO_ERROR &&
                error != NvsAssetPackageManager.ASSET_PACKAGE_MANAGER_ERROR_ALREADY_INSTALLED) {
            Log.e("weiwei", "Failed to install package!" + path);
            return null;
        }

        if (error == NvsAssetPackageManager.ASSET_PACKAGE_MANAGER_ERROR_ALREADY_INSTALLED) {
            error = mStreamingContext.getAssetPackageManager().upgradeAssetPackage(path, licpath,
                    NvsAssetPackageManager.ASSET_PACKAGE_TYPE_ANIMATEDSTICKER, true, themeId);
            if (error != NvsAssetPackageManager.ASSET_PACKAGE_MANAGER_ERROR_NO_ERROR) {
                Log.e("weiwei", "Failed to upgrade package!" + path);
            }
        }
        return themeId.toString();
    }

    private void editLisenter() {
        EdittextUtlis.showCommentEdit(PublishActivity.this, et_input, new EdittextUtlis.liveCommentResult() {
            @Override
            public void onResult(boolean confirmed, String comment) {
                if (confirmed) {
                    et_input.setText(comment);
                }
            }
        });
        EdittextUtlis.setSendText(getString(R.string.ok));
    }

    public boolean isLogin() {
        if (!UserSpCache.getInstance(AppConfig.getAppContext()).getBoolean(Constant.KEY_IS_USER_LOGIN)) {
            mDialog.dismiss();
            Bundle bundle = new Bundle();
            bundle.putInt(Constant.OTHER_LOGIN, 1);

            loginDialog.show();
            //startActivity(LoginActivity.class, bundle);
            return false;
        } else {
            return true;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals(Constant.LOGIN_SUCCESS)) {
            if (isLogin()) {
                mDialog.show();
                mPresenter.onWcsToken(wcsRequest, request);
            }

        }
    }



    @Override
    public void onClickEvent(View v) {
        if (v == btn_publish) {

            if (isComplie) {
                if (isLogin()) {
                    mDialog.show();
                    mPresenter.onWcsToken(wcsRequest, request);
                }
            } else {
                compileVideo();
            }

        } else if (v == btn_close) {
            finish();
        } else if (v == et_input) {
            editLisenter();
        } else if (v == ib_push) {
            startActivityForResult(SelectImageActivity.class, 1001);
        } else if(v == tv_draft){

            if(hasSave){
                ToastUtils.showShort(this,R.string.tip_dratfs);
                return;
            }

            TimelineManager.getInstance().save();
            hasSave = true;
            ToastUtils.showShort(this,R.string.tip_dratfs);
        }
    }

    @Override
    public void initObject() {
        setMVP();
        initLiveWindow();
        initTags();
        //initWaterMark();

    }


    /**
     * 定位
     */
    private float longitude = 0;
    private float latitude = 0;
    private String country = null;


    private void initTags() {
        tags_rv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mTagsAdapter = new TagsAdapter(mContext);
        tags_rv.setAdapter(mTagsAdapter);

        mTagsAdapter.setmOnItemClickLisenter(new TagsAdapter.OnItemClickLisenter() {
            @Override
            public void onItemClick(int position) {
                if (position == 0) {
                    Intent intent = new Intent(mContext, TagActivity.class);
                    startActivityForResult(intent, 1000);
                }
            }
        });
    }

    private void initLiveWindow() {
        mStreamingContext = NvsStreamingContext.getInstance();
        masterTimeline = TimelineManager.getInstance().getMasterTimeline();

        if (masterTimeline != null) {
            mTimeline = masterTimeline.getTimeline();
        }

        if (null == mTimeline) {
            LogUtil.showLog("mTimeline is null!");
            return;
        }

        //initWaterMark();

        liveWindow.setFillMode(NvsLiveWindow.FILLMODE_STRETCH);
        mStreamingContext.connectTimelineWithLiveWindow(mTimeline, liveWindow);
        //playVideo(mStreamingContext.getTimelineCurrentPosition(mTimeline), mTimeline.getDuration());
        playVideo(0, mTimeline.getDuration());

        mStreamingContext.setPlaybackCallback(new NvsStreamingContext.PlaybackCallback() {
            @Override
            public void onPlaybackPreloadingCompletion(NvsTimeline nvsTimeline) {

            }

            @Override
            public void onPlaybackStopped(NvsTimeline nvsTimeline) {

            }

            @Override
            public void onPlaybackEOF(NvsTimeline nvsTimeline) {
                LogUtil.showLog("play over");
                playVideo(0, mTimeline.getDuration());
            }
        });

    }

    public void playVideo(long startTime, long endTime) {
        // 播放视频
        mStreamingContext.playbackTimeline(mTimeline, startTime, endTime, NvsStreamingContext.VIDEO_PREVIEW_SIZEMODE_LIVEWINDOW_SIZE, true, 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mDialog.dismiss();
    }

    private String test(String t) {
        File captureDir = new File(Environment.getExternalStorageDirectory(), "watch$earn" + File.separator + "Record");
        if (!captureDir.exists() && !captureDir.mkdirs()) {
            return null;
        }
        String filename = t + ".mp4";

        File file = new File(captureDir, filename);
        if (file.exists()) {
            file.delete();
        }

        return file.getAbsolutePath();
    }


    final int[] pixls = new int[]{
            NvsStreamingContext.COMPILE_VIDEO_RESOLUTION_GRADE_360,
            NvsStreamingContext.COMPILE_VIDEO_RESOLUTION_GRADE_480,
            NvsStreamingContext.COMPILE_VIDEO_RESOLUTION_GRADE_720,
            NvsStreamingContext.COMPILE_VIDEO_RESOLUTION_GRADE_1080,
            NvsStreamingContext.COMPILE_VIDEO_RESOLUTION_GRADE_2160
    };

    final int[] rates = new int[]{
            NvsStreamingContext.VIDEO_CAPTURE_RESOLUTION_GRADE_LOW,
            NvsStreamingContext.VIDEO_CAPTURE_RESOLUTION_GRADE_MEDIUM,
            NvsStreamingContext.VIDEO_CAPTURE_RESOLUTION_GRADE_HIGH
    };


    private PublishInfo request;
    private WcsRequest wcsRequest;
    private String fileName;

    private void complieWater() {
        path = PathUtils.getRecordVideoPath(fileName + "_water");
        mStreamingContext.compileTimeline(waterTimeLine.getTimeline(), 0, waterTimeLine.getTimeline().getDuration(), path, pixls[3], rates[1], NvsStreamingContext.STREAMING_ENGINE_COMPILE_FLAG_DISABLE_HARDWARE_ENCODER);

        mStreamingContext.setCompileCallback(new NvsStreamingContext.CompileCallback() {
            @Override
            public void onCompileProgress(NvsTimeline nvsTimeline, int i) {
                mDialog.setMessage((50+i/2) + "%");
            }

            @Override
            public void onCompileFinished(NvsTimeline nvsTimeline) {
                request.setWarterVideoPath(path);

                if (isLogin()) {
                    mPresenter.onWcsToken(wcsRequest, request);
                }

            }

            @Override
            public void onCompileFailed(NvsTimeline nvsTimeline) {
                LogUtil.showLog("nvsTimeline error");
                mDialog.dismiss();
                ToastUtils.showShort(mContext,getString(R.string.sendCodeerror));
            }
        });
    }


    public void compileVideo() {
        mDialog.show();
        fileName = PathUtils.getCharacterAndNumber();
        path = PathUtils.getRecordVideoPath(fileName);

        mStreamingContext.compileTimeline(mTimeline, 0, mTimeline.getDuration(), path, pixls[3], rates[1], NvsStreamingContext.STREAMING_ENGINE_COMPILE_FLAG_DISABLE_HARDWARE_ENCODER);
        //mStreamingContext.compileTimeline(waterTimeLine.getTimeline(), 0, waterTimeLine.getTimeline().getDuration(), path, pixls[3], rates[1], 0);

        mDialog.setInfo(getString(R.string.synthesis_video));
        mStreamingContext.setCompileCallback(new NvsStreamingContext.CompileCallback() {
            @Override
            public void onCompileProgress(NvsTimeline nvsTimeline, int i) {
                mDialog.setMessage(i + "%");
            }

            @Override
            public void onCompileFinished(NvsTimeline nvsTimeline) {

                if (TextUtils.isEmpty(imagePath)) {
                    Bitmap bitmap = mStreamingContext.grabImageFromTimeline(nvsTimeline, mStreamingContext.getTimelineCurrentPosition(nvsTimeline), new NvsRational(16, 9));
                    File file = ImageUtils.saveBitmapToSdCard(bitmap);
                    imagePath = file.getAbsolutePath();
                }

                UpdatePhotoUtils.udpateMedia(PublishActivity.this, path);

                isComplie = true;

                request = new PublishInfo();
                request.setDuration(mTimeline.getDuration() / 1000);
                request.setHeight(1920);
                request.setWidth(1080);
                request.setTitle(et_input.getText().toString());
                request.setImagePath(imagePath);
                request.setVideoPath(path);
                request.setTag(getTags());
                request.setCountry(country);
                request.setLat(latitude);
                request.setLng(longitude);

                wcsRequest = new WcsRequest();
                wcsRequest.setDuration(mTimeline.getDuration() / 1000);
                wcsRequest.setHeight(1920);
                wcsRequest.setWidth(1080);
                wcsRequest.setTitle(et_input.getText().toString());
                wcsRequest.setTag(getTags());

                if (isLogin()) {
                    mPresenter.onWcsToken(wcsRequest, request);
                }
                //complieWater();
                //mPresenter.onWcsToken(wcsRequest, request);

            }

            @Override
            public void onCompileFailed(NvsTimeline nvsTimeline) {
                LogUtil.showLog("nvsTimeline error");
                mDialog.dismiss();
                ToastUtils.showShort(mContext,getString(R.string.sendCodeerror));
            }
        });
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {
        mDialog.dismiss();
    }

    @Override
    public void showErrorTip(int code, final String msg) {

        if (code == 100001) {
            LogUtil.showLog("设置上传==="+Thread.currentThread().getName());
            mDialog.setInfo(getString(R.string.image_upload));

        } else if (code == 100002) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDialog.setMessage(msg);
                }
            });
        } else if(code == 100003) {
            LogUtil.showLog("设置上传100003==="+Thread.currentThread().getName());
            mDialog.dismiss();
        }else {
            mDialog.dismiss();
            ToastUtils.showShort(this, msg);
            long cur_time = mStreamingContext.getTimelineCurrentPosition(mTimeline);
            playVideo(cur_time, mTimeline.getDuration());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        loginDialog.registerCallBack(requestCode,resultCode,data,false);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                mTagsDatas = (List<TagResponse.DataBean>) data.getSerializableExtra("Tags");
                mTagsAdapter.setDatas(mTagsDatas);
            } else if (requestCode == 1001) {  //选择封面
                imagePath = data.getStringExtra("imagePath");

                File file = new File(imagePath);
                if (file.exists()) {
                    Bitmap bm = BitmapFactory.decodeFile(imagePath);
                    //将图片显示到ImageView中
                    ib_push.setImageBitmap(bm);
                }
                initLiveWindow();
            }
        }
    }

    private String getTags() {
        if (mTagsDatas == null) {
            return "";
        }
        List<String> mTags = new ArrayList<>();
        for (int i = 0; i < mTagsDatas.size(); i++) {
            if (mTagsDatas.get(i).getTag() != null) {
                mTags.add(mTagsDatas.get(i).getTag());
            }
        }
        String tag = CommonUtils.getStringByStringList(mTags, ",");
        return tag;
    }


    @Override
    public void setService(WcsBean dataBean, PublishInfo videoInfo) {
        mDialog.dismiss();
        //mProgressDialog.cancel();

        clearData();
        ActivityManager.getInstance().finishActivity();
        LogUtil.showLog("启动service" + Thread.currentThread().getName());
        Intent intent = new Intent(mContext, UploadService.class);
        intent.putExtra("tokenInfo", dataBean);
        intent.putExtra("videoInfo", videoInfo);
        startService(intent);

    }

    private void clearData() {
        //TimelineManager.getInstance().save();
        TimelineManager.getInstance().clear();
        TimelineData.instance().clear();
        TimelineData.instance().setMasterMusic(null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mStreamingContext != null) {
            mStreamingContext.stop();
        }

        mDialog.dismiss();

        if(Constants.UMENG_TEST){
            MobclickAgent.onPageEnd("PublishActivity");
            MobclickAgent.onPause(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        long cur_time = mStreamingContext.getTimelineCurrentPosition(mTimeline);
//        playVideo(cur_time, mTimeline.getDuration());
        initLiveWindow();

        //获取当前屏幕内容的高度
        int screenHeight = getWindow().getDecorView().getHeight();
        getWindow().getDecorView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                //获取View可见区域的bottom
                Rect rect = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                if (bottom != 0 && oldBottom != 0 && bottom - rect.bottom <= 0) {
                    if (onSoftChangeListener != null) {
                        onSoftChangeListener.onSoftChange(true);
                    }
                } else {
                    if (onSoftChangeListener != null) {
                        onSoftChangeListener.onSoftChange(false);
                    }
                }
            }
        });


        if(Constants.UMENG_TEST){
            MobclickAgent.onPageStart("PublishActivity");
            MobclickAgent.onResume(this);
        }

    }


    public static OnSoftInputListener onSoftChangeListener;


    public interface OnSoftInputListener {
        void onSoftChange(boolean hide);
    }


}
