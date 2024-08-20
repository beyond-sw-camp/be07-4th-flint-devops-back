package com.hotel.flint.user.employee.dto;

import com.hotel.flint.common.enumdir.Option;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeModRoomDto {

    //adultCnt, childCnt, adultBfCnt, childBfCnt, parkingYN, requestContents만 수정 가능
    private int adultCnt;
    private int childCnt;
    private int adultBfCnt;
    private int childBfCnt;
    private Option parkingYN;
    private String requestContents;


}
