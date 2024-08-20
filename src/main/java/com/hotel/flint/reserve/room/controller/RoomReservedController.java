package com.hotel.flint.reserve.room.controller;

import com.hotel.flint.common.dto.CommonErrorDto;
import com.hotel.flint.common.dto.CommonResDto;
import com.hotel.flint.common.enumdir.Option;
import com.hotel.flint.reserve.room.domain.RoomReservation;
import com.hotel.flint.reserve.room.dto.PossibleRoomDto;
import com.hotel.flint.reserve.room.dto.RoomReservedDetailDto;
import com.hotel.flint.reserve.room.dto.RoomReservedDto;
import com.hotel.flint.reserve.room.dto.RoomReservedListDto;
import com.hotel.flint.reserve.room.service.RoomReservedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/reserve")
@Slf4j
public class RoomReservedController {

    private final RoomReservedService roomReservedService;
    @Autowired
    public RoomReservedController (RoomReservedService roomReservedService) {
        this.roomReservedService = roomReservedService;
    }

    /**
     * 객실 예약 진행
     */
    @PostMapping("/room")
    public ResponseEntity<?> roomReservation(@RequestBody RoomReservedDto dto) {

        try {
            double totalPrice = roomReservedService.roomReservation(dto);

            CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "예약 금액은 " + totalPrice + "원 입니다.", null);
            return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {

            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException e) {

            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 객실 예약 취소
     */
    @GetMapping("/room/cancel/{id}")
    public ResponseEntity<?> reservationRoomCanceled(@PathVariable Long id) { // id : room reservation의 id

        try {
            roomReservedService.delete(id);

            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "delete reservation", null);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * 객실 예약 내역 조회 - 목록
     */
    @GetMapping("/room/list")
    public Page<RoomReservedListDto> reservationRoomListCheck(@PageableDefault(size=10, sort = "checkInDate"
            , direction = Sort.Direction.ASC) Pageable pageable) {

        return roomReservedService.roomReservedList(pageable);
    }

    /**
     * 객실 예약 내역 조회 - 단 건 상세내역
     */
    @GetMapping("/room/detail/{id}")
    public ResponseEntity<?> reservationRoomDetailCheck(@PathVariable Long id) {

        try {
            RoomReservedDetailDto roomReservedDetailDto = roomReservedService.roomReservedDetail(id);

            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "조회 성공", roomReservedDetailDto);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * 원하는 날짜에 남은 객실이 있는지 목록 조회
     */
    @GetMapping("/room/remain")
    public List<PossibleRoomDto> checkRemainRoom(@RequestParam("checkInDate") String checkInStr,
                                                 @RequestParam("checkOutDate") String checkOutStr,
                                                 @RequestParam("adultCnt") int adultCnt,
                                                 @RequestParam("childCnt") int childCnt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate checkInDate = LocalDate.parse(checkInStr, formatter);
        LocalDate checkOutDate = LocalDate.parse(checkOutStr, formatter);

        return roomReservedService.checkRemainRoom(checkInDate, checkOutDate, adultCnt, childCnt);
    }

    /**
     * 객실 가격 조회 - 화면 하단 안내용
     */
    @GetMapping("/room/getPrice")
    public long getPrice(@RequestParam("checkInDate") String checkInStr,
                         @RequestParam("checkOutDate") String checkOutStr,
                         @RequestParam int adultCnt,
                         @RequestParam int childCnt,
                         @RequestParam long roomId) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate checkInDate = LocalDate.parse(checkInStr, formatter);
        LocalDate checkOutDate = LocalDate.parse(checkOutStr, formatter);

        RoomReservedDto dto = new RoomReservedDto();
        dto.setCheckInDate(checkInDate);
        dto.setCheckOutDate(checkOutDate);
        dto.setAdultCnt(adultCnt);
        dto.setChildCnt(childCnt);
        dto.setRoomId(roomId);

        return roomReservedService.getPrice(dto);
    }

}