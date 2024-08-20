package com.hotel.flint.reserve.room.repository;

import com.hotel.flint.reserve.room.domain.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SeasonRepository extends JpaRepository<Season, Long> {
}