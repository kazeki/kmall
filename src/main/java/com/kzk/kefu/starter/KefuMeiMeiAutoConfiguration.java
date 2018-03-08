package com.kzk.kefu.starter;

import com.kzk.kefu.core.KefuMeiMeiService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(KefuMeiMeiService.class)
@ConditionalOnMissingBean(KefuMeiMeiService.class)
@EnableConfigurationProperties(KefuMeiMeiProperties.class)
public class KefuMeiMeiAutoConfiguration {

    @Bean
    KefuMeiMeiService kefuMeiMeiService(KefuMeiMeiProperties kefuMeiMeiProperties){
        KefuMeiMeiService kefuMeiMeiService = new KefuMeiMeiService();
        kefuMeiMeiService.setPrefix(kefuMeiMeiProperties.getPre());
        kefuMeiMeiService.setSuffix(kefuMeiMeiProperties.getSuf());
        return kefuMeiMeiService;
    }
}
