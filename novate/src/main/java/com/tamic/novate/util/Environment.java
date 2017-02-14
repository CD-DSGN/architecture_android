package com.tamic.novate.util;

/**
 * Created by zhangmengqi on 2017/1/22.
 */

public class Environment {
    public static final int ENVIRONMENT_PRODUCTION = 0;
    public static final int ENVIROMENT_DEVELOPMENT = 1;
    public static final int ENVIROMENT_MOCKSERVER = 3;


    public static final String BASEULR_PRODUCTION = "http://192.168.1.115/reading-partner-php/api/web/";
    public static final String BASEULR_DEVELOPMENT = "http://192.168.1.165/";


    public static int getEnviroment() {
        return ENVIRONMENT_PRODUCTION;
    }

    public static String getUrl() {

        if (getEnviroment() == ENVIRONMENT_PRODUCTION) {
            return BASEULR_PRODUCTION;
        } else {
            return BASEULR_DEVELOPMENT;
        }

    }


}
