package com.example.taxation.util;

/**
 * 存放全局调用的常量
 */
public class ConstantUtil {

    public final static int USERNAME_LENGTH = 11; //手机号长度
    public final static int PASSWORD_LENGTH = 8; //密码长度

    //数据库操作返回码
    public final static String DB_OK = "200"; //数据库操作正常完成
    public final static String DB_FORBIDDEN = "403"; //数据库的操作被拒绝，例如插入一个存在的信息
    public final static String DB_NOT_FOUND = "404"; //经查询后，数据库无此数据

    //月份列表
    public final static String[] MONTH = {"5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", "1月", "2月", "3月", "上月", "本月"};
}
