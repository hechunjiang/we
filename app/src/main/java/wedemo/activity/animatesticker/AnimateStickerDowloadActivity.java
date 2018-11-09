package wedemo.activity.animatesticker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sven.huinews.international.R;

import wedemo.activity.download.AssetListFragment;
import wedemo.activity.interfaces.OnTitleBarClickListener;
import wedemo.activity.view.CustomTitleBar;
import wedemo.base.BaseActivity;
import wedemo.utils.AppManager;
import wedemo.utils.asset.NvAsset;
import wedemo.utils.dataInfo.TimelineData;


public class AnimateStickerDowloadActivity extends BaseActivity {
    private CustomTitleBar mTitleBar;
    AssetListFragment mAssetListFragment;

    @Override
    public int getLayoutId() {
        return R.layout.activity_animate_sticker_dowload;
    }

    @Override
    public void initView() {
        mTitleBar = (CustomTitleBar) findViewById(R.id.title_bar);
    }

    @Override
    public void initObject() {
        initAssetFragment();
    }

    @Override
    public void initEvents() {
        mTitleBar.setOnTitleBarClickListener(new OnTitleBarClickListener() {
            @Override
            public void OnBackImageClick() {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
            }

            @Override
            public void OnNextTextClick() {

            }

        });
    }

    @Override
    public void onClickEvent(View v) {

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        AppManager.getInstance().finishActivity();
        super.onBackPressed();
    }

    private void initAssetFragment() {
        Bundle bundle = new Bundle();
        bundle.putInt("ratio", TimelineData.instance().getMakeRatio());
        bundle.putInt("assetType", NvAsset.ASSET_ANIMATED_STICKER);
        mAssetListFragment = new AssetListFragment();
        mAssetListFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.spaceLayout, mAssetListFragment)
                .commit();
    }
}
