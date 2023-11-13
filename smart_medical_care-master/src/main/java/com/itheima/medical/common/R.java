package com.itheima.medical.common;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;

//这是一个通用的包(通用返回结果），所以这里放的是统一的返回类（相当于Result)，响应回去的数据最终都会封装在这个类里面
//这里<T>是说明data是一个泛型（静态的），这是为了增强通用性，因为后面使用的时候可能传进来的data不一定只是一个employee对象，也可以是别的对象
@Data
public class R<T> {

    private Integer code; //编码：1成功，0和其它数字为失败

    private String msg; //错误信息

    private T data; //数据

    private Map map = new HashMap(); //动态数据

    //响应成功的时候使用（这里r.code = 1，因为前端页面校准的时候就是在判断这个code是不是等于1）
    //所以将id存入session之后就返回一个success，而在登出的时候需要把session中存储的当前员工的id直接删除掉（直接删除掉userinfo）
    public static <T> R<T> success(T object) {
        R<T> r = new R<T>();
        r.data = object;
        r.code = 1;
        return r;
    }

    //响应错误的时候，需要把错误的信息传进来，msg就是一个字符串
    public static <T> R<T> error(String msg) {
        R r = new R();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    //这个是用来操作动态数据的
    public R<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

}

