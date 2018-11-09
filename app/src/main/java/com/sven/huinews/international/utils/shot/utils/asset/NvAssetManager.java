package com.sven.huinews.international.utils.shot.utils.asset;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.meicam.sdk.NvsAssetPackageManager;
import com.meicam.sdk.NvsStreamingContext;
import com.sven.huinews.international.utils.shot.utils.PathUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by shizhouhu on 2018/6/14.
 */

public class NvAssetManager implements NvHttpRequest.NvHttpRequestListener, NvsAssetPackageManager.AssetPackageManagerCallback {
    private static final String TAG = "NvAssetManager ";
    private static NvAssetManager m_instance = null;
    private NvHttpRequest m_httpRequest;
    /**
     * 所有素材字典，包括本地素材和在线素材
     */
    private HashMap<String, ArrayList<NvAsset>> assetDict;
    /**
     * 同时下载限制个数
     */
    private int maxConcurrentAssetDownloadNum = 10;
    /**
     * 等待下载队列
     */
    private ArrayList<String> pendingAssetsToDownload = new ArrayList<>();
    /**
     * 正在下载的个数
     */
    private int downloadingAssetsCounter;
    /**
     * 在线素材的顺序表
     */
    private HashMap<String, ArrayList<String>> remoteAssetsOrderedList;
    /**
     * 自定义贴纸素材。说明：自定义贴纸只有自定义图片的路径和自定义模板的包，没有单独的自定义贴纸包，所以需要单独存储。
     * 并且自定义贴纸的信息存储在User defaults里面。
     */
    private ArrayList<NvCustomStickerInfo> customStickerArray;
    /**
     * 是否同步安装素材，默认同步安装。
     */
    public boolean isSyncInstallAsset;

    private String assetDownloadDirPathTheme;
    private String assetDownloadDirPathFilter;
    private String assetDownloadDirPathCaption;
    private String assetDownloadDirPathAnimatedSticker;
    private String assetDownloadDirPathTransition;
    private String assetDownloadDirPathCaptureScene;
    private String assetDownloadDirPathParticle;
    private String assetDownloadDirPathFaceSticker;
    private String assetDownloadDirPathCustomAnimatedSticker;
    private String assetDownloadDirPathFace1Sticker;
    private boolean isLocalAssetSearchedTheme;
    private boolean isLocalAssetSearchedFilter;
    private boolean isLocalAssetSearchedCaption;
    private boolean isLocalAssetSearchedAnimatedSticker;
    private boolean isLocalAssetSearchedTransition;
    private boolean isLocalAssetSearchedCaptureScene;
    private boolean isLocalAssetSearchedParticle;
    private boolean isLocalAssetSearchedFaceSticker;
    private boolean isLocalAssetSearchedCustomAnimatedSticker;
    private boolean isLocalAssetSearchedFace1Sticker;

    private NvsStreamingContext streamingContext;
    private static SharedPreferences preferences;
    private static final String assetdata = "assetdata";
    private static final String NV_CDN_URL = "http://omxuaeaki.bkt.clouddn.com";
    private static final String NV_DOMAIN_URL = "https://meishesdk.meishe-app.com";

    public NvAssetManagerListener listener;
    private Context mContext;
    private NvsAssetPackageManager packageManager;

    public static NvAssetManager sharedInstance(Context context){
        if(m_instance == null)
            m_instance = new NvAssetManager(context);
        return m_instance;
    }

    private NvAssetManager(Context context) {
        mContext = context;
        m_httpRequest = NvHttpRequest.sharedInstance();
        streamingContext = NvsStreamingContext.getInstance();
        preferences = context.getSharedPreferences(assetdata, Context.MODE_PRIVATE);
        assetDict = new HashMap<String, ArrayList<NvAsset>>();
        remoteAssetsOrderedList = new HashMap<String, ArrayList<String>>();
        customStickerArray = new ArrayList<NvCustomStickerInfo>();
        packageManager = streamingContext.getAssetPackageManager();
        packageManager.setCallbackInterface(this);
        isSyncInstallAsset = true;
        // 测试自定义贴纸的时候把注释打开
//        initCustomStickerInfoFromSharedPreferences();
    }

    /**
     * 下载在线素材信息
     */
    public void downloadRemoteAssetsInfo(int assetType, int aspectRatio, int categoryId, int page, int pageSize) {
        m_httpRequest.getAssetList(assetType, aspectRatio, categoryId, page, pageSize, this);
    }

    /**
     * 下载素材
     */
    public boolean downloadAsset(String uuid) {
        NvAsset asset = findAsset(uuid);
        if (asset == null) {
            Log.e(TAG,"Invalid asset uuid " + uuid);
            return false;
        }

        if (!asset.hasRemoteAsset()) {
            Log.e(TAG,"Asset doesn't have a remote url!" + uuid);
            return false;
        }

        switch (asset.downloadStatus) {
            case NvAsset.DownloadStatusNone:
            case NvAsset.DownloadStatusFinished:
            case NvAsset.DownloadStatusFailed:
                break;
            case NvAsset.DownloadStatusPending:
                Log.e(TAG, "Asset has already in pending download state!" + uuid);
                return false;
            case NvAsset.DownloadStatusInProgress:
                Log.e(TAG, "Asset is being downloaded right now!" + uuid);
                return false;
            case NvAsset.DownloadStatusDecompressing:
                Log.e(TAG, "Asset is being uncompressed right now!" + uuid);
                return false;
            default:
                Log.e(TAG, "Invalid status for Asset !" + uuid);
                return false;
        }

        pendingAssetsToDownload.add(asset.uuid);
        asset.downloadStatus = NvAsset.DownloadStatusPending;
        downloadPendingAsset();

        return true;
    }

