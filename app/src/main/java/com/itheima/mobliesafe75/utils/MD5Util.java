package com.itheima.mobliesafe75.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by jack_yzh on 2017/12/2.
 */

public class MD5Util {
    public static String passwordMD5(String password){
        StringBuffer sb = new StringBuffer();
        try {
            //获取数据分解方法
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            //将字符串转化成字节数组，在MD5转换成16字节的字节数组
            byte[] digest = messageDigest.digest(password.getBytes());
            //for循环 将1字节8位整数转换成2个16进制的字符串
            for (int i=0;i<digest.length;i++){
                //把负数变成正数
                int result = digest[i] & 0Xff;
                //把整数变成字符串
                String hexString = Integer.toHexString(result);
                //把省略的0补回来
                if (hexString.length()<2){
                    sb.append("0");
                }
                //收集字符串
                sb.append(hexString);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
