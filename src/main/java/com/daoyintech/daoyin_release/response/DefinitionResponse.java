package com.daoyintech.daoyin_release.response;

import java.util.Date;

public class DefinitionResponse {

    public static final String URI = "http://pemeusmpv.bkt.clouddn.com/";

    public static final String RESERVATION_DATE = "reservation_date";

    public static final Integer DEFAULT_RESERVATION_NUMBER = 10;

    public static String getImgUrl(String url) {
        return URI + url;
    }

    public static String getReservationDate(Date date){
        Long time = date.getTime();
        return RESERVATION_DATE+"-"+time;
    }

}
