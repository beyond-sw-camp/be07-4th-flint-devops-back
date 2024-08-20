package com.hotel.flint.room.domain;

import com.hotel.flint.room.dto.RoomInfoResDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoomInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomTypeName;
    private long roomTypePrice;

//    s3 이미지 링크 추가
    private String imagePath;

    @OneToMany(mappedBy = "roomInfo", cascade = CascadeType.ALL)
    private List<RoomPrice> roomPrices;

    @OneToMany(mappedBy = "roomInfo", cascade = CascadeType.ALL)
    private List<RoomDetails> roomDetails;

    public void updateRoomPrice(long newPrice){
        this.roomTypePrice = newPrice;
    }
    public RoomInfoResDto fromEntity(){
        return RoomInfoResDto.builder()
                .id(this.id)
                .roomTypeName(this.roomTypeName)
                .roomTypePrice(this.roomTypePrice)
                .build();
    }
}
