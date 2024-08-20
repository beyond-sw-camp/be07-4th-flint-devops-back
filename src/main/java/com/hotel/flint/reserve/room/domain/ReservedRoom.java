package com.hotel.flint.reserve.room.domain;

import com.hotel.flint.room.domain.RoomDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * 예약된 객실 테이블
 */
@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservedRoom {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date; // 날짜

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private RoomDetails rooms; // room details 엔티티와 관계 설정
}
