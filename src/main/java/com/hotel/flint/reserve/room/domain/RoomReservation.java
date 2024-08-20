package com.hotel.flint.reserve.room.domain;


import com.hotel.flint.common.enumdir.Option;
import com.hotel.flint.room.domain.RoomDetails;
import com.hotel.flint.user.employee.dto.EmployeeModRoomDto;
import com.hotel.flint.user.employee.dto.InfoRoomDetResDto;
import com.hotel.flint.user.employee.dto.InfoRoomResDto;
import com.hotel.flint.user.employee.dto.InfoUserResDto;
import com.hotel.flint.reserve.room.dto.RoomReservedDetailDto;
import com.hotel.flint.reserve.room.dto.RoomReservedListDto;
import com.hotel.flint.user.member.domain.Member;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomReservation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ColumnDefault("0")
    private int adultCnt;

    @ColumnDefault("0")
    private int childCnt;

    @Column(nullable = false)
    private LocalDate checkInDate;

    @Column(nullable = false)
    private LocalDate checkOutDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Option parkingYN;

    @Column(nullable = false)
    private String payment;

    @Column(nullable = false)
    private int adultBfCnt; // 성인 조식 신청인원

    @Column(nullable = false)
    private int childBfCnt; // 아이 조식 신청인원

    private String requestContents;


    // member 테이블과 관계설정
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // RoomDetails 테이블과 관계설정
    @OneToOne
    @JoinColumn(name = "room_id", nullable = false)
    private RoomDetails rooms;

//    고객 객실 예약 정보를 담은 데이터 반환 
    public InfoRoomResDto toInfoRoomResDto() {
        return InfoRoomResDto.builder()
                .id(this.id)
                .checkInDate(this.checkInDate)
                .checkOutDate(this.checkOutDate)
                .build();
    }
    public RoomReservedListDto listFromEntity(int no) {
        RoomReservedListDto roomReservedListDto = RoomReservedListDto.builder()
                .no(no)
                .id(this.id)
                .roomType(this.getRooms().getRoomInfo().getRoomTypeName())
                .checkInDate(this.checkInDate)
                .checkOutDate(this.checkOutDate)
                .build();
        return roomReservedListDto;
    }

    public RoomReservedDetailDto detailFromEntity() {
        RoomReservedDetailDto roomReservation = RoomReservedDetailDto.builder()
                .id(this.id)
                .adultCnt(this.adultCnt)
                .childCnt(this.childCnt)
                .roomType(this.getRooms().getRoomInfo().getRoomTypeName())
                .checkInDate(this.checkInDate)
                .checkOutDate(this.checkOutDate)
                .parkingYN(this.parkingYN)
                .payment("kakaopay")
                .adultBfCnt(this.adultBfCnt)
                .childBfCnt(this.childBfCnt)
                .requestContents(this.requestContents)
                .build();

        return roomReservation;
    }

    /**
     * 요청 시, 객실 예약 내역 수정 (직원용)
     */
    public RoomReservation updateFromEntity(EmployeeModRoomDto dto) {
        this.adultCnt = dto.getAdultCnt();
        this.adultBfCnt = dto.getAdultBfCnt();
        this.childCnt = dto.getChildCnt();
        this.childBfCnt = dto.getChildBfCnt();
        this.parkingYN = dto.getParkingYN();
        this.requestContents = dto.getRequestContents();
        return this;
    }

    public InfoRoomDetResDto toInfoRoomDetResEntity(){
        return InfoRoomDetResDto.builder()
                .id(this.id)
                .roomType(rooms.getRoomInfo().getRoomTypeName())
                .checkin(this.checkInDate)
                .checkout(this.checkOutDate)
                .adultCnt(this.adultCnt)
                .childCnt(this.childCnt)
                .adultBfCnt(this.adultBfCnt)
                .childBfCnt(this.childBfCnt)
                .parkingYN(this.parkingYN)
                .requestContents(this.requestContents)
                .build();
    }

}