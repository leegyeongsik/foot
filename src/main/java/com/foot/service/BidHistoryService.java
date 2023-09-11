package com.foot.service;


import com.foot.dto.bidProduct.BidHistoryChartData;
import com.foot.repository.BidHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BidHistoryService {

    private final BidHistoryRepository bidHistoryRepository;
    public List<BidHistoryChartData> getChartDataForLastWeek() {
        LocalDateTime endDate = LocalDateTime.now();  // 현재 날짜
        LocalDateTime startDate = endDate.minusWeeks(1);  // 오늘로부터 일주일 전의 날짜 계산

        // Spring Data JPA 쿼리 메서드 호출
        return bidHistoryRepository.getChartDataByDate(startDate, endDate);
    }

}
