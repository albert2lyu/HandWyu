package com.imstuding.www.handwyu.ToolUtil;

/**
 * Created by yangkui on 2018/4/10.
 */

public class CardInf {
    private String kanum;
    private String creattime;
    private String describeInfo;
    private String phone;
    private String style;

    public CardInf(String kanum, String creattime, String describeInfo, String phone, String style) {
        this.kanum = kanum;
        this.creattime = creattime;
        this.describeInfo = describeInfo;
        this.phone = phone;
        this.style = style;
    }

    public String getKanum() {
        return kanum;
    }

    public void setKanum(String kanum) {
        this.kanum = kanum;
    }

    public String getCreattime() {
        return creattime;
    }

    public void setCreattime(String creattime) {
        this.creattime = creattime;
    }

    public String getDescribeInfo() {
        return describeInfo;
    }

    public void setDescribeInfo(String describeInfo) {
        this.describeInfo = describeInfo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
