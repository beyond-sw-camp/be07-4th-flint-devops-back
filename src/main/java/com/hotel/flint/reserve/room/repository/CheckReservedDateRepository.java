package com.hotel.flint.reserve.room.repository;

import com.hotel.flint.reserve.room.domain.ReservedRoom;
import com.hotel.flint.room.domain.RoomDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

/**
 * 예약된 객실을 담고있는 레퍼지토리
 */
@Repository
public interface CheckReservedDateRepository extends JpaRepository<ReservedRoom, Long> {

    Optional<ReservedRoom> findByDateAndRooms(LocalDate date, RoomDetails roomDetails);
    void deleteByDateAndRooms(LocalDate date, RoomDetails roomDetails);
}
