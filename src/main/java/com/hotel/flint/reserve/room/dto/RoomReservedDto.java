package com.hotel.flint.reserve.room.dto;

import com.hotel.flint.common.enumdir.Option;
import com.hotel.flint.reserve.room.domain.ReservedRoom;
import com.hotel.flint.room.domain.RoomDetails;
import com.hotel.flint.reserve.room.domain.RoomReservation;
import com.hotel.flint.user.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomReservedDto {

    private int adultCnt; // 투숙하는 성인 수
    private int childCnt; // 투숙하는 아이 수
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    private int adultBfCnt; // 성인 조식 신청 인원
    private int childBfCnt; // 아이 조식 신청 인원
    private Option parkingYN; // 주차 여부
    private String requestContents; // 요청사항

    private Long roomId; // 일단 사용자가 룸을 직접 지정한다고 가정 후 추후에 수정


    public RoomReservation toEntity(Member member, RoomDetails roomDetails) {
        RoomReservation roomReservation = RoomReservation.builder()
                .adultCnt(this.adultCnt)
                .childCnt(this.childCnt)
                .checkInDate(this.checkInDate)
                .checkOutDate(this.checkOutDate)
                .adultBfCnt(this.adultBfCnt)
                .childBfCnt(this.childBfCnt)
                .parkingYN(this.parkingYN)
                .requestContents(this.requestContents)
                .payment("kakaopay")
                .rooms(roomDetails)
                .member(member)
                .build();
        return roomReservation;
    }

    public ReservedRoom toEntity(LocalDate date, RoomDetails roomDetails) {
        ReservedRoom reservedRoom = ReservedRoom.builder()
                .date(date)
                .rooms(roomDetails)
                .build();

        return reservedRoom;
    }
}