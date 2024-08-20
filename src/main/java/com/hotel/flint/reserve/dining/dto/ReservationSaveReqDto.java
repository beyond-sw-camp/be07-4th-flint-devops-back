package com.hotel.flint.reserve.dining.dto;

import com.hotel.flint.dining.domain.Dining;
import com.hotel.flint.reserve.dining.domain.DiningReservation;
import com.hotel.flint.user.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationSaveReqDto {

    private Long diningId;
    private int adult;
    private int child;
    private String comment;

    private LocalDateTime reservationDateTime;

    public DiningReservation toEntity(Member member, Dining dining){
        return DiningReservation.builder()
                .memberId(member)
                .diningId(dining)
                .adult(this.adult)
                .child(this.child)
                .comment(this.comment)
                .reservationDateTime(this.reservationDateTime)
                .build();
    }

}