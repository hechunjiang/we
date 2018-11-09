package wedemo.config.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import wedemo.activity.down.DownRequest;
import wedemo.config.api.Api;
import wedemo.music.MusicListRequest;
import wedemo.music.MusicSearchRequest;


/**
 * Created by sfy. on 2018/4/9 0009.
 */

public class SignJson {

    /**
     * 音乐列表
     *
     * @param request
     * @return
     */
    public static String signMusicList(MusicListRequest request) {
        ParmsUtils parmsUtils = new ParmsUtils();
        parmsUtils.getPostBody(Api.ID, request.getId() + "");
        parmsUtils.getPostBody(Api.PAGE, request.getPage() + "");
        parmsUtils.getPostBody(Api.PAGE_SIZE, request.getPage_size() + "");
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }

    /**
     * 音乐搜索
     *
     * @param request
     * @return
     */
    public static String signMusicSearchList(MusicSearchRequest request) {
        ParmsUtils parmsUtils = new ParmsUtils();
        parmsUtils.getPostBody(Api.KEYWORDS, request.getKeyword() + "");
        parmsUtils.getPostBody(Api.PAGE, request.getPage() + "");
        parmsUtils.getPostBody(Api.PAGE_SIZE, request.getPage_size() + "");
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }


    /**
     * 素材分类
     *
     * @param request
     * @return
     */
    public static String signSDKCategroy(DownRequest request) {
        ParmsUtils parmsUtils = new ParmsUtils();
        parmsUtils.getPostBody(Api.TYPE, request.getType() + "");
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }

    /**
     * 获取素材下载列表
     *
     * @param request
     * @return
     */
    public static String signSDKDownload(DownRequest request) {
        ParmsUtils parmsUtils = new ParmsUtils();
        parmsUtils.getPostBody(Api.TYPE, request.getType() + "");
//        parmsUtils.getPostBody(Api.CATEGORY, request.getCategory() + "");
//        parmsUtils.getPostBody(Api.PAGE, request.getPage() + "");
//        parmsUtils.getPostBody(Api.PAGE_SIZE, request.getPage_size() + "");
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(parmsUtils.params);
        return json;
    }
}