    private void downloadPendingAsset() {
        while (downloadingAssetsCounter < maxConcurrentAssetDownloadNum && pendingAssetsToDownload.size() > 0) {
            String uuid = pendingAssetsToDownload.get(pendingAssetsToDownload.size() - 1);
            pendingAssetsToDownload.remove(pendingAssetsToDownload.size() - 1);

            if (!startDownloadAsset(uuid)) {
                NvAsset asset = findAsset(uuid);
                asset.downloadStatus = NvAsset.DownloadStatusFailed;
                listener.onDonwloadAssetFailed(uuid);
            }
        }
    }

    private boolean startDownloadAsset(String uuid) {
        NvAsset asset = findAsset(uuid);
        if (asset == null) {
            Log.e(TAG,"Invalid asset uuid " + uuid);
            return false;
        }

        if (!asset.hasRemoteAsset()) {
            Log.e(TAG,"Asset doesn't have a remote url!" + uuid);
            return false;
        }

        String assetDownloadDir = getAssetDownloadDir(asset.assetType);
        int lastIndex = asset.remotePackageUrl.lastIndexOf("/");
        String assetPackageName = asset.remotePackageUrl.substring(lastIndex);
        String assetDownloadDestPath = assetDownloadDir + assetPackageName ;
        m_httpRequest.downloadAsset(asset.remotePackageUrl, assetDownloadDestPath, this, asset.uuid);

        downloadingAssetsCounter++;
        asset.downloadProgress = 0;
        asset.downloadStatus = NvAsset.DownloadStatusInProgress;

        return true;
    }

    /**
     * 取消下载素材
     */
    public boolean cancelAssetDownload(String uuid) {
        NvAsset asset = findAsset(uuid);
        if (asset == null) {
            Log.e(TAG,"Invalid asset uuid " + uuid);
            return false;
        }
        switch (asset.downloadStatus) {
            case NvAsset.DownloadStatusPending:{
                pendingAssetsToDownload.remove(uuid);
                asset.downloadStatus = NvAsset.DownloadStatusNone;
                break;
            }
            case NvAsset.DownloadStatusInProgress:{
                asset.downloadStatus = NvAsset.DownloadStatusNone;
                break;
            }
            default: {
                Log.e(TAG, "You can't cancel downloading asset while it is not in any of the download states!" + uuid);
                return false;
            }
        }
        return true;
    }

    /**
     * 获取在线素材信息
     */
    public ArrayList<NvAsset> getRemoteAssets(int assetType, int aspectRatio, int categoryId) {
        ArrayList<NvAsset> array = new ArrayList<NvAsset>();
        ArrayList<String> assets = remoteAssetsOrderedList.get(String.valueOf(assetType));
        if (assets != null) {
            for (String uuid: assets) {
                NvAsset asset = findAsset(uuid);
                if (aspectRatio == NvAsset.AspectRatio_All && categoryId == NvAsset.NV_CATEGORY_ID_ALL) {
                    array.add(asset);
                } else if (aspectRatio == NvAsset.AspectRatio_All && categoryId != NvAsset.NV_CATEGORY_ID_ALL) {
                    if (asset.categoryId == categoryId)
                        array.add(asset);
                } else if (aspectRatio != NvAsset.AspectRatio_All && categoryId == NvAsset.NV_CATEGORY_ID_ALL) {
                    if ((asset.aspectRatio & aspectRatio) == aspectRatio)
                        array.add(asset);
                } else {
                    if ((asset.aspectRatio & aspectRatio) == aspectRatio && asset.categoryId == categoryId)
                        array.add(asset);
                }
            }
        }
        return array;
    }

    /**
     * 根据页面获取在线素材信息
     */
    public ArrayList<NvAsset> getRemoteAssetsWithPage(int assetType, int aspectRatio, int categoryId, int page, int pageSize) {
        ArrayList<NvAsset> array = new ArrayList<NvAsset>();
        ArrayList<String> assets = remoteAssetsOrderedList.get(String.valueOf(assetType));
        if (assets != null) {
            for (int i = page*pageSize; i < (page+1)*pageSize; i++) {
                if (i >= assets.size())
                    break;
                String uuid = assets.get(i);
                NvAsset asset = findAsset(uuid);
                if (aspectRatio == NvAsset.AspectRatio_All && categoryId == NvAsset.NV_CATEGORY_ID_ALL) {
                    array.add(asset);
                } else if (aspectRatio == NvAsset.AspectRatio_All && categoryId != NvAsset.NV_CATEGORY_ID_ALL) {
                    if (asset.categoryId == categoryId)
                        array.add(asset);
                } else if (aspectRatio != NvAsset.AspectRatio_All && categoryId == NvAsset.NV_CATEGORY_ID_ALL) {
                    if ((asset.aspectRatio & aspectRatio) == aspectRatio)
                        array.add(asset);
                } else {
                    if ((asset.aspectRatio & aspectRatio) == aspectRatio && asset.categoryId == categoryId)
                        array.add(asset);
                }
            }
        }
        return array;
    }

