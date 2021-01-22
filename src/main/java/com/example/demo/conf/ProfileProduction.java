package com.example.demo.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Profile(value="live")
@PropertySource({"classpath:profiles/live/application-live.properties"})
public class ProfileProduction {

}
