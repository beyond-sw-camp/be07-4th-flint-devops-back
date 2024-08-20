package com.hotel.flint.user.employee.dto;

import com.hotel.flint.reserve.dining.domain.DiningReservation;
import com.hotel.flint.reserve.room.domain.RoomReservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InfoMemberReserveListResDto {
    List<InfoMemberReserveListResDto.DiningReserveId> diningReservations;
    List<InfoMemberReserveListResDto.RoomReserveId> roomReservations;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RoomReserveId{
        private Long roomReservationId;
        private LocalDate reservationCheckin;
        private LocalDate reservationCheckout;
    }

    public static RoomReserveId fromRoomEntity(RoomReservation roomReservation) {
        return RoomReserveId.builder()
                .roomReservationId(roomReservation.getId())
                .reservationCheckin(roomReservation.getCheckInDate())
                .reservationCheckout(roomReservation.getCheckOutDate())
                .build();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DiningReserveId{
        private Long diningReservationId;
        private LocalDateTime reservationDateTime;
    }

    public static DiningReserveId fromDiningEntity(DiningReservation diningReservation){
        return DiningReserveId.builder()
                .diningReservationId(diningReservation.getId())
                .reservationDateTime(diningReservation.getReservationDateTime())
                .build();
    }
}
