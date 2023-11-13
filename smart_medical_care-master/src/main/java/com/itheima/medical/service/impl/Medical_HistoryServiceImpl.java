package com.itheima.medical.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.medical.entity.MedicalHistory;
import com.itheima.medical.mapper.Medical_HistoryMapper;
import com.itheima.medical.service.Medical_HistoryService;
import org.springframework.stereotype.Service;

@Service
public class Medical_HistoryServiceImpl extends ServiceImpl<Medical_HistoryMapper,MedicalHistory> implements Medical_HistoryService {
}
