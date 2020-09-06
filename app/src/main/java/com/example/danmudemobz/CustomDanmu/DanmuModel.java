package com.example.danmudemobz.CustomDanmu;

/**
 * author  : Liushuai
 * time    : 2020/9/6 9:58
 * desc    :
 */
public class DanmuModel {

    private int type;//弹幕类型
    private String content;//内容

    private String userName;//发送人昵称
    private String headImg;//发送人头像

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
