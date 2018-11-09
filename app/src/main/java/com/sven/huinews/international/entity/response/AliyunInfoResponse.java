package com.sven.huinews.international.entity.response;

import com.sven.huinews.international.base.BaseResponse;

import java.io.Serializable;

public class AliyunInfoResponse extends BaseResponse implements Serializable {


    /**
     * data : {"RequestId":"E571CC8A-F9FA-4EF5-9F6A-DA3A034C88E1","AccessKeyId":"STS.NK4q3seyXdTFVqKpqZxLcASPq","AccessKeySecret":"4pqPvW1FVnATcNpNTJWFmDHomP6PhQJ1kaiBGwfLai2Y","Expiration":"2018-09-19T16:42:14Z","SecurityToken":"CAISrgJ1q6Ft5B2yfSjIr4iBOonHiKZ505atVFf6lHEPdMNPrpb7kzz2IHxKeXVgCekfsv4ylGlW6P4ZlqwsE8AaGxQ72nSNWdAFnzm6aq/t5uZThN5t0e9FcAr+Ihr/29CoVYedZdjBe/CrRknZnytou9XTfimjWFrXH//0l5lrPP8NUwCkYAdeANBfKjVjpMIdVwXPMvr/CBPkmGfLDHdwvg11hV1+5am4oLCC7RaMp0f3w/MYmpz1JZGoTsxlM5oFJLXT5uFtcbfb2yN98gVD8LwM7JZJ4jDapNqQcTIzilekS7OMqYU/d1MpP/hhQ/Uf/aLG+Kcm6rCJpePe0A1QOOxZaSPbSb27zdHMcOHTbYpnKOqray2UjorSZsCr7Vt/ex8HORhWIsrpybzh/8WuIxqAAQEE4wdsjEEBQezGrnGVaQoSTPXLPDE1SnbnCtFGDi9QPnLthlmk5hbn9bKk3sTCB9CpnVQUif2l4ZQhWBzfhjN08LPwUtl+FeTuszM+MEWIi3QCYHkfTiA9vUlI4fWKMCQwenF1WhxDdL1egXPZTMo3Y3ct1O065S8Ke//tzWRI"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * RequestId : E571CC8A-F9FA-4EF5-9F6A-DA3A034C88E1
         * AccessKeyId : STS.NK4q3seyXdTFVqKpqZxLcASPq
         * AccessKeySecret : 4pqPvW1FVnATcNpNTJWFmDHomP6PhQJ1kaiBGwfLai2Y
         * Expiration : 2018-09-19T16:42:14Z
         * SecurityToken : CAISrgJ1q6Ft5B2yfSjIr4iBOonHiKZ505atVFf6lHEPdMNPrpb7kzz2IHxKeXVgCekfsv4ylGlW6P4ZlqwsE8AaGxQ72nSNWdAFnzm6aq/t5uZThN5t0e9FcAr+Ihr/29CoVYedZdjBe/CrRknZnytou9XTfimjWFrXH//0l5lrPP8NUwCkYAdeANBfKjVjpMIdVwXPMvr/CBPkmGfLDHdwvg11hV1+5am4oLCC7RaMp0f3w/MYmpz1JZGoTsxlM5oFJLXT5uFtcbfb2yN98gVD8LwM7JZJ4jDapNqQcTIzilekS7OMqYU/d1MpP/hhQ/Uf/aLG+Kcm6rCJpePe0A1QOOxZaSPbSb27zdHMcOHTbYpnKOqray2UjorSZsCr7Vt/ex8HORhWIsrpybzh/8WuIxqAAQEE4wdsjEEBQezGrnGVaQoSTPXLPDE1SnbnCtFGDi9QPnLthlmk5hbn9bKk3sTCB9CpnVQUif2l4ZQhWBzfhjN08LPwUtl+FeTuszM+MEWIi3QCYHkfTiA9vUlI4fWKMCQwenF1WhxDdL1egXPZTMo3Y3ct1O065S8Ke//tzWRI
         */

        private String RequestId;
        private String AccessKeyId;
        private String AccessKeySecret;
        private String Expiration;
        private String SecurityToken;

        public String getRequestId() {
            return RequestId;
        }

        public void setRequestId(String RequestId) {
            this.RequestId = RequestId;
        }

        public String getAccessKeyId() {
            return AccessKeyId;
        }

        public void setAccessKeyId(String AccessKeyId) {
            this.AccessKeyId = AccessKeyId;
        }

        public String getAccessKeySecret() {
            return AccessKeySecret;
        }

        public void setAccessKeySecret(String AccessKeySecret) {
            this.AccessKeySecret = AccessKeySecret;
        }

        public String getExpiration() {
            return Expiration;
        }

        public void setExpiration(String Expiration) {
            this.Expiration = Expiration;
        }

        public String getSecurityToken() {
            return SecurityToken;
        }

        public void setSecurityToken(String SecurityToken) {
            this.SecurityToken = SecurityToken;
        }
    }
}
