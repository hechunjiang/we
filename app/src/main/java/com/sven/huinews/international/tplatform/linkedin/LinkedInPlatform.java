package com.sven.huinews.international.tplatform.linkedin;

import android.app.Activity;

import com.google.gson.Gson;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;
import com.sven.huinews.international.entity.jspush.JsShareType;
import com.sven.huinews.international.entity.response.LinkedInResponse;
import com.sven.huinews.international.utils.CommonUtils;
import com.sven.huinews.international.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sfy. on 2018/4/27 0027.
 */

public class LinkedInPlatform {
    private Activity activity;

    public LinkedInPlatform(Activity activity) {
        this.activity = activity;
    }

    public void initLinkedInLogin() {
        if (!CommonUtils.isApplicationAvilible(activity, "com.linkedin.android")) {
            ToastUtils.showShort(activity, "You've not installed Linkedin App,Please re-try after installation.\n");
            return;
        }
        LISessionManager.getInstance(activity).init(activity, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {
                // Authentication was successful.  You can now do
                // other calls with the SDK.
                String url = "https://api.linkedin.com/v1/people/~:(firstName,lastName,id,picture-url)?format=json";

                APIHelper apiHelper = APIHelper.getInstance(activity);
                apiHelper.getRequest(activity, url, new ApiListener() {
                    @Override
                    public void onApiSuccess(ApiResponse apiResponse) {


                        //  apiResponse.getResponseDataAsJson().

                        LinkedInResponse response = new Gson().fromJson(apiResponse.getResponseDataAsString(), LinkedInResponse.class);
                        if (linkedInLoginLisenter != null) {
                            linkedInLoginLisenter.getLinkedInfoSucceed(response);
                        }
                    }

                    @Override
                    public void onApiError(LIApiError LIApiError) {

                        if (linkedInLoginLisenter != null) {
                            linkedInLoginLisenter.getLinkedInfoError();
                        }
                    }
                });
            }

            @Override
            public void onAuthError(LIAuthError error) {
                // Handle authentication errors

            }
        }, false);
    }


    public void linkedInShare(final JsShareType jsShareType) {
        if (!CommonUtils.isApplicationAvilible(activity, "com.linkedin.android")) {
            ToastUtils.showShort(activity, "You've not installed Linkedin App,Please re-try after installation.\n");
            return;
        }
        LISessionManager.getInstance(activity).init(activity, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {

                String url = "https://api.linkedin.com/v1/people/~/shares?format=json";
                String shareJsonText = "{ \n" +
                        "   \"comment\":\"" + jsShareType.getContent() + "\"," +
                        "   \"visibility\":{ " +
                        "      \"code\":\"anyone\"" +
                        "   }," +
                        "   \"content\":{ " +
                        "      \"title\":\"" + jsShareType.getTitle() + "\"," +
                        "      \"description\":\"" + jsShareType.getContent() + "\"," +
                        "      \"submitted-url\":\"" + jsShareType.getUrl() + "\"," +
                        "      \"submitted-image-url\":\"" + jsShareType.getImgUrl() + "\"" +
                        "   }" +
                        "}";

                APIHelper apiHelper = APIHelper.getInstance(activity);
                apiHelper.postRequest(activity, url, shareJsonText, new ApiListener() {
                    @Override
                    public void onApiSuccess(ApiResponse apiResponse) {

                        if (linkedInShareLisenter != null) {

                            linkedInShareLisenter.getShareOk(CommonUtils.getShareSuccesResponse());
                        }
                    }

                    @Override
                    public void onApiError(LIApiError LIApiError) {

                        if (linkedInShareLisenter != null) {
                            linkedInShareLisenter.getShareFail(CommonUtils.getShareFailResponse());
                        }
                    }
                });
            }

            @Override
            public void onAuthError(LIAuthError error) {
                // Handle authentication errors

            }
        }, false);
    }

    // Build the list of member permissions our LinkedIn session requires
    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.W_SHARE);
    }


    public interface linkedInLoginLisenter {
        void getLinkedInfoSucceed(LinkedInResponse response);

        void getLinkedInfoError();
    }

    private linkedInLoginLisenter linkedInLoginLisenter;

    public void setLinkedInLoginLisenter(linkedInLoginLisenter l) {
        this.linkedInLoginLisenter = l;
    }

    public interface linkedInShareLisenter {
        void getShareOk(String response);

        void getShareFail(String response);
    }

    private linkedInShareLisenter linkedInShareLisenter;

    public void linkedInShareLisenter(linkedInShareLisenter l) {
        this.linkedInShareLisenter = l;
    }
}
