package com.sven.huinews.international.utils.shot.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.meicam.sdk.NvsAVFileInfo;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimelineCaption;
import com.meicam.sdk.NvsVideoFrameRetriever;
import com.sven.huinews.international.R;
import com.sven.huinews.international.utils.shot.utils.asset.NvAsset;
import com.sven.huinews.international.utils.shot.utils.dataInfo.ClipInfo;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.meicam.sdk.NvsAVFileInfo.AV_FILE_TYPE_IMAGE;

public class Util {
    private final static String TAG = "Util";

    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    Log.i("后台", appProcess.processName);
                    return true;
                }else{
                    Log.i("前台", appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

    //把String转化为double
    public static double convertToDouble(String number, double defaultValue) {
        if (TextUtils.isEmpty(number)) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(number);
        } catch (Exception e) {
            return defaultValue;
        }

    }

    public static ArrayList<Bitmap> getBitmapListFromClipList(Context context, ArrayList<ClipInfo> clipInfoArrayList) {
        ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();
        for(int i = 0;i < clipInfoArrayList.size();i++) {
            ClipInfo clipInfo = clipInfoArrayList.get(i);
            if(clipInfo == null) {
                bitmapArrayList.add(null);
                continue;
            }

            Bitmap bitmap = getBitmapFromClipInfo(context, clipInfo);
            bitmapArrayList.add(bitmap);
        }

        return bitmapArrayList;
    }

    public static Bitmap getBitmapFromClipInfo(Context context, ClipInfo clipInfo) {
        Bitmap bitmap = null;
        String clipPath = clipInfo.getFilePath();
        if(TextUtils.isEmpty(clipPath)) {
            return bitmap;
        }

        long timeStamp = clipInfo.getTrimIn();
        if(timeStamp < 0) {
            timeStamp = 0;
        }

        NvsStreamingContext streamingContext = NvsStreamingContext.getInstance();
        if(context == null)
            return bitmap;

        NvsAVFileInfo fileInfo = streamingContext.getAVFileInfo(clipPath);
        if(fileInfo == null) {
          //  bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.edit_clip_default_bg);
            bitmap = Bitmap.createScaledBitmap(bitmap, 540, 960, true);
            return bitmap;
        }
        if(fileInfo.getAVFileType() == AV_FILE_TYPE_IMAGE) {
            bitmap = BitmapFactory.decodeFile(clipPath);
            bitmap = centerSquareScaleBitmap(bitmap, 180, 320);
        }else {
            NvsVideoFrameRetriever videoFrameRetriever = streamingContext.createVideoFrameRetriever(clipPath);
            if(videoFrameRetriever != null)
                bitmap = videoFrameRetriever.getFrameAtTime(timeStamp, NvsVideoFrameRetriever.VIDEO_FRAME_HEIGHT_GRADE_480);
                bitmap = centerSquareScaleBitmap(bitmap, 180, 320);
        }

        return bitmap;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static boolean getBundleFilterInfo(Context context, ArrayList<NvAsset> assetArrayList, String bundlePath) {
        if(context == null)
            return false;

        if(TextUtils.isEmpty(bundlePath))
            return false;

        try {
            InputStream nameListStream = context.getAssets().open(bundlePath);
            BufferedReader nameListBuffer = new BufferedReader(new InputStreamReader(nameListStream,"GBK"));

            String strLine;
            while ((strLine = nameListBuffer.readLine()) != null) {
                String[] strNameArray = strLine.split(",");
                    if(strNameArray.length < 3)
                        continue;

                    for (int i = 0; i < assetArrayList.size();++i){
                        NvAsset assetItem = assetArrayList.get(i);
                        if(assetItem == null)
                            continue;

                        if(!assetItem.isReserved)
                            continue;

                        String packageId = assetItem.uuid;
                        if(TextUtils.isEmpty(packageId))
                            continue;

                        if(packageId.equals(strNameArray[0])){
                            assetItem.name = strNameArray[1];
                            assetItem.aspectRatio = Integer.parseInt(strNameArray[2]);
                            break;
                        }

                }
            }
            nameListBuffer.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static int getStatusBarHeight(Context context) {
        int statusBarHeight1 = -1;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight1;
    }

    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int width, int height)
    {
        if(null == bitmap || width <= 0 || height <= 0)
        {
            return  null;
        }

        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();

        if(widthOrg > width && heightOrg > height)
        {
            //压缩到一个最小长度是edgeLength的bitmap
            float wRemainder = widthOrg / (float)width;
            float hRemainder = heightOrg / (float)height;

            int scaledWidth;
            int scaledHeight;
            if(wRemainder > hRemainder) {
                scaledWidth = (int)(widthOrg / hRemainder);
                scaledHeight = (int)(heightOrg / hRemainder);
            } else {
                scaledWidth = (int)(widthOrg / wRemainder);
                scaledHeight = (int)(heightOrg / wRemainder);
            }

            Bitmap scaledBitmap;
            try{
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
            }
            catch(Exception e){
                return null;
            }

            //从图中截取正中间的部分。
            int xTopLeft = (scaledWidth - width) / 2;
            int yTopLeft = (scaledHeight - height) / 2;

            try{
                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, width, height);
                scaledBitmap.recycle();
            }
            catch(Exception e){
                return null;
            }
        }

        return result;
    }

 /*   public static void showDialog(Context context, final String title, final String first_tip, final String second_tip) {
        final CommonDialog dialog = new CommonDialog(context, 1);
        dialog.setOnCreateListener(new CommonDialog.OnCreateListener() {
            @Override
            public void OnCreated() {
                dialog.setTitleTxt(title);
                dialog.setFirstTipsTxt(first_tip);
                dialog.setSecondTipsTxt(second_tip);
            }
        });
        dialog.setOnBtnClickListener(new CommonDialog.OnBtnClickListener() {
            @Override
            public void OnOkBtnClicked(View view) {
                dialog.dismiss();
            }

            @Override
            public void OnCancelBtnClicked(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void showDialog(Context context, final String title, final String first_tip) {
        final CommonDialog dialog = new CommonDialog(context, 1);
        dialog.setOnCreateListener(new CommonDialog.OnCreateListener() {
            @Override
            public void OnCreated() {
                dialog.setTitleTxt(title);
                dialog.setFirstTipsTxt(first_tip);
            }
        });
        dialog.setOnBtnClickListener(new CommonDialog.OnBtnClickListener() {
            @Override
            public void OnOkBtnClicked(View view) {
                dialog.dismiss();
            }

            @Override
            public void OnCancelBtnClicked(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    *//**
     * 根据x坐标进行排序
     *//*
    public static class PointXComparator implements Comparator<PointF> {

        @Override
        public int compare(PointF bean1, PointF bean2) {
            return (int) (bean1.x - bean2.x);
        }
    }

    *//**
     * 根据x坐标进行排序
     *//*
    public static class PointYComparator implements Comparator<PointF> {

        @Override
        public int compare(PointF bean1, PointF bean2) {
            return (int) (bean1.y - bean2.y);
        }
    }

    *//**
     * 根据声效rank排序
     *//*
    public static class RecordFxIndexComparator implements Comparator<RecordFxListItem> {

        @Override
        public int compare(RecordFxListItem bean1, RecordFxListItem bean2) {
            return bean1.index - bean2.index;
        }
    }

    //保存数据
    public static CaptionInfo saveCaptionData(NvsTimelineCaption caption){
        if(caption == null)
            return null;
        CaptionInfo captionInfo = new CaptionInfo();
        long inPoint = caption.getInPoint();
        captionInfo.setInPoint(inPoint);
        long outPoint = caption.getOutPoint();
        long captionDuration = outPoint - inPoint;
        captionInfo.setDuration(captionDuration);
        captionInfo.setText(caption.getText());
        captionInfo.setCaptionZVal((int)caption.getZValue());
        captionInfo.setAnchor(caption.getAnchorPoint());
        PointF pointF = caption.getCaptionTranslation();
        captionInfo.setTranslation(pointF);
        captionInfo.setBold(caption.getBold());
        captionInfo.setItalic(caption.getItalic());
        captionInfo.setShadow(caption.getDrawShadow());
        float fontSize = caption.getFontSize();
        captionInfo.setCaptionSize(fontSize);
        captionInfo.setCaptionCategory(caption.getCategory());
        return captionInfo;
    }

    *//**
     * 获取json文件记录的录音特效列表
     *//*
    public static List<RecordFxListItem> listRecordFxFromJson(Context context) {
        List<RecordFxListItem> fileList = new ArrayList<>();
        try {
            InputStreamReader isr = new InputStreamReader(context.getAssets().open("record/record.json"),"UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuilder builder = new StringBuilder();
            while((line = br.readLine()) != null){
                builder.append(line);
            }
            br.close();
            isr.close();
            JSONObject data = new JSONObject(builder.toString());
            JSONArray array = data.getJSONArray("record_fx");
            for (int i = 0; i < array.length(); i++) {
                JSONObject role = array.getJSONObject(i);
                RecordFxListItem audioFxListItem = new RecordFxListItem();
                audioFxListItem.nameCH = role.getString("name");
                audioFxListItem.fxID = role.getString("builtin_fx_name");
                audioFxListItem.index = role.getInt("rank");

                switch (i) {
                    case 0:
                        audioFxListItem.image_drawable = ContextCompat.getDrawable(context, R.drawable.record_fx_1);
                        break;
                    case 1:
                        audioFxListItem.image_drawable = ContextCompat.getDrawable(context, R.drawable.record_fx_2);
                        break;
                    case 2:
                        audioFxListItem.image_drawable = ContextCompat.getDrawable(context, R.drawable.record_fx_3);
                        break;
                    case 3:
                        audioFxListItem.image_drawable = ContextCompat.getDrawable(context, R.drawable.record_fx_4);
                        break;
                    case 4:
                        audioFxListItem.image_drawable = ContextCompat.getDrawable(context, R.drawable.record_fx_5);
                        break;
                    case 5:
                        audioFxListItem.image_drawable = ContextCompat.getDrawable(context, R.drawable.record_fx_6);
                        break;
                    case 6:
                        audioFxListItem.image_drawable = ContextCompat.getDrawable(context, R.drawable.record_fx_7);
                        break;
                    case 7:
                        audioFxListItem.image_drawable = ContextCompat.getDrawable(context, R.drawable.record_fx_8);
                        break;
                    default:
                        break;
                }

                fileList.add(audioFxListItem);
            }
            Collections.sort(fileList, new RecordFxIndexComparator()); // 排序

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return fileList;
    }*/

    /**
     * 清除录音文件
     */
    public static void clearRecordAudioData() {
        String dir = PathUtils.getAudioRecordFilePath();
        if(dir == null) {
            return;
        }
        File record_dir = new File(dir);
        if(!record_dir.exists()) {
            return;
        }
        for(File file: record_dir.listFiles()) {
            if(file.isFile()) {
                file.delete();
            }
        }
    }


    /**
     *  将bitmap保存到SD卡
     */
    public static boolean saveBitmapToSD(Bitmap bt, String target_path) {
        if(bt == null || target_path == null || target_path.isEmpty()) {
            return false;
        }
        File file = new File(target_path);
        try {
            FileOutputStream out = new FileOutputStream(file);
            bt.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
