package com.itheima.medical.common;

public class Algorithm {

    //算法内核
    public int use(int age,int sex,int trestbps,int thalach,int target){

        int sum = 0;

        //必患病
        if (target ==0){
            return 100;
        }

        if (age>50&&age<90){
            sum=20;
        }

        if(sex == 1){
            sum += 10;
        }

        if (trestbps>80&&trestbps<100){
            sum +=10;
        }

        if (thalach>120){
            sum +=20;
        }
        //下面就是对这个传进来的病例参数进行一个运算
        return sum;
    }

}
