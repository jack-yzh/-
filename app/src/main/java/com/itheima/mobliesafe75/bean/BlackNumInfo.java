package com.itheima.mobliesafe75.bean;

/**
 * Created by jack_yzh on 2018/1/20.
 */

public class BlackNumInfo {
    //方便添加数据
    public BlackNumInfo(String blacknum, int mode) {
        this.blacknum = blacknum;
        this.mode = mode;
    }

    //黑名单号码
    private String blacknum;
    //拦截模式
    private int mode;

    public String getBlacknum() {
        return blacknum;
    }

    public void setBlacknum(String blacknum) {
        this.blacknum = blacknum;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        if(mode >=0 && mode<=2){
            this.mode = mode;
        }else{
            this.mode = 0;
        }
    }
    //方便测试数据
    @Override
    public String toString() {
        return "BlackNumInfo{" +
                "blacknum='" + blacknum + '\'' +
                ", mode=" + mode +
                '}';
    }
}
