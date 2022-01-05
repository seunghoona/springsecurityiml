package com.inflearn.springsecurityiml.aopsecurity;

import static java.lang.System.out;

import org.springframework.stereotype.Service;

@Service
public class AopMethodService {
    
    public void methodSecured() {
        out.println("메소드 보안 확인");
    }

}
