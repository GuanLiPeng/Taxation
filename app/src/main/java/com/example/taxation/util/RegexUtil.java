package com.example.taxation.util;

/**
 * 正则表达式
 */
public class RegexUtil {

    //手机号正则
    private static final String REGEX_PHONE_NUMBER = "^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$";


    /**
     * 检测手机号格式是否正确
     *
     * @param phoneNum 用户输入的手机号
     * @return 格式正确返回true，否则返回false
     */
    public boolean isPhoneNumber(String phoneNum) {
        return phoneNum.matches(REGEX_PHONE_NUMBER);
    }
}
