package com.hotel.flint.room.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomInfoResDto {
    private Long id;
    private String roomTypeName;
    private Long roomTypePrice;
}
