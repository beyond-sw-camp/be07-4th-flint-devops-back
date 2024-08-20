package com.hotel.flint.support.qna.controller;

import com.hotel.flint.common.dto.CommonErrorDto;
import com.hotel.flint.common.dto.CommonResDto;
import com.hotel.flint.support.qna.domain.QnA;
import com.hotel.flint.support.qna.dto.*;
import com.hotel.flint.support.qna.service.EmployeeQnaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
public class EmployeeQnaController {

    private final EmployeeQnaService qnaService;

    @Autowired
    public EmployeeQnaController(EmployeeQnaService qnaService) {
        this.qnaService = qnaService;
    }

    // QnA 답변 작성
    @PostMapping("/qna/answer/create/{qna_id}")
    public ResponseEntity<?> createAnswer ( @PathVariable Long qna_id , @RequestBody CreateAnswerDto dto) {

        try {
            QnA qna = qnaService.createAnswer(qna_id, dto);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "답변 등록 성공", qna.getId() );
            return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {

            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);

        }
    }

    // QnA 답변 수정
    @PostMapping("/qna/answer/update/{qna_id}")
    public ResponseEntity<?> updateAnswer ( @PathVariable Long qna_id , @RequestBody UpdateAnswerDto dto) {

        try {

            QnA qna = qnaService.updateAnswer(qna_id, dto);

            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "답변 수정 성공", qna.getId() );
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);

        } catch (IllegalArgumentException e) {

            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);

        }
    }


    // QnA 답변 삭제
    @PostMapping("/qna/answer/delete/{qna_id}")
    public ResponseEntity<?> deleteAnswer ( @PathVariable Long qna_id ) {

        try {

            QnA qna = qnaService.deleteAnswer(qna_id);

            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "답변 삭제 성공", qna.getId() );
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);

        } catch (IllegalArgumentException e) {

            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);

        }
    }

    // QnA 리스트 목록 전체 조회
    @GetMapping("/qna/list")
    public ResponseEntity<?> EmployeeQnaListPage (Pageable pageable) {

        try {

            Page<EmployeeQnaListDto> employeeQnaListPage = qnaService.employeeQnaListPage( pageable );

            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "전체 조회 성공",  employeeQnaListPage );
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);

        } catch (IllegalArgumentException e) {

            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);

        }
    }


    // QnA 및 답변 상세 조회
    @GetMapping("/qna/detail/{qna_id}")
    public ResponseEntity<?> EmployeeDetailQnA (@PathVariable Long qna_id) {

        try {

            EmployeeQnaDetailDto employeeQnaDetailDto = qnaService.employeeDetailQnA( qna_id );

            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, qna_id + "번 조회 성공",  employeeQnaDetailDto );
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);

        } catch (IllegalArgumentException e) {

            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);

        }
    }



}
