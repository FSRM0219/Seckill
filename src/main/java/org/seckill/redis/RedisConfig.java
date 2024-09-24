package org.seckill.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取配置文件的redis信息
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "redis")
public class RedisConfig {

    private String host;

    private int port;

    private int timeout;

    private String password;

    private int poolMaxTotal;

    private int poolMaxIdle;

    private int poolMaxWait;

}
