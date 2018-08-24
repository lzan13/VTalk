package com.vmloft.develop.app.vtalk.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Create by lzan13 on 18/8/14 下午3:58
 * 一言对象类
 */
public class TalkBean {

    /**
     * 参数名称	类型      描述
     * c	    可选      Cat，即类型，提交不同的参数代表不同的类别，具体：
     * a	Anime - 动画
     * b	Comic – 漫画
     * c	Game – 游戏
     * d	Novel – 小说
     * e	Myself – 原创
     * f	Internet – 来自网络
     * g	Other – 其他
     * 其他不存在参数	任意类型随机取得
     *
     * encode	可选
     * text	        返回纯净文本
     * json	        返回不进行unicode转码的json文本
     * js	            返回指定选择器(默认.hitokoto)的同步执行函数
     * 其他不存在参数	返回unicode转码的json文本
     *
     * charset	可选
     * utf-8	返回 UTF-8 编码的内容，支持与异步函数同用
     * gbk	返回 GBK 编码的内容，不支持与异步函数同用
     *
     * callback	可选          回调函数	将返回的内容传参给指定的异步函数
     */
    private String id; // 本条一言的id,可以链接到https://hitokoto.cn?id=[id]查看这个一言的完整信息
    private String hitokoto; // 一言正文，编码方式unicode使用utf-8
    private String type; // 类型，请参考第三节参数的表格
    private String from; // 一言的出处
    private String creator; // 添加者
    @SerializedName("create_at")
    private long createdAt; // 添加时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHitokoto() {
        return hitokoto;
    }

    public void setHitokoto(String hitokoto) {
        this.hitokoto = hitokoto;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n\tid:" + id);
        builder.append("\n\thitokoto:" + hitokoto);
        builder.append("\n\ttype:" + type);
        builder.append("\n\tfrom:" + from);
        builder.append("\n\tcreator:" + creator);
        builder.append("\n\tcreatedAt:" + createdAt);
        return builder.toString();
    }
}
