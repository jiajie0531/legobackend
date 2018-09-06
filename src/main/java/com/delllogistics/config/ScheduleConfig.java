package com.delllogistics.config;

import com.delllogistics.config.properties.SchedulePoolProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**调度器
 * Created by xzm on 2018-3-27.
 */
@Configuration
public class ScheduleConfig {

    private final SchedulePoolProperties schedulePoolProperties;

    @Autowired
    public ScheduleConfig(SchedulePoolProperties schedulePoolProperties) {
        this.schedulePoolProperties = schedulePoolProperties;
    }

    /**
     *  初始化线程池
     * @return 线程池
     */
    @Bean
    public ScheduledExecutorService initScheduledExecutor() {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(schedulePoolProperties.getCorePoolSize());
        executor.setMaximumPoolSize(schedulePoolProperties.getMaximumPoolSize());
       /*
       空闲时间单位为秒
        */
        executor.setKeepAliveTime(schedulePoolProperties.getKeepAliveTime(),TimeUnit.SECONDS);
        return executor;
    }


}


