package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shkstart
 * @create 2023--01-10:43
 */
@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;

    /**
     * @Description:营业额统计接口
     * @return: com.sky.vo.TurnoverReportVO
     * @author: chen
     * @date: 2023/8/1 10:49
     */
    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        LocalDate date=begin;
        dateList.add(date);
        while (!date.equals(end)){
            date=date.plusDays(1);
            dateList.add(date);
        }

        List<Double> turnoverList = new ArrayList<>();
        dateList.forEach(data->{
            LocalDateTime beginTime = LocalDateTime.of(data, LocalTime.MIN);
            LocalDateTime endTime=LocalDateTime.of(data,LocalTime.MAX);
            Map map = new HashMap();
            map.put("status", Orders.COMPLETED);
            map.put("begin",beginTime);
            map.put("end", endTime);
            Double turnover = orderMapper.sumByMap(map);
            turnover=turnover==null?0.0:turnover;
            turnoverList.add(turnover);
        });
        TurnoverReportVO turnoverReportVO = TurnoverReportVO.builder().dateList(StringUtils.join(dateList, ",")).turnoverList(StringUtils.join(turnoverList, ",")).build();
        return turnoverReportVO;
    }


}
