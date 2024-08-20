package com.hotel.flint.support.qna.controller;

import com.hotel.flint.common.dto.CommonErrorDto;
import com.hotel.flint.common.dto.CommonResDto;
import com.hotel.flint.support.qna.dto.CreateQnaDto;
import com.hotel.flint.support.qna.dto.QnaDetailDto;
import com.hotel.flint.support.qna.dto.QnaListDto;
import com.hotel.flint.support.qna.dto.QnaUpdateDto;
import com.hotel.flint.support.qna.service.QnaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/mypage/qna")
public class QnaController {

    private final QnaService qnaService;

    @Autowired
    public QnaController(QnaService qnaService) {
        this.qnaService = qnaService;
    }

    /**
     * qna 등록
     */
    @PostMapping("/create")
    public ResponseEntity<?> createQnA(@RequestBody CreateQnaDto dto) {

        try {
            qnaService.createQnA(dto);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "qna 등록 성공", null);
            return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * qna 목록 조회 - 마이페이지에서 본인것 조회
     */
    @GetMapping("/list")
    public List<QnaListDto> qnaList(Pageable pageable) {

        return qnaService.qnaList(pageable);
    }

    /**
     * qna 상세 조회
     */
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detailQnA(@PathVariable Long id) {

        try {
            QnaDetailDto qnaDetailDto = qnaService.qnaDetail(id);

            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "qna 상세조회", qnaDetailDto);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * qna 수정
     */
    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateQnA(@PathVariable Long id, @RequestBody QnaUpdateDto dto) {

        try {
            qnaService.qnaUpdate(id, dto);

            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "qna 수정 성공", null);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * qna 삭제
     */
    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deleteQnA(@PathVariable Long id) {

        try {
            qnaService.delete(id);

            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "qna 삭제 성공", null);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }
}
