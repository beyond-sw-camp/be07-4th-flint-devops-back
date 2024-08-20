package com.hotel.flint.reserve.dining.dto;

import com.hotel.flint.common.domain.BaseTimeEntity;
import com.hotel.flint.common.enumdir.DiningName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationDetailDto{

    private Long id;
    private Long memberId;
    private DiningName diningName;
    private int adult;
    private int child;
    private String comment;
    private LocalDateTime reservationDateTime;


    private LocalDateTime createdTime; // 최초 예약을 한 날짜 시간
    private LocalDateTime updatedTime; // 마지막으로 예약 수정 한 날짜 시간

}
