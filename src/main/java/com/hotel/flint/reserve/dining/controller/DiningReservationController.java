package com.hotel.flint.reserve.dining.controller;

import com.hotel.flint.common.dto.CommonErrorDto;
import com.hotel.flint.common.dto.CommonResDto;
import com.hotel.flint.reserve.dining.domain.DiningReservation;
import com.hotel.flint.reserve.dining.dto.*;
import com.hotel.flint.reserve.dining.service.DiningReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/reserve")
public class DiningReservationController {

    private final DiningReservationService diningReservationService;

    @Autowired
    public DiningReservationController(DiningReservationService diningReservationService){
        this.diningReservationService = diningReservationService;
    }

    // 다이닝 예약
    @PostMapping("/dining/create")
    public ResponseEntity<?> diningReservation(@RequestBody ReservationSaveReqDto dto){
        System.out.println(dto);
        try {
            DiningReservation diningReservation = diningReservationService.create(dto);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "예약 성공", diningReservation.getId());
            return new ResponseEntity<>( commonResDto, HttpStatus.CREATED );
        }catch (IllegalArgumentException | EntityNotFoundException e) {
        CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

    // 예약 전체 조회
    @GetMapping("/dining/list")
    public ResponseEntity<?> resevationDiningListCheck(Pageable pageable){
        try {
            Page<ReservationListResDto> reservationListResDtos = diningReservationService.list(pageable);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "예약 성공", reservationListResDtos);
            return new ResponseEntity<>( commonResDto, HttpStatus.CREATED );
        }catch (IllegalArgumentException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

    // 예약 단건 조회
    @GetMapping("/dining/detail/{diningReservationId}")
    public ResponseEntity<?> reservationDiningDetailCheck(@PathVariable Long diningReservationId){
        try {
            ReservationDetailDto reservationDetailDto = diningReservationService.detailList(diningReservationId);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK,  reservationDetailDto.getId() + "번 예약 조회", reservationDetailDto);
            return new ResponseEntity<>( commonResDto, HttpStatus.OK );
        }catch (IllegalArgumentException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.UNAUTHORIZED);
        }catch (EntityNotFoundException e){
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

    // 회원별 예약 전체 목록 조회
    @GetMapping("/dining/userList")
    public ResponseEntity<?> reservationDiningUserListCheck(){
        try {
            List<ReservationListResDto> reservationListResDtos = diningReservationService.userList();
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK,  reservationListResDtos.get(0).getMemberId() + "님 예약 조회", reservationListResDtos);
            return new ResponseEntity<>( commonResDto, HttpStatus.OK );
        }catch (IllegalArgumentException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }
    // 예약 삭제/취소
    @GetMapping("/dining/delete/{id}")
    public ResponseEntity<?> reservationDiningCanceled(@PathVariable Long id){
        try {
            diningReservationService.delete(id);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK,  "예약 취소 완료", id);
            return new ResponseEntity<>( commonResDto, HttpStatus.OK );
        }catch (IllegalArgumentException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }
}