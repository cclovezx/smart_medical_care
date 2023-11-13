package com.itheima.medical.entity;

import lombok.Data;

@Data
public class Illinfo {

    private  String name;

    private int age;

    //1为男，0为女
    private int sex;

    //静息血压
    private int trestbps;

    //最高心跳
    private  int thalach;

    //是否患病
    private int target;


    //    //空腹血糖
//    private  int fbs;
//
//    //胸部疼痛类型
//    private int chest_pain_type;
//
//    //静息心电图
//    private int restecg;
//
//    //运动时有无心绞痛症状
//    private  int exang;

}
