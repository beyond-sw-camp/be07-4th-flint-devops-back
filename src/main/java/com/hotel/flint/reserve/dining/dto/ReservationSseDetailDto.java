package com.hotel.flint.reserve.dining.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationSseDetailDto {
    private Long id;
    private Long memberId;
    private int adult;
    private int child;
    private String comment;
    private String ReserveDate;
    private String ReserveTime;
}
