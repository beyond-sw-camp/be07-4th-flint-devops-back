package com.hotel.flint.common.controller;

import com.hotel.flint.common.configs.RabbitMqConfig;
import com.hotel.flint.common.dto.CommonErrorDto;
import com.hotel.flint.common.dto.CommonResDto;
import com.hotel.flint.common.service.RequestQueueManager;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class MqRequestController {

    @Autowired
    private RequestQueueManager queueManager;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping("/submit")
    public ResponseEntity<?> submitRequest(@RequestParam String email) { // 이메일을 파라미터로 받음
        try {
            // 새로운 요청 ID를 생성하고 Redis 큐에 추가
            String requestId = queueManager.addRequest(email);

            if (requestId == null) {
                throw new IllegalStateException("요청 생성에 실패했습니다.");
            }

            // RabbitMQ에 요청 ID를 전송
            rabbitTemplate.convertAndSend(RabbitMqConfig.WAITING_LIST_QUEUE, requestId);

            // 현재 요청의 대기열 위치를 가져옴
            int position = queueManager.getPositionInQueue(requestId);

            // 대기열 위치가 유효하지 않으면 예외 발생
            if (position == -1) {
                CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), "대기열에서 요청 ID의 위치를 가져오는 데 실패했습니다.");
                return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
            }

            // 응답 데이터 구성
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("requestId", requestId);
            responseData.put("position", position);

            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "요청이 성공적으로 처리되었습니다.", responseData);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);

        } catch (IllegalStateException e) {
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/leave/{requestId}")
    public ResponseEntity<Void> leaveQueue(@PathVariable String requestId) {
        queueManager.removeRequest(requestId);
        return ResponseEntity.noContent().build(); // 성공적으로 삭제했음을 의미(Http 204 반환)
    }
}