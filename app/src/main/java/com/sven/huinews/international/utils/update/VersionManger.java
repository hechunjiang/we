package com.sven.huinews.international.utils.update;

import android.util.Xml;

import com.sven.huinews.international.config.Constant;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * auther: sunfuyi
 * data: 2018/5/30
 * effect:
 */
public class VersionManger {
    public static VersionInfo getVersion() {
        VersionInfo mVersion = new VersionInfo();
        try {
            URL url = new URL(Constant.UPDATE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream is = conn.getInputStream();
            XmlPullParser parser = Xml.newPullParser();
            try {
                parser.setInput(is, "utf-8");//设置解析的数据源
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            mVersion = ParseXml(parser);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return mVersion;
    }


    /**
     * 解析xml文件
     *
     * @param parser
     * @return
     */
    public static VersionInfo ParseXml(XmlPullParser parser) {
        VersionInfo mGetVersion = new VersionInfo();
        try {
            //开始解析事件
            int eventType = parser.getEventType();
            //处理事件，不碰到文档结束就一直处理
            while (eventType != XmlPullParser.END_DOCUMENT) {
                //因为定义了一堆静态常量，所以这里可以用switch
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        // 不做任何操作或初开始化数据
                        break;
                    case XmlPullParser.START_TAG:
                        // 解析XML节点数据
                        // 获取当前标签名字
                        String tagName = parser.getName();
                        if ("version_code".equals(tagName)) {
                            mGetVersion.setVersion_code(parser.nextText()); //获取版本号
                        } else if ("apk_url".equals(tagName)) {
                            mGetVersion.setApk_url(parser.nextText()); //获取要升级的APK文件
                        } else if ("version_name".equals(tagName)) {
                            mGetVersion.setVersion_name(parser.nextText()); //获取该文件的信息
                        } else if ("apk_name".equals(tagName)) {
                            mGetVersion.setApk_name(parser.nextText());//获取apk名称
                        } else if ("apk_size".equals(tagName)) {
                            mGetVersion.setApk_size(parser.nextText());//获取APK大小
                        } else if ("must_update".equals(tagName)) {
                            mGetVersion.setMust_update(parser.nextText());//是否强制更新，1，是 2，否
                        } else if ("version_msg".equals(tagName)) {
                            mGetVersion.setVersion_msg(parser.nextText());
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        // 单节点完成，可往集合里边添加新的数据
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                }

                // 别忘了用next方法处理下一个事件，忘了的结果就成死循环#_#
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mGetVersion;
    }

}
