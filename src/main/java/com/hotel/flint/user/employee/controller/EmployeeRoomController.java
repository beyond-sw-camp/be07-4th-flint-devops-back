package com.hotel.flint.user.employee.controller;

import com.hotel.flint.common.dto.CommonErrorDto;
import com.hotel.flint.common.dto.CommonResDto;
import com.hotel.flint.reserve.room.repository.RoomReservationRepository;
import com.hotel.flint.reserve.room.service.RoomReservedService;
import com.hotel.flint.user.employee.dto.EmployeeModRoomDto;
import com.hotel.flint.user.employee.dto.InfoDiningResDto;
import com.hotel.flint.user.employee.dto.InfoRoomDetResDto;
import com.hotel.flint.user.employee.dto.InfoRoomResDto;
import com.hotel.flint.user.employee.service.EmployeeRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employee/room")
public class EmployeeRoomController {
    private final EmployeeRoomService employeeRoomService;
    private final RoomReservationRepository roomReservationRepository;
    private final RoomReservedService roomReservedService;

    @Autowired
    public EmployeeRoomController(EmployeeRoomService employeeRoomService, RoomReservationRepository roomReservationRepository, RoomReservedService roomReservedService) {
        this.employeeRoomService = employeeRoomService;
        this.roomReservationRepository = roomReservationRepository;
        this.roomReservedService = roomReservedService;
    }

    @PatchMapping("/modprice/{room_type_id}")
    public ResponseEntity<?> modRoomPrice(@PathVariable Long room_type_id,
                                          @RequestBody Map<String, Long> request) {
        try {
            employeeRoomService.modRoomPrice(room_type_id, request.get("newPrice"));
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "가격 수정 완료", null);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e){
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.FORBIDDEN.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.FORBIDDEN);
        }
    }
    @GetMapping("/roominfo")
    public ResponseEntity<?> getRoomInfo(){
        try {
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "조회 완료", employeeRoomService.roomInfoList());
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (IllegalArgumentException e){
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.FORBIDDEN.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.FORBIDDEN);
        }
    }
    @GetMapping("/reserve")
    public ResponseEntity<?> memberReservatioRoomCheck(@RequestParam String email, Pageable pageable) {
        try {
            List<InfoRoomResDto> infoRoomResDtos = employeeRoomService.memberReservationRoomCheck(email, pageable);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "고객 예약 리스트 ", infoRoomResDtos);
            System.out.println(infoRoomResDtos.size());
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e){
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.FORBIDDEN.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.FORBIDDEN);
        }
    }

    /**
     * 직원의 권한으로 회원의 객실 예약 조회 - 단건 (detail)
     */
    @GetMapping("/reserve/{id}")
    public ResponseEntity<?> reserveRoomDetail(@PathVariable Long id) {
        try {
            InfoRoomDetResDto dto = roomReservedService.roomDetail(id);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e){
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.FORBIDDEN.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.FORBIDDEN);
        }
    }

    /**
     * 직원의 권한으로 회원의 객실 예약 취소
     */
    @PostMapping("/cancel_reserve_room/{id}")
    public ResponseEntity<?> memberReservationCncRoomByEmployee(@PathVariable Long id) {
        try {
            InfoRoomDetResDto dto = roomReservedService.roomDetail(id);
            employeeRoomService.memberReservationCncRoomByEmployee(dto);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "삭제 완료", null);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e){
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.FORBIDDEN.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.FORBIDDEN);
        }
    }

    /**
     * 고객의 요청시, 객실 예약내역 수정
     */
    @PostMapping("/modify/{id}") // reserve_id
    public ResponseEntity<?> memberReservationModRoomByEmployee(@PathVariable Long id, @RequestBody EmployeeModRoomDto dto) {

        try {
            employeeRoomService.updateRoomReservation(id, dto);

            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "객실 예약 내역 수정 완료", null);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

}
