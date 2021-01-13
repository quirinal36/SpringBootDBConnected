package com.example.demo.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({ProfileDevelop.class, ProfileProduction.class})
@Configuration
public class ProfileConfig {

}
