package com.hotel.flint.user.employee.controller;

import com.hotel.flint.common.dto.CommonErrorDto;
import com.hotel.flint.common.dto.CommonResDto;
import com.hotel.flint.user.employee.dto.*;
import com.hotel.flint.user.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Controller
@RequestMapping("/employee")
public class EmployeeMemberController {
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeMemberController(EmployeeService employeeService){
        this.employeeService = employeeService;
    }

//    직원이 멤버에 대한 정보를 조회하는 기능.
    @PostMapping("/userinfo")
    @ResponseBody
    public InfoUserResDto memberInfoCheck(@RequestParam String email){
        return employeeService.memberInfo(email);
    }

//    직원이 멤버 리스트를 조회하는 기능
    @GetMapping("/memberlist")
    @ResponseBody
    public ResponseEntity<?> memberList(){
        try{
            List<EmployeeToMemberListDto> dto = employeeService.memberList();
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "Employee Details Find" , dto);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        }catch (EntityNotFoundException e){
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_GATEWAY);
        }
    }

    /**
     * 관리자 권한으로 회원 detail 조회
     */
    @GetMapping("/member/detail/{id}")
    public ResponseEntity<?> EmployeeToMemberDetail(@PathVariable Long id) {
        try{
            EmployeeToMemberDetailDto dto = employeeService.employeeToMemberDetail(id);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "Employee Details Find" , dto);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        }catch (EntityNotFoundException e){
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_GATEWAY);
        }
    }

//    @GetMapping("/list_reserve")
//    @ResponseBody
//    public ResponseEntity<?> employeeMemberReserveList(@RequestParam String email){
//        System.out.println(email);
//        try {
//            InfoMemberReserveListResDto info = employeeService.employeeMemberReserveList(email);
//            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "Member Reserve List" , info);
//            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
//        }
//        catch(EntityNotFoundException e){
//            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
//            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
//        }catch (IllegalArgumentException e){
//            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
//            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
//        } catch (SecurityException e){
//            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
//            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_GATEWAY);
//        }
//    }
}