package com.hotel.flint.user.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InfoRoomResDto {
    private Long id;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}
