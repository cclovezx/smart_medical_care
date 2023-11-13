package com.itheima.medical.common;


import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

//自定义原数据对象处理器（用来进行公共字段填充的，必须继承MetaObjectHandler这个接口，这都是基于mybitas-plus）
//这个注释代表让spring框架来管理它
@Component
@Slf4j
public class MyMetaObjecthandler implements MetaObjectHandler {

    //metaObject就是我们的原数据
    //插入的时候自动填充
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填充【insert】");
        log.info(metaObject.toString());
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime",LocalDateTime.now());
    }

    //更新的时候自动填充
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填充【update】");
        log.info(metaObject.toString());

        //获取当前的线程id
        //Long id =Thread.currentThread().getId();
        //log.info("线程id为：{}",id);
        metaObject.setValue("updateTime",LocalDateTime.now());
    }
}
