package com;

import com.configuration.SpringConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by Denys Kovalenko on 10/7/2014.
 */
@ComponentScan
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SpringConfiguration.class, args);
    }
}
