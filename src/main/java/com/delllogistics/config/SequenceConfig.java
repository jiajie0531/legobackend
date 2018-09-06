package com.delllogistics.config;

import com.delllogistics.sequence.Sequence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**ID生成
 * Created by xzm on 2018-1-15.
 */
@Configuration
public class SequenceConfig {

    @Bean
    public Sequence initSequence(){
        return new Sequence(0,0);
    }
}
