package com.hotel.flint.room.repository;

import com.hotel.flint.common.enumdir.Option;
import com.hotel.flint.common.enumdir.RoomView;
import com.hotel.flint.common.enumdir.Season;
import com.hotel.flint.room.domain.RoomInfo;
import com.hotel.flint.room.domain.RoomPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomPriceRepository extends JpaRepository<RoomPrice, Long> {
    RoomPrice findByIsHolidayAndSeasonAndRoomViewAndRoomInfo(Option isHoliday, Season season, RoomView roomView, RoomInfo roomInfo);
}
