package com.itheima.medical.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

//为什么我们需要设置一个实体类，因为到时候后端在数据库中查出来的数据响应回去的时候需要封装在这个实体类中，所以我们实体类中变量的定义是和数据库中的变量的定义名称要保持一致（但是注意驼峰命名，在实体类中要驼峰命名）
@Data
public class User implements Serializable{

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String username;

    private String password;

    private String sex;

//    下述注解是用于设置公共字段填充的策略（方法），也就是在执行插入和更新的时候自动向数据库的指定字段中填充入值。
//    INSERT是代表插入（比如新增员工数据）的时候填充。INSERT_UPDATE是代表插入和更新（比如新增和修改员工数据）的时候都填充
//
//    创建时间只在插入的时候设置，更新时间在插入和更新的时候都要设置，所以update是INSERT_UPDATE，create是INSERT
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
