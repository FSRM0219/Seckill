package org.seckill.redis;

/**
 * expireSeconds()<br>getPrefix()
 */
public interface KeyPrefix {

    int expireSeconds();

    /**
     * String className = getClass().getSimpleName();<br>
     * getClass()方法返回当前对象的运行时类<br>
     * getSimpleName()方法返回类的简单名称<br>
     * return className + ":" + prefix;
     */
    String getPrefix();

}
