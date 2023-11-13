package com.itheima.medical.controller;

import com.itheima.medical.common.Algorithm;
import com.itheima.medical.common.Code;
import com.itheima.medical.common.R;
import com.itheima.medical.entity.Illinfo;
import com.itheima.medical.entity.MedicalHistory;
import com.itheima.medical.entity.MedicalHistory;
import com.itheima.medical.service.Medical_HistoryService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;

@Slf4j
@RestController
@RequestMapping("/analyse")
public class AnalyseController {
    @Autowired
    private Medical_HistoryService medicalhistory_Service;


    @PostMapping("/ill")
    public R<String> anas(@RequestBody Illinfo illinfo){
        log.info("收到的病例信息为：{}",illinfo);

        //此处就是我们的算法，利用这个算法对我们前端收集到的数据进行加工和分析（jar包还有点问题，先写在common类里面吧）
        Algorithm algorithm = new Algorithm();
        //这里是我们算法跑出来的一个综合分数
        int Overall_scoring = algorithm.use(illinfo.getAge(),illinfo.getSex(),illinfo.getTrestbps(),illinfo.getThalach(),illinfo.getTarget());

        MedicalHistory medicalhistory= new MedicalHistory();

        //综合分数在这个区间的，代表患者罹患主动脉瓣狭窄，二尖瓣狭窄，骨性关节炎这三种疾病
        //if(Overall_scoring>50&&Overall_scoring<70){
        String name =illinfo.getName();
        if(name == null){
            return R.error("数据为空");
        }
        medicalhistory.setName(name);
        medicalhistory.setAsa(0);
        medicalhistory.setMs(0);
        medicalhistory.setOa(0);
        medicalhistory.setMi(0);
        medicalhistory.setHc(0);
        medicalhistory.setPah(0);
        medicalhistory.setPfps(0);
        medicalhistory.setPt(0);

        medicalhistory_Service.save(medicalhistory);

        return R.success(Code.Ana_Bad);
    }
}
