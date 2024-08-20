package com.hotel.flint.reserve.room.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomReservedListDto {
    private Long id;
    private int no; // 화면에 뿌려지는 순서를 나타내는 컬럼
    private String roomType; // 디력스, 스위트 등등
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}
