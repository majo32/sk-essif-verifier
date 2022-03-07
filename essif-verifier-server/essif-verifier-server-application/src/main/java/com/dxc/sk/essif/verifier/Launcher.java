package com.dxc.sk.essif.verifier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.dxc.sk.essif")
public class Launcher extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Launcher.class, args);
    }
}