    /**
     * 获取可用素材id列表
     */
    public ArrayList<NvAsset> getUsableAssets(int assetType, int aspectRatio, int categoryId) {
        ArrayList<NvAsset> assets = assetDict.get(String.valueOf(assetType));

        Comparator c = new Comparator<NvAsset>() {
            @Override
            public int compare(NvAsset asset1, NvAsset asset2) {
                String filePath1 = asset1.isReserved() ? asset1.bundledLocalDirPath : asset1.localDirPath;
                String filePath2 = asset2.isReserved() ? asset2.bundledLocalDirPath : asset2.localDirPath;
                return PathUtils.getFileModifiedTime(filePath1) < PathUtils.getFileModifiedTime(filePath2) ? 1 : -1;
            }
        };
        Collections.sort(assets, c);

        ArrayList<NvAsset> array = new ArrayList<NvAsset>();
        if (assets != null) {
            for (NvAsset asset: assets) {
                if (aspectRatio == NvAsset.AspectRatio_All && categoryId == NvAsset.NV_CATEGORY_ID_ALL) {
                    if (asset.isUsable())
                        array.add(asset);
                } else if (aspectRatio == NvAsset.AspectRatio_All && categoryId != NvAsset.NV_CATEGORY_ID_ALL) {
                    if (asset.categoryId == categoryId && asset.isUsable())
                        array.add(asset);
                } else if (aspectRatio != NvAsset.AspectRatio_All && categoryId == NvAsset.NV_CATEGORY_ID_ALL) {
                    if ((asset.aspectRatio & aspectRatio) == aspectRatio && asset.isUsable())
                        array.add(asset);
                } else {
                    if ((asset.aspectRatio & aspectRatio) == aspectRatio && asset.categoryId == categoryId && asset.isUsable())
                        array.add(asset);
                }
            }
        }
        return array;
    }

    /**
     * 获取预装素材id列表
     */
    public ArrayList<NvAsset> getReservedAssets(int assetType, int aspectRatio, int categoryId) {
        ArrayList<NvAsset> assets = assetDict.get(String.valueOf(assetType));
        ArrayList<NvAsset> array = new ArrayList<NvAsset>();
        if (assets != null) {
            for (NvAsset asset: assets) {
                if (aspectRatio == NvAsset.AspectRatio_All && categoryId == NvAsset.NV_CATEGORY_ID_ALL) {
                    if (asset.isReserved())
                        array.add(asset);
                } else if (aspectRatio == NvAsset.AspectRatio_All && categoryId != NvAsset.NV_CATEGORY_ID_ALL) {
                    if (asset.categoryId == categoryId && asset.isUsable() && asset.isReserved())
                        array.add(asset);
                } else if (aspectRatio != NvAsset.AspectRatio_All && categoryId == NvAsset.NV_CATEGORY_ID_ALL) {
                    if ((asset.aspectRatio & aspectRatio) == aspectRatio && asset.isUsable() && asset.isReserved())
                        array.add(asset);
                } else {
                    if ((asset.aspectRatio & aspectRatio) == aspectRatio && asset.categoryId == categoryId && asset.isUsable() && asset.isReserved())
                        array.add(asset);
                }
            }
        }
        return array;
    }

    /**
     * 获取素材对象
     */
    public NvAsset getAsset(String uuid) {
        return findAsset(uuid);
    }

    /**
     * 搜索本地素材，搜索结果存入素材字典
     */
    public void searchLocalAssets(int assetType) {
        if (getIsLocalAssetSearched(assetType))
            return;

        String dirPath = getAssetDownloadDir(assetType);
        searchAssetInLocalPath(assetType, dirPath);

        setIsLocalAssetSearched(assetType, true);
    }

    /**
     * 搜索预装素材，搜索结果存入素材字典
     */
    public void searchReservedAssets(int assetType, String bundlePath) {
        searchAssetInBundlePath(assetType, bundlePath);
    }

