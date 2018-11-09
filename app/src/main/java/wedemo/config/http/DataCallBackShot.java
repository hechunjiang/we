package wedemo.config.http;


import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;
import rx.Subscriber;
import wedemo.base.BaseResponse;

/**
 * auther: sunfuyi
 * data: 2018/5/14
 * effect:
 */
public abstract class DataCallBackShot extends Subscriber<String> {

    public static final String NET_ERROR_MEG = "服务器繁忙或网络错误，请稍后再试";
    public static final String NETWOKR_ERROR_MEG = "网络无法连接，请检查网络后再试";
    public static final String NEW_ERROR_TIME_OUT = "网络请求超时，请稍后再试";
    public static final int NET_ERROR_CODE = -1000001;
    public static final int API_CODE_SUCCEED = 200;
    public static final int API_CODE_CIPHERTEXT = 201;
    public static final int API_CODE_TOKEN_EXPIRE = 9999;
    public static final int NET_TIME_OUT = 400;
    public static final int SMS_CODE_ERROR = 20004; // 验证码错误
    public static final int WECHAT_BIND_PHONE = 20006; // 绑定手机号
    public static final int NO_LOGIN = 9997;
    //对应HTTP的状态码
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int REQUEST_TIMEOUT = 408;
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int BAD_GATEWAY = 502;
    public static final int SERVICE_UNAVAILABLE = 503;
    public static final int GATEWAY_TIMEOUT = 504;
    public static final int NETWORK_ERROR = 501;

    @Override
    public void onCompleted() {
        onComplete();
    }

    @Override
    public void onError(Throwable e) {
        //  LogUtil.showLog("responseBody onError:" + e.getMessage());
        BaseResponse response = new BaseResponse();
        /**
         * 处理http错误
         */
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            // LogUtil.showLog("msg---httpException:" + httpException.code());
            //LogUtil.showLog("msg---httpException:" + httpException.message());
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                    //权限错误，需要实现
                    break;
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                    //返回超时
                    response.setCode(NET_TIME_OUT);
                    response.setMsg(NEW_ERROR_TIME_OUT);
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                    //服务器错误
                    response.setCode(INTERNAL_SERVER_ERROR);
                    response.setMsg(NET_ERROR_MEG);
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    //网络错误

                    break;
            }
        } else if (e instanceof ConnectException) {
            response.setCode(INTERNAL_SERVER_ERROR);
            response.setMsg(NET_ERROR_MEG);
        } else if (e instanceof UnknownHostException) {
            response.setCode(NETWORK_ERROR);
            response.setMsg(NETWOKR_ERROR_MEG);
        } else if (e instanceof SocketTimeoutException) {
            response.setCode(NET_TIME_OUT);
            response.setMsg(NEW_ERROR_TIME_OUT);
        }
        onFail(response);
    }

    @Override
    public void onNext(String s) {
//        BaseResponse response = new Gson().fromJson(s, BaseResponse.class);
//        if (response.getCode() == API_CODE_SUCCEED) {
//            onSucceed(s);
//        } else if (response.getCode() == API_CODE_TOKEN_EXPIRE) {
//            //验证token失效
//           // EventBus.getDefault().post(new TokenExpireEvent());
//        } else if (response.getCode() == API_CODE_CIPHERTEXT) {
        onSucceed(s);
//        } else {
//            onFail(response);
//        }
    }


    public abstract void onComplete();

    public abstract void onSucceed(String json);

    public abstract void onFail(BaseResponse baseResponse);
}
