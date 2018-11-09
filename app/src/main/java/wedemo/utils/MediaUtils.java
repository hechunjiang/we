package wedemo.utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import wedemo.shot.bean.MediaData;


/**
 * Created by ${gexinyu} on 2018/5/28.
 */

public class MediaUtils {

    /**
     * 获取视频的缩略图
     * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
     *
     * @param videoPath 视频的路径
     * @param width     指定输出视频缩略图的宽度
     * @param height    指定输出视频缩略图的高度度
     * @param kind      参照MediaStore.Images(Video).Thumbnails类中的常量MINI_KIND和MICRO_KIND。
     *                  其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
     * @return 指定大小的视频缩略图
     */
    public static Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
        Bitmap bitmap = null;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        if (bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }

    // 获取视频缩略图
    public static Bitmap getVideoThumbnail(String filePath) {
        Bitmap b = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            b = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();

        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return b;
    }

    /**
     * 读取手机中所有图片信息
     */
    public static void getAllPhotoInfo(final Activity activity, final LocalMediaCallback localMediaCallback) {

        ThreadPoolUtils.getExecutor().execute(new Runnable() {
            @Override
            public void run() {
                final List<MediaData> mediaBeen = new ArrayList<>();
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

                String[] projection = {
                        MediaStore.Images.Media.DATA,
                        MediaStore.Images.Media.DATE_ADDED,
                        MediaStore.Images.Thumbnails.DATA
                };
                //全部图片
                String where = MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=?";
                //指定格式
                String[] whereArgs = {"image/jpeg", "image/png", "image/jpg"};
                //查询
                Cursor mCursor = activity.getContentResolver().query(
                        mImageUri, projection, null, null,
                        null);

                if (mCursor != null) {
                    while (mCursor.moveToNext()) {
                        // 获取图片的路径

                        // 获取图片的路径
                        int thumbPathIndex = mCursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
                        int timeIndex = mCursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED);
                        int pathIndex = mCursor.getColumnIndex(MediaStore.Images.Media.DATA);
                        int id = mCursor.getColumnIndex(MediaStore.Images.Media._ID);
                        MediaData fi = new MediaData();
                        String thumbPath = mCursor.getString(thumbPathIndex);
                        Long date = mCursor.getLong(timeIndex) * 1000;
                        String filepath = mCursor.getString(pathIndex);

                        //判断文件是否存在，存在才去加入
                        boolean b = FileManagerUtils.fileIsExists(filepath);
                        if (b) {
                            File f = new File(filepath);
                            fi.setId(id);
                            fi.setSize(date);
                            fi.setThumbPath(thumbPath);
                            fi.setPath(filepath);
                            fi.setDisplayName(f.getName());
                            fi.setType(MediaConstant.IMAGE);
                            fi.setState(false);
                            mediaBeen.add(fi);
                        }

                    }
                    mCursor.close();
                }
                //更新界面
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //让日期从最近的排序
                        Collections.reverse(mediaBeen);
                        localMediaCallback.onLocalMediaCallback(mediaBeen);
                    }
                });
            }
        });
    }


    /**
     * 获取手机中所有视频的信息
     */
    public static void getAllVideoInfos(final Activity activity, final LocalMediaCallback localMediaCallback) {


        ThreadPoolUtils.getExecutor().execute(new Runnable() {
            @Override
            public void run() {
                final List<MediaData> videoList = new ArrayList<>();

                Uri mVideoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                getUriColumns(mVideoUri, activity);
                String[] projection = {MediaStore.Video.Thumbnails._ID
                        , MediaStore.Video.Thumbnails.DATA
                        , MediaStore.Video.Media.DURATION
                        , MediaStore.Video.Media.SIZE
                        , MediaStore.Video.Media.DATE_ADDED
                        , MediaStore.Video.Media.DISPLAY_NAME
                        , MediaStore.Video.Media.DATE_MODIFIED};

                //全部视频
                String where = MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Video.Media.MIME_TYPE + "=? or "
                        + MediaStore.Video.Media.MIME_TYPE + "=? or "
                        + MediaStore.Video.Media.MIME_TYPE + "=? or "
                        + MediaStore.Video.Media.MIME_TYPE + "=? or "
                        + MediaStore.Video.Media.MIME_TYPE + "=? or "
                        + MediaStore.Video.Media.MIME_TYPE + "=? or "
                        + MediaStore.Video.Media.MIME_TYPE + "=? or "
                        + MediaStore.Video.Media.MIME_TYPE + "=?";
                String[] whereArgs = {"video/mp4", "video/3gp", "video/aiv", "video/rmvb", "video/vob", "video/flv",
                        "video/mkv", "video/mov", "video/mpg"};

                Cursor mCursor = activity.getContentResolver().query(mVideoUri,
                        projection, where, whereArgs, MediaStore.Video.Media.DATE_ADDED + " DESC ");


                if (mCursor != null) {
                    while (mCursor.moveToNext()) {
                        // 获取视频的路径
                        int videoId = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Video.Media._ID));
                        String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Video.Media.DATA));
                        long duration = mCursor.getLong(mCursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                        long size = mCursor.getLong(mCursor.getColumnIndex(MediaStore.Video.Media.SIZE)) / 1024; //单位kb
                        if (size < 0) {
                            //某些设备获取size<0，直接计算
                            Log.e("dml", "this video size < 0 " + path);
                            size = new File(path).length() / 1024;
                        }
                        String displayName = mCursor.getString(mCursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));

                        //用于展示相册初始化界面
                        int timeIndex = mCursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED);
                        Long date = mCursor.getLong(timeIndex) * 1000;

                        //需要判断当前文件是否存在  一定要加，不然有些文件已经不存在图片显示不出来
                        boolean fileIsExists = FileManagerUtils.fileIsExists(path);
                        if (fileIsExists) {
                            videoList.add(new MediaData(videoId, MediaConstant.VIDEO, path, path, duration, date, displayName, false));
                        }
                    }

                    mCursor.close();
                }
                //更新界面
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //...
                        localMediaCallback.onLocalMediaCallback(videoList);
                    }
                });
            }
        });
    }


    /**
     * 读取手机中所有图片信息
     */
    public static void getAllPhotoAndVideo(final Activity activity, final LocalMediaCallback localMediaCallback) {

        getAllPhotoInfo(activity, new LocalMediaCallback() {
            @Override
            public void onLocalMediaCallback(final List<MediaData> allPhotos) {

                getAllVideoInfos(activity, new LocalMediaCallback() {
                    @Override
                    public void onLocalMediaCallback(List<MediaData> allVideos) {
                        //图片
                        final List<MediaData> allMediaList = new ArrayList<>();

                        final List<MediaData> allPhotoList = new ArrayList<>();
                        if (allPhotos != null && allPhotos.size() > 0) {
                            allPhotoList.addAll(allPhotos);
                        }

                        //视频
                        final List<MediaData> allVideoList = new ArrayList<>();
                        if (allVideos != null && allVideos.size() > 0) {
                            allVideoList.addAll(allVideos);
                        }

                        //下面是排序
                        if (allPhotoList != null && allPhotoList.size() > 0 && allVideoList != null && allVideoList.size() > 0) {
                            allMediaList.addAll(allPhotoList);
                            allMediaList.addAll(allVideoList);
                        }
                        //采用冒泡排序的方式排列数据
                        sortByTimeRepoList(allMediaList);
                        localMediaCallback.onLocalMediaCallback(allMediaList);
                    }


                });
            }
        });
    }

    /**
     * 根据类型返回media
     *
     * @param mActivity
     * @param index
     */
    public static void getMediasByType(Activity mActivity, int index, LocalMediaCallback localMediaCallback) {
        if (index == MediaConstant.ALL_MEDIA) {
            getAllPhotoAndVideo(mActivity, localMediaCallback);
        } else if (index == MediaConstant.VIDEO) {
            getAllVideoInfos(mActivity, localMediaCallback);
        } else {
            getAllPhotoInfo(mActivity, localMediaCallback);
        }
    }

    /**
     * 根据时间进行排序
     *
     * @param itemInfoList
     */
    public static void sortByTimeRepoList(List<MediaData> itemInfoList) {

        Collections.sort(itemInfoList, new Comparator<MediaData>() {
                    @Override
                    public int compare(MediaData item1, MediaData item2) {
                        Date date1 = new Date(item1.getSize() * 1000L);
                        Date date2 = new Date(item2.getSize() * 1000L);
                        return date2.compareTo(date1);
                    }
                }
        );
    }

    //获取数据库字段
    private static void getUriColumns(Uri uri, Context context) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String[] columns = cursor.getColumnNames();

        for (String string : columns) {
            System.out.println(cursor.getColumnIndex(string) + " = " + string);
        }
    }

    public interface LocalMediaCallback {

        void onLocalMediaCallback(List<MediaData> allMediaTemp);
    }

}
