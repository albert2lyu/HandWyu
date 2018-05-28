package com.imstuding.www.handwyu.ToolUtil;

/**
 * Created by yangkui on 2018/1/26.
 */

public class SubJect {
    private String kcmc;//课程名称
    private String xf;//学分
    private String zcj;//总成绩
    private String xdfsmc;//修读方式名称
    private boolean check;
    private String cjjd;

    public SubJect(String kcmc, String xf, String zcj, String xdfsmc, String cjjd, boolean check) {
        this.kcmc = kcmc;
        this.xf = xf;
        this.zcj = zcj;
        this.xdfsmc = xdfsmc;
        this.check = check;
        this.cjjd=cjjd;
    }

    public String getCjjd() {
        return cjjd;
    }

    public void setCjjd(String cjjd) {
        this.cjjd = cjjd;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getKcmc() {
        return kcmc;
    }

    public void setKcmc(String kcmc) {
        this.kcmc = kcmc;
    }

    public String getXdfsmc() {
        return xdfsmc;
    }

    public String getZcj() {
        return zcj;
    }

    public void setZcj(String zcj) {
        this.zcj = zcj;
    }

    public String getXf() {

        return xf;
    }

    public void setXf(String xf) {
        this.xf = xf;
    }

    public void setXdfsmc(String xdfsmc) {
        this.xdfsmc = xdfsmc;
    }

}
