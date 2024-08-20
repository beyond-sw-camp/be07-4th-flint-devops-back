package com.hotel.flint.reserve.room.service;

import com.hotel.flint.reserve.room.repository.HolidayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class HolidayService {

    private final HolidayRepository holidayRepository;
    @Autowired
    public HolidayService (HolidayRepository holidayRepository) {
        this.holidayRepository = holidayRepository;
    }

    /**
     * 공휴일 및 주말(금,토)인지 확인해서 true/false로 반환
     */
    public boolean isHoliday(LocalDate date) {
        return holidayRepository.findByHolidayDate(date).isPresent();
    }
}