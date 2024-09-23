package com.jesper.seckill.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 使用Apache Commons Codec库中的DigestUtils.md5Hex方法进行MD5加密，返回加密后的十六进制字符串
 * 1、inputPassToFormPass方法：负责处理用户输入的密码，进行第一次加密，用于网络传输；
 * 2、formPassToDBPass方法：负责将经过一次加密的密码进行二次加密，用于数据库存储；
 * 3、inputPassToDbPass方法：整合前两个方法功能，提供一个完整的流程，从用户输入到数据库存储的密码转换
 */
public class MD5Util {

    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    private static final String salt = "1a2b3c4d";

    /**
     * 第一次MD5加密，用于网络传输
     */
    public static String inputPassToFormPass(String inputPass) {
        return formPassToDBPass(inputPass, salt);
    }

    /**
     * 第二次MD5加密，用于存储到数据库
     */
    public static String formPassToDBPass(String formPass, String salt) {
        String str = "" + salt.charAt(0) + salt.charAt(2) + formPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    public static String inputPassToDbPass(String input, String saltDB) {
        String formPass = inputPassToFormPass(input);
        return formPassToDBPass(formPass, saltDB);
    }

    public static void main(String[] args) {
        System.out.println(inputPassToDbPass("123456", "1a2b3c4d"));
    }
}
