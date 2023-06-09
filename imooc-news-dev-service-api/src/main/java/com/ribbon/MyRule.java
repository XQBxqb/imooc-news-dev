package com.ribbon;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 昴星
 * @date 2023-06-07 18:56
 * @explain
 */
@Configuration
public class MyRule {
    @Bean
    public IRule iRule(){
        return new RandomRule();
    }
}
