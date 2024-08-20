package com.hotel.flint.reserve.room.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PossibleRoomDto {

    private Long roomId;
    private String roomTypeName;
    private long roomPrice;
//    s3 이미지 링크 추가
    private String imagePath;
}
