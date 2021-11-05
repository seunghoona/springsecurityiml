package com.inflearn.springsecurityiml.service;

import org.springframework.stereotype.Service;

import com.inflearn.springsecurityiml.annotation.DomainService;
import com.inflearn.springsecurityiml.domain.Account;
import com.inflearn.springsecurityiml.domain.AccountDto;
import com.inflearn.springsecurityiml.domain.PasswordService;
import com.inflearn.springsecurityiml.infrastructure.UserRepository;

import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordService passwordService;

	@Override
	public void createUser(AccountDto account) {
		userRepository.save(new Account(account,passwordService));
	}
}
