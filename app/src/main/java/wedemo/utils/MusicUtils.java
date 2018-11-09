package wedemo.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import wedemo.music.MusicListResponse;

public class MusicUtils {

    /**
     * 扫描系统里面的音频文件，返回一个list集合
     */
    public static List<MusicListResponse.DataBean> getMusicData(Context context,int type_id) {
        List<MusicListResponse.DataBean> list = new ArrayList<>();
        // 媒体库查询语句（写一个工具类MusicUtils）
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                MusicListResponse.DataBean song = new MusicListResponse.DataBean();
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String DISPLAY_NAME = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                if(!TextUtils.isEmpty(title)){
                    song.setTitle(title.trim());
                }

                song.setId(type_id);
                song.setMusic_singer(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                song.setType_id(type_id);
                song.setMusic_duration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)) / 1000);
                song.setMusic_path(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
                int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
                if(isMusic != 0 && DISPLAY_NAME.endsWith(".mp3")){
                    list.add(song);
                }
            }
            // 释放资源
            cursor.close();
        }

        return list;
    }
}
