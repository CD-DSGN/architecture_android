package com.grandmagic.readingmate.utils;

/**
 * Created by zhangmengqi on 2017/1/22.
 */

public class Environment {
    public static final int ENVIRONMENT_PRODUCTION = 0;
    public static final int ENVIROMENT_DEVELOPMENT = 1;
    public static final int ENVIROMENT_MOCKSERVER = 3;


    public static final String BASEULR_PRODUCTION = "www.baidu.com";
    public static final String BASEULR_DEVELOPMENT = "www.baidu.com";

    public static int getEnviroment() {
        return ENVIROMENT_DEVELOPMENT;
    }

    public static String getUrl() {

        if (getEnviroment() == ENVIRONMENT_PRODUCTION) {
            return BASEULR_PRODUCTION;
        } else {
            return BASEULR_DEVELOPMENT;
        }

    }


}