    private void searchAssetInBundlePath(int assetType, String dirPath) {
        String assetSuffix = getAssetSuffix(assetType);
        ArrayList<NvAsset> assets = assetDict.get(String.valueOf(assetType));
        if (assets == null) {
            assets = new ArrayList<>();
            assetDict.put(String.valueOf(assetType), assets);
        }
        try {
            String[] fileList = mContext.getAssets().list(dirPath);
            for (int index = 0; index < fileList.length;++index){
                String filePath = fileList[index];
                if(TextUtils.isEmpty(filePath))
                    continue;

                if(filePath.endsWith(assetSuffix)){
                    String tmpPackagePath = "assets:/" + dirPath + File.separator + filePath;
                    NvAsset asset = installAssetPackage(tmpPackagePath, assetType, true);
                    if(asset == null)
                        continue;

                    asset.isReserved = true;
                    asset.assetType = assetType;
                    if (assetType != NvAsset.ASSET_FACE1_STICKER) {
                        asset.bundledLocalDirPath = tmpPackagePath;
                        asset.coverUrl = dirPath + File.separator + asset.uuid + ".png";
                    }

                    NvAsset assetInfo = findAsset(asset.uuid);
                    if (assetInfo == null) {
                        assets.add(asset);
                    } else {
                        if (assetInfo.version < asset.version) {
                            assetInfo.copyAsset(asset);
                        }
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchAssetInLocalPath(int assetType, String dirPath) {
        String assetSuffix = getAssetSuffix(assetType);
        ArrayList<NvAsset> assets = assetDict.get(String.valueOf(assetType));
        if (assets == null) {
            assets = new ArrayList<>();
            assetDict.put(String.valueOf(assetType), assets);
        }
        File dir = new File(dirPath);
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (File file: files) {
                String filePath = file.getAbsolutePath();
                if (!filePath.endsWith(assetSuffix))
                    continue;


                NvAsset asset = installAssetPackage(filePath, assetType, false);
                if(asset == null)
                    continue;

                asset.isReserved = false;
                asset.assetType = assetType;
                if (assetType != NvAsset.ASSET_FACE1_STICKER)
                    asset.localDirPath = filePath;
                NvUserAssetInfo userAssetInfo = getAssetInfoFromSharedPreferences(asset.uuid, assetType);
                if(userAssetInfo != null){
                    if (assetType != NvAsset.ASSET_FACE1_STICKER)
                        asset.coverUrl = userAssetInfo.coverUrl;
                    asset.name = userAssetInfo.name;
                    asset.categoryId = userAssetInfo.categoryId;
                    asset.aspectRatio = userAssetInfo.aspectRatio;
                    asset.remotePackageSize = userAssetInfo.remotePackageSize;
                }

                NvAsset assetInfo = findAsset(asset.uuid);
                if (assetInfo == null) {
                    assets.add(asset);
                } else {
                    if (assetInfo.version < asset.version) {
                        assetInfo.copyAsset(asset);
                    }
                }
            }
        }
    }

    private String getAssetSuffix(int assetType) {
        String assetSuffix;
        switch (assetType) {
            case NvAsset.ASSET_THEME:
                assetSuffix = ".theme";
                break;
            case NvAsset.ASSET_FILTER:
                assetSuffix = ".videofx";
                break;
            case NvAsset.ASSET_CAPTION_STYLE:
                assetSuffix = ".captionstyle";
                break;
            case NvAsset.ASSET_ANIMATED_STICKER:
                assetSuffix = ".animatedsticker";
                break;
            case NvAsset.ASSET_VIDEO_TRANSITION:
                assetSuffix = ".videotransition";
                break;
            case NvAsset.ASSET_CAPTURE_SCENE:
                assetSuffix = ".capturescene";
                break;
            case NvAsset.ASSET_PARTICLE:
                assetSuffix = ".videofx";
                break;
            case NvAsset.ASSET_FACE_STICKER:
                assetSuffix = ".capturescene";
                break;
            case NvAsset.ASSET_CUSTOM_ANIMATED_STICKER:
                assetSuffix = ".animatedsticker";
                break;
            case NvAsset.ASSET_FACE1_STICKER:
                assetSuffix = ".zip";
                break;
            case NvAsset.ASSET_FACE_BUNDLE_STICKER:
                assetSuffix = ".bundle";
                break;
            default:
                assetSuffix = ".videofx";
                break;
        }
        return assetSuffix;
    }


    public NvAsset installAssetPackage(String filePath, int assetType, boolean isReserved) {
        NvAsset asset = new NvAsset();
        String fileName = filePath.substring(filePath.lastIndexOf("/")+1);
        if(TextUtils.isEmpty(fileName))
            return null;

        asset.assetType = assetType;
        asset.uuid = fileName.split("\\.")[0];
        if(TextUtils.isEmpty(asset.uuid))
            return null;

        StringBuilder packageId = new StringBuilder();
        NvsAssetPackageManager manager = streamingContext.getAssetPackageManager();
        if(manager == null)
            return null;

        asset.downloadStatus = NvAsset.DownloadStatusDecompressing;
        if (assetType == NvAsset.ASSET_FACE1_STICKER) {
            String destPath = PathUtils.getAssetDownloadPath(NvAsset.ASSET_FACE1_STICKER);
            boolean ret = false;
            if (isReserved) {
                String assetfile = filePath.replace("assets:/", "");
                ret = PathUtils.unZipAsset(mContext, assetfile, destPath + File.separator, true);
            } else {
                ret = PathUtils.unZipFile(filePath, destPath + File.separator);
            }
            if (ret) {
                asset.downloadStatus = NvAsset.DownloadStatusFinished;
                asset.version = PathUtils.getAssetVersionWithPath(filePath);
                if (isReserved) {
                    asset.bundledLocalDirPath = destPath + File.separator + asset.uuid + File.separator + asset.uuid + ".bundle";
                    String coverfile = filePath.replace("assets:/", "").replace(".zip", ".png");
                    asset.coverUrl = coverfile;
                } else {
                    asset.localDirPath = destPath + File.separator + asset.uuid + File.separator + asset.uuid + ".bundle";
                    asset.coverUrl = destPath + File.separator + asset.uuid + File.separator + asset.uuid + ".png";
                }
            } else {
                asset.downloadStatus = NvAsset.DownloadStatusDecompressingFailed;
            }
        } else if(assetType != NvAsset.ASSET_FACE_BUNDLE_STICKER){
            if (isSyncInstallAsset) {
                int error = manager.installAssetPackage(filePath, null, asset.getPackageType(), true, packageId);
                if (error == NvsAssetPackageManager.ASSET_PACKAGE_MANAGER_ERROR_NO_ERROR) {
                    asset.downloadStatus = NvAsset.DownloadStatusFinished;
                    asset.version = manager.getAssetPackageVersion(asset.uuid, asset.getPackageType());
                    asset.aspectRatio = manager.getAssetPackageSupportedAspectRatio(asset.uuid, asset.getPackageType());
                } else if (error == NvsAssetPackageManager.ASSET_PACKAGE_MANAGER_ERROR_ALREADY_INSTALLED) {
                    asset.downloadStatus = NvAsset.DownloadStatusFinished;
                    asset.version = manager.getAssetPackageVersion(asset.uuid, asset.getPackageType());
                    asset.aspectRatio = manager.getAssetPackageSupportedAspectRatio(asset.uuid, asset.getPackageType());
                    int version = manager.getAssetPackageVersionFromAssetPackageFilePath(filePath);
                    if (version > asset.version) {
                        error = manager.upgradeAssetPackage(filePath, null, asset.getPackageType(), false, packageId);
                        if (error == NvsAssetPackageManager.ASSET_PACKAGE_MANAGER_ERROR_NO_ERROR) {
                            asset.version = version;
                        }
                    }
                } else {
                    asset.downloadStatus = NvAsset.DownloadStatusDecompressingFailed;
                }
            } else {
                if (manager.getAssetPackageStatus(asset.uuid, asset.getPackageType()) == NvsAssetPackageManager.ASSET_PACKAGE_MANAGER_ERROR_ALREADY_INSTALLED) {
                    int version = manager.getAssetPackageVersionFromAssetPackageFilePath(filePath);
                    NvAsset assetInfo = findAsset(asset.uuid);
                    if (assetInfo == null) {
                        ;// 正常情况下不会调用到这，因为已安装的素材肯定能找到
                    } else {
                        if (version > assetInfo.version) {
                            manager.upgradeAssetPackage(filePath, null, asset.getPackageType(), false, packageId);
                        }
                    }
                } else {
                    manager.installAssetPackage(filePath, null, asset.getPackageType(), false, packageId);
                }
            }
        }

        asset.name = "";
        asset.categoryId = NvAsset.NV_CATEGORY_ID_ALL;
        asset.aspectRatio = NvAsset.AspectRatio_All;
        return asset;
    }

    public void setAssetInfoToSharedPreferences() {
        String separator = ";";
        for (String key: assetDict.keySet()) {
            ArrayList<NvAsset> assetList = assetDict.get(key);
            HashMap<String,String> assetsInfo=new HashMap<>();
            for (NvAsset asset: assetList) {
                assetsInfo.put(asset.uuid,
                                "name:" + asset.name + separator +
                                "coverUrl:" + asset.coverUrl + separator +
                                "categoryId:" + String.valueOf(asset.categoryId) + separator +
                                "aspectRatio:" + String.valueOf(asset.aspectRatio) + separator +
                                "remotePackageSize:" + String.valueOf(asset.remotePackageSize) + separator +
                                "assetType:" + String.valueOf(asset.assetType)
                        );
            }
            JSONObject jsonObject = new JSONObject(assetsInfo);

            String assetDownloadPath = PathUtils.getAssetDownloadPath(-1);
            File infoFile = new File(assetDownloadPath + File.separator + "info_" + key + ".json");

            // 文件的接口会抛异常，需要处理
            try {
                if (!infoFile.exists()) {
                    infoFile.createNewFile();
                }
                FileWriter writer = new FileWriter(infoFile.getAbsoluteFile());
                BufferedWriter bufferedWriter = new BufferedWriter(writer);
                bufferedWriter.write(jsonObject.toString());
                bufferedWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 测试自定义贴纸的时候把注释打开
//        setCustomStickerInfoToSharedPreferences();
    }

    private void setCustomStickerInfoToSharedPreferences() {
        String separator = ";";
        HashMap<String,String> assetsInfo=new HashMap<>();
        for (NvCustomStickerInfo info: customStickerArray) {
            assetsInfo.put(info.uuid,
                    "templateUuid:" + info.templateUuid + separator +
                            "imagePath:" + info.imagePath + separator +
                            "order:" + String.valueOf(info.order)
            );
        }
        JSONObject jsonObject = new JSONObject(assetsInfo);

        String assetDownloadPath = PathUtils.getAssetDownloadPath(-1);
        File infoFile = new File(assetDownloadPath + File.separator + "info_" + String.valueOf(NvAsset.ASSET_CUSTOM_ANIMATED_STICKER) + ".json");

        // 文件的接口会抛异常，需要处理
        try {
            if (!infoFile.exists()) {
                infoFile.createNewFile();
            }
            FileWriter writer = new FileWriter(infoFile.getAbsoluteFile());
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(jsonObject.toString());
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initCustomStickerInfoFromSharedPreferences() {
        String assetDownloadPath = PathUtils.getAssetDownloadPath(-1);
        File infoFile = new File(assetDownloadPath + File.separator + "info_"+ String.valueOf(NvAsset.ASSET_CUSTOM_ANIMATED_STICKER)+".json");

        // 文件的接口会抛异常，需要处理
        try {
            if (!infoFile.exists()) {
                return ;
            }
            BufferedReader bufferedReader = new BufferedReader(new FileReader(infoFile));
            String assetsInfo = "", temp;
            while((temp=bufferedReader.readLine())!=null){
                assetsInfo += temp;
            }
            JSONObject jsonObject = new JSONObject(assetsInfo);
            Iterator iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String uuid = (String)iterator.next();
                String assetInfo = jsonObject.getString(uuid);
                NvCustomStickerInfo userAssetInfo = new NvCustomStickerInfo();
                String[] assetList = assetInfo.split(";");
                for (String str : assetList) {
                    userAssetInfo.uuid = uuid;
                    if (str.indexOf("templateUuid:") >= 0) {
                        userAssetInfo.templateUuid = str.replaceAll("templateUuid:", "");
                    } else if (str.indexOf("imagePath:") >= 0) {
                        userAssetInfo.imagePath = str.replaceAll("imagePath:", "");
                    } else if (str.indexOf("order:") >= 0) {
                        userAssetInfo.order = Integer.parseInt(str.replaceAll("order:", ""));
                    } else {
                        continue;
                    }
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NvUserAssetInfo getAssetInfoFromSharedPreferences(String uuid, int assetType) {
        String assetDownloadPath = PathUtils.getAssetDownloadPath(-1);
        File infoFile = new File(assetDownloadPath + File.separator + "info_"+ String.valueOf(assetType)+".json");

        // 文件的接口会抛异常，需要处理
        try {
            if (!infoFile.exists()) {
                return null;
            }
            BufferedReader bufferedReader = new BufferedReader(new FileReader(infoFile));
            String assetsInfo = "", temp;
            while((temp=bufferedReader.readLine())!=null){
                assetsInfo += temp;
            }
            JSONObject jsonObject = new JSONObject(assetsInfo);
            Iterator iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String uuidIn = (String)iterator.next();
                if (uuidIn.equals(uuid)) {
                    String assetInfo = jsonObject.getString(uuid);
                    NvUserAssetInfo userAssetInfo = new NvUserAssetInfo();
                    String[] assetList = assetInfo.split(";");
                    for (String str : assetList) {
                        if (str.indexOf("uuid:") >= 0) {
                            userAssetInfo.uuid = uuid;
                        } else if (str.indexOf("name:") >= 0) {
                            userAssetInfo.name = str.replaceAll("name:", "");
                        } else if (str.indexOf("coverUrl:") >= 0) {
                            userAssetInfo.coverUrl = str.replaceAll("coverUrl:", "");
                        } else if (str.indexOf("categoryId:") >= 0) {
                            userAssetInfo.categoryId = Integer.parseInt(str.replaceAll("categoryId:", ""));
                        } else if (str.indexOf("aspectRatio:") >= 0) {
                            userAssetInfo.aspectRatio = Integer.parseInt(str.replaceAll("aspectRatio:", ""));
                        } else if (str.indexOf("remotePackageSize:") >= 0) {
                            userAssetInfo.remotePackageSize = Integer.parseInt(str.replaceAll("remotePackageSize:", ""));
                        } else if (str.indexOf("assetType:") >= 0) {
                            userAssetInfo.assetType = Integer.parseInt(str.replaceAll("assetType:", ""));
                        } else {
                            continue;
                        }
                    }
                    return userAssetInfo;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getAssetDownloadDir(int assetType) {
        switch (assetType) {
            case NvAsset.ASSET_THEME:
                assetDownloadDirPathTheme = PathUtils.getAssetDownloadPath(assetType);
                return assetDownloadDirPathTheme;
            case NvAsset.ASSET_FILTER:
                assetDownloadDirPathFilter = PathUtils.getAssetDownloadPath(assetType);
                return assetDownloadDirPathFilter;
            case NvAsset.ASSET_CAPTION_STYLE:
                assetDownloadDirPathCaption = PathUtils.getAssetDownloadPath(assetType);
                return assetDownloadDirPathCaption;
            case NvAsset.ASSET_ANIMATED_STICKER:
                assetDownloadDirPathAnimatedSticker = PathUtils.getAssetDownloadPath(assetType);
                return assetDownloadDirPathAnimatedSticker;
            case NvAsset.ASSET_VIDEO_TRANSITION:
                assetDownloadDirPathTransition = PathUtils.getAssetDownloadPath(assetType);
                return assetDownloadDirPathTransition;
            case NvAsset.ASSET_CAPTURE_SCENE:
                assetDownloadDirPathCaptureScene = PathUtils.getAssetDownloadPath(assetType);
                return assetDownloadDirPathCaptureScene;
            case NvAsset.ASSET_PARTICLE:
                assetDownloadDirPathParticle = PathUtils.getAssetDownloadPath(assetType);
                return assetDownloadDirPathParticle;
            case NvAsset.ASSET_FACE_STICKER:
                assetDownloadDirPathFaceSticker = PathUtils.getAssetDownloadPath(assetType);
                return assetDownloadDirPathFaceSticker;
            case NvAsset.ASSET_CUSTOM_ANIMATED_STICKER:
                assetDownloadDirPathCustomAnimatedSticker = PathUtils.getAssetDownloadPath(assetType);
                return assetDownloadDirPathCustomAnimatedSticker;
            case NvAsset.ASSET_FACE1_STICKER:
                assetDownloadDirPathFace1Sticker = PathUtils.getAssetDownloadPath(assetType);
                return assetDownloadDirPathFace1Sticker;
            default:
                break;
        }
        return "";
    }

    private  boolean getIsLocalAssetSearched(int assetType) {
        switch (assetType) {
            case NvAsset.ASSET_THEME:
                return isLocalAssetSearchedTheme;
            case NvAsset.ASSET_FILTER:
                return isLocalAssetSearchedFilter;
            case NvAsset.ASSET_CAPTION_STYLE:
                return isLocalAssetSearchedCaption;
            case NvAsset.ASSET_ANIMATED_STICKER:
                return isLocalAssetSearchedAnimatedSticker;
            case NvAsset.ASSET_VIDEO_TRANSITION:
                return isLocalAssetSearchedTransition;
            case NvAsset.ASSET_CAPTURE_SCENE:
                return isLocalAssetSearchedCaptureScene;
            case NvAsset.ASSET_PARTICLE:
                return isLocalAssetSearchedParticle;
            case NvAsset.ASSET_FACE_STICKER:
                return isLocalAssetSearchedFaceSticker;
            case NvAsset.ASSET_CUSTOM_ANIMATED_STICKER:
                return isLocalAssetSearchedCustomAnimatedSticker;
            case NvAsset.ASSET_FACE1_STICKER:
                return isLocalAssetSearchedFace1Sticker;
            default:
                break;
        }
        return false;
    }

    private void setIsLocalAssetSearched(int assetType, boolean isSearched) {
        switch (assetType) {
            case NvAsset.ASSET_THEME:
                isLocalAssetSearchedTheme = isSearched;
                break;
            case NvAsset.ASSET_FILTER:
                isLocalAssetSearchedFilter = isSearched;
                break;
            case NvAsset.ASSET_CAPTION_STYLE:
                isLocalAssetSearchedCaption = isSearched;
                break;
            case NvAsset.ASSET_ANIMATED_STICKER:
                isLocalAssetSearchedAnimatedSticker = isSearched;
                break;
            case NvAsset.ASSET_VIDEO_TRANSITION:
                isLocalAssetSearchedTransition = isSearched;
                break;
            case NvAsset.ASSET_CAPTURE_SCENE:
                isLocalAssetSearchedCaptureScene = isSearched;
                break;
            case NvAsset.ASSET_PARTICLE:
                isLocalAssetSearchedParticle = isSearched;
                break;
            case NvAsset.ASSET_FACE_STICKER:
                isLocalAssetSearchedFaceSticker = isSearched;
                break;
            case NvAsset.ASSET_CUSTOM_ANIMATED_STICKER:
                isLocalAssetSearchedCustomAnimatedSticker = isSearched;
                break;
            case NvAsset.ASSET_FACE1_STICKER:
                isLocalAssetSearchedFace1Sticker = isSearched;
                break;
            default:
                break;
        }
    }

    private NvAsset findAsset(String uuid) {
        for (String key: assetDict.keySet()) {
            ArrayList<NvAsset> assetList = assetDict.get(key);
            for (NvAsset asset: assetList) {
                if (asset.uuid.equals(uuid))
                    return asset;
            }
        }
        return null;
    }

    private void addRemoteAssetData(ArrayList<NvHttpRequest.NvAssetInfo> resultsArray, int assetType) {
        ArrayList<NvAsset> assets = assetDict.get(String.valueOf(assetType));
        if (assets == null) {
            assets = new ArrayList<NvAsset>();
        }
        for (NvHttpRequest.NvAssetInfo assetInfo: resultsArray) {
            NvAsset asset = new NvAsset();
            asset.assetType = assetType;
            asset.categoryId = assetInfo.getCategory();
            asset.tags = assetInfo.getTags();
            asset.remotePackageSize = assetInfo.getPackageSize();
            asset.uuid = assetInfo.getId();
            asset.minAppVersion = assetInfo.getMinAppVersion();
            asset.remotePackageUrl = assetInfo.getPackageUrl().replaceAll(NV_DOMAIN_URL, NV_CDN_URL);
            asset.remoteVersion = assetInfo.getVersion();
            asset.coverUrl = assetInfo.getCoverUrl().replaceAll(NV_DOMAIN_URL, NV_CDN_URL);
            asset.aspectRatio = assetInfo.getSupportedAspectRatio();
            asset.name = assetInfo.getName();
            asset.desc = assetInfo.getDesc();

            NvAsset foundAsset = findAsset(asset.uuid);
            if (foundAsset == null) {
                assets.add(asset);
            } else {
                foundAsset.categoryId = asset.categoryId;
                foundAsset.name = asset.name;
                foundAsset.coverUrl = asset.coverUrl;
                foundAsset.aspectRatio = asset.aspectRatio;
                foundAsset.remotePackageSize = asset.remotePackageSize;
                foundAsset.remoteVersion = asset.remoteVersion;
                foundAsset.remotePackageUrl = asset.remotePackageUrl;
            }
        }
        assetDict.put(String.valueOf(assetType), assets);
    }

    private void addRemoteAssetOrderedList(ArrayList<NvHttpRequest.NvAssetInfo> resultsArray, int assetType) {
        ArrayList<String> assets = remoteAssetsOrderedList.get(String.valueOf(assetType));
        if (assets == null) {
            assets = new ArrayList<String>();
        }
        for (NvHttpRequest.NvAssetInfo assetInfo: resultsArray) {
            if (!assets.contains(assetInfo.getId())) {
                assets.add(assetInfo.getId());
            }
        }
        remoteAssetsOrderedList.put(String.valueOf(assetType), assets);
    }

    @Override
    public void onGetAssetListSuccess(ArrayList responseArrayList, int assetType, boolean hasNext) {
        addRemoteAssetData(responseArrayList, assetType);
        addRemoteAssetOrderedList(responseArrayList, assetType);

        listener.onRemoteAssetsChanged(hasNext);
    }

    @Override
    public void onGetAssetListFailed(IOException e, int assetType) {
        listener.onGetRemoteAssetsFailed();
    }

    @Override
    public void onDonwloadAssetProgress(int progress, String downloadId) {
        listener.onDownloadAssetProgress(downloadId, progress);
    }

    @Override
    public void onDonwloadAssetSuccess(boolean success, String downloadPath, String downloadId) {
        downloadingAssetsCounter--;
        NvAsset asset = findAsset(downloadId);
        asset.downloadProgress = 1;
        asset.downloadStatus = NvAsset.DownloadStatusDecompressing;
        asset.localDirPath = downloadPath;

        NvAsset assetInfo = installAssetPackage(downloadPath, asset.assetType, false);
        if (isSyncInstallAsset) {
            asset.downloadStatus = assetInfo.downloadStatus;
            asset.version = assetInfo.version;
        }
        if (asset.assetType == NvAsset.ASSET_FACE1_STICKER) {
            asset.downloadStatus = assetInfo.downloadStatus;
            asset.version = assetInfo.version;
            asset.localDirPath = assetInfo.localDirPath;
        }

        listener.onDonwloadAssetSuccess(downloadId);

        setAssetInfoToSharedPreferences();
    }

    @Override
    public void onDonwloadAssetFailed(Exception e, String downloadId) {
        listener.onDonwloadAssetFailed(downloadId);
    }

    @Override
    public void onFinishAssetPackageInstallation(String assetPackageId, String assetPackageFilePath, int assetPackageType, int error) {
        if (error == NvsAssetPackageManager.ASSET_PACKAGE_MANAGER_ERROR_NO_ERROR || error == NvsAssetPackageManager.ASSET_PACKAGE_MANAGER_ERROR_ALREADY_INSTALLED) {
            NvAsset asset = findAsset(assetPackageId);
            asset.downloadStatus = NvAsset.DownloadStatusFinished;
            asset.version = packageManager.getAssetPackageVersion(assetPackageId, assetPackageType);
            asset.aspectRatio = packageManager.getAssetPackageSupportedAspectRatio(asset.uuid, asset.getPackageType());
        } else {
            NvAsset asset = findAsset(assetPackageId);
            asset.downloadStatus = NvAsset.DownloadStatusDecompressingFailed;
        }
        listener.onFinishAssetPackageInstallation(assetPackageId);
    }

    @Override
    public void onFinishAssetPackageUpgrading(String assetPackageId, String assetPackageFilePath, int assetPackageType, int error) {
        if (error == NvsAssetPackageManager.ASSET_PACKAGE_MANAGER_ERROR_NO_ERROR || error == NvsAssetPackageManager.ASSET_PACKAGE_MANAGER_ERROR_ALREADY_INSTALLED) {
            NvAsset asset = findAsset(assetPackageId);
            asset.downloadStatus = NvAsset.DownloadStatusFinished;
            asset.version = packageManager.getAssetPackageVersion(assetPackageId, assetPackageType);
            asset.aspectRatio = packageManager.getAssetPackageSupportedAspectRatio(asset.uuid, asset.getPackageType());
        } else {
            NvAsset asset = findAsset(assetPackageId);
            asset.downloadStatus = NvAsset.DownloadStatusDecompressingFailed;
        }
        listener.onFinishAssetPackageUpgrading(assetPackageId);
    }

    public interface NvAssetManagerListener {
        /**
         * 获取到在线素材列表后执行该回调。
         */
        void onRemoteAssetsChanged(boolean hasNext);
        /**
         * 获取到在线素材列表失败执行该回调。
         */
        void onGetRemoteAssetsFailed();
        /**
         * 下载在线素材进度执行该回调。
         */
        void onDownloadAssetProgress(String uuid, int progress);
        /**
         * 下载在线素材失败执行该回调。
         */
        void onDonwloadAssetFailed(String uuid);
        /**
         * 下载在线素材完成执行该回调。
         */
        void onDonwloadAssetSuccess(String uuid);
        /**
         * 如果素材为异步安装，安装完成后执行该回调。
         */
        void onFinishAssetPackageInstallation(String uuid);
        /**
         * 如果素材为异步安装，升级完成后执行该回调。
         */
        void onFinishAssetPackageUpgrading(String uuid);
    }

    public class NvUserAssetInfo {
        public String uuid;
        public String name;
        public String coverUrl;
        public int categoryId;
        public int aspectRatio;
        public int remotePackageSize;
        public int assetType;
    }

    public class NvCustomStickerInfo {
        public String uuid;
        public String templateUuid;
        public String imagePath;
        public int order;
    }
}
