package com.imstuding.www.handwyu.ToolUtil;

/**
 * Created by yangkui on 2018/3/30.
 */

public class Course {
    private String kcmc;//课程名称
    private String jxcdmc;//教室
    private String teaxms;//教师
    private String zc;//周次
    private String js;//星期几第几节

    public Course(String kcmc, String jxcdmc, String teaxms, String zc, String js) {
        this.kcmc = kcmc;
        this.jxcdmc = jxcdmc;
        this.teaxms = teaxms;
        this.zc = zc;
        this.js = js;
    }

    public String getKcmc() {
        return kcmc;
    }

    public String getJxcdmc() {
        return jxcdmc;
    }

    public String getTeaxms() {
        return teaxms;
    }

    public String getZc() {
        return zc;
    }

    public String getJs() {
        return js;
    }

    public void setKcmc(String kcmc) {
        this.kcmc = kcmc;
    }

    public void setJxcdmc(String jxcdmc) {
        this.jxcdmc = jxcdmc;
    }

    public void setTeaxms(String teaxms) {
        this.teaxms = teaxms;
    }

    public void setZc(String zc) {
        this.zc = zc;
    }

    public void setJs(String js) {
        this.js = js;
    }
}
