package org.seckill.util;

import java.util.UUID;

/**
 * 唯一id生成类<br>生成不带连字符的随机UUID
 */
public class UUIDUtil {

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
