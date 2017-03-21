package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by lps  on 2017/3/15.
 * 处理返回jsonobject 变成“”的情况
 */

public class ObjectDeserializer implements JsonDeserializer {
    private static final String TYPE_NAME_PREFIX = "class ";

    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
      if (json.isJsonObject()){
          Gson mGson=new Gson();
          return mGson.fromJson(json,typeOfT);
      }else {
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
