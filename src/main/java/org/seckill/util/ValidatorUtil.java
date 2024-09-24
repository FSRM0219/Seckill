package org.seckill.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 登录校验工具类——
 * 默认以1开头后面加10个数字为手机号，
 * 匹配返回true，否则返回false
 */
public class ValidatorUtil {

    private static final Pattern mobile_pattern = Pattern.compile("1\\d{10}");

    public static boolean isMobile(String src) {
        if (StringUtils.isEmpty(src)) {
            return false;
        }
        Matcher m = mobile_pattern.matcher(src);
        /*true if, and only if, the entire region sequence matches this matcher's pattern*/
        return m.matches();
    }
}
