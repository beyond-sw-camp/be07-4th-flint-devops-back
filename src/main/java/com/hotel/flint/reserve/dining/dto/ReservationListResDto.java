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
public class ReservationListResDto {

    private Long id;
    private Long memberId;
    private DiningName diningName;
    private int adult;
    private int child;
    private String comment;
    private LocalDateTime reservationDateTime;

}