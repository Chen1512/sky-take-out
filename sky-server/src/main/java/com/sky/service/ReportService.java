package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

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

    /**
     * @Description:用户统计接口
     * @return: com.sky.vo.UserReportVO
     * @author: chen
     * @date: 2023/8/1 11:44
     */
    UserReportVO getUserStatistics(LocalDate begin, LocalDate end);

    /**
     * @Description:订单统计接口
     * @return: com.sky.vo.OrderReportVO
     * @author: chen
     * @date: 2023/8/1 14:30
     */
    OrderReportVO getOrdersStatistics(LocalDate begin, LocalDate end);

    /**
     * @Description:查询销量排名top10接口
     * @return: com.sky.vo.SalesTop10ReportVO
     * @author: chen
     * @date: 2023/8/1 15:12
     */
    SalesTop10ReportVO getTop10(LocalDate begin, LocalDate end);
}
