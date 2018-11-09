package com.sven.huinews.international.config.http;

import java.util.TreeMap;

/**
 * Created by sfy. on 2018/4/9 0009.
 */

public class ParmsUtils {

    public TreeMap<String, String> params;

    public ParmsUtils getPostBody(String key, String value) {
        if (this.params == null) {
            params = new TreeMap<>();
        }
        try {
            params.put(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }




}
