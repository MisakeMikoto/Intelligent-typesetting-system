package com.hyc.nsms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyc.nsms.mapper.HospitalMapper;
import com.hyc.nsms.model.entity.Hospital;
import com.hyc.nsms.service.HospitalService;
import org.springframework.stereotype.Service;

@Service
public class HospitalServiceImpl extends ServiceImpl<HospitalMapper, Hospital> implements HospitalService {

}
