package com.itheima.medical.config;

import com.itheima.medical.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

//输出日志
@Slf4j
//这个注解是说明这是一个配置类
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    //扩展Mvc框架的消息转换器(配置完转换其之后需要重新启动一下）
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("扩展消息转换器");
        //创建一个新的消息转换器
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //设置对象转换，底层使用Jackson将java对象转为json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //将上面的消息转换器追加到mvc框架的转换集合中(这个0是把我们扩展的这个转换器放在最前面优先使用）
        converters.add(0,messageConverter);
    }

}
