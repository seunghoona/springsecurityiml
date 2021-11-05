package com.inflearn.springsecurityiml.domain;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PasswordService {

	private final PasswordEncoder passwordEncoder;

	public Password encode(Password password) {
		String encode = passwordEncoder.encode(password);
		return new Password(encode);
	}

}
