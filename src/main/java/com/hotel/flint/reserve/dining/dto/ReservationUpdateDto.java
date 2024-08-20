package com.hotel.flint.reserve.dining.dto;

import com.hotel.flint.common.enumdir.DiningName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationUpdateDto {

    private Long memberId; // 회원 ID
    private DiningName diningName; // 한식, 중식, 일식, 라운지
    private int adult;
    private int child;
    private String comment;
    private LocalDateTime reservationDateTime;

}