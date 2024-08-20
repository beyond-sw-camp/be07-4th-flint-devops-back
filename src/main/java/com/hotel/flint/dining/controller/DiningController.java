package com.hotel.flint.dining.controller;

import com.hotel.flint.common.dto.CommonErrorDto;
import com.hotel.flint.common.dto.CommonResDto;
import com.hotel.flint.common.enumdir.DiningName;
import com.hotel.flint.dining.service.DiningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DiningController {

    private final DiningService diningService;

    @Autowired
    public DiningController(DiningService diningService) {
        this.diningService = diningService;
    }

    @GetMapping("/dining/{id}")
    public ResponseEntity<?> diningName(@PathVariable Long id){

        try {

            DiningName diningName = diningService.getName(id);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK,  "성공", diningName);
            return new ResponseEntity<>( commonResDto, HttpStatus.OK );

        }catch (IllegalArgumentException e) {

            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);

        }

    }


}
