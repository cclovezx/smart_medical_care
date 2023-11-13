package com.itheima.medical;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//这是一个启动类
@Slf4j
//开启web组件，用于过滤器Filter的使用
@ServletComponentScan
//springboot的一个注解
@SpringBootApplication
//开启事务注解的支持
@EnableTransactionManagement
public class MedicalApplication {
    public static void main(String[] args) {
        SpringApplication.run(MedicalApplication.class,args);
        log.info("项目启动成功");
    }
}
