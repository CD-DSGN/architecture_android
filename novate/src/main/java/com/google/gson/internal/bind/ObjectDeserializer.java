package com.google.gson.internal.bind;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

/**
 * Created by lps  on 2017/3/15.
 * 处理返回jsonobject 变成“”的情况
 * @since 2017年4月7日15:50:21
 * 处理定义的int类型，服务端返回null或“”的情况
 */

public class ObjectDeserializer implements JsonDeserializer {
    private static final String TYPE_NAME_PREFIX = "class ";

    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Gson mGson = new Gson();
        if (typeOfT.equals(int.class)) {
            try {
                return mGson.fromJson(json, int.class);
            } catch (JsonSyntaxException mE) {
                mE.printStackTrace();
                return 0;
            }
        }
        if (json.isJsonObject()) {
            return mGson.fromJson(json, typeOfT);
        } else if (json.isJsonArray()) {
            return mGson.fromJson(json, typeOfT);
        } else {
            String className = typeOfT.toString();
            if (className.startsWith(TYPE_NAME_PREFIX)) {
                className = className.substring(TYPE_NAME_PREFIX.length());
            }

            try {
                return Class.forName(className).newInstance();
            } catch (InstantiationException mE) {
                mE.printStackTrace();
            } catch (IllegalAccessException mE) {
                mE.printStackTrace();
            } catch (ClassNotFoundException mE) {
                mE.printStackTrace();
            }

        }
        return null;
    }
}
