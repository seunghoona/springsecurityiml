package com.inflearn.springsecurityiml.service;

import com.inflearn.springsecurityiml.service.dto.UserDto;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.inflearn.springsecurityiml.annotation.DomainService;
import com.inflearn.springsecurityiml.domain.Account;
import com.inflearn.springsecurityiml.domain.AccountDto;
import com.inflearn.springsecurityiml.domain.PasswordService;
import com.inflearn.springsecurityiml.infrastructure.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@DomainService
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordService passwordService;

	public void createUser(AccountDto account) {
		userRepository.save(new Account(account,passwordService));
	}

	@Transactional
	public UserDto getUser(Long id) {
		Account account = userRepository.findById(id).orElse(new Account(new AccountDto(), passwordService));
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(account, UserDto.class);

		List<String> roles = account.getUserRoles()
			.stream()
			.map(role -> role.getRoleName())
			.collect(Collectors.toList());

		userDto.setRoles(roles);
		return userDto;
	}

	@Transactional
	public List<Account> getUsers() {
		return userRepository.findAll();
	}

	@Override
	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}
}
