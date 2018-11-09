package com.sven.huinews.international.utils.shot.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.sven.huinews.international.utils.shot.utils.asset.NvAsset;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Created by admin on 2018-6-5.
 */

public class PathUtils {

    private static final String TAG = PathUtils.class.getName();

    private static String RECORDING_DIRECTORY = "watch$earn" + File.separator + "Record";
    private static String MUSIC_DOWNLOAD_DIRECTORY = "watch$earn" + File.separator + "Music";
    private static String AUDIO_RECORD_DIRECTORY = "NvStreamingSdk" + File.separator + "AudioRecord";

    private static String ASSET_DOWNLOAD_DIRECTORY = "NvStreamingSdk" + File.separator + "Asset";
    private static String ASSET_DOWNLOAD_DIRECTORY_FILTER = "NvStreamingSdk" + File.separator + "Asset" + File.separator + "Filter";
    private static String ASSET_DOWNLOAD_DIRECTORY_THEME = "NvStreamingSdk" + File.separator + "Asset" + File.separator + "Theme";
    private static String ASSET_DOWNLOAD_DIRECTORY_CAPTION = "NvStreamingSdk" + File.separator + "Asset" + File.separator + "Caption";
    private static String ASSET_DOWNLOAD_DIRECTORY_ANIMATEDSTICKER = "NvStreamingSdk" + File.separator + "Asset" + File.separator + "AnimatedSticker";
    private static String ASSET_DOWNLOAD_DIRECTORY_TRANSITION = "NvStreamingSdk" + File.separator + "Asset" + File.separator + "Transition";
    private static String ASSET_DOWNLOAD_DIRECTORY_CAPTURE_SCENE = "NvStreamingSdk" + File.separator + "Asset" + File.separator + "CaptureScene";
    private static String ASSET_DOWNLOAD_DIRECTORY_PARTICLE = "NvStreamingSdk" + File.separator + "Asset" + File.separator + "Particle";
    private static String ASSET_DOWNLOAD_DIRECTORY_FACE_STICKER = "NvStreamingSdk" + File.separator + "Asset" + File.separator + "FaceSticker";
    private static String ASSET_DOWNLOAD_DIRECTORY_CUSTOM_ANIMATED_STICKER = "NvStreamingSdk" + File.separator + "Asset" + File.separator + "CustomAnimatedSticker";
    private static String ASSET_DOWNLOAD_DIRECTORY_FACE1_STICKER = "NvStreamingSdk" + File.separator + "Asset" + File.separator + "Face1Sticker";

    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                deleteDirectoryFile(f);
            }
            file.delete();//如要保留文件夹，只删除文件，请注释这行
        } else if (file.exists()) {
            file.delete();
        }
    }

    public static void deleteDirectoryFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                deleteDirectoryFile(f);
            }
            //file.delete();//如要保留文件夹，只删除文件，请注释这行
        } else if (file.exists()) {
            file.delete();
        }
    }

    public static File getMusicDownloadFile(String fileNmae) {
        File captureDir = new File(Environment.getExternalStorageDirectory(), MUSIC_DOWNLOAD_DIRECTORY);
        if (!captureDir.exists() && !captureDir.mkdirs()) {
            Log.e(TAG, "Failed to make NetEase directory");
            return null;
        }

        File file = new File(captureDir, fileNmae + ".mp3");
        if (file.exists()) {
            file.delete();
        }

        return file;
    }

    public static String getMusicDownloadDirectory(String fileName) {
        File captureDir = new File(Environment.getExternalStorageDirectory(), MUSIC_DOWNLOAD_DIRECTORY);
        if (!captureDir.exists() && !captureDir.mkdirs()) {
            Log.e(TAG, "Failed to make NetEase directory");
            return null;
        }

        File file = new File(captureDir, fileName + ".mp3");
        if (file.exists()) {
            file.delete();
        }

        return file.getAbsolutePath();
    }

    public static String getRecordVideoPath() {
        File captureDir = new File(Environment.getExternalStorageDirectory(), RECORDING_DIRECTORY);
        if (!captureDir.exists() && !captureDir.mkdirs()) {
            Log.e(TAG, "Failed to make NetEase directory");
            return null;
        }

        String filename = getCharacterAndNumber() + ".mp4";

        File file = new File(captureDir, filename);
        if (file.exists()) {
            file.delete();
        }

        return file.getAbsolutePath();
    }

    public static String getRecordVideoPath(String name) {
        File captureDir = new File(Environment.getExternalStorageDirectory(), RECORDING_DIRECTORY);
        if (!captureDir.exists() && !captureDir.mkdirs()) {
            Log.e(TAG, "Failed to make NetEase directory");
            return null;
        }

        String filename = name + ".mp4";

        File file = new File(captureDir, filename);
        if (file.exists()) {
            file.delete();
        }

        return file.getAbsolutePath();
    }


    public static String getRecordPicturePath() {
        File captureDir = new File(Environment.getExternalStorageDirectory(), RECORDING_DIRECTORY);
        if (!captureDir.exists() && !captureDir.mkdirs()) {
            Log.e(TAG, "Failed to make NetEase directory");
            return null;
        }

        String filename = getCharacterAndNumber() + ".jpg";

        File file = new File(captureDir, filename);
        if (file.exists()) {
            file.delete();
        }

        return file.getAbsolutePath();
    }

    public static String getAudioRecordFilePath() {
        File captureDir = new File(Environment.getExternalStorageDirectory(), AUDIO_RECORD_DIRECTORY);
        if (!captureDir.exists() && !captureDir.mkdirs()) {
            Log.e(TAG, "Failed to make audio record directory");
            return null;
        }
        return captureDir.getAbsolutePath();
    }

    public static String getCharacterAndNumber() {
        String rel = "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());
        rel = formatter.format(curDate);
        return rel;
    }

    public static String getAssetDownloadPath(int assetType) {
        File assetDownloadDir = new File(Environment.getExternalStorageDirectory(), ASSET_DOWNLOAD_DIRECTORY);
        if (!assetDownloadDir.exists()) {
            if (!assetDownloadDir.mkdirs()) {
                Log.e(TAG, "Failed to make asset download directory");
                return null;
            }
        }

        switch (assetType) {
            case NvAsset.ASSET_THEME: {
                File assetDownloadDirTheme = new File(Environment.getExternalStorageDirectory(), ASSET_DOWNLOAD_DIRECTORY_THEME);
                if (!assetDownloadDirTheme.exists()) {
                    if (!assetDownloadDirTheme.mkdirs()) {
                        Log.e(TAG, "Failed to make asset download theme directory");
                        return null;
                    }
                }
                return assetDownloadDirTheme.getAbsolutePath();
            }
            case NvAsset.ASSET_FILTER: {
                File assetDownloadDirFilter = new File(Environment.getExternalStorageDirectory(), ASSET_DOWNLOAD_DIRECTORY_FILTER);
                if (!assetDownloadDirFilter.exists()) {
                    if (!assetDownloadDirFilter.mkdirs()) {
                        Log.e(TAG, "Failed to make asset download filter directory");
                        return null;
                    }
                }
                return assetDownloadDirFilter.getAbsolutePath();
            }
            case NvAsset.ASSET_CAPTION_STYLE: {
                File assetDownloadDirCaption = new File(Environment.getExternalStorageDirectory(), ASSET_DOWNLOAD_DIRECTORY_CAPTION);
                if (!assetDownloadDirCaption.exists()) {
                    if (!assetDownloadDirCaption.mkdirs()) {
                        Log.e(TAG, "Failed to make asset download caption directory");
                        return null;
                    }
                }
                return assetDownloadDirCaption.getAbsolutePath();
            }
            case NvAsset.ASSET_ANIMATED_STICKER: {
                File assetDownloadDirAnimatedSticker = new File(Environment.getExternalStorageDirectory(), ASSET_DOWNLOAD_DIRECTORY_ANIMATEDSTICKER);
                if (!assetDownloadDirAnimatedSticker.exists()) {
                    if (!assetDownloadDirAnimatedSticker.mkdirs()) {
                        Log.e(TAG, "Failed to make asset download animated sticker directory");
                        return null;
                    }
                }
                return assetDownloadDirAnimatedSticker.getAbsolutePath();
            }
            case NvAsset.ASSET_VIDEO_TRANSITION: {
                File assetDownloadDirTransition = new File(Environment.getExternalStorageDirectory(), ASSET_DOWNLOAD_DIRECTORY_TRANSITION);
                if (!assetDownloadDirTransition.exists()) {
                    if (!assetDownloadDirTransition.mkdirs()) {
                        Log.e(TAG, "Failed to make asset download transition directory");
                        return null;
                    }
                }
                return assetDownloadDirTransition.getAbsolutePath();
            }
            case NvAsset.ASSET_CAPTURE_SCENE: {
                File assetDownloadDirCaptureScene = new File(Environment.getExternalStorageDirectory(), ASSET_DOWNLOAD_DIRECTORY_CAPTURE_SCENE);
                if (!assetDownloadDirCaptureScene.exists()) {
                    if (!assetDownloadDirCaptureScene.mkdirs()) {
                        Log.e(TAG, "Failed to make asset download capture scene directory");
                        return null;
                    }
                }
                return assetDownloadDirCaptureScene.getAbsolutePath();
            }
            case NvAsset.ASSET_PARTICLE: {
                File assetDownloadDirParticle = new File(Environment.getExternalStorageDirectory(), ASSET_DOWNLOAD_DIRECTORY_PARTICLE);
                if (!assetDownloadDirParticle.exists()) {
                    if (!assetDownloadDirParticle.mkdirs()) {
                        Log.e(TAG, "Failed to make asset download particle directory");
                        return null;
                    }
                }
                return assetDownloadDirParticle.getAbsolutePath();
            }
            case NvAsset.ASSET_FACE_STICKER: {
                File assetDownloadDirFaceSticker = new File(Environment.getExternalStorageDirectory(), ASSET_DOWNLOAD_DIRECTORY_FACE_STICKER);
                if (!assetDownloadDirFaceSticker.exists()) {
                    if (!assetDownloadDirFaceSticker.mkdirs()) {
                        Log.e(TAG, "Failed to make asset download face sticker directory");
                        return null;
                    }
                }
                return assetDownloadDirFaceSticker.getAbsolutePath();
            }
            case NvAsset.ASSET_CUSTOM_ANIMATED_STICKER: {
                File assetDownloadDirCustomSticker = new File(Environment.getExternalStorageDirectory(), ASSET_DOWNLOAD_DIRECTORY_CUSTOM_ANIMATED_STICKER);
                if (!assetDownloadDirCustomSticker.exists()) {
                    if (!assetDownloadDirCustomSticker.mkdirs()) {
                        Log.e(TAG, "Failed to make asset download custom sticker directory");
                        return null;
                    }
                }
                return assetDownloadDirCustomSticker.getAbsolutePath();
            }
            case NvAsset.ASSET_FACE1_STICKER: {
                File assetDownloadDirFace1Sticker = new File(Environment.getExternalStorageDirectory(), ASSET_DOWNLOAD_DIRECTORY_FACE1_STICKER);
                if (!assetDownloadDirFace1Sticker.exists()) {
                    if (!assetDownloadDirFace1Sticker.mkdirs()) {
                        Log.e(TAG, "Failed to make asset download face1 sticker directory");
                        return null;
                    }
                }
                return assetDownloadDirFace1Sticker.getAbsolutePath();
            }
        }
        return assetDownloadDir.getAbsolutePath();
    }

    public static boolean unZipAsset(Context context, String assetName, String outputDirectory, boolean isReWrite) {
        // 创建解压目标目录
        File file = new File(outputDirectory);
        // 如果目标目录不存在，则创建
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            // 打开压缩文件
            InputStream inputStream = context.getAssets().open(assetName);
            ZipInputStream zipInputStream = new ZipInputStream(inputStream);
            // 读取一个进入点
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            // 使用1Mbuffer
            byte[] buffer = new byte[1024 * 1024];
            // 解压时字节计数
            int count = 0;
            // 如果进入点为空说明已经遍历完所有压缩包中文件和目录
            while (zipEntry != null) {
                // 如果是一个目录
                if (zipEntry.isDirectory()) {
                    file = new File(outputDirectory + File.separator + zipEntry.getName());
                    // 文件需要覆盖或者是文件不存在
                    if (isReWrite || !file.exists()) {
                        file.mkdir();
                    }
                } else {
                    // 如果是文件
                    file = new File(outputDirectory + File.separator + zipEntry.getName());
                    // 文件需要覆盖或者文件不存在，则解压文件
                    if (isReWrite || !file.exists()) {
                        file.createNewFile();
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        while ((count = zipInputStream.read(buffer)) > 0) {
                            fileOutputStream.write(buffer, 0, count);
                        }
                        fileOutputStream.close();
                    }
                }
                // 定位到下一个文件入口
                zipEntry = zipInputStream.getNextEntry();
            }
            zipInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean unZipFile(String zipFile, String folderPath) {
        ZipFile zfile = null;
        try {
            // 转码为GBK格式，支持中文
            zfile = new ZipFile(zipFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        Enumeration zList = zfile.entries();
        ZipEntry ze = null;
        byte[] buf = new byte[1024];
        while (zList.hasMoreElements()) {
            ze = (ZipEntry) zList.nextElement();
            // 列举的压缩文件里面的各个文件，判断是否为目录
            if (ze.isDirectory()) {
                String dirstr = folderPath + ze.getName();
                dirstr.trim();
                File f = new File(dirstr);
                f.mkdir();
                continue;
            }
            OutputStream os = null;
            FileOutputStream fos = null;
            // ze.getName()会返回 script/start.script这样的，是为了返回实体的File
            File realFile = getRealFileName(folderPath, ze.getName());
            try {
                fos = new FileOutputStream(realFile);
            } catch (FileNotFoundException e) {
                return false;
            }
            os = new BufferedOutputStream(fos);
            InputStream is = null;
            try {
                is = new BufferedInputStream(zfile.getInputStream(ze));
            } catch (IOException e) {
                return false;
            }
            int readLen = 0;
            // 进行一些内容复制操作
            try {
                while ((readLen = is.read(buf, 0, 1024)) != -1) {
                    os.write(buf, 0, readLen);
                }
            } catch (IOException e) {
                return false;
            }
            try {
                is.close();
                os.close();
            } catch (IOException e) {
                return false;
            }
        }
        try {
            zfile.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static File getRealFileName(String baseDir, String absFileName) {
        absFileName = absFileName.replace("\\", "/");
        String[] dirs = absFileName.split("/");
        File ret = new File(baseDir);
        String substr = null;
        if (dirs.length > 1) {
            for (int i = 0; i < dirs.length - 1; i++) {
                substr = dirs[i];
                ret = new File(ret, substr);
            }

            if (!ret.exists())
                ret.mkdirs();
            substr = dirs[dirs.length - 1];
            ret = new File(ret, substr);
            return ret;
        } else {
            ret = new File(ret, absFileName);
        }
        return ret;
    }

    public static int getAssetVersionWithPath(String path) {
        String[] strings = path.split("/");
        if (strings.length > 0) {
            String filename = strings[strings.length - 1];
            String[] parts = filename.split(".");
            if (parts.length == 3) {
                return Integer.parseInt(parts[1]);
            } else {
                return 1;
            }
        } else {
            return 1;
        }
    }

    public static long getFileModifiedTime(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return 0;
        } else {
            return file.lastModified();
        }
    }

    //判断文件是否存在
    public static boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public static String getMusicPath(String name) {
        return Environment.getExternalStorageDirectory() + File.separator + MUSIC_DOWNLOAD_DIRECTORY + File.separator + name + ".mp3";
    }
}
