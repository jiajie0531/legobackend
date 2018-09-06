package com.delllogistics.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/** 调度线程池配置
 * Created by xzm on 2018-3-27.
 */
@Configuration
@ConfigurationProperties(prefix = SchedulePoolProperties.SCHEDULE_PREFIX)
@Setter
@Getter
public class SchedulePoolProperties {

    public static final String SCHEDULE_PREFIX="schedulePool";
    private int corePoolSize; //初始线程池数量
    private int maximumPoolSize; //最大线程池数量
    private int keepAliveTime; //线程空闲时间
}
