package com.hotel.flint.reserve.room.dto;

import com.hotel.flint.common.enumdir.Option;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomReservedDetailDto {

    private Long id;
    private int adultCnt;
    private int childCnt;
    private String roomType; // 디럭스, 스위트 등
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Option parkingYN;

    private String payment;
    private int adultBfCnt; // 성인 조식 신청인원
    private int childBfCnt; // 아이 조식 신청인원
    private String requestContents;

}
