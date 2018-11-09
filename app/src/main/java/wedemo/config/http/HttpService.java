package wedemo.config.http;


import java.util.HashMap;

import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import rx.Observable;
import wedemo.activity.down.DownRequest;
import wedemo.base.BaseRequest;
import wedemo.config.api.Api;
import wedemo.music.MusicListRequest;
import wedemo.music.MusicSearchRequest;

/**
 * http请求接口在此写
 */
public interface HttpService {

    /**
     * 音乐分类
     *
     * @param map
     * @param request
     * @return
     */
    @POST(Api.MUSIC_CATEGROY)
    Observable<String> onMusicCategroy(@HeaderMap HashMap<String, String> map, @Body BaseRequest request);

    /**
     * 音乐列表
     *
     * @param map
     * @param request
     * @return
     */
    @POST(Api.MUSIC_LIST)
    Observable<String> onMusicList(@HeaderMap HashMap<String, String> map, @Body MusicListRequest request);

    /**
     * 音乐列表
     *
     * @param map
     * @param request
     * @return
     */
    @POST(Api.MUSIC_SEARCH)
    Observable<String> onMusicSearchList(@HeaderMap HashMap<String, String> map, @Body MusicSearchRequest request);

    /**
     * 阿里云临时上传token
     *
     * @param map
     * @param request
     * @return
     */
    @POST(Api.ALI_UOLOAD_TOKEN)
    Observable<String> onAliyunUploadToken(@HeaderMap HashMap<String, String> map, @Body BaseRequest request);


    /**
     * 美摄素材分类
     *
     * @param map
     * @param request
     * @return
     */
    @POST(Api.SDK_CATEGORY)
    Observable<String> onSDKCategory(@HeaderMap HashMap<String, String> map, @Body DownRequest request);

    /**
     * 素材下载
     * @param map
     * @param request
     * @return
     */
    @POST(Api.SDK_LIST)
    Observable<String> onSDKDown(@HeaderMap HashMap<String, String> map, @Body DownRequest request);

}
