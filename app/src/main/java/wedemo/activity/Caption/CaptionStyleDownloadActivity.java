package wedemo.activity.Caption;//package com.mobile.wedemo.activity.Caption;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//
//
//import com.mobile.wedemo.R;
//import com.mobile.wedemo.activity.interfaces.OnTitleBarClickListener;
//import com.mobile.wedemo.activity.view.CustomTitleBar;
//import com.mobile.wedemo.base.BaseActivity;
//import com.mobile.wedemo.utils.AppManager;
//import com.mobile.wedemo.utils.asset.NvAsset;
//import com.mobile.wedemo.utils.dataInfo.TimelineData;
//
//public class CaptionStyleDownloadActivity extends BaseActivity {
//    private CustomTitleBar mTitleBar;
//    AssetListFragment mAssetListFragment;
//
//    @Override
//    public int getLayoutId() {
//        return R.layout.activity_caption_style_download;
//    }
//
//
//
//    @Override
//    protected void initData() {
//        initAssetFragment();
//    }
//
//    @Override
//    protected void initListener() {
//        mTitleBar.setOnTitleBarClickListener(new OnTitleBarClickListener() {
//            @Override
//            public void OnBackImageClick() {
//                Intent intent = new Intent();
//                setResult(RESULT_OK, intent);
//            }
//
//            @Override
//            public void OnCenterTextClick() {
//
//            }
//
//            @Override
//            public void OnRightTextClick() {
//
//            }
//        });
//    }
//
//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent();
//        setResult(RESULT_OK, intent);
//        AppManager.getInstance().finishActivity();
//        super.onBackPressed();
//    }
//
//
//
//    @Override
//    public void initView() {
//
//    }
//
//    @Override
//    public void initEvents() {
//
//    }
//
//    @Override
//    public void onClickEvent(View v) {
//
//    }
//
//    @Override
//    public void onClick(View v) {
//
//    }
//
//    @Override
//    public void initObject() {
//
//    }
//
//    private void initAssetFragment() {
//        Bundle bundle = new Bundle();
//        bundle.putInt("ratio", TimelineData.instance().getMakeRatio());
//        bundle.putInt("assetType", NvAsset.ASSET_CAPTION_STYLE);
//        mAssetListFragment = new AssetListFragment();
//        mAssetListFragment.setArguments(bundle);
//        getFragmentManager().beginTransaction()
//                .add(R.id.spaceLayout, mAssetListFragment)
//                .commit();
//        getFragmentManager().beginTransaction().show(mAssetListFragment);
//    }
//}
