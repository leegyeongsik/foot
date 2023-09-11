package com.foot.service;


import com.foot.dto.bidProduct.BidHistoryChartData;
import com.foot.dto.bidProduct.BidProductChartData;
import com.foot.repository.BidHistoryRepository;
import com.foot.repository.BidProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BidHistoryService {

    private final BidHistoryRepository bidHistoryRepository;
    private final BidProductRepository bidProductRepository;

    // 경매 히스토리 그래프
    public List<BidHistoryChartData> getChartDataForLastWeek() {
        LocalDateTime endDate = LocalDateTime.now();  // 현재 날짜
        LocalDateTime startDate = endDate.minusWeeks(1);  // 오늘로부터 일주일 전의 날짜 계산

        // Spring Data JPA 쿼리 메서드 호출
        List<BidHistoryChartData> chartDataList = bidHistoryRepository.getChartDataByDate(startDate, endDate);

        // 일별로 그룹화된 데이터를 생성
        List<BidHistoryChartData> dailyChartDataList = groupChartDataByDay(chartDataList);

        return dailyChartDataList;
    }


    // 경매 상품 등록 그래프
    public List<BidProductChartData> getChartData() {
        LocalDateTime endDate = LocalDateTime.now();  // 현재 날짜
        LocalDateTime startDate = endDate.minusWeeks(1);  // 오늘로부터 일주일 전의 날짜 계산

        // Spring Data JPA 쿼리 메서드 호출
        List<BidProductChartData> chartDataList = bidProductRepository.getChartDataByDateRange(startDate, endDate);

        List<BidProductChartData> dailyChartDataList = groupChartData(chartDataList);
        return dailyChartDataList;
    }


    private List<BidProductChartData> groupChartData(List<BidProductChartData> chartDataList) {
        Map<LocalDate, Long> dailyChartDataMap = new LinkedHashMap<>();

        for (BidProductChartData data : chartDataList) {
            LocalDateTime createdAt = data.getDate();
            LocalDate date = createdAt.toLocalDate();
            long count = data.getCount();

            // 이미 해당 날짜에 데이터가 있는 경우 더하고, 없으면 새로 추가
            dailyChartDataMap.merge(date, count, Long::sum);
        }

        // Map을 다시 BidHistoryChartData 리스트로 변환
        return dailyChartDataMap.entrySet().stream()
                .map(entry -> new BidProductChartData(entry.getKey().atStartOfDay(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private List<BidHistoryChartData> groupChartDataByDay(List<BidHistoryChartData> chartDataList) {
        Map<LocalDate, Long> dailyChartDataMap = new LinkedHashMap<>();

        for (BidHistoryChartData data : chartDataList) {
            LocalDateTime createdAt = data.getDate();
            LocalDate date = createdAt.toLocalDate();
            long count = data.getCount();

            // 이미 해당 날짜에 데이터가 있는 경우 더하고, 없으면 새로 추가
            dailyChartDataMap.merge(date, count, Long::sum);
        }

        // Map을 다시 BidHistoryChartData 리스트로 변환
        return dailyChartDataMap.entrySet().stream()
                .map(entry -> new BidHistoryChartData(entry.getKey().atStartOfDay(), entry.getValue()))
                .collect(Collectors.toList());
    }




}
