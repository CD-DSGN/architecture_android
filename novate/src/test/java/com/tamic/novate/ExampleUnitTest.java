package com.tamic.novate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
     String jsonstr="{\"code\":200,\"message\":\"\\u5df2\\u6210\\u529f\\u67e5\\u8be2\\u5230\\u8be5\\u4e66\\u8be6\\u60c5\",\"data\":{\"book_name\":\"\\u5510\\u8bd7\\u4e09\\u767e\\u9996\\u7cbe\\u90092\",\"synopsis\":\"\",\"author\":\"\\u5d14\\u949f\\u96f72\",\"photo\":\"\\/image\\/6.jpg\",\"publisher\":\"\\u4eba\\u6c11\\u51fa\\u7248\\u793e\",\"pub_date\":\"2009-9-3\",\"collect_count\":\"4\",\"collect_user\":[{\"user_id\":\"11\",\"avatar_url\":{\"large\":\"\\/images\\/18161239627\\/be2ca648986a448f99fdf2b6eae09959.jpg\",\"mid\":\"\\/images\\/18161239627\\/thumb_be2ca648986a448f99fdf2b6eae09959.jpg\"}},{\"user_id\":\"8\",\"avatar_url\":{\"large\":\"\\/images\\/18228170109\\/b4bd0210c18b4d72a213212ef1a5fae0.jpg\",\"mid\":\"\\/images\\/18228170109\\/thumb_b4bd0210c18b4d72a213212ef1a5fae0.jpg\"}},{\"user_id\":\"1\",\"avatar_url\":{\"large\":\"\\/images\\/18508236987\\/3804f901bba742e0b0a92d885aaaf5d7.jpg\",\"mid\":\"\\/images\\/18508236987\\/thumb_3804f901bba742e0b0a92d885aaaf5d7.jpg\"}},{\"user_id\":\"3\",\"avatar_url\":{\"large\":\"\",\"mid\":\"\"}}],\"score_times\":\"\",\"total_score\":\"\",\"is_follow\":1}}";
        Gson mGson=new Gson();
        A mJson = mGson.fromJson(jsonstr, A.class);
        System.out.println(mJson.toString());

    }
}