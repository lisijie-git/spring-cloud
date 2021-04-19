package com.lisijietech.service.config;

import org.springframework.cloud.netflix.ribbon.RibbonClient;

/**
 * Ribbon的针对某个服务的java配置。
 * 如果java配置方式的全局配置中配置了IRule（MyRibbonConfig），这里指定的配置类也配置了，则会冲突报错规则bean有多个。
 * @author LiSiJie
 * @date 2021年3月11日 上午6:02:40
 */
//@RibbonClient(name = "consumer",configuration = MyRibbonOneTypeConfig.class)
public class MyRibbonAnnotationConfig {

}
