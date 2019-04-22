package com.gmail.etauroginskaya.springbootmodule.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = {
        "com.gmail.etauroginskaya.service",
        "com.gmail.etauroginskaya.data"})
@PropertySource("classpath:application.properties")
@EnableAspectJAutoProxy
public class AppConfig {
}
