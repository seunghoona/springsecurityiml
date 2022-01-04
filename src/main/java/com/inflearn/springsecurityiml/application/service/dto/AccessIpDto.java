package com.inflearn.springsecurityiml.application.service.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessIpDto{

    private Long id;
    private String ipAddress;

}
