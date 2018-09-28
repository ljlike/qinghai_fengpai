package com.daoyintech.daoyin_release.response;

public class DefinitionResponse {

    private static final String URI = "http://pemeusmpv.bkt.clouddn.com/";

    public static String getImgUrl(String url) {
        return URI + url;
    }

}
