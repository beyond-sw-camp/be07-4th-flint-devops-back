package com.hotel.flint.user.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InfoDiningResDto {
    private Long diningReservationId;
    private LocalDateTime reservationDate;
    private LocalDateTime reservationTime;
}
