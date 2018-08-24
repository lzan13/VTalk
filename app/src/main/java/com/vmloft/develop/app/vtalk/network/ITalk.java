package com.vmloft.develop.app.vtalk.network;

import com.vmloft.develop.app.vtalk.bean.TalkBean;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Create by lzan13 on 18/8/14 下午3:51
 * 一言相关接口
 */
public interface ITalk {


    /**
     * 获取随机的一句话
     */
    @GET("/")
    public Call<TalkBean> getRandomTalk();

    @GET("/")
    Call<TalkBean> getTypeTalk(@Query("c") String type);
}
