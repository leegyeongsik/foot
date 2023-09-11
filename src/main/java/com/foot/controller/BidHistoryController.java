package com.foot.controller;

import com.foot.dto.bidProduct.BidHistoryChartData;
import com.foot.service.BidHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
public class BidHistoryController {

    @Autowired
    private BidHistoryService bidHistoryService;

    @GetMapping("/view/chart")
    public String showBidHistoryChart(Model model) {

        // Service를 사용하여 데이터를 가져옴
        List<BidHistoryChartData> chartDataList = bidHistoryService.getChartDataForLastWeek();

        // 모델에 데이터 추가
        model.addAttribute("chartData", chartDataList);

        return "bidHistoryChart"; // 그래프를 표시할 HTML 페이지
    }
}
