package com.hotel.flint.room.repository;

import com.hotel.flint.room.domain.RoomDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomDetailsRepository extends JpaRepository<RoomDetails, Long> {
}
