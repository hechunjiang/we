package com.sven.huinews.international.utils.update;

/**
 * auther: sunfuyi
 * data: 2018/5/30
 * effect:
 */
public class VersionInfo {
    /**
     * <version_code>234</version_code>
     * <version_name>2.3.4</version_name>
     * <apk_size>19MB</apk_size>
     * <apk_name>ju_nuws.apk</apk_name>
     * <apk_url>www.baidu.com/ju_nuws.apk</url>
     * <must_update>2</must_update>
     * <update_info>
     * <item>11111111</item>
     * <item>22222222</item>
     * <item>22222222</item>
     * <item>22222222</item>
     * <item>22222222</item>
     * </update_info>
     */

    private String version_code;
    private String version_name;
    private String version_msg;
    private String apk_size;
    private String apk_name;
    private String apk_url;
    private String must_update;  //如果值为1则是强制更新, 值为2则是强制更新


    public String getVersion_msg() {
        return version_msg;
    }

    public void setVersion_msg(String version_msg) {
        this.version_msg = version_msg;
    }

    public String getVersion_name() {
        return version_name;
    }

    public void setVersion_name(String version_name) {
        this.version_name = version_name;
    }

    public String getApk_size() {
        return apk_size;
    }

    public void setApk_size(String apk_size) {
        this.apk_size = apk_size;
    }

    public String getApk_name() {
        return apk_name;
    }

    public void setApk_name(String apk_name) {
        this.apk_name = apk_name;
    }

    public String getApk_url() {
        return apk_url;
    }

    public void setApk_url(String apk_url) {
        this.apk_url = apk_url;
    }

    public String getMust_update() {
        return must_update;
    }

    public void setMust_update(String must_update) {
        this.must_update = must_update;
    }

    public String getVersion_code() {
        return version_code;
    }

    public void setVersion_code(String version_code) {
        this.version_code = version_code;
    }

    @Override
    public String toString() {
        return "VersionInfo{" +
                "version_code='" + version_code + '\'' +
                ", version_name='" + version_name + '\'' +
                ", version_msg='" + version_msg + '\'' +
                ", apk_size='" + apk_size + '\'' +
                ", apk_name='" + apk_name + '\'' +
                ", apk_url='" + apk_url + '\'' +
                ", must_update='" + must_update + '\'' +
                '}';
    }
}
