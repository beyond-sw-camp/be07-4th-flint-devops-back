package com.hotel.flint.user.employee.controller;

import com.hotel.flint.common.auth.JwtAuthFilter;
import com.hotel.flint.common.auth.JwtTokenProvider;
import com.hotel.flint.common.dto.CommonErrorDto;
import com.hotel.flint.common.dto.CommonResDto;
import com.hotel.flint.common.dto.FindPasswordRequest;
import com.hotel.flint.common.dto.UserLoginDto;
import com.hotel.flint.common.enumdir.Department;
import com.hotel.flint.common.service.MailService;
import com.hotel.flint.user.employee.domain.Employee;
import com.hotel.flint.user.employee.dto.*;
import com.hotel.flint.user.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthFilter jwtAuthFilter;

    @Autowired
    MailService mailService;

    @Autowired
    public EmployeeController(EmployeeService employeeService, JwtTokenProvider jwtTokenProvider, JwtAuthFilter jwtAuthFilter) {
        this.employeeService = employeeService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @PostMapping("/create")
//  Office 부서만 가능. 직원 생성하는 로직
    public ResponseEntity<?> makeEmployee(@RequestBody EmployeeMakeDto dto) {
        try {
            Employee employee = employeeService.makeEmployee(dto);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "계정 생성이 성공적으로 완료되었습니다", null);
            return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
//    직원 로그인
    public ResponseEntity<?> doLogin(@RequestBody UserLoginDto dto) {
        try {
            Employee employee = employeeService.login(dto);
//            로그인 성공시 employeetoken 발급. payload에 department, id 담겨있음
            String jwtToken = jwtTokenProvider.createEmployeeToken(employee.getEmail(), employee.getId(), employee.getDepartment().toString());
            Map<String, Object> loginInfo = new HashMap<>();
            loginInfo.put("employeetoken", jwtToken);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "환영합니다 " + employee.getFirstName() + employee.getLastName() + "님!", loginInfo);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/findemail")
//    직원 이메일 찾기. 회원과 동일한 로직임
    public ResponseEntity<?> findEmail(@RequestBody Map<String, String> request) {
        try {
            String Email = employeeService.findEmailToPhoneNum(request.get("phoneNumber"));
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "조회에 성공하였습니다.",
                    "회원님의 이메일은 " + Email + "입니다");
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/findpassword")
//    직원 비밀번호 찾기. 회원과 같은 로직
    public ResponseEntity<?> findPassword(@RequestBody FindPasswordRequest request) {
        try {
            mailService.sendTempPassword(request.getEmail());
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "임시 비밀번호를 이메일로 발송했습니다.", null);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/delaccount")
//    Office만 사용 가능. Id를 통해 DelYN Y로 변경시킴.
    public ResponseEntity<?> delEmployee(@RequestBody Map<String, Long> request) {
        try {
            employeeService.delAccount(request.get("employeeId"));
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "퇴사 처리 완료", null);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

    //    직원 자신의 상세 정보
    @GetMapping("/detail")
    @ResponseBody
    public ResponseEntity<?> empDetail() {
        try {
            EmployeeDetResDto dto = employeeService.employeeDetail();
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "Employee Details Find", dto);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_GATEWAY);
        }
    }

    //    Office 직원이 해당 직원 id값을 통해 정보 조회.
    @GetMapping("/detail/{id}")
    @ResponseBody
    public ResponseEntity<?> empDetail(@PathVariable("id") Long id) {
        try {
            EmployeeDetResDto dto = employeeService.employeeDetail(id);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "Employee Details Find", dto);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_GATEWAY);
        }
    }

    //    직원 정보 수정 : 비밀번호 수정만 있음.
//    해당 직원 id, 수정할 값이 들어있음.
    @PutMapping("/modify")
    @ResponseBody
    public ResponseEntity<?> memberModify(@RequestBody EmployeeModResDto dto) {
        try {
            employeeService.employeeModify(dto);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "Employee Password Modify", null);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

    //    직원의 직급을 수정
//    직원 수정은 office 부서만 가능
//    dto 안에 수정하는 직원 id, 수정 대상 id, 수정할 값 이 들어있다.
    @PutMapping("/mod_rank")
    @ResponseBody
    public ResponseEntity<?> modEmployeeRank(@RequestBody EmployeeRankModResDto dto) {
        try {
            employeeService.modEmployeeRank(dto);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "Employee Rank Modify", null);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

        @GetMapping("/list")
        public ResponseEntity<?> employeeList (
                @RequestParam(value = "searchType", required = false) String searchType,
                @RequestParam(value = "searchValue", required = false) String searchValue){
            EmployeeSearchDto dto = new EmployeeSearchDto();

            if ("email".equals(searchType)) {
                dto.setEmail(searchType);
            } else if ("employeeNumber".equals(searchType) && searchValue != null) {
                dto.setEmployeeNumber(searchValue);
            } else if ("department".equals(searchType) && searchValue != null) {
                dto.setDepartment(Department.valueOf(searchValue));
            }
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "조회 성공", employeeService.getEmployeeList(dto));
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
            }
}
