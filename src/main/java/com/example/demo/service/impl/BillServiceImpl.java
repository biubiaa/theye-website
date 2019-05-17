package com.example.demo.service.impl;

import com.example.demo.dao.BillSchedule;
import com.example.demo.mapper.BillScheduleMapper;
import com.example.demo.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BillServiceImpl implements BillService {
    @Autowired
    BillScheduleMapper billScheduleMapper;
    @Override
    public int insertBill(BillSchedule billSchedule) {
        billScheduleMapper.insert(billSchedule);
        return 1;
    }
}
