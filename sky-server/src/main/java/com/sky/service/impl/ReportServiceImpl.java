package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author shkstart
 * @create 2023--01-10:43
 */
@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private WorkspaceService workspaceService;

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

    /**
     * @Description:用户统计接口
     * @return: com.sky.vo.UserReportVO
     * @author: chen
     * @date: 2023/8/1 11:44
     */
    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        LocalDate date=begin;
        dateList.add(date);
        while (!date.equals(end)){
            date=date.plusDays(1);
            dateList.add(date);
        }
        List<Integer> newUserList = new ArrayList<>();
        List<Integer> totalUserList = new ArrayList<>();

        dateList.forEach(data->{
            LocalDateTime beginTime = LocalDateTime.of(data, LocalTime.MIN);
            LocalDateTime endTime=LocalDateTime.of(data,LocalTime.MAX);
            Integer newUser = getUserCount(beginTime, endTime);
            newUser=newUser==null?0:newUser;
            newUserList.add(newUser);
            Integer totalUser = getUserCount(null, endTime);
            totalUser=totalUser==null?0:totalUser;
            totalUserList.add(totalUser);
        });

        UserReportVO userReportVO = UserReportVO.builder().dateList(StringUtils.join(dateList,",")).newUserList(StringUtils.join(newUserList,",")).totalUserList(StringUtils.join(totalUserList,",")).build();
        return userReportVO;
    }
/**
 * @Description:订单统计接口
 * @return: com.sky.vo.OrderReportVO
 * @author: chen
 * @date: 2023/8/1 14:31
 */
    @Override
    public OrderReportVO getOrdersStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        LocalDate date=begin;
        dateList.add(date);
        while (!date.equals(end)){
            date=date.plusDays(1);
            dateList.add(date);
        }
        List<Integer> orderCountList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();

        dateList.forEach(data-> {
            LocalDateTime beginTime = LocalDateTime.of(data, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(data, LocalTime.MAX);
            Integer totalOrderCount = getOrderCount(beginTime,endTime,null);
            totalOrderCount=totalOrderCount==null?0:totalOrderCount;
            orderCountList.add(totalOrderCount);
            Integer validOrderCount = getOrderCount(beginTime,endTime,Orders.COMPLETED);
            validOrderCount=validOrderCount==null?0:validOrderCount;
            validOrderCountList.add(validOrderCount);
        });
        Integer totalOrderCount = orderCountList.stream().reduce(Integer::sum).get();
        Integer validOrderCount = validOrderCountList.stream().reduce(Integer::sum).get();
        Double orderCompletionRate = 0.0;
        if(totalOrderCount != 0){
            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
        }
        OrderReportVO orderReportVO = OrderReportVO.builder().dateList(StringUtils.join(dateList, ","))
                .orderCountList(StringUtils.join(orderCountList, ","))
                .validOrderCountList(StringUtils.join(validOrderCountList, ","))
                .orderCompletionRate(orderCompletionRate)
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount).build();
        return orderReportVO;
    }

    /**
     * @Description:查询销量排名top10接口
     * @return: com.sky.vo.SalesTop10ReportVO
     * @author: chen
     * @date: 2023/8/1 15:12
     */
    @Override
    public SalesTop10ReportVO getTop10(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
        List<GoodsSalesDTO> goodsSalesDTOList = orderMapper.getSalesTop10(beginTime, endTime);
        List<String> nameList = goodsSalesDTOList.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        List<Integer> numberList = goodsSalesDTOList.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());
        SalesTop10ReportVO salesTop10ReportVO = SalesTop10ReportVO.builder().nameList(StringUtils.join(nameList, ",")).numberList(StringUtils.join(numberList, ",")).build();
        return salesTop10ReportVO;
    }

    /**
     * @Description:导出Excel报表接口
     * @return: void
     * @author: chen
     * @date: 2023/8/1 21:57
     */
    @Override
    public void exportBusinessData(HttpServletResponse response) {
        LocalDate begin = LocalDate.now().plusDays(-30);
        LocalDate end = LocalDate.now().plusDays(-1);
        //查询概览运营数据，提供给Excel模板文件
        BusinessDataVO businessData = workspaceService.businessData(LocalDateTime.of(begin,LocalTime.MIN), LocalDateTime.of(end, LocalTime.MAX));
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");
        try {
            //基于提供好的模板文件创建一个新的Excel表格对象
            XSSFWorkbook excel = new XSSFWorkbook(inputStream);
            //获得Excel文件中的一个Sheet页
            XSSFSheet sheet = excel.getSheet("Sheet1");

            sheet.getRow(1).getCell(1).setCellValue(begin + "至" + end);
            //获得第4行
            XSSFRow row = sheet.getRow(3);
            //获取单元格
            row.getCell(2).setCellValue(businessData.getTurnover());
            row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
            row.getCell(6).setCellValue(businessData.getNewUsers());
            row = sheet.getRow(4);
            row.getCell(2).setCellValue(businessData.getValidOrderCount());
            row.getCell(4).setCellValue(businessData.getUnitPrice());
            for (int i = 0; i < 30; i++) {
                LocalDate date = begin.plusDays(i);
                //准备明细数据
                businessData = workspaceService.businessData(LocalDateTime.of(date,LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));
                row = sheet.getRow(7 + i);
                row.getCell(1).setCellValue(date.toString());
                row.getCell(2).setCellValue(businessData.getTurnover());
                row.getCell(3).setCellValue(businessData.getValidOrderCount());
                row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
                row.getCell(5).setCellValue(businessData.getUnitPrice());
                row.getCell(6).setCellValue(businessData.getNewUsers());
            }
            //通过输出流将文件下载到客户端浏览器中
            ServletOutputStream out = response.getOutputStream();
            excel.write(out);
            //关闭资源
            out.flush();
            out.close();
            excel.close();

        }catch (IOException e){
            e.printStackTrace();
        }

    }


    /**
     * 根据时间区间统计用户数量
     * @param beginTime
     * @param endTime
     * @return
     */
    private Integer getUserCount(LocalDateTime beginTime, LocalDateTime endTime) {
        Map map = new HashMap();
        map.put("begin",beginTime);
        map.put("end", endTime);
        return userMapper.countByMap(map);
    }

    /**
     * 根据时间区间统计指定状态的订单数量
     * @param beginTime
     * @param endTime
     * @param status
     * @return
     */
    private Integer getOrderCount(LocalDateTime beginTime, LocalDateTime endTime, Integer status) {
        Map map = new HashMap();
        map.put("status", status);
        map.put("begin",beginTime);
        map.put("end", endTime);
        return orderMapper.countByMap(map);
    }
}
