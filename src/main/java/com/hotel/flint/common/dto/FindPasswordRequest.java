package com.hotel.flint.common.dto;

import lombok.Data;

@Data
public class FindPasswordRequest {
    private String firstName;
    private String lastName;
    private String email;
}
