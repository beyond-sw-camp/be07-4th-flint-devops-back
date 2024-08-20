package com.hotel.flint.reserve.room.service;

import com.hotel.flint.reserve.room.domain.Season;
import com.hotel.flint.reserve.room.repository.SeasonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class SeasonService {

    private final SeasonRepository seasonRepository;
    @Autowired
    public SeasonService (SeasonRepository seasonRepository) {
        this.seasonRepository = seasonRepository;
    }

    public boolean isSeason(LocalDate date) {
        List<Season> peakSeasons = seasonRepository.findAll();

        for (Season season: peakSeasons) {
            // 성수기 내의 날짜면
            log.info("Checking date: " + date);
            if (!date.isBefore(season.getStartDate()) && !date.isAfter(season.getEndDate())) {
                return true;
            }
        }
        return false;
    }



}