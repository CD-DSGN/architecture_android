package com.grandmagic.readingmate.bean.request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.grandmagic.readingmate.base.AppBaseRequestBean;

import java.io.IOException;

/**
 * Created by zhangmengqi on 2017/3/13.
 */

public class PersonalInfoChangeRequestBean extends AppBaseRequestBean{
    private String user_name = null;
    private String signature = null;
    private int gender = 0;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    //强制输出""
    @Override
    public String toGson() {
        GsonBuilder gsonBuilder = new GsonBuilder().registerTypeAdapter(String.class, new TypeAdapter<String>() {

            @Override
            public void write(JsonWriter out, String value) throws IOException {
                if (value == null) {
                    // out.nullValue();
                    out.value(""); // 序列化时将 null 转为 ""
                } else {
                    out.value(value);
                }
            }

            @Override
            public String read(JsonReader in) throws IOException {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                }
                // return in.nextString();
                String str = in.nextString();
                if (str.equals("")) { // 反序列化时将 "" 转为 null
                    return null;
                } else {
                    return str;
                }
            }

        });

        Gson gson = gsonBuilder.create();
        return gson.toJson(this);
    }
}
