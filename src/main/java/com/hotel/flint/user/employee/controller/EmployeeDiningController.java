package com.hotel.flint.user.employee.controller;

import com.hotel.flint.common.dto.CommonErrorDto;
import com.hotel.flint.common.dto.CommonResDto;
import com.hotel.flint.common.enumdir.Department;
import com.hotel.flint.dining.dto.MenuSaveDto;
import com.hotel.flint.reserve.dining.domain.DiningReservation;
import com.hotel.flint.reserve.dining.dto.ReservationDetailDto;
import com.hotel.flint.reserve.dining.dto.ReservationUpdateDto;
import com.hotel.flint.reserve.dining.service.DiningReservationService;
import com.hotel.flint.user.employee.dto.DiningMenuDto;
import com.hotel.flint.user.employee.dto.InfoDiningResDto;
import com.hotel.flint.user.employee.dto.MenuSearchDto;
import com.hotel.flint.user.employee.dto.memberDiningResDto;
import com.hotel.flint.user.employee.service.EmployeeDiningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employee/dining")
public class EmployeeDiningController {
    private final EmployeeDiningService employeeDiningService;
    private final DiningReservationService diningReservationService;

    @Autowired
    public EmployeeDiningController(EmployeeDiningService employeeDiningService, DiningReservationService diningReservationService){
        this.employeeDiningService = employeeDiningService;
        this.diningReservationService = diningReservationService;
    }

    @GetMapping("/list")
    public ResponseEntity<?> menuList(
            @RequestParam("department") Department department,
            @RequestParam(value = "searchType", required = false) String searchType,
            @RequestParam(value = "searchValue", required = false) String searchValue) {
//        본인 부서 메뉴 리스트 출력. 아래는 검색 기능 관련임.
        MenuSearchDto searchDto = new MenuSearchDto();

        if ("menuName".equals(searchType)) {
            searchDto.setMenuName(searchValue);
        } else if ("menuId".equals(searchType) && searchValue != null) {
            try {
                searchDto.setId(Long.parseLong(searchValue));
            } catch (NumberFormatException e) {
                return new ResponseEntity<>(new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), "Invalid menuId format"), HttpStatus.BAD_REQUEST);
            }
        }

        List<DiningMenuDto> dtos = employeeDiningService.getMenuList(department, searchDto);
        try {
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "조회 성공", dtos);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
//            부서 찾기 결과가 없으면 400(현재 서비스의 경우 절대 안터짐) -> 아래 전부 동일함
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
//            권한 없으면 403
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.FORBIDDEN.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.FORBIDDEN);
        }
    }


    @PostMapping("/addmenu")
    public ResponseEntity<?> addMenu(@RequestBody MenuSaveDto menuSaveDto) {
//        로그인 된 직원의 부서에 메뉴 추가
        try {
            CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "메뉴가 성공적으로 생성되었습니다", menuSaveDto.getMenuName());
            employeeDiningService.addDiningMenu(menuSaveDto);
            return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
        } catch (EntityNotFoundException e){
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        } catch (SecurityException | IllegalArgumentException e){
//            권한 없으면 403(어차피 로그인 시점에서 걸림)
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.FORBIDDEN.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.FORBIDDEN);
        }

    }

    @PatchMapping("/modmenu/{id}")
    public ResponseEntity<?> modDiningMenu(@PathVariable Long id,
                                           @RequestBody Map<String, Integer> request) {
//        id, cost 화면에서 받아와서 해당하는 메뉴 가격 수정
        int newCost = request.get("cost");
        try {
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "가격이 변경되었습니다", null);
            employeeDiningService.modDiningMenu(id, newCost);
            return new ResponseEntity<>(commonResDto ,HttpStatus.OK);
        } catch (EntityNotFoundException e){
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        } catch (SecurityException | IllegalArgumentException e){
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.FORBIDDEN.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/delmenu/{id}")
    public ResponseEntity<?> delDiningMenu(@PathVariable Long id){
        try {
//            본인 부서 메뉴 삭제
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "메뉴가 삭제되었습니다.", null);
            employeeDiningService.delDiningMenu(id);
            return new ResponseEntity<>(commonResDto ,HttpStatus.OK);
        } catch (EntityNotFoundException e){
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        } catch (SecurityException | IllegalArgumentException e){
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.FORBIDDEN.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.FORBIDDEN);
        }
    }

//    직원의 고객 다이닝 예약 내역 리스트
    @GetMapping("/reserve")
    public ResponseEntity<?> memberReservationDiningCheck(@RequestParam String email, Pageable pageable) {
        try {
            List<InfoDiningResDto> infoDiningResDto = employeeDiningService.memberReservationDiningCheck(email, pageable);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "고객 예약 리스트 ", infoDiningResDto);
            System.out.println(infoDiningResDto.size());
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e){
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.FORBIDDEN.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.FORBIDDEN);
        }
    }

//    직원이 고객의 다이닝 예약 내역을 취소.
    @GetMapping("/cancel_reserve_dining/{id}")
    public ResponseEntity<?> memberReservationCncDiningByEmployee(@PathVariable Long id){
        try{
            ReservationDetailDto dto = employeeDiningService.memberReservationCncDiningByEmployee(id);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }catch (EntityNotFoundException e){
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_GATEWAY);
        }
    }

    // 다이닝 예약 정보 ( 멤버랑 해당 다이닝 )
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> reserveDiningDetail(@PathVariable Long id){
        System.out.println(id);
        try{
            memberDiningResDto dto = diningReservationService.diningDetail(id);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK,  "예약 상세정보 출력", dto);
            return new ResponseEntity<>( commonResDto, HttpStatus.OK );
        }catch (IllegalArgumentException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> reserveDiningUpdate(@PathVariable Long id, @RequestBody ReservationUpdateDto dto) {
        try {

            DiningReservation diningReservation = diningReservationService.updateDiningFields(id, dto.getAdult(), dto.getChild(), dto.getComment(), dto.getReservationDateTime());
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "예약 수정 완료", diningReservation.getId());
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.NOT_FOUND.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }
}
