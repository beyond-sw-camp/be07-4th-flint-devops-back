package com.hotel.flint.reserve.dining.dto;

import com.hotel.flint.common.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationDeleteDto extends BaseTimeEntity {

    private Long memberId;
    private Long reservationId;

}
