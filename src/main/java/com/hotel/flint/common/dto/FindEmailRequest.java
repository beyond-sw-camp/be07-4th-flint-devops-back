package com.hotel.flint.common.dto;

import lombok.Data;

@Data
public class FindEmailRequest {
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
