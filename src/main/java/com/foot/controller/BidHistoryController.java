package com.foot.controller;

import com.foot.dto.bidProduct.BidHistoryChartData;
import com.foot.dto.bidProduct.BidProductChartData;
import com.foot.repository.BidHistoryRepository;
import com.foot.repository.BidProductRepository;
import com.foot.service.BidHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BidHistoryController {

    private final BidHistoryService bidHistoryService;

    private final BidProductRepository bidProductRepository;

    @GetMapping("/view/chart")
    public String showBidHistoryChart(Model model) {

        // Service를 사용하여 데이터를 가져옴
        List<BidHistoryChartData> chartDataList = bidHistoryService.getChartDataForLastWeek();

        // 모델에 데이터 추가
        model.addAttribute("chartData", chartDataList);

        return "bidHistoryChart"; // 그래프를 표시할 HTML 페이지
    }

    @GetMapping("/view/bpChart")
    public String bidProductChart(Model model) {
        List<BidProductChartData> chartData = bidHistoryService.getChartData();
        // 그래프 데이터를 모델에 추가
        model.addAttribute("chartData", chartData);

        return "bidProductChart";  // 그래프를 표시할 HTML 템플릿
    }
}
