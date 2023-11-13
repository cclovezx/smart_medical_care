package com.itheima.medical.common;

//这里先梳理一下异常的一个概念，就是当我们后端服务报错的时候，我们的java就会抛出异常的对象，并将异常信息传递给调用者进行处理。注意这里的报错有些可能不是代码的问题，
//而是前端传入信息的一些问题，所以这个时候我们可以又两种方法，第一种就是通过try，catch来手动捕获异常，并且给出异常的一个处理方法。
// 第二种则是通过设置一个全局异常处理器，来将我们项目中的所有异常都放在这个里面

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

//全局异常处理器（底层就是基于代理，代理我们的这些controller，当controller抛出异常的时候通过aop将我们的那些增删改查什么的方法都拦截到，然后在这里统一去处理）

//这里后面的注解就是拦截那些类上加了RestController这个注解的（这里的拦截不是登录校验的拦截，而是当抛出异常的时候才去拦截，然后放在这里处理）
@ControllerAdvice(annotations = {RestController.class, Controller.class})
//这里的注释是因为后面我们需要返回一个json格式的数据
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    //进行异常处理的方法（下面的注释表示针对SQLIntegrityConstraintViolationException这个异常）
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    //将这个异常的信息传入进来（这里是处理“名称重复”的异常，因为数据库中很多表中的name都有unique的唯一约束，所以是sql数据库抛出来的！！！）
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.info(ex.getMessage());

        if(ex.getMessage().contains("Duplicate entry")){
            //这个代码是利用spilt对数组进行分割，然后再存入spilt数组
            String[] spilt = ex.getMessage().split(" ");
            //根据我们看到的结果，名字这一栏是在第三个块，也就是数组下标为2的位置
            String msg = spilt[2] + "已存在";
            return R.error(msg);
        }
        return R.error("未知错误");
    }
}
