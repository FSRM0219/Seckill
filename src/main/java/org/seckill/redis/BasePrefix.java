package org.seckill.redis;

public abstract class BasePrefix implements KeyPrefix {

    private final int expireSeconds;

    private final String prefix;

    public BasePrefix(String prefix) {
        this(0, prefix); // 0代表永不过期
    }

    public BasePrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    @Override
    public int expireSeconds() {
        return expireSeconds;
    }

    /**
     * getClass()方法返回当前对象的运行时类<br>
     * getSimpleName()方法返回类的简单名称<br>
     */
    @Override
    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className + ":" + prefix;
    }
}
