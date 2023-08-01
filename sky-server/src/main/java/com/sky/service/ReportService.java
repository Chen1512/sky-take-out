package com.sky.service;

import com.sky.vo.TurnoverReportVO;

import java.time.LocalDate;

/**
 * @author shkstart
 * @create 2023--01-10:43
 */
public interface ReportService {
    /**
     * @Description:营业额统计接口
     * @return: com.sky.vo.TurnoverReportVO
     * @author: chen
     * @date: 2023/8/1 10:57
     */
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);
}